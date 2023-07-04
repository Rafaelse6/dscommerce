package com.rafaelsantos.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaelsantos.dscommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
