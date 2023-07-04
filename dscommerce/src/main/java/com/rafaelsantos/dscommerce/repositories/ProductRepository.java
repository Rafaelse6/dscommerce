package com.rafaelsantos.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelsantos.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
