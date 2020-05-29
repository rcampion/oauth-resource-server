package com.rkc.zds.resource.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rkc.zds.resource.dto.PhoneDto;

public interface PhoneService {
    Page<PhoneDto> findPhones(Pageable pageable, int contactId);
    
    PhoneDto getPhone(int id);  
    
    public void savePhone(PhoneDto phone);
      
    public void updatePhone(PhoneDto phone);
 
	void deletePhone(int id);
}
