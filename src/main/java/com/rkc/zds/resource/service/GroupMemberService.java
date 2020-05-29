package com.rkc.zds.resource.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rkc.zds.resource.dto.ContactDto;
import com.rkc.zds.resource.dto.GroupMemberDto;

public interface GroupMemberService {
    Page<GroupMemberDto> findGroupMembers(Pageable pageable, int groupId);
    
    List<GroupMemberDto> findAllMembers(int groupId);

    Page<ContactDto> findFilteredContacts(Pageable pageable, int groupId);  
     
    public void saveGroupMember(GroupMemberDto groupMember);    
  
	void deleteGroupMember(int id);
}
