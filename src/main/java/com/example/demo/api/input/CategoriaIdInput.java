package com.example.demo.api.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoriaIdInput {
	
	@NotNull
	private Long id;
	
}
