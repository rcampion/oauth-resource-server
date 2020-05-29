package com.rkc.zds.resource.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.AuthorityDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.service.UserService;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {

		UserDto user = userService.findByUserName(userName);
		if (user != null) {
			List<GrantedAuthority> authorities = buildUserAuthority(user.getAuthorities());

			return buildUserForAuthentication(user, authorities);
		} else {
            throw new UsernameNotFoundException("User "+userName+" not found");
		}
	}

	// Converts UserDto user to
	// org.springframework.security.core.userdetails.User
	private User buildUserForAuthentication(UserDto user, List<GrantedAuthority> authorities) {
		return new User(user.getUserName(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
	}
	
	private List<GrantedAuthority> buildUserAuthority(List<AuthorityDto> authorityDtos) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (AuthorityDto authorityDto : authorityDtos) {
			setAuths.add(new SimpleGrantedAuthority(authorityDto.getAuthority()));
		}

		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);

		return result;
	}
}