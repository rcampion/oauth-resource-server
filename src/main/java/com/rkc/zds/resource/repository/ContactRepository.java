package com.rkc.zds.resource.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rkc.zds.resource.dto.ContactDto;

public interface ContactRepository extends JpaRepository<ContactDto, Integer>, JpaSpecificationExecutor<ContactDto> {
  
	Page<ContactDto> findByLastNameIgnoreCaseLike(Pageable pageable, String lastName);
	 
}
