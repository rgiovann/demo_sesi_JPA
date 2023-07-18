package com.example.demo.infraestructure.configuration;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.api.input.CategoriaIdInput;
import com.example.demo.domain.model.Categoria;

@Configuration
public class ModelMapperConfig {

    @Bean
    ModelMapper modelMapper() {
    	
    	var modelMapper = new ModelMapper();  
    	
    	Converter<CategoriaIdInput, Categoria> categoriaConverter = new AbstractConverter<>() {
    		@Override
    		protected Categoria convert(CategoriaIdInput source) {
    			Categoria categoria = new Categoria();
    			categoria.setId(source.getId());
    			return categoria;
    		}
    	};
    	
    	modelMapper.addConverter(categoriaConverter, CategoriaIdInput.class, Categoria.class);

    	return modelMapper;
    	

   
    }
    
}
