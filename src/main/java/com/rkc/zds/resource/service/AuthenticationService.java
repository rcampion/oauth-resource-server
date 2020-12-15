package com.rkc.zds.resource.service;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.UserDto;

@Service
public class AuthenticationService {

	public static final String CSRF_CLAIM_HEADER = "X-HMAC-CSRF";
    public static final String ACCESS_TOKEN_COOKIE = "access_token";
	public static final String JWT_CLAIM_LOGIN = "login";

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	/**
	 * Authenticate a user in Spring Security The following headers are set in
	 * the response: - X-TokenAccess: JWT - X-Secret: Generated secret in base64
	 * using SHA-256 algorithm - WWW-Authenticate: Used algorithm to encode
	 * secret The authenticated user is set in the Spring Security context The
	 * generated secret is stored in a static list for every user
	 * 
	 * @param loginDTO
	 *            credentials
	 * @param response
	 *            http response
	 * @return UserDTO instance
	 * @throws HmacException
	 * /
	public UserDto authenticate(LoginDto loginDTO, HttpServletRequest request, HttpServletResponse response) throws HmacException {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getLogin(), loginDTO.getPassword());
		Authentication authentication = null;

		try {
			authentication = authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			try {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
				return null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (AuthenticationCredentialsNotFoundException x) {
			try {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
				return null;
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}			
		}
				
		SecurityContextHolder.getContext().setAuthentication(authentication);
 
		// Retrieve security user after authentication
		UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getLogin());

		UserDto userDTO = userService.findByUserName(userDetails.getUsername());

		// SecurityUser securityUser = (SecurityUser)
		// userDetailsService.loadUserByUsername(loginDTO.getLogin());
		SecurityUser securityUser = new SecurityUser(userDTO.getId(), userDetails.getUsername(),
				userDetails.getPassword(), null, userDetails.getAuthorities());

		// Parse Granted authorities to a list of string authorities
		List<String> authorities = new ArrayList<>();
		for (GrantedAuthority authority : securityUser.getAuthorities()) {
			authorities.add(authority.getAuthority());
		}

		// Get Hmac signed token
		String csrfId = UUID.randomUUID().toString();
		Map<String, String> customClaims = new HashMap<>();
		customClaims.put(HmacSigner.ENCODING_CLAIM_PROPERTY, HmacUtils.HMAC_SHA_256);
		customClaims.put(JWT_CLAIM_LOGIN, loginDTO.getLogin());
		customClaims.put(CSRF_CLAIM_HEADER, csrfId);

		// Generate a random secret
//		String privateSecret = HmacSigner.generateSecret();
//		String publicSecret = HmacSigner.generateSecret();

        //Get jwt secret from properties
        String jwtSecret = securityProperties.getJwt().getSecret();

        //Get hmac secret from config
        String hmacSharedSecret = securityProperties.getHmac().getSecret();
		
        // Jwt is generated using the secret defined in configuration file
        HmacToken hmacToken = SecurityUtils.getSignedToken(jwtSecret,loginDTO.getLogin(), SecurityService.JWT_TTL,customClaims);

		for (UserDto userDto : userService.getUsers()) {
			if (userDto.getId() == (securityUser.getId())) {
				// Store in cache both private and public secrets
				userDto.setPublicSecret(jwtSecret);
				userDto.setPrivateSecret(hmacSharedSecret);
			}
		}

        // Add jwt as a cookie
        Cookie jwtCookie = new Cookie(ACCESS_TOKEN_COOKIE, hmacToken.getJwt());
        //jwtCookie.setPath(request.getContextPath().length() > 0 ? request.getContextPath() : "/");
        System.out.println("request.getContextPath:"+request.getContextPath());
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(securityProperties.getJwt().getMaxAge());
        //Cookie cannot be accessed via JavaScript
        jwtCookie.setHttpOnly(true);		

		// Set public secret and encoding in headers
		response.setHeader(HmacUtils.X_SECRET, hmacSharedSecret);
		response.setHeader(HttpHeaders.WWW_AUTHENTICATE, HmacUtils.HMAC_SHA_256);
		response.setHeader(CSRF_CLAIM_HEADER, csrfId);

		// Set JWT as a cookie
		response.addCookie(jwtCookie);

		// UserDto userDTO = new UserDto();
		userDTO.setId(securityUser.getId());
		userDTO.setLogin(securityUser.getUsername());
//		userDTO.setAuthorities(securityUser.getAuthorities());
		userDTO.setProfile(securityUser.getProfile());
		return userDTO;
	}
*/
	public UserDto authenticateViaSSO(HttpServletRequest request, HttpServletResponse response) {
	
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();        
        KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String userName = accessToken.getPreferredUsername();
        UserDto userDTO = userService.findByUserName(userName);
		return userDTO;
	}
	
	/**
	 * Logout a user - Clear the Spring Security context - Remove the stored UserDTO
	 * secret
	 */
	public void logout() {
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

			KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder
					.getContext().getAuthentication();

			Principal principal = (Principal) authentication.getPrincipal();

			if (principal instanceof KeycloakPrincipal) {

				KeycloakPrincipal<KeycloakSecurityContext> kPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;

				AccessToken token = kPrincipal.getKeycloakSecurityContext().getToken();
				UserDto userDTO = userService.findByUserName(token.getPreferredUsername());
				if (userDTO != null) {
					userDTO.setPublicSecret(null);
				}
			}
		}
	}

	/**
	 * Authentication for every request - Triggered by every http request except
	 * the authentication
	 * 
	 * @see com.rkc.zds.config.security.XAuthTokenFilter Set the authenticated
	 *      user in the Spring Security context
	 * @param userName
	 *            userName
	 */
	public void tokenAuthentication(String userName) {
		UserDetails details = userDetailsService.loadUserByUsername(userName);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details,
				details.getPassword(), details.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

	public UserDto findByUserName(String login) {
		UserDto userDTO = userService.findByUserName(login);
		return userDTO;
	}

}
