package com.example.demo.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.demo.api.input.EventoInput;
import com.example.demo.domain.model.Evento;

@Component
public class EventoInputDisassembler extends EntityInputDisassembler<EventoInput, Evento>{

	public EventoInputDisassembler(ModelMapper mapper) {
		super(mapper);
	}
}
