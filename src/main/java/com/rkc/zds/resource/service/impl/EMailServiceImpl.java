package com.rkc.zds.resource.service.impl;

import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.EMailDto;
import com.rkc.zds.resource.email.SendMailUsingAuthentication;
import com.rkc.zds.resource.model.EMailSend;
import com.rkc.zds.resource.repository.EMailRepository;
import com.rkc.zds.resource.service.EMailService;

@Service
public class EMailServiceImpl implements EMailService {

	@Autowired
	private EMailRepository eMailRepo;

	@Override
	public Page<EMailDto> findEMails(Pageable pageable, int contactId) {

		Page<EMailDto> page = eMailRepo.findByContactId(pageable, contactId);

		return page;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void saveEMail(EMailDto email) {

		eMailRepo.save(email);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updateEMail(EMailDto email) {

		eMailRepo.saveAndFlush(email);

	}

	@Override
	public void deleteEMail(int id) {

		eMailRepo.deleteById(id);

	}

	@Override
	public EMailDto getEMail(int id) {

		Optional<EMailDto> email = eMailRepo.findById(id);
		if (email.isPresent())
			return email.get();
		else
			return null;
	}

	@Override
	public void sendEMail(EMailSend emailSend) {
		SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
		try {
			String[] array = emailSend.getEmailList().split(",");
			smtpMailSender.postMail(array, emailSend.getEmailSubjectTxt(), emailSend.getEmailMsgTxt(),
					emailSend.getEmailFromAddress());

		} catch (MessagingException e) {
			e.printStackTrace();

		}
	}

}
