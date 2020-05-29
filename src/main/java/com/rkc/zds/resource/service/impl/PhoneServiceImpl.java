package com.rkc.zds.resource.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.PhoneDto;
import com.rkc.zds.resource.repository.PhoneRepository;
import com.rkc.zds.resource.service.PhoneService;

@Service
public class PhoneServiceImpl implements PhoneService {

	@Autowired
	private PhoneRepository phoneRepo;

	@Override
	public Page<PhoneDto> findPhones(Pageable pageable, int contactId) {

		Page<PhoneDto> page = phoneRepo.findByContactId(pageable, contactId);

		return page;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void savePhone(PhoneDto phone) {

		phoneRepo.save(phone);
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updatePhone(PhoneDto phone) {

		phoneRepo.saveAndFlush(phone);
		
	}
	
	@Override
	public void deletePhone(int id) {

		phoneRepo.deleteById(id);

	}

	@Override
	public PhoneDto getPhone(int id) {
	
		Optional<PhoneDto> phone = phoneRepo.findById(id);
		if(phone.isPresent())
			return phone.get();
		else
			return null;
	}

}
