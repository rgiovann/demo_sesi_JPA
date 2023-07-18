package com.example.demo.domain.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.exception.EventoNaoEncontradoException;
import com.example.demo.domain.model.Categoria;
import com.example.demo.domain.model.Evento;
import com.example.demo.domain.repository.EventoRepository;

@Service
public class CadastroEventoService {

	private final EventoRepository eventoRepository;
	private final CadastroCategoriaService  cadastroCategoriaService;


	public CadastroEventoService(EventoRepository eventoRepository,
								 CadastroCategoriaService  cadastroCategoriaService) {
		this.eventoRepository = eventoRepository;
		this.cadastroCategoriaService = cadastroCategoriaService;

	}

	public Page<Evento> listar(Pageable eventosPage) {

		return eventoRepository.findAll(eventosPage);

	}

	@Transactional
	public Evento salvar(Evento evento) {

		Long categoriaId = evento.getCategoria().getId();

		Categoria categoria = cadastroCategoriaService.buscarOuFalhar(categoriaId);

		evento.setCategoria(categoria);

		return eventoRepository.save(evento);

	}

	@Transactional
	public void excluir(Long eventoId) {
		try {

			eventoRepository.deleteById(eventoId);

			eventoRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new EventoNaoEncontradoException(eventoId);
		}

	}

	public Evento buscarOuFalhar(Long eventoId) {
		return eventoRepository.findById(eventoId).orElseThrow(() -> new EventoNaoEncontradoException(eventoId));
	}

}