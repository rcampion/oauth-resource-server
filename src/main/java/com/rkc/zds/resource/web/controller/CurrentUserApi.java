package com.rkc.zds.resource.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.exception.InvalidRequestException;
import com.rkc.zds.resource.model.UserData;
import com.rkc.zds.resource.model.UserWithToken;
import com.rkc.zds.resource.repository.UserRepository;
import com.rkc.zds.resource.service.UserQueryService;

@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
@RequestMapping(path = "/api/user")
public class CurrentUserApi {
	private UserQueryService userQueryService;
	private UserRepository userRepository;

	@Autowired
	public CurrentUserApi(UserQueryService userQueryService, UserRepository userRepository) {
		this.userQueryService = userQueryService;
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity currentUser(@RequestHeader(value = "Authorization") String authorization) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        KeycloakPrincipal principal=(KeycloakPrincipal)authentication.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String userName = accessToken.getPreferredUsername();
        
		Optional<UserDto> userDto = userRepository.findByUserName(userName);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}

		UserData userData = userQueryService.findById(user.getId()).get();

		return ResponseEntity.ok(user);
	}

	@PutMapping
	public ResponseEntity updateProfile(@AuthenticationPrincipal UserDto currentUser,
			@RequestHeader("Authorization") String token, @Valid @RequestBody UpdateUserParam updateUserParam,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		checkUniquenessOfUserNameAndEmail(currentUser, updateUserParam, bindingResult);

		currentUser.update(updateUserParam.getEmail(), updateUserParam.getUserName(), updateUserParam.getPassword(),
				updateUserParam.getBio(), updateUserParam.getImage());
		userRepository.save(currentUser);
		UserData userData = userQueryService.findById(currentUser.getId()).get();
		return ResponseEntity.ok(userResponse(new UserWithToken(userData, token.split(" ")[1])));
	}

	private void checkUniquenessOfUserNameAndEmail(UserDto currentUser, UpdateUserParam updateUserParam,
			BindingResult bindingResult) {
		
		UserDto byUserName = null;
		
		if (!"".equals(updateUserParam.getUserName())) {
			Optional<UserDto> userDto = userRepository.findByUserName(updateUserParam.getUserName());

			if(userDto.isPresent()) {
				byUserName = userDto.get();
			}
					
			if ((byUserName != null) && !byUserName.equals(currentUser)) {
				bindingResult.rejectValue("userName", "DUPLICATED", "userName already exist");
			}
		}

		if (!"".equals(updateUserParam.getEmail())) {
			Optional<UserDto> byEmail = userRepository.findByEmail(updateUserParam.getEmail());
			if (byEmail.isPresent() && !byEmail.get().equals(currentUser)) {
				bindingResult.rejectValue("email", "DUPLICATED", "email already exist");
			}
		}

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
	}

	private Map<String, Object> userResponse(UserWithToken userWithToken) {
		return new HashMap<String, Object>() {

			private static final long serialVersionUID = 8292663812453081620L;

			{
				put("user", userWithToken);
			}
		};
	}
}
/*
 * @Getter
 * 
 * @JsonRootName("user")
 * 
 * @NoArgsConstructor class UpdateUserParam {
 * 
 * @Email(message = "should be an email") private String email = ""; private
 * String password = ""; private String userName = ""; private String bio = "";
 * private String image = ""; }
 */
