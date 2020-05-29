package com.rkc.zds.resource.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.ContactDto;
import com.rkc.zds.resource.dto.GroupMemberDto;
import com.rkc.zds.resource.repository.ContactRepository;
import com.rkc.zds.resource.repository.GroupMemberRepository;
import com.rkc.zds.resource.service.ContactService;
import com.rkc.zds.resource.util.SearchCriteria;

@Service
public class ContactServiceImpl implements ContactService {
	private static final int PAGE_SIZE = 50;

    @PersistenceContext
    private EntityManager entityManager;
    
	@Autowired
	private ContactRepository contactRepo;

	@Autowired
	private GroupMemberRepository groupMemberRepo;
	
	@Override
	public Page<ContactDto> findContacts(Pageable pageable) {

		return contactRepo.findAll(pageable);
	}
	
	@Override
	public Page<ContactDto> findFilteredContacts(Pageable pageable, int groupId) {

		List<ContactDto> contacts = contactRepo.findAll();

		List<GroupMemberDto> memberList = groupMemberRepo.findByGroupId(groupId);
		
		List<ContactDto> testList = new ArrayList<ContactDto>();

		List<ContactDto> filteredList = new ArrayList<ContactDto>();

		// build member list of Contacts
		Optional<ContactDto> contact;
		for (GroupMemberDto element : memberList) {
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
	public ContactDto getContact(int id) {
	
		Optional<ContactDto> contact = contactRepo.findById(id);
		if(contact.isPresent())
			return contact.get();
		else
			return null;
	}

	@Override
	public Page<ContactDto> searchContacts(String name) {

		final PageRequest pageRequest = PageRequest.of(0, 10, sortByNameASC());

		return contactRepo.findByLastNameIgnoreCaseLike(pageRequest, "%" + name + "%");
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void saveContact(ContactDto contact) {

		contactRepo.save(contact);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updateContact(ContactDto contact) {

		contactRepo.saveAndFlush(contact);
	}

	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteContact(int id) {

		contactRepo.deleteById(id);
	}

	private Sort sortByNameASC() {
		return Sort.by(Sort.Direction.ASC, "lastName");
	}

	@Override
	public Page<ContactDto> searchContacts(Pageable pageable, List<SearchCriteria> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ContactDto> searchContacts(Pageable pageable, Specification<ContactDto> spec) {
		return contactRepo.findAll(spec, pageable);
	}

}
