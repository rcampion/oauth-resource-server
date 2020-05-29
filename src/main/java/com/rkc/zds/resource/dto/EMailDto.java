package com.rkc.zds.resource.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the PCM_CONTACT_EMAILS database table.
 * 
 */
@Entity
@Table(name="PCM_CONTACT_EMAILS")
public class EMailDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int emailId;

	@Column(name="CONTACT_ID")
	private int contactId;

	@Column(name="EMAIL")
	private String email;
	
	@Column(name="EMAILKIND")
	private int emailKind;
	
	public int getEmailId() {
		return emailId;
	}

	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEmailKind() {
		return emailKind;
	}

	public void setEmailKind(int emailKind) {
		this.emailKind = emailKind;
	}

	public EMailDto() {
    }
}
    