package com.rafaelsantos.dscommerce.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rafaelsantos.dscommerce.entities.Role;
import com.rafaelsantos.dscommerce.entities.User;
import com.rafaelsantos.dscommerce.entities.dto.UserDTO;
import com.rafaelsantos.dscommerce.projections.UserDetailsProjection;
import com.rafaelsantos.dscommerce.repositories.UserRepository;
import com.rafaelsantos.dscommerce.util.CustomUserUtil;

@Service
public class UserService implements UserDetailsService {

	private UserRepository userRepository;
	
	private CustomUserUtil customUserUtil;

	public UserService(UserRepository userRepository, CustomUserUtil customUserUtil) {
		this.userRepository = userRepository;
		this.customUserUtil = customUserUtil;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
		if (result.size() == 0) {
			throw new UsernameNotFoundException("Email not found");
		}

		User user = new User();
		user.setEmail(result.get(0).getUsername());
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}

		return user;
	}

	protected User authenticated() {
		try {
			String username = customUserUtil.getLoggedUsername();
			return userRepository.findByEmail(username).get();
		} catch (Exception e) {
			throw new UsernameNotFoundException("Invalid user");
		}
	}

	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User entity = authenticated();
		return new UserDTO(entity);
	}
}
