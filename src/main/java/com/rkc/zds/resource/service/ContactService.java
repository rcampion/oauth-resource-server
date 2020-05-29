package com.rkc.zds.resource.service;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.resource.dto.ContactDto;
import com.rkc.zds.resource.util.SearchCriteria;

import org.springframework.context.annotation.Bean;

@Service
public interface ContactService {

    Page<ContactDto> findContacts(Pageable pageable);

    Page<ContactDto> searchContacts(String name);
    
    Page<ContactDto> searchContacts(Pageable pageable, List<SearchCriteria> params);
    
	Page<ContactDto> searchContacts(Pageable pageable, Specification<ContactDto> spec);

    Page<ContactDto> findFilteredContacts(Pageable pageable, int groupId);
       
    ContactDto getContact(int id);    
     
    public void saveContact(ContactDto contact);
       
    public void updateContact(ContactDto contact);

	void deleteContact(int id);

}
