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

import com.example.demo.api.assembler.EventoDtoAssembler;
import com.example.demo.api.assembler.EventoInputDisassembler;
import com.example.demo.api.dto.EventoDto;
import com.example.demo.api.input.EventoInput;
import com.example.demo.domain.exception.CategoriaNaoEncontradaException;
import com.example.demo.domain.exception.NegocioException;
import com.example.demo.domain.model.Evento;
import com.example.demo.domain.service.CadastroEventoService;

@RestController
@RequestMapping(value = "/eventos")
public class EventoController {

	private final CadastroEventoService eventoService;
	private final EventoDtoAssembler eventoDtoAssembler;
	private final EventoInputDisassembler eventoInputDisassembler;

	public EventoController(CadastroEventoService eventoService, 
			                 EventoDtoAssembler eventoDtoAssembler,
			                 EventoInputDisassembler eventoInputDisassembler) {
		
		this.eventoService = eventoService;
		this.eventoDtoAssembler = eventoDtoAssembler;
		this.eventoInputDisassembler = eventoInputDisassembler;
	}

	@GetMapping
	public Page<EventoDto> listar(@PageableDefault(size=10) Pageable pageable) {
		
		Page<Evento> eventoPage = eventoService.listar(pageable);
		
		List<EventoDto> eventoDtoList = eventoDtoAssembler.toCollectionDto(eventoPage.getContent());
		
		Page<EventoDto> eventoDtoPage = new PageImpl<>(eventoDtoList,pageable,eventoPage.getTotalElements());

		return eventoDtoPage;

	}

	@GetMapping("/{eventoId}")
	public EventoDto buscar(@PathVariable Long eventoId) {
		Evento evento = eventoService.buscarOuFalhar(eventoId);
		EventoDto eventoDTO = eventoDtoAssembler.toDto(evento); 
		return eventoDTO;

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EventoDto adicionar(@RequestBody @Valid EventoInput eventoNomeInput) {

		Evento evento = eventoInputDisassembler.toEntity(eventoNomeInput);
		evento = eventoService.salvar(evento);
 
		EventoDto eventoDto = eventoDtoAssembler
				.toDto(eventoService.salvar(evento));
		
		return eventoDto;

	}

	@PutMapping("/{eventoId}")
	public EventoDto atualizar(@PathVariable Long eventoId, @RequestBody @Valid EventoInput eventoInput) {

		Evento evento = eventoService.buscarOuFalhar(eventoId);

		eventoInputDisassembler.copyToEntity(eventoInput, evento);
		
		try {
			
			return eventoDtoAssembler.toDto(eventoService.salvar(evento));

		} catch (CategoriaNaoEncontradaException e) {
			
			throw new NegocioException(e.getMessage(), e);
		}


	}

	@DeleteMapping("/{eventoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long eventoId) {

		eventoService.excluir(eventoId);
	}

}
