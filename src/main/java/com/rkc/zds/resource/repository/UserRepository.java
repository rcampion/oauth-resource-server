package com.rkc.zds.resource.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rkc.zds.resource.dto.UserDto;

public interface UserRepository extends JpaRepository<UserDto, Integer>, JpaSpecificationExecutor<UserDto>{

	UserDto findByLogin(String login);
	
    Optional<UserDto> findById(String id);

    Optional<UserDto> findByUserName(String userName);

    Optional<UserDto> findByEmail(String email);

}
