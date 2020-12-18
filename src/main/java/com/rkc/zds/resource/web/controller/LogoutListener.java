package com.rkc.zds.resource.web.controller;

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
    	
		UserDto userDTO = null;
/*    	
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

			KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder
					.getContext().getAuthentication();

			Principal principal = (Principal) authentication.getPrincipal();

			if (principal instanceof KeycloakPrincipal) {

				KeycloakPrincipal<KeycloakSecurityContext> kPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;

				AccessToken token = kPrincipal.getKeycloakSecurityContext().getToken();
				userDTO = userService.findByUserName(token.getPreferredUsername());
				if (userDTO != null) {
					userDTO.setPublicSecret(null);
				}
			}
		}
*/    		
    	
    	List<SecurityContext> lstSecurityContext = event.getSecurityContexts();
    	
    	Authentication authentication;
    	
    	for (SecurityContext securityContext : lstSecurityContext) {
    		// authentication = (Authentication) securityContext.getAuthentication().getPrincipal();
    		//org.keycloak.KeycloakPrincipal
    		//KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) securityContext.getAuthentication().getPrincipal();
    		KeycloakPrincipal principal = (KeycloakPrincipal) securityContext.getAuthentication().getPrincipal();
    		KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
    		AccessToken accessToken = session.getToken();
    		String userName = accessToken.getPreferredUsername();
    		userDTO = userService.findByUserName(userName);
    		
    		Message message = new Message();
    		Node newNode = new Node(userDTO);
    		message.data = newNode;
    		message.message = "Session Expired";
    		
    		// send a message to subscribed apps that session has expired
    		webSocket.convertAndSend("/topic/user/auth", message);
    	}
    }
}
