package com.rkc.zds.resource.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rkc.zds.resource.dto.UserContactDto;

public interface UserContactsRepository extends JpaRepository<UserContactDto, Integer> {
  
	Page<UserContactDto> findByUserId(Pageable pageable, int userId);

	List<UserContactDto> findByUserId(int userId);
	       
}