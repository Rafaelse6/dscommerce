package com.rafaelsantos.dscommerce.services;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rafaelsantos.dscommerce.entities.Order;
import com.rafaelsantos.dscommerce.entities.OrderItem;
import com.rafaelsantos.dscommerce.entities.Product;
import com.rafaelsantos.dscommerce.entities.dto.OrderDTO;
import com.rafaelsantos.dscommerce.entities.dto.OrderItemDTO;
import com.rafaelsantos.dscommerce.entities.enums.OrderStatus;
import com.rafaelsantos.dscommerce.repositories.OrderItemRepository;
import com.rafaelsantos.dscommerce.repositories.OrderRepository;
import com.rafaelsantos.dscommerce.repositories.ProductRepository;
import com.rafaelsantos.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	private OrderRepository orderRepository;
	private ProductRepository productRepository;
	private OrderItemRepository orderItemRepository;

	public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.orderItemRepository = orderItemRepository;
	}

	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new OrderDTO(order);
	}
	
	@Transactional
	public OrderDTO insert(OrderDTO dto) {
		Order order = new Order();
		
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		
		for(OrderItemDTO itemDto : dto.getItems()) {
			Product product = productRepository.getReferenceById(itemDto.getProductId());
			OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
			order.getItems().add(item);
		}
		
		orderRepository.save(order);
		orderItemRepository.saveAll(order.getItems());
		
		return new OrderDTO(order);
	}
}
