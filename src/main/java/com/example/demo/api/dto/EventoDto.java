package com.example.demo.api.dto;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class EventoDto {
 
	private Long id;
	private String nome;
	private String nomeAdmin;
 	private OffsetDateTime data;
 	private CategoriaDto categoria;

}