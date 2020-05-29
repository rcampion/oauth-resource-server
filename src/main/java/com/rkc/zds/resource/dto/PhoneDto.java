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
@Table(name="PCM_CONTACT_PHONES")
public class PhoneDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer phoneId;

	@Column(name="CONTACT_ID")
	private int contactId;

	@Column(name="PHONE")
	private String phone;
	
	@Column(name="PHONEKIND")
	private int phoneKind;

	public Integer getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(Integer phoneId) {
		this.phoneId = phoneId;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPhoneKind() {
		return phoneKind;
	}

	public void setPhoneKind(int phoneKind) {
		this.phoneKind = phoneKind;
	}

	public PhoneDto() {
    }
}
    