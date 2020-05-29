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
import com.rkc.zds.resource.dto.UserContactDto;
import com.rkc.zds.resource.dto.UserContactElementDto;
import com.rkc.zds.resource.service.ContactService;
import com.rkc.zds.resource.service.UserContactsService;

@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
@RequestMapping(value = "/api/user/contacts")
public class UserContactsController {

	@Autowired
	UserContactsService userContactsService;

	@Autowired
	ContactService contactService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<UserContactElementDto>> getUserContacts(@PathVariable int id, Pageable pageable,
			HttpServletRequest req) {
		Page<UserContactDto> userContactsPage = userContactsService.findUserContacts(pageable, id);

		List<UserContactDto> contents = userContactsPage.getContent();
		List<UserContactElementDto> userContactsList = new ArrayList<UserContactElementDto>();

		ContactDto contact;
		for (UserContactDto element : contents) {
			contact = contactService.getContact(element.getContactId());
			//ignore contacts that may have been deleted
			if (contact != null) {
				UserContactElementDto newElement = new UserContactElementDto();
				newElement.setId(element.getId());
				newElement.setUserId(element.getUserId());
				newElement.setContactId(contact.getId());
				newElement.setFirstName(contact.getFirstName());
				newElement.setLastName(contact.getLastName());
				newElement.setCompany(contact.getCompany());

				userContactsList.add(newElement);
				
				// update the user contacts info
				element.setFirstName(contact.getFirstName());
				element.setLastName(contact.getLastName());
				element.setCompany(contact.getCompany());
				element.setTitle(contact.getTitle());
				userContactsService.saveUserContact(element);
			}
			else {
				//delete the user contact, the contact no longer exists
				userContactsService.deleteUserContact(element.getId());
			}
		}

		PageRequest pageRequest = PageRequest.of(userContactsPage.getNumber(), userContactsPage.getSize());

		PageImpl<UserContactElementDto> page = new PageImpl<UserContactElementDto>(userContactsList, pageRequest,
				userContactsPage.getTotalElements());

		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/filtered/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ContactDto>> findFilteredContacts(@PathVariable int userId, Pageable pageable, HttpServletRequest req) {
		Page<ContactDto> page = userContactsService.findFilteredContacts(pageable, userId);
		ResponseEntity<Page<ContactDto>> response = new ResponseEntity<>(page, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/{userId}/{contactId}", method = RequestMethod.POST)
	public void createUserContact(@PathVariable int userId, @PathVariable int contactId) {
		UserContactDto userContact = new UserContactDto();
		userContact.setUserId(userId);
		userContact.setContactId(contactId);
		userContactsService.saveUserContact(userContact);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteUserContact(@PathVariable int id) {
		userContactsService.deleteUserContact(id);
		return Integer.toString(id);
	}
}
