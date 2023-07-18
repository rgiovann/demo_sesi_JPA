package com.example.demo.domain.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND)
// não precisa responsestatus pois a classe pai já tem esse annotation
public class EventoNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public EventoNaoEncontradoException(String msg) {
		super(msg);
	}
	
	public EventoNaoEncontradoException(Long eventoId) {
		// chamando o construtor anterior;
		 this(String.format("Evento de código %d não encontrado.",eventoId));
	}


}

