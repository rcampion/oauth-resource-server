package com.rkc.zds.resource.model;

public class EMailSend {
	  private String emailSubjectTxt = "Contact Request";
	  private String emailList = "richard.campion@zdslogic.com";
	  private String emailFirstName;
	  private String emailFromAddress;
	  private String emailMsgTxt;
	  
	public String getEmailSubjectTxt() {
		return emailSubjectTxt;
	}
	public void setEmailSubjectTxt(String emailSubjectTxt) {
		this.emailSubjectTxt = emailSubjectTxt;
	}
	public String getEmailList() {
		return emailList;
	}
	public void setEmailList(String emailList) {
		this.emailList = emailList;
	}
	public String getEmailFirstName() {
		return emailFirstName;
	}
	public void setEmailFirstName(String emailFirstName) {
		this.emailFirstName = emailFirstName;
	}
	public String getEmailFromAddress() {
		return emailFromAddress;
	}
	public void setEmailFromAddress(String emailFromAddress) {
		this.emailFromAddress = emailFromAddress;
	}
	public String getEmailMsgTxt() {
		return emailMsgTxt;
	}
	public void setEmailMsgTxt(String emailMsgTxt) {
		this.emailMsgTxt = emailMsgTxt;
	}
	  

}
