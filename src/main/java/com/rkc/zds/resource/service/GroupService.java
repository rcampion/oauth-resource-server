package com.rkc.zds.resource.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.rkc.zds.resource.dto.GroupDto;
import com.rkc.zds.resource.dto.GroupMemberDto;

public interface GroupService {

    Page<GroupDto> findGroups(Pageable pageable);

    Page<GroupDto> searchGroups(String name);
    
	Page<GroupDto> searchGroups(Pageable pageable, Specification<GroupDto> spec);

    GroupDto getGroup(int id);    

    Page<GroupMemberDto> findGroupMembers(int id); 
    
    public void saveGroup(GroupDto group);

    public void updateGroup(GroupDto group);

	void deleteGroup(int id);

}
