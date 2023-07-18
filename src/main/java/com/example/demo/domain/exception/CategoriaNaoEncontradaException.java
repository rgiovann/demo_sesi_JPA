package com.example.demo.domain.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND)
// não precisa responsestatus pois a classe pai já tem esse annotation
public class CategoriaNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public CategoriaNaoEncontradaException(String msg) {
		super(msg);
	}
	
	public CategoriaNaoEncontradaException(Long categoriaId) {
		// chamando o construtor anterior;
		 this(String.format("Categoria de código %d não encontrada.",categoriaId));
	}


}

