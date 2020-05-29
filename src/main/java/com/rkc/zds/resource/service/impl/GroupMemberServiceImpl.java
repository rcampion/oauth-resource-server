package com.rkc.zds.resource.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.ContactDto;
import com.rkc.zds.resource.dto.GroupMemberDto;
import com.rkc.zds.resource.repository.ContactRepository;
import com.rkc.zds.resource.repository.GroupMemberRepository;
import com.rkc.zds.resource.service.GroupMemberService;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {

	@Autowired
	private ContactRepository contactRepo;
	
	@Autowired
	private GroupMemberRepository groupMemberRepo;

	@Override
	public Page<GroupMemberDto> findGroupMembers(Pageable pageable, int id) {

		Page<GroupMemberDto> page = groupMemberRepo.findByGroupId(pageable, id);

		return page;
	}
	
	@Override
	public Page<ContactDto> findFilteredContacts(Pageable pageable, int groupId) {

		List<ContactDto> contacts = contactRepo.findAll();

		List<GroupMemberDto> groupMemberList = groupMemberRepo.findByGroupId(groupId);
		
		List<ContactDto> testList = new ArrayList<ContactDto>();

		List<ContactDto> filteredList = new ArrayList<ContactDto>();

		// build member list of Contacts
		Optional<ContactDto> contact;
		for (GroupMemberDto element : groupMemberList) {
			contact= contactRepo.findById(element.getContactId());
			testList.add(contact.get());
		}

		// check member list of Contacts
		for (ContactDto element : contacts) {
			// if the contact is in the members list, ignore it
			if (!testList.contains(element)) {
				filteredList.add(element);
			}
		}

		int size = filteredList.size();
		if(size == 0) {
			size = 1;
		}
		
		PageRequest pageRequest = PageRequest.of(0, size);

		PageImpl<ContactDto> page = new PageImpl<ContactDto>(filteredList, pageRequest, size);

		return page;
	}
	
	@Override
	public List<GroupMemberDto> findAllMembers(int groupId) {

		List<GroupMemberDto> list = groupMemberRepo.findByGroupId(groupId);

		return list;
	}

	@Override
	public void saveGroupMember(GroupMemberDto groupMember) {
		// checking for duplicates
		List<GroupMemberDto> list = groupMemberRepo.findByGroupId(groupMember.getGroupId());

		// return if duplicate found
		for (GroupMemberDto element : list) {
			if (element.getContactId() == groupMember.getContactId()) {
				return;
			}
		}

		groupMemberRepo.save(groupMember);
	}

	@Override
	public void deleteGroupMember(int id) {

		groupMemberRepo.deleteById(id);

	}
}
