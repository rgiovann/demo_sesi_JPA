package com.example.demo.api.input;

import java.time.OffsetDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EventoInput {
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String nomeAdmin;
	
	@NotNull
	private OffsetDateTime data;
	
	@NotNull
	@Valid
	private CategoriaIdInput categoria;


}
