package com.rkc.zds.resource.web.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkc.zds.resource.dto.AuthorityDto;
import com.rkc.zds.resource.dto.ContactDto;
import com.rkc.zds.resource.dto.EMailDto;
import com.rkc.zds.resource.dto.GroupDto;
import com.rkc.zds.resource.dto.LoginDto;
import com.rkc.zds.resource.dto.Profile;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.rsql.CustomRsqlVisitor;
import com.rkc.zds.resource.service.UserService;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
@RequestMapping(value = "/api")
public class UsersController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<UserDto>> findAllUsers(Pageable pageable, HttpServletRequest req) {
		Page<UserDto> page = userService.findUsers(pageable);
		ResponseEntity<Page<UserDto>> response = new ResponseEntity<>(page, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> getUser(@PathVariable int id, HttpServletRequest req) {
		UserDto user = userService.getUser(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteUser(@PathVariable int id) {
		userService.deleteUser(id);
		return Integer.toString(id);
	}

	@RequestMapping(value = "/users/password", method = RequestMethod.POST)
	public UserDto changePassword(@RequestBody LoginDto loginDTO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return userService.changePassword(loginDTO, request, response);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public void createUser(@RequestBody String jsonString) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		UserDto userDTO = new UserDto();
		try {
			userDTO = mapper.readValue(jsonString, UserDto.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// hack
		userDTO.setLogin(userDTO.getUserName());

		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		userDTO.setEnabled(1);
		userService.saveUser(userDTO);

		AuthorityDto role = new AuthorityDto();
		role.setUserName(userDTO.getLogin());
		role.setAuthority("ROLE_USER");

		userService.saveAuthority(role);
	}

	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.PUT, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public UserDto updateUser(@RequestBody String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		// mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		UserDto user = new UserDto();
		try {
			user = mapper.readValue(jsonString, UserDto.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UserDto userTemp = userService.getUser(user.getId());
		if (!userTemp.getPassword().equals(user.getPassword())) {
			if (user.getPassword() != null) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			}
		}
		user.setEnabled(1);
		user.setAuthorities(userTemp.getAuthorities());
		userService.updateUser(user);
		
		return user;

	}

	@RequestMapping("/users/profiles")
	public List<String> getProfiles() {
		List<String> profiles = new ArrayList<>();
		for (Profile profile : Profile.values()) {
			profiles.add(profile.name());
		}
		return profiles;
	}

	@RequestMapping(value = "/users/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<UserDto>> findAllByRsql(Pageable pageable,
			@RequestParam(value = "search") String search) {
		Node rootNode = new RSQLParser().parse(search);
		Specification<UserDto> spec = rootNode.accept(new CustomRsqlVisitor<UserDto>());
		// return dao.findAll(spec);
		Page<UserDto> page = userService.searchUsers(pageable, spec);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/authorities", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<AuthorityDto>> findAllAuthorities(@RequestBody String userName, Pageable pageable,
			HttpServletRequest req) {
		Page<AuthorityDto> page = userService.findAuthorities(pageable, userName);
		ResponseEntity<Page<AuthorityDto>> response = new ResponseEntity<>(page, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/users/authorities/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthorityDto> getAuthority(@PathVariable int id, HttpServletRequest req) {
		AuthorityDto user = userService.getAuthority(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/authority", method = RequestMethod.POST, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public void createAuthority(@RequestBody String jsonString) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		AuthorityDto authority = new AuthorityDto();
		try {
			authority = mapper.readValue(jsonString, AuthorityDto.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		userService.saveAuthority(authority);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/authority/authority", method = RequestMethod.PUT, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
	public void updateAuthority(@RequestBody String jsonString) {
		ObjectMapper mapper = new ObjectMapper();

		AuthorityDto authority = new AuthorityDto();
		try {
			authority = mapper.readValue(jsonString, AuthorityDto.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		userService.updateAuthority(authority);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/authority/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteAuthority(@PathVariable int id) {
		userService.deleteAuthority(id);
		return Integer.toString(id);
	}
}
