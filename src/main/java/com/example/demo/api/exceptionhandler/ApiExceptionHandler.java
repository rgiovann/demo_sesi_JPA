package com.example.demo.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.domain.exception.EntidadeNaoEncontradaException;
import com.example.demo.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema "
			+"persistir, entre em contato com o administrador do sistema.";
	
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontradoException(EntidadeNaoEncontradaException ex,
			WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		Problem problem = createProblemBuilder(status, ProblemType.RECURSO_NAO_ENCONTRADO, ex.getMessage())
				.build();		

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		Problem problem = createProblemBuilder(status, ProblemType.PROBLEMA_NA_REQUISICAO, ex.getMessage()).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		ex.printStackTrace();  // #debug
		String details = MSG_ERRO_GENERICA_USUARIO_FINAL;
		Problem problem = createProblemBuilder(status, ProblemType.ERRO_DO_SISTEMA, details).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		
		String recurso = ex.getRequestURL();
		String detail = String.format("O recurso '%s', que você tentou acessar, é inexistente",recurso);
		Problem problem = createProblemBuilder(status, ProblemType.RECURSO_NAO_ENCONTRADO, detail) 
						  .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
						  .build();		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		// rootCause é NumberFormatException mas MethodArgumentTypeMismatchException é lançada
		// sendo herdada de TypeMismatchException		
		
		// ex.printStackTrace(); // #debug
		
		if (ex instanceof MethodArgumentTypeMismatchException) {

			return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}  

		String detail = "Parâmetro da URL inválido. Verifique o erro de sintaxe.";
		Problem problem = createProblemBuilder(status, ProblemType.PARAMETRO_INVALIDO, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();		

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		//rootCause.printStackTrace(); //#debug
 
		if (rootCause instanceof InvalidFormatException) {

			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		
		} else if (rootCause instanceof UnrecognizedPropertyException) {

			return handleUnrecognizedPropertyException((UnrecognizedPropertyException) rootCause, headers, status,
					request);

		} else if (rootCause instanceof IgnoredPropertyException) {

			return handleIgnoredPropertyException((IgnoredPropertyException) rootCause, headers, status, request);

		}

		String detail = "O corpo da requisição está corrompido, não é possivel fazer o parsing da mensagem Json. Verifique a sintaxe da mensagem.";
		Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_CORROMPIDA, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();		

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (body == null) {
			body = Problem.builder()
					.title(status.getReasonPhrase())
					.status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.timestamp(OffsetDateTime.now())
					.build();
		} else if (body instanceof String) {
			body = Problem.builder()
					.title((String) body)
					.status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.timestamp(OffsetDateTime.now())
					.build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		return handleValidationInternal( ex,ex.getBindingResult(),headers, status,request);

	}

	

	private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String name = ex.getName();

		String detail = String.format(
				"O parâmetro de URL '%s' recebeu o valor '%s' "
						+ "que é de um tipo inválido. Corrija e informe o valor compatível com o tipo %s",
				name, ex.getValue(), ex.getParameter().getParameterType().getSimpleName());
		Problem problem = createProblemBuilder(status, ProblemType.PARAMETRO_INVALIDO, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();						
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		String detail = String.format("A propriedade '%s' não é reconhecida para a entidade '%s'.", path,
				ex.getReferringClass().getSimpleName());

		Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_CORROMPIDA, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();				

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleIgnoredPropertyException(IgnoredPropertyException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		String detail = String.format("A propriedade '%s' da entidade '%s' deve ser ignorada no corpo da requisição.",
				path, ex.getReferringClass().getSimpleName());

		Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_CORROMPIDA,detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();


		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		String detail = String.format(
				"A propriedade '%s' recebeu o valor '%s' "
						+ "que é do tipo inválido. Corrija e informe o valor compatível com o tipo %s",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_CORROMPIDA, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		return Problem.builder().status(status.value()).type(problemType.getUri()).title(problemType.getTitle())
				.detail(detail).timestamp(OffsetDateTime.now());

	}

	
	// agora trata referencias em Collections
	private String joinPath(List<Reference> references) {
		return references.stream()
				.map(ref -> {
					// == null pega o index do elemento do atributo na collection
					if (ref.getFieldName() == null) {
						return "[" + ref.getIndex() + "]";
					} else {
						return ref.getFieldName();
					}
				})
				.collect(Collectors.joining("."));
	}
	
	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
			HttpStatus status, WebRequest request)
	{
 		List<Problem.Field> problemObjects = bindingResult.getAllErrors().stream()
				.map(objectError -> {
						String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
						
						String name = objectError.getObjectName();
						
						if(objectError instanceof FieldError) {
							name = ((FieldError) objectError).getField();
						}
						
						return Problem.Field.builder()
						.name(name)
						.userMessage(message).build();
				})
				.collect(Collectors.toList());
				
		String detail = "Um ou mais dados estão inválidos. Faça o preenchimento correto e tente novamente.";
		Problem problem = createProblemBuilder(status, ProblemType.DADOS_INVALIDO, detail)
				.userMessage(detail)
				.objects(problemObjects)
				.build();	
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

}
