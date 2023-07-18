package com.example.demo.domain.exception;

//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//tratado pela classe ApiExceptionHandler
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NegocioException(String msg) {
		super(msg);
	}
	
	public NegocioException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

