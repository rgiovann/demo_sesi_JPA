package com.example.demo.api.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoriaNomeInput {
	
	@NotBlank
	private String nome;
	
}
