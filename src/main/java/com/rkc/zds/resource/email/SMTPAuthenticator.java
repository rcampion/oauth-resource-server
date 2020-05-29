package com.rkc.zds.resource.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {
	SMTPAuthenticator() {
	}

	public PasswordAuthentication getPasswordAuthentication() {
		String userName = "richard.campion";
		String password = "ChangeIt";
		return new PasswordAuthentication(userName, password);
	}
}
