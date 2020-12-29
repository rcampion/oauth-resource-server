package com.rkc.zds.resource.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.AuthorityDto;
import com.rkc.zds.resource.dto.LoginDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.exception.UserAlreadyExistException;
import com.rkc.zds.resource.repository.AuthorityRepository;
import com.rkc.zds.resource.repository.UserRepository;
import com.rkc.zds.resource.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDto findByUserName(String userName) {
		Optional<UserDto> userDto = userRepository.findByUserName(userName);

		UserDto user = null;

		if (userDto.isPresent()) {
			user = userDto.get();
		}

		return user;
	}

	@Override
	public Page<UserDto> findUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public UserDto findById(Integer id) {
		return userRepository.getOne(id);
	}

	@Override
	public List<UserDto> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public UserDto getUser(int id) {

		Optional<UserDto> user = userRepository.findById(id);
		if (user.isPresent())
			return user.get();
		else
			return null;
	}

	@Override
	public void updateUser(UserDto user) {

		userRepository.saveAndFlush(user);

	}

	@Override
	public void saveUser(UserDto user) {

		userRepository.save(user);

	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteUser(int id) {

		UserDto user = null;
		/*
		 * // delete authorities for this user Optional<UserDto> userOptional =
		 * userRepository.findById(id);
		 * 
		 * if (userOptional.isPresent()) { user = userOptional.get(); }
		 * 
		 * if (user != null) { Set<AuthorityDto> userAuthorities =
		 * user.getAuthorities();
		 * 
		 * for (Iterator<AuthorityDto> iterator = userAuthorities.iterator();
		 * iterator.hasNext();) { AuthorityDto authority = iterator.next();
		 * authorityRepository.deleteById(authority.getId()); }
		 * 
		 * userRepository.deleteById(id); }
		 */
		userRepository.deleteById(id);
	}

	@Override
	public UserDto registerNewUserAccount(final UserDto accountDto) {
		if (loginExist(accountDto.getLogin())) {
			throw new UserAlreadyExistException("There is an account with that userName: " + accountDto.getLogin());
		}

		String keycloakPassword = accountDto.getPassword();

		accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
		accountDto.setEnabled(1);
		accountDto.setIsLoggedIn(0);		
		UserDto user = userRepository.save(accountDto);

		AuthorityDto role = new AuthorityDto();
		role.setUserName(accountDto.getLogin());
		role.setAuthority("ROLE_USER");

		authorityRepository.save(role);

		// String realm = "zdslogic";
		String realm = "master";

		Keycloak kc = Keycloak.getInstance("https://www.zdslogic.com/keycloak/auth", "zdslogic", "richard.campion",
				"ArcyAdmin8246+", "admin-cli");		

		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(keycloakPassword);

		UserRepresentation keycloakUser = new UserRepresentation();
		keycloakUser.setUsername(accountDto.getLogin());
		keycloakUser.setFirstName(accountDto.getFirstName());
		keycloakUser.setLastName(accountDto.getLastName());
		keycloakUser.setEmail(accountDto.getEmail());
		keycloakUser.setCredentials(Arrays.asList(credential));
		keycloakUser.setEnabled(true);
		keycloakUser.setRealmRoles(Arrays.asList("user"));

		// Get realm
		RealmResource realmResource = kc.realm("zdslogic");
		UsersResource usersRessource = realmResource.users();

		// Create Keycloak user
		Response result = null;
		try {
			result = usersRessource.create(keycloakUser);
		} catch (Exception e) {
			System.out.println(e);
		}

		if (result == null || result.getStatus() != 201) {
			System.err.println("Couldn't create Keycloak user.");
			UserDto temp = userRepository.findByLogin(accountDto.getLogin());
			userRepository.delete(temp);
			authorityRepository.delete(role);

		} else {
			System.out.println("Keycloak user created.... verify in keycloak!");
		}

		return user;

	}

	private boolean loginExist(final String login) {

		UserDto user = userRepository.findByLogin(login);
		if (user != null) {

			return true;
		}

		return false;
	}

	@Override
	public Page<UserDto> searchUsers(Pageable pageable, Specification<UserDto> spec) {
		return userRepository.findAll(spec, pageable);
	}

	public UserDto changePassword(LoginDto loginDTO, HttpServletRequest request, HttpServletResponse response) {
		Optional<UserDto> user = userRepository.findByUserName(loginDTO.getLogin());
		
		UserDto userDto = null;
		
		if (user.isPresent()) {
			userDto = user.get();

			userDto.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
			userDto.setEnabled(1);

			userDto = userRepository.save(userDto);
		}
		return userDto;
	}

	@Override
	public Page<AuthorityDto> findAuthorities(Pageable pageable, String userName) {

		Page<AuthorityDto> authority = authorityRepository.findByUserName(pageable, userName);

		return authority;
	}

	@Override
	public AuthorityDto getAuthority(int id) {
		Optional<AuthorityDto> authority = authorityRepository.findById(id);
		if (authority.isPresent())
			return authority.get();
		else
			return null;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updateAuthority(AuthorityDto authority) {

		authorityRepository.saveAndFlush(authority);

	}

	@Override
	public void saveAuthority(AuthorityDto role) {

		authorityRepository.save(role);

	}

	@Override
	public void deleteAuthority(int id) {

		authorityRepository.deleteById(id);

	}
}
