package com.rkc.zds.resource.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rkc.zds.resource.dto.ContactDto;
import com.rkc.zds.resource.dto.GroupMemberDto;
import com.rkc.zds.resource.dto.GroupMemberElementDto;
import com.rkc.zds.resource.service.ContactService;
import com.rkc.zds.resource.service.GroupMemberService;

@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
@RequestMapping(value = "/api/group/member")
public class GroupMemberController {

	@Autowired
	GroupMemberService groupMemberService;

	@Autowired
	ContactService contactService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<GroupMemberElementDto>> getGroupMembers(@PathVariable int id, Pageable pageable,
			HttpServletRequest req) {
		Page<GroupMemberDto> groupMembersPage = groupMemberService.findGroupMembers(pageable, id);

		List<GroupMemberDto> contents = groupMembersPage.getContent();
		List<GroupMemberElementDto> memberList = new ArrayList<GroupMemberElementDto>();

		ContactDto contact;
		for (GroupMemberDto element : contents) {
			contact = contactService.getContact(element.getContactId());
			//ignore contacts that may have been deleted
			if (contact != null) {
				GroupMemberElementDto newElement = new GroupMemberElementDto();
				newElement.setId(element.getId());
				newElement.setGroupId(element.getGroupId());
				newElement.setContactId(contact.getId());
				newElement.setFirstName(contact.getFirstName());
				newElement.setLastName(contact.getLastName());
				newElement.setCompany(contact.getCompany());

				memberList.add(newElement);
			}
			else {
				//delete the group member, the contact no longer exists
				groupMemberService.deleteGroupMember(element.getId());
			}
		}

		PageRequest pageRequest = PageRequest.of(groupMembersPage.getNumber(), groupMembersPage.getSize());

		PageImpl<GroupMemberElementDto> page = new PageImpl<GroupMemberElementDto>(memberList, pageRequest,
				groupMembersPage.getTotalElements());

		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/filtered/{groupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ContactDto>> findFilteredContacts(@PathVariable int groupId, Pageable pageable, HttpServletRequest req) {
		Page<ContactDto> page = groupMemberService.findFilteredContacts(pageable, groupId);
		ResponseEntity<Page<ContactDto>> response = new ResponseEntity<>(page, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/{groupId}/{contactId}", method = RequestMethod.POST)
	public void createGroupMember(@PathVariable int groupId, @PathVariable int contactId) {
		GroupMemberDto groupMember = new GroupMemberDto();
		groupMember.setGroupId(groupId);
		groupMember.setContactId(contactId);
		groupMemberService.saveGroupMember(groupMember);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteGroupMember(@PathVariable int id) {
		groupMemberService.deleteGroupMember(id);
		return Integer.toString(id);
	}
}
