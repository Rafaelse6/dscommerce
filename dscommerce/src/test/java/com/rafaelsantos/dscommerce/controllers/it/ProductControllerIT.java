package com.rafaelsantos.dscommerce.controllers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelsantos.dscommerce.entities.Product;
import com.rafaelsantos.dscommerce.entities.dto.ProductDTO;
import com.rafaelsantos.dscommerce.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String productName;
	private ProductDTO productDTO;
	private Product product;
	
	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken, invalidToken;
	
	
	@BeforeEach
	void setUp() throws Exception {
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		
		productName = "Macbook";
		
		adminToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";
	}

	@Test
	public void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception{

		ResultActions result = mockMvc
				.perform(get("/products?name={productName}", productName)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(3L));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[0].price").value(1250.0));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
	}
	
	@Test
	public void findAllShouldReturnPageWhenParamIsEmpty() throws Exception{

		ResultActions result = mockMvc
				.perform(get("/products", productName)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(1L));
		result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
		result.andExpect(jsonPath("$.content[0].price").value(90.5));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
	}
	
	@Test
	public void insertShouldReturnProductDTOCreatedWhenAdminLogged() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.accept(MediaType.APPLICATION_JSON));
	}
}
