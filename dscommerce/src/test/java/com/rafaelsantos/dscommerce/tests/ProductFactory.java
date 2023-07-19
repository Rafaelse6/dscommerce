package com.rafaelsantos.dscommerce.tests;

import com.rafaelsantos.dscommerce.entities.Category;
import com.rafaelsantos.dscommerce.entities.Product;

public class ProductFactory {

	public static Product createProduct() {
		Category category = CategoryFactory.createCategory();
		Product product = new Product(1L, "Console Playstation 5", "This is the product descritpion", 3999.0,
				"This is the product URL");
		product.getCategories().add(category);
		return product;
	}

	public static Product createProduct(String name) {
		Product product = createProduct();
		product.setName(name);
		return product;
	}
}
