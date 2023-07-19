package com.rafaelsantos.dscommerce.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rafaelsantos.dscommerce.entities.Product;
import com.rafaelsantos.dscommerce.entities.dto.ProductDTO;
import com.rafaelsantos.dscommerce.repositories.ProductRepository;
import com.rafaelsantos.dscommerce.services.exceptions.ResourceNotFoundException;
import com.rafaelsantos.dscommerce.tests.ProductFactory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private long existingProductId, nonExistingProductId;
	private String productName;
	private Product product;

	@BeforeEach
	void setUp() throws Exception {
		existingProductId = 1L;
		nonExistingProductId = 2L;

		productName = "Playstation 5";

		product = ProductFactory.createProduct(productName);

		Mockito.when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingProductId)).thenReturn(Optional.empty());
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findById(existingProductId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingProductId);
		Assertions.assertEquals(result.getName(), product.getName());
	}
	
	@Test
	public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingProductId);
		});
	}
}
