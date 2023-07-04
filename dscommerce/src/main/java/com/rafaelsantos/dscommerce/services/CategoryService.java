package com.rafaelsantos.dscommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rafaelsantos.dscommerce.entities.Category;
import com.rafaelsantos.dscommerce.entities.dto.CategoryDTO;
import com.rafaelsantos.dscommerce.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	private CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public List<CategoryDTO> findAll(){
		List<Category> list = categoryRepository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).toList();
	}
}
