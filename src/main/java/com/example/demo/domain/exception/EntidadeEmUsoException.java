package com.example.demo.domain.exception;

//@ResponseStatus(value = HttpStatus.CONFLICT)
// tratado pela classe ApiExceptionHandler
public class EntidadeEmUsoException extends NegocioException {

	private static final long serialVersionUID = 1L;
	
	public EntidadeEmUsoException(String msg) {
		super(msg);
	}

}

