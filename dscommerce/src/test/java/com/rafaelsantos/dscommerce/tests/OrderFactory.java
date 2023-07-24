package com.rafaelsantos.dscommerce.tests;

import java.time.Instant;

import com.rafaelsantos.dscommerce.entities.Order;
import com.rafaelsantos.dscommerce.entities.OrderItem;
import com.rafaelsantos.dscommerce.entities.Payment;
import com.rafaelsantos.dscommerce.entities.Product;
import com.rafaelsantos.dscommerce.entities.User;
import com.rafaelsantos.dscommerce.entities.enums.OrderStatus;

public class OrderFactory {

	public static Order createOrder(User client) {
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, client, new Payment());

		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);

		return order;
	}
}
