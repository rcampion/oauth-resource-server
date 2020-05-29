package com.rkc.zds.resource.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.rkc.zds.resource.dto.AuthorityDto;
import com.rkc.zds.resource.dto.LoginDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.exception.UserAlreadyExistException;

public interface UserService {
    Page<UserDto> findUsers(Pageable pageable);
    
	UserDto findByUserName(String userName);
    
	UserDto findById(Integer id);

	List<UserDto> getUsers();
	
    UserDto getUser(int id);  
	  
    public void updateUser(UserDto user);
    
	void deleteUser(int id);
    
    public void saveUser(UserDto user);

	UserDto registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

	Page<UserDto> searchUsers(Pageable pageable, Specification<UserDto> spec);

	UserDto changePassword(LoginDto loginDTO, HttpServletRequest request, HttpServletResponse response);

	Page<AuthorityDto> findAuthorities(Pageable pageable, String userName);

	AuthorityDto getAuthority(int id);
	
    public void saveAuthority(AuthorityDto role);
    
    public void updateAuthority(AuthorityDto authority);

	void deleteAuthority(int id);

}