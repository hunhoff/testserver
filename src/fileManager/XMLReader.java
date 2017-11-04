package fileManager;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import xml.ConfigXML;
import xml.EmailXML;
import xml.QTPResultsXML;
import xml.RegKey;
import xml.RegKeyString;
import xml.TestsXML;
import xml.VcemInputXML;
import xml.VcemOutputXML;

/**
 *******************************************************************************
 *Class responsible to read the XML files and manage their informations.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class XMLReader {

	private String fileName;
	private String log = "";

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	/**
	 *************************************************************************************
	 * Class contructor. 
	 * @param fileName - local and name where the XML file is located.
	 *************************************************************************************
	 */
	public XMLReader(String fileName) {
		this.fileName = fileName;
	}

	/**
	 *************************************************************************************
	 * Read a XML file with the following format:
	 * <pre>
	 * < MvcdInstallOutput>
	 *   < Status>Status< /Status>
	 *   < ErrorCode>ErrorCode< /ErrorCode>
	 *   < ErrorMessage>ErrorMessage< /ErrorMessage> 
	 * < /MvcdInstallOutput>
	 * </pre>
	 * @return VcemOutputXML containing the XML information.
	 *************************************************************************************
	 */
	public VcemOutputXML ReadVcemOutputXML() {

		VcemOutputXML vcemXML = new VcemOutputXML();

		try {
			List elements = loadFile();
			Iterator i = elements.iterator();    		

			while(i.hasNext()) {  

				Element element = (Element) i.next();

				if (element.getName().equals("Status")){
					vcemXML.setStatus(element.getText());
				}
				if (element.getName().equals("ErrorCode")){
					vcemXML.setErrorCode(element.getText());
				}
				if (element.getName().equals("ErrorMessage")){
					vcemXML.setErrorMessage(element.getText());
				}
			}
		}
		catch (JDOMException e) {
			log += "Error reading VcemOutput XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			log += "Error reading VcemOutput XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		return vcemXML;
	}

	/**
	 *************************************************************************************
	 * Read a XML file with the following format:
	 * <pre>
	 * < key-list>
	 *   < key path=''>
	 *     < string-list>
	 *       < string name='' data=''>< /string>
	 *       < string name='' data=''>< /string>
	 *     < /string-list>
	 *   < /key>
	 *   < key path=''>
	 *     < string-list>
	 *       < string name='' data=''>< /string>
	 *       < string name='' data=''>< /string>
	 *     < /string-list>
	 *   < /key>
	 * < /key-list>
	 * </pre>
	 * @return RegKey LinkedList containing the XML information.
	 *************************************************************************************
	 */
	public LinkedList<RegKey> ReadRegistryXML() {

		RegKeyString keyString;
		RegKey regKey;
		LinkedList<RegKey> regKeyList = new LinkedList<RegKey>(); 

		try {
			List elements = loadFile();
			Iterator i = elements.iterator();    		

			while(i.hasNext()) {  
				Element element = (Element) i.next();
				regKey = new RegKey();
				regKey.setKey(element.getAttributeValue("path"));

				if (element.getChild("string-list") != null){
					Element stringListChild = element.getChild("string-list");

					if (stringListChild.getChild("string") != null){
						List stringChildrens = stringListChild.getChildren();

						Iterator it = stringChildrens.iterator();
						while(it.hasNext()){
							Element string = (Element) it.next();
							keyString = new RegKeyString();
							keyString.setName(string.getAttributeValue("name"));
							keyString.setData(string.getAttributeValue("data"));
							regKey.addRegString(keyString);
						}
					}
				}
				regKeyList.add(regKey);
			}
		}
		catch (JDOMException e) {
			log += "Error reading registry XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			log += "Error reading registry XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		return regKeyList;
	}

	/**
	 *************************************************************************************
	 * Read a XML file with the following format:
	 * <pre>
	 * < MvcdInstallInput>
	 *   < Action value="" />
	 *   < XMLOutput value="" />
	 *   < Lang id="" />
	 * < /MvcdInstallInput>
	 * </pre>
	 * @return VcemInputXML containing the XML information.
	 *************************************************************************************
	 */
	public VcemInputXML ReadVcemInputXML() {

		VcemInputXML vcemXML = new VcemInputXML();

		try {
			List elements = loadFile();
			Iterator i = elements.iterator();    		

			while(i.hasNext()) {  

				Element element = (Element) i.next();
				if (element.getName().equals("Action")){
					vcemXML.setActionValue(element.getAttributeValue("value"));
				}
				if (element.getName().equals("XMLOutput")){
					vcemXML.setXMLOutputValue(element.getAttributeValue("value"));
				}
				if (element.getName().equals("Lang")){
					vcemXML.setLangID(element.getAttributeValue("id"));
				}
				if (element.getName().equals("InstallType")){
					vcemXML.setInstallType(element.getAttributeValue("value"));
				}
			}
		}
		catch (JDOMException e) {
			log += "Error reading input XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			log += "Error reading input XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		return vcemXML;
	}

	/**
	 *************************************************************************************
	 * Read a XML file with the following format:
	 * <pre>
	 * < tests>
	 *   < installation>< /installation>
	 *   < DeployedFiles>< /DeployedFiles>
	 *   < InstLog>< /InstLog>
	 *   < InstRegKeys>< /InstRegKeys>
	 *   < InstVCEMOutput>< /InstVCEMOutput>
	 *   < InstEnvVars>< /InstEnvVars>
	 *   < QTPTests>< /QTPTests>
	 *   < Uninstallation>< /Uninstallation>
	 *   < RemovedFiles>< /RemovedFiles>
	 *   < UninstLog>< /UninstLog>
	 *   < UninstRegKeys>< /UninstRegKeys>
	 *   < UninstEnvVars>< /UninstEnvVars>
	 * < /tests>
	 * </pre>
	 * @return TestsXML containing the XML information.
	 *************************************************************************************
	 */
	public TestsXML readTestsXML() {

		TestsXML tests = new TestsXML();

		try {
			List elements = loadFile();
			Iterator i = elements.iterator();    		

			while(i.hasNext()) {  

				Element element = (Element) i.next();

				if (element.getName().equals("installation")){
					tests.setInstallation(element.getText());
				}
				if (element.getName().equals("DeployedFiles")){
					tests.setDeployedFiles(element.getText());
				}
				if (element.getName().equals("InstLog")){
					tests.setInstLog(element.getText());
				}
				if (element.getName().equals("InstRegKeys")){
					tests.setInstRegKeys(element.getText());
				}
				if (element.getName().equals("InstVCEMOutput")){
					tests.setInstVCEMOutput(element.getText());
				}
				if (element.getName().equals("InstEnvVars")){
					tests.setInstEnvVars(element.getText());
				}
				if (element.getName().equals("QTPTests")){
					tests.setQTPTests(element.getText());
				}
				if (element.getName().equals("Uninstallation")){
					tests.setUninstallation(element.getText());
				}
				if (element.getName().equals("RemovedFiles")){
					tests.setRemovedFiles(element.getText());
				}
				if (element.getName().equals("UninstLog")){
					tests.setUninstLog(element.getText());
				}
				if (element.getName().equals("UninstRegKeys")){
					tests.setUninstRegKeys(element.getText());
				}
				if (element.getName().equals("UninstEnvVars")){
					tests.setUninstEnvVars(element.getText());
				}
			}
		}
		catch (JDOMException e) {
			log += "Error reading tests XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			log += "Error reading tests XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		return tests;
	}

	/**
	 *************************************************************************************
	 * Read a XML file with the following format:
	 * <pre>
	 * < CONFIG>
	 *   < BUILD>< /BUILD>
	 *   < PROXY_HOST>< /PROXY_HOST>
	 *   < PROXY_PORT>< /PROXY_PORT>
	 *   < VCEM_INSTALL_PATH>< /VCEM_INSTALL_PATH>
	 *   < HPSIM_INSTALL_PATH>< /HPSIM_INSTALL_PATH>
	 *   < DBPWD>< /DBPWD>
	 *   < INSTALLTYPE>< /INSTALLTYPE>
	 *   < LANGID>< /LANGID>
	 *   < ENV_VAR_NAME>< /ENV_VAR_NAME>
	 *   < ENV_VAR_VALUE>< /ENV_VAR_VALUE>
	 *   < TS_NAME>< /TS_NAME>
	 *   < TS_PATH>< /TS_PATH>
	 *   < URL>< /URL>
	 *   < PROJECT>< /PROJECT>
	 *   < FILE_LIST>< /FILE_LIST>
	 *   < WEBSERVER_ADDRESS>< /WEBSERVER_ADDRESS>
	 *   < WEBSERVER_EXT_ADDRESS>< /WEBSERVER_EXT_ADDRESS>
	 *   < QTPSERVER_SSH>< /QTPSERVER_SSH>
	 *   < QTPFILE>< /QTPFILE>
	 *   < QTPLOGFILE>< /QTPLOGFILE>
	 *   < QTPRESULTSFILE>< /QTPRESULTSFILE>
	 * < /CONFIG>
	 * </pre>
	 * @return ConfigXML containing the XML information.
	 *************************************************************************************
	 */
	public ConfigXML readConfigXML() {

		ConfigXML config = new ConfigXML();

		try {
			List elements = loadFile();
			Iterator i = elements.iterator();    		

			while(i.hasNext()) {  

				Element element = (Element) i.next();

				if (element.getName().equals("BUILD")){
					config.setBUILD(element.getText());
				}
				if (element.getName().equals("PROXY_HOST")){
					config.setPROXY_HOST(element.getText());
				}
				if (element.getName().equals("PROXY_PORT")){
					config.setPROXY_PORT(element.getText());
				}
				if (element.getName().equals("VCEM_INSTALL_PATH")){
					config.setVCEM_INSTALL_PATH(element.getText());
				}
				if (element.getName().equals("HPSIM_INSTALL_PATH")){
					config.setHPSIM_INSTALL_PATH(element.getText());
				}
				if (element.getName().equals("DBPWD")){
					config.setDBPWD(element.getText());
				}
				if (element.getName().equals("INSTALLTYPE")){
					config.setINSTALLTYPE(element.getText());
				}
				if (element.getName().equals("LANGID")){
					config.setLANGID(element.getText());
				}
				if (element.getName().equals("ENV_VAR_NAME")){
					config.setENV_VAR_NAME(element.getText());
				}
				if (element.getName().equals("ENV_VAR_VALUE")){
					config.setENV_VAR_VALUE(element.getText());
				}
				if (element.getName().equals("TS_NAME")){
					config.setTS_NAME(element.getText());
				}
				if (element.getName().equals("TS_PATH")){
					config.setTS_PATH(element.getText());
				}
				if (element.getName().equals("URL")){
					config.setURL(element.getText());
				}
				if (element.getName().equals("PROJECT")){
					config.setPROJECT(element.getText());
				}
				if (element.getName().equals("FILE_LIST")){
					config.setFILE_LIST(element.getText());
				}
				if (element.getName().equals("WEBSERVER_ADDRESS")){
					config.setWEBSERVER_ADDRESS(element.getText());
				}
				if (element.getName().equals("WEBSERVER_EXT_ADDRESS")){
					config.setWEBSERVER_EXT_ADDRESS(element.getText());
				}
				if (element.getName().equals("QTPSERVER_SSH")){
					config.setQTPSERVER_SSH(element.getText());
				}
				if (element.getName().equals("QTPFILE")){
					config.setQTPFILE(element.getText());
				}
				if (element.getName().equals("QTPLOGFILE")){
					config.setQTPLOGFILE(element.getText());
				}
				if (element.getName().equals("QTPRESULTSFILE")){
					config.setQTPRESULTSFILE(element.getText());
				}
			}
		}
		catch (JDOMException e) {
			log += "Error reading config XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			log += "Error reading config XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		return config;
	}

	/**
	 *************************************************************************************
	 * Read a XML file with the following format:
	 * <pre>
	 * < testresult-list>
	 *   < testresult>
	 *     < name>< /name>
	 *     < description>< /description>
	 *     < result>< /result>
	 *     < comment-list>
	 *       < comment>< /comment>
	 *       < comment>< /comment>
	 *     < /comment-list>
	 *   < /testresult>
	 *   < testresult>
	 *     < name>< /name>
	 *     < description>< /description>
	 *     < result>< /result>
	 *     < comment-list>
	 *       < comment>< /comment>
	 *       < comment>< /comment>
	 *     < /comment-list>
	 *   < /testresult>
	 * < /testresult-list>
	 * </pre>
	 * @return QTPResultsXML LinkedList containing the XML information.
	 *************************************************************************************
	 */
	public LinkedList<QTPResultsXML> readQTPResultsXML() {

		LinkedList<QTPResultsXML> qtpResultList = new LinkedList<QTPResultsXML>();
		QTPResultsXML qtp;

		try {
			List elements = loadFile();
			Iterator i = elements.iterator();    		

			while(i.hasNext()) {  
				qtp = new QTPResultsXML();
				Element element = (Element) i.next();

				if (element.getChild("name") != null){
					qtp.setName(element.getChildText("name"));
				}

				if (element.getChild("description") != null){
					qtp.setDescription(element.getChildText("description"));
				}

				if (element.getChild("result") != null){
					qtp.setResult(element.getChildText("result"));
				}

				if (element.getChild("comment-list") != null){
					Element commentListChild = element.getChild("comment-list");

					if (commentListChild.getChild("comment") != null){
						List commentChildrens = commentListChild.getChildren();

						Iterator it = commentChildrens.iterator();
						while(it.hasNext()){
							Element comment = (Element) it.next();
							qtp.addComment(comment.getText());
						}
					}
				}
				qtpResultList.add(qtp);
				//System.out.println(element.getName());
			}
		}
		catch (JDOMException e) {
			log += "Error reading QTP results XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			log += "Error reading QTP results XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		return qtpResultList;
	}

	/**
	 *************************************************************************************
	 * Load the XML file.
	 * @return This method returns all children as a list.
	 * @throws JDOMException
	 * @throws IOException
	 *************************************************************************************
	 **/
	private List loadFile() throws JDOMException, IOException {
		File f = new File(fileName);
		SAXBuilder sb = new SAXBuilder();   		  
		Document d = sb.build(f);   		  
		Element element = d.getRootElement();   
		return element.getChildren();   
	}

	/**
	 *************************************************************************************
	 * Read a XML file with the following format:
	 * <pre>
	 * < email>
	 *   < smtpName></smtpName>
	 *   < msgTxt></msgTxt>
	 *   < subjectTxt></subjectTxt>
	 *   < fromAddress></fromAddress>
	 *   < toAddress-list>
	 *     < toAddress></toAddress>
	 *     < toAddress></toAddress>
	 *   < /toAddress-list>
	 * < /email>
	 * </pre>
	 * @return EmailXML containing the XML information.
	 *************************************************************************************
	 */	
	public EmailXML readEmailXML() {

		EmailXML email = new EmailXML();

		try {
			List elements = loadFile();
			Iterator i = elements.iterator();    		

			while(i.hasNext()) {  

				Element element = (Element) i.next();
				if (element.getName().equals("smtpName")){
					email.setSmtpName(element.getText());
				}
				if (element.getName().equals("msgTxt")){
					email.setMsgTxt(element.getText());
				}
				if (element.getName().equals("subjectTxt")){
					email.setSubjectTxt(element.getText());
				}
				if (element.getName().equals("fromAddress")){
					email.setFromAddress(element.getText());
				}
				if (element.getName().equals("toAddress-list")){
					List toAddressChildrens = element.getChildren();

					Iterator it = toAddressChildrens.iterator();
					while(it.hasNext()){
						Element toAddress = (Element) it.next();
						email.addtoAddressList(toAddress.getText());
					}
				}
			}
		}
		catch (JDOMException e) {
			log += "Error reading email XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			log += "Error reading email XML: "+e.getMessage();
			System.out.println(e.getMessage());
		}
		return email;
	}
}
