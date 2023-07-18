package com.example.demo.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.exception.CategoriaNaoEncontradaException;
import com.example.demo.domain.exception.EntidadeEmUsoException;
import com.example.demo.domain.model.Categoria;
import com.example.demo.domain.repository.CategoriaRepository;

@Service
public class CadastroCategoriaService {
	
	private static final String CATEGORIA_EM_USO = "Categoria de código %d não pode ser removida, pois está em uso.";

	private final CategoriaRepository categoriaRepository;

	public CadastroCategoriaService(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;

	}

	public Page<Categoria> listar(Pageable categoriasPage) {

		return categoriaRepository.findAll(categoriasPage);

	}

	@Transactional
	public Categoria salvar(Categoria categoria) {

		return categoriaRepository.save(categoria);
	}

	@Transactional
	public void excluir(Long categoriaId) {
		try {

			categoriaRepository.deleteById(categoriaId);
			
			categoriaRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new CategoriaNaoEncontradaException(categoriaId);
		}   catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(CATEGORIA_EM_USO, categoriaId));
		}

	}
	

	public Categoria buscarOuFalhar(Long categoriaId) {
		return categoriaRepository.findById(categoriaId).orElseThrow(() -> new CategoriaNaoEncontradaException(categoriaId));
	}

}