package com.example.demo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.model.Categoria;

 
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	
	

}
