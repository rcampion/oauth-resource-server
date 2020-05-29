package com.rkc.zds.resource.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rkc.zds.resource.dto.EMailDto;
import com.rkc.zds.resource.model.EMailSend;

public interface EMailService {
    Page<EMailDto> findEMails(Pageable pageable, int contactId);
       
    EMailDto getEMail(int id);  
   
    public void saveEMail(EMailDto email);
        
    public void updateEMail(EMailDto email);
      
	void deleteEMail(int id);

	void sendEMail(EMailSend emailSend);
}
