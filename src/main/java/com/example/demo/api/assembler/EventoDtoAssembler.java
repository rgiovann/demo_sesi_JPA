package com.example.demo.api.assembler;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.demo.api.dto.EventoDto;
import com.example.demo.domain.model.Evento;

 

@Component
public class EventoDtoAssembler extends EntitytDtoAssembler<EventoDto, Evento>{

	public EventoDtoAssembler(ModelMapper mapper) {
		super(mapper);
	}

}