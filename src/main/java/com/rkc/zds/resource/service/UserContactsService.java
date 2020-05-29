package com.rkc.zds.resource.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rkc.zds.resource.dto.ContactDto;
import com.rkc.zds.resource.dto.UserContactDto;

public interface UserContactsService {
    Page<UserContactDto> findUserContacts(Pageable pageable, int userId);

    Page<ContactDto> findFilteredContacts(Pageable pageable, int userId);    

    List<UserContactDto> findAllUserContacts(int userId);
    
    List<UserContactDto> getAllUserContacts();

    public void addUserContact(UserContactDto userContact);
    
    public void saveUserContact(UserContactDto userContact); 
    
	void deleteUserContact(int id);
}
