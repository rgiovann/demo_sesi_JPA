package com.example.demo.api.assembler;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.demo.api.dto.CategoriaDto;
import com.example.demo.domain.model.Categoria;

 

@Component
public class CategoriaDtoAssembler extends EntitytDtoAssembler<CategoriaDto, Categoria>{

	public CategoriaDtoAssembler(ModelMapper mapper) {
		super(mapper);
	}

}