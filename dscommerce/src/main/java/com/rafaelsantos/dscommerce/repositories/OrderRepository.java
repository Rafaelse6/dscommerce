package com.rafaelsantos.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelsantos.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{

}
