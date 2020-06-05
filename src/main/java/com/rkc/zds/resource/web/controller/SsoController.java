package com.rkc.zds.resource.web.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rkc.zds.resource.service.UserService;

// import com.rkc.zds.dto.LoginDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.service.AuthenticationService;

@RestController
@RequestMapping(value = "/api")
public class SsoController {
	
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;
    
    @RequestMapping(value = "/sso/login",method = RequestMethod.POST)
    public UserDto authenticate(@RequestBody String dummy, HttpServletRequest request, HttpServletResponse response) throws Exception{

    	// createDefaultAccount();
    	
    	// fixUserContacts();
    	
    	return authenticationService.authenticateViaSSO(request, response);
    }
    
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public void logout(){
        authenticationService.logout();
    }
/*    
    @GetMapping("/sso/login")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt principal) {
        return Collections.singletonMap("user_name", principal.getClaimAsString("preferred_username"));
    }
    
    @GetMapping("/sso/login/state")
    public Map<String, Object> getUserState(@AuthenticationPrincipal Jwt principal) {
        return Collections.singletonMap("user_name", principal.getClaimAsString("preferred_username"));
    }
*/
}
