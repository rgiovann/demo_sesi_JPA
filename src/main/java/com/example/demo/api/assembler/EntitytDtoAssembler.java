package com.example.demo.api.assembler;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import lombok.Getter;

@Getter
public abstract class EntitytDtoAssembler<M, D> {
	
	 
	private final ModelMapper mapper;
	private final Class<M> dtoObject;
	
		
	@SuppressWarnings("unchecked")
	public EntitytDtoAssembler(ModelMapper mapper) {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		this.mapper = mapper;
		this.dtoObject = (Class<M>) type.getActualTypeArguments()[0];
	}
	
	public M toDto(D entityObject) {
		return  this.mapper.map(entityObject, this.dtoObject);
	}
	
 
	public List<M> toCollectionDto(Collection<D> listOfEntityObjects) {
		return listOfEntityObjects.stream().map(o -> this.toDto(o)).collect(Collectors.toList());
	}
	
}
