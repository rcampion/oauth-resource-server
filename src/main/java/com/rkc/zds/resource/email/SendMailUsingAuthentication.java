package com.rkc.zds.resource.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailUsingAuthentication
{
  private static final String SMTP_HOST_NAME = "mail.zdslogic.com";
  private static final String SMTP_AUTH_USER = "richard.campion";
  private static final String SMTP_AUTH_PWD = "ChangeIt";
  private static final String emailMsgTxt = "Contact Confirmation Message. Also include the Tracking Number.";
  private static final String emailSubjectTxt = "Contact Confirmation Subject";
  private static final String emailFromAddress = "richard.campion@zdslogic.com";
  private static final String[] emailList = { "richard.campion@zdslogic.com" };
  
  public static void main(String[] args)
    throws Exception
  {
    SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
    smtpMailSender.postMail(emailList, "Contact Confirmation Subject", "Contact Confirmation Message. Also include the Tracking Number.", 
      "richard.campion@zdslogic.com");
    System.out.println("Sucessfully Sent mail to All Users");
  }
  
  public void postMail(String[] recipients, String subject, String message, String from)
    throws MessagingException
  {
    boolean debug = false;
    
    Properties props = new Properties();
    props.put("mail.smtp.host", "mail.zdslogic.com");
    props.put("mail.smtp.auth", "true");
    
    Authenticator auth = new SMTPAuthenticator();
    Session session = Session.getDefaultInstance(props, auth);
    
    session.setDebug(debug);
    
    Message msg = new MimeMessage(session);
    
    InternetAddress addressFrom = new InternetAddress(from);
    msg.setFrom(addressFrom);
    
    InternetAddress[] addressTo = new InternetAddress[recipients.length];
    for (int i = 0; i < recipients.length; i++) {
      addressTo[i] = new InternetAddress(recipients[i]);
    }
    msg.setRecipients(Message.RecipientType.TO, addressTo);
    
    msg.setSubject(subject);
    msg.setContent(message, "text/plain");
    Transport.send(msg);
  }
  

}
