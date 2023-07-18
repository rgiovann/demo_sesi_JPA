package com.example.demo.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.demo.api.input.CategoriaNomeInput;
import com.example.demo.domain.model.Categoria;

@Component
public class CategoriaNomeInputDisassembler extends EntityInputDisassembler<CategoriaNomeInput, Categoria>{

	public CategoriaNomeInputDisassembler(ModelMapper mapper) {
		super(mapper);
	}
}
