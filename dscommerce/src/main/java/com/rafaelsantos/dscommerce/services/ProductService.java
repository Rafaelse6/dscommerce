package com.rafaelsantos.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rafaelsantos.dscommerce.entities.Category;
import com.rafaelsantos.dscommerce.entities.Product;
import com.rafaelsantos.dscommerce.entities.dto.CategoryDTO;
import com.rafaelsantos.dscommerce.entities.dto.ProductDTO;
import com.rafaelsantos.dscommerce.entities.dto.ProductMinDTO;
import com.rafaelsantos.dscommerce.repositories.ProductRepository;
import com.rafaelsantos.dscommerce.services.exceptions.DatabaseException;
import com.rafaelsantos.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));

		return new ProductDTO(product);
	}

	@Transactional(readOnly = true)
	public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
		Page<Product> list = productRepository.searchByName(name, pageable);
		return list.map(x -> new ProductMinDTO(x));
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = productRepository.save(entity);

		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = productRepository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = productRepository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			productRepository.deleteById(id);
		}

		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());

		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			Category cat = new Category();
			cat.setId(catDto.getId());
			entity.getCategories().add(cat);
		}
	}
}
