package com.rkc.zds.resource.config.security;

import java.security.Principal;
import java.util.List;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.model.Message;
import com.rkc.zds.resource.model.Node;
import com.rkc.zds.resource.service.UserService;

@Component
public class LogoutListener implements ApplicationListener<SessionDestroyedEvent> {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	
	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {

		// System.out.println("Session Destroyed Event");

		UserDto userDTO = null;

		List<SecurityContext> lstSecurityContext = event.getSecurityContexts();

		for (SecurityContext securityContext : lstSecurityContext) {
			Authentication auth = securityContext.getAuthentication();
			if (auth instanceof KeycloakAuthenticationToken) {
				KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) securityContext.getAuthentication()
						.getPrincipal();
				KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
				AccessToken accessToken = session.getToken();
				String userName = accessToken.getPreferredUsername();
				userDTO = userService.findByUserName(userName);
				userDTO.setIsLoggedIn(0);
				userService.saveUser(userDTO);
			}

		}

		Message<UserDto> message = new Message<UserDto>();
		Node<UserDto> node = new Node<UserDto>(userDTO);
		message.setData(node);
		message.setMessage("Session Expired");

		// send a message to subscribed apps that session has expired
		// System.out.println("Sending Session Expired Message");
		webSocket.convertAndSend("/topic/user/auth", message);
	}
}
