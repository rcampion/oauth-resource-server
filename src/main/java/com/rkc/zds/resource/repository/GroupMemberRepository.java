package com.rkc.zds.resource.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rkc.zds.resource.dto.GroupMemberDto;

public interface GroupMemberRepository extends JpaRepository<GroupMemberDto, Integer> {
  
	Page<GroupMemberDto> findByGroupId(Pageable pageable, int groupId);

	List<GroupMemberDto> findByGroupId(int groupId);
	       
}
