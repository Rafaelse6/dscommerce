package com.rafaelsantos.dscommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rafaelsantos.dscommerce.entities.User;
import com.rafaelsantos.dscommerce.projections.UserDetailsProjection;
import com.rafaelsantos.dscommerce.repositories.UserRepository;
import com.rafaelsantos.dscommerce.tests.UserDetailsFactory;
import com.rafaelsantos.dscommerce.tests.UserFactory;
import com.rafaelsantos.dscommerce.util.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Mock
	private CustomUserUtil userUtil;

	private String existingUsername, nonExistingUsername;
	private User user;

	private List<UserDetailsProjection> userDetails;

	@BeforeEach
	void setUp() throws Exception {
		existingUsername = "maria@gmail.com";
		nonExistingUsername = "user@gmail.com";

		user = UserFactory.createCustomClientUser(1L, existingUsername);
		userDetails = UserDetailsFactory.createCustomAdminUser(existingUsername);

		Mockito.when(repository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetails);
		Mockito.when(repository.searchUserAndRolesByEmail(nonExistingUsername)).thenReturn(new ArrayList<>());

		Mockito.when(repository.findByEmail(existingUsername)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByEmail(nonExistingUsername)).thenReturn(Optional.empty());
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserDetails result = service.loadUserByUsername(existingUsername);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingUsername);
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistingUsername);
		});
	}

	@Test
	public void authenticatedShouldReturnUserWhenUserExists() {
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingUsername);

		User result = service.authenticated();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingUsername);
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
		Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
	}
}