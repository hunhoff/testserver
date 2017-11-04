package mail;
import javax.mail.*;
import javax.mail.internet.*;

import xml.EmailXML;
import main.Main;
import java.util.*; 

/**
 ****************************************
 *Class responsible to send mail.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 ****************************************
 **/

public class PostMail {

	private LinkedList<String> toAddressList = new LinkedList<String>();
	
	/**
	 *******************************************************************************
	 * Method responsible to send mail.
	 * @param emailXML - informations needed to send the mail.
	 * @param TestServerName - String containing the Test Server name.
	 * @param htmlAddress - String containing the html address to the log file.
	 * @param ftpAddress - String containing the ftp address to the log files.
	 * @throws MessagingException
	 *******************************************************************************
	 **/	
	public void postMail(EmailXML emailXML, String TestServerName, String htmlAddress, String ftpAddress) throws MessagingException
	{
		boolean debug = false;

		Properties props = new Properties();
		props.put("mail.smtp.host", emailXML.getSmtpName());
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);

		Message msg = new MimeMessage(session);

		InternetAddress addressFrom = new InternetAddress(emailXML.getFromAddress());
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[emailXML.getToAddressList().size()]; 

		toAddressList = emailXML.getToAddressList();

		int i = 0;
		Iterator it = toAddressList.iterator();
		while(it.hasNext()){
			addressTo[i] = new InternetAddress(it.next().toString());
			i++;
		}

		msg.setRecipients(Message.RecipientType.TO, addressTo);
		
		String txt = emailXML.getMsgTxt();
		if(txt.contains("$TS"))
			txt = txt.replace("$TS", TestServerName);
		if(txt.contains("$ts"))
			txt = txt.replace("$ts", TestServerName);
		if(txt.contains("$html"))
			txt = txt.replace("$html", htmlAddress);
		if(txt.contains("$HTML"))
			txt = txt.replace("$HTML", htmlAddress);
		if(txt.contains("$ftp"))
			txt = txt.replace("$ftp", ftpAddress);
		if(txt.contains("$FTP"))
			txt = txt.replace("$FTP", ftpAddress);
		
		msg.setSubject(emailXML.getSubjectTxt());
		msg.setContent(txt, "text/plain");
		Transport.send(msg);
	}
}