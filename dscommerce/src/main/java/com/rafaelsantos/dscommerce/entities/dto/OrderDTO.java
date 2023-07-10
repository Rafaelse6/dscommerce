package com.rafaelsantos.dscommerce.entities.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.rafaelsantos.dscommerce.entities.Order;
import com.rafaelsantos.dscommerce.entities.OrderItem;
import com.rafaelsantos.dscommerce.entities.enums.OrderStatus;

import jakarta.validation.constraints.NotEmpty;

public class OrderDTO {

	private Long id;
	private Instant moment;
	private OrderStatus status;

	@NotEmpty(message = "Deve ter pelo menos um item")
	public List<OrderItemDTO> items = new ArrayList<>();

	public OrderDTO(Long id, Instant moment, OrderStatus status) {
		super();
		this.id = id;
		this.moment = moment;
		this.status = status;
	}

	public OrderDTO(Order entity) {
		this.id = entity.getId();
		this.moment = entity.getMoment();
		this.status = entity.getStatus();

		for (OrderItem item : entity.getItems()) {
			OrderItemDTO itemDTO = new OrderItemDTO(item);
			items.add(itemDTO);
		}
	}

	public Long getId() {
		return id;
	}

	public Instant getMoment() {
		return moment;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}

	public Double getTotal() {
		double sum = 0.0;
		for (OrderItemDTO item : items) {
			sum += item.getSubTotal();
		}

		return sum;
	}
}
