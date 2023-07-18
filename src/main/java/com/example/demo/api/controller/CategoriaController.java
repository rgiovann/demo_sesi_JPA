package com.example.demo.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.assembler.CategoriaDtoAssembler;
import com.example.demo.api.assembler.CategoriaNomeInputDisassembler;
import com.example.demo.api.dto.CategoriaDto;
import com.example.demo.api.input.CategoriaNomeInput;
import com.example.demo.domain.model.Categoria;
import com.example.demo.domain.service.CadastroCategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaController {

	private final CadastroCategoriaService categoriaService;
    private final CategoriaDtoAssembler categoriaDtoAssembler;
    private final CategoriaNomeInputDisassembler categoriaNomeInputDisassembler;
	

	public CategoriaController(CadastroCategoriaService categoriaService, CategoriaDtoAssembler categoriaDtoAssembler,
			CategoriaNomeInputDisassembler categoriaInputDisassembler) {
		this.categoriaService = categoriaService;
		this.categoriaDtoAssembler = categoriaDtoAssembler;
		this.categoriaNomeInputDisassembler = categoriaInputDisassembler;
	}

 	
	@GetMapping
	public Page<CategoriaDto> listar(@PageableDefault(size=10) Pageable pageable) {
		
		Page<Categoria> eventoPage = categoriaService.listar(pageable);
		
		List<CategoriaDto> eventoDtoList = categoriaDtoAssembler.toCollectionDto(eventoPage.getContent());
		
		Page<CategoriaDto> eventoDtoPage = new PageImpl<>(eventoDtoList,pageable,eventoPage.getTotalElements());

		return eventoDtoPage;

	}
	
	
	@GetMapping("/{categoriaId}")
	public CategoriaDto buscar(@PathVariable Long categoriaId) {

		return  categoriaDtoAssembler.toDto(categoriaService.buscarOuFalhar(categoriaId));

	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoriaDto adicionar(@RequestBody @Valid CategoriaNomeInput categoriaNomeInput) {
		
		return  categoriaDtoAssembler
				.toDto( categoriaService.salvar( categoriaNomeInputDisassembler.toEntity(categoriaNomeInput)));

	}
	
	@PutMapping("/{categoriaId}")
	public CategoriaDto atualizar(@PathVariable Long categoriaId, @RequestBody @Valid  CategoriaNomeInput categoriaNomeInput)
	{        
			Categoria categoria = categoriaService.buscarOuFalhar(categoriaId);
			
			categoriaNomeInputDisassembler.copyToEntity(categoriaNomeInput,categoria);
		 
			return  categoriaDtoAssembler.toDto( categoriaService.salvar(categoria));
  
	}

	@DeleteMapping("/{categoriaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long categoriaId) {

			categoriaService.excluir(categoriaId);
	}

}
