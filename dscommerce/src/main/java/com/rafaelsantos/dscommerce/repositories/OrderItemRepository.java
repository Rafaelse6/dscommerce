package com.rafaelsantos.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelsantos.dscommerce.entities.OrderItem;
import com.rafaelsantos.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK>{

}
