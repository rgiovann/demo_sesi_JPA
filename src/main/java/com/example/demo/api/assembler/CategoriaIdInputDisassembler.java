package com.example.demo.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.demo.api.input.CategoriaIdInput;
import com.example.demo.domain.model.Categoria;

@Component
public class CategoriaIdInputDisassembler extends EntityInputDisassembler<CategoriaIdInput, Categoria>{

	public CategoriaIdInputDisassembler(ModelMapper mapper) {
		super(mapper);
	}
}
