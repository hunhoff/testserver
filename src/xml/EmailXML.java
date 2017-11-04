package xml;
import java.util.LinkedList;

/**
 *******************************************************************************
 *Class responsible to manage the Email configuration properties.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class EmailXML {

	private String smtpName = "";
	private String msgTxt = "";
	private String subjectTxt = "";
	private String fromAddress = "";
	private LinkedList<String> toAddressList = new LinkedList<String>();
	
	/** Add a address to the mail LinkedList containing the addresses that will receive the message.
	 * @param toAddress - address to be added.*/ 
	public void addtoAddressList(String toAddress){
		toAddressList.add(toAddress);
	}
	/** @return String containing the mail of the sender.*/
	public String getFromAddress() {
		return fromAddress;
	}
	/** @param fromAddress - set the mail of the sender.*/
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	/** @return String containing the message content.*/
	public String getMsgTxt() {
		return msgTxt;
	}
	/** @param msgTxt - set the message content.*/
	public void setMsgTxt(String msgTxt) {
		this.msgTxt = msgTxt;
	}
	/** @return String containing the Simple Mail Transfer Protocol (SMTP)*/
	public String getSmtpName() {
		return smtpName;
	}
	/** @param smtpName - set the Simple Mail Transfer Protocol (SMTP)*/
	public void setSmtpName(String smtpName) {
		this.smtpName = smtpName;
	}
	/** @return String containing the subject content.*/
	public String getSubjectTxt() {
		return subjectTxt;
	}
	/** @param subjectTxt - set the subject content.*/
	public void setSubjectTxt(String subjectTxt) {
		this.subjectTxt = subjectTxt;
	}
	/** @return a LinkedList containing the addresses that will receive the message.*/
	public LinkedList<String> getToAddressList() {
		return toAddressList;
	}
	/** @param toAddressList - set the addresses that will receive the message.*/
	public void setToAddressList(LinkedList<String> toAddressList) {
		this.toAddressList = toAddressList;
	}
}
