package com.rkc.zds.resource.web.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rkc.zds.resource.service.UserService;

// import com.rkc.zds.dto.LoginDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.service.AuthenticationService;


@CrossOrigin(origins = {"http://localhost:8089", "http://localhost:4200"})
@RestController
@RequestMapping(value = "/api")
public class SsoController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authenticationService;
	
	@RequestMapping(value = "/sso/login", method = RequestMethod.POST)
	public UserDto authenticate(@RequestBody String dummy, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// createDefaultAccount();

		// fixUserContacts();

		return authenticationService.authenticateViaSSO(request, response);
	}

	@RequestMapping(value = "/sso/getuser", method = RequestMethod.POST)
	public UserDto getUser(@RequestBody String dummy, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// createDefaultAccount();

		// fixUserContacts();

		return authenticationService.getUser(request, response);
	}
	
	@MessageMapping("/user/login")
	@SendTo("/topic/user/auth")
	public String save(String post) {
		return post;
	}

	@RequestMapping(value = "/sso/logout", method = RequestMethod.GET)
	public void logout() {
		authenticationService.logout();
	}
	/*
	 * @GetMapping("/sso/login") public Map<String, Object>
	 * getUserInfo(@AuthenticationPrincipal Jwt principal) { return
	 * Collections.singletonMap("user_name",
	 * principal.getClaimAsString("preferred_username")); }
	 * 
	 * @GetMapping("/sso/login/state") public Map<String, Object>
	 * getUserState(@AuthenticationPrincipal Jwt principal) { return
	 * Collections.singletonMap("user_name",
	 * principal.getClaimAsString("preferred_username")); }
	 */
}

