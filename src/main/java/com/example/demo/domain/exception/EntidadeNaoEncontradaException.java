package com.example.demo.domain.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND)
//tratado pela classe ApiExceptionHandler
public abstract class EntidadeNaoEncontradaException extends NegocioException {

	private static final long serialVersionUID = 1L;
	
	public EntidadeNaoEncontradaException(String msg) {
		super(msg);
	}

}

