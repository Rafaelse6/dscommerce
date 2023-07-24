package com.rafaelsantos.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rafaelsantos.dscommerce.entities.Order;
import com.rafaelsantos.dscommerce.entities.Product;
import com.rafaelsantos.dscommerce.entities.User;
import com.rafaelsantos.dscommerce.entities.dto.OrderDTO;
import com.rafaelsantos.dscommerce.repositories.OrderItemRepository;
import com.rafaelsantos.dscommerce.repositories.OrderRepository;
import com.rafaelsantos.dscommerce.repositories.ProductRepository;
import com.rafaelsantos.dscommerce.services.exceptions.ForbiddenException;
import com.rafaelsantos.dscommerce.services.exceptions.ResourceNotFoundException;
import com.rafaelsantos.dscommerce.tests.OrderFactory;
import com.rafaelsantos.dscommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

	@InjectMocks
	private OrderService service;

	@Mock
	private AuthService authService;

	@Mock
	private UserService userService;

	@Mock
	private OrderRepository repository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private OrderItemRepository orderItemRepository;

	private long existingOrderId, nonExistingOrderId;

	private Order order;
	private User admin, client;
	private Product product;
	private OrderDTO orderDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingOrderId = 1L;
		nonExistingOrderId = 2L;

		admin = UserFactory.createCustomAdminUser(1L, "Jef");
		client = UserFactory.createCustomClientUser(2L, "Bob");
		order = OrderFactory.createOrder(client);

		orderDTO = new OrderDTO(order);

		Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
	}

	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

		OrderDTO result = service.findById(existingOrderId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingOrderId);
	}

	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() {
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

		OrderDTO result = service.findById(existingOrderId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingOrderId);
	}
	
	@Test
	public void findByIdShouldThrowsForbbidenExceptionAndOtherClientLogged() {
		Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());
		
		Assertions.assertThrows(ForbiddenException.class, ()-> {
			@SuppressWarnings("unused")
			OrderDTO result = service.findById(existingOrderId);
		});
	}
	
	@Test
	public void findByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExist() {
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
			@SuppressWarnings("unused")
			OrderDTO result = service.findById(nonExistingOrderId);
		});
	}
}
