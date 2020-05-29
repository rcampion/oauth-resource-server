package com.rkc.zds.resource.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rkc.zds.resource.dto.GroupDto;

public interface GroupRepository extends JpaRepository<GroupDto, Integer>, JpaSpecificationExecutor<GroupDto> {
  
	Page<GroupDto> findByGroupNameLike(Pageable pageable, String groupName);
    
}
