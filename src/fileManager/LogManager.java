package fileManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;

import xml.VcemInputXML;

/**
 *******************************************************************************
 *Class responsible to manage the log files.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class LogManager {

	private FileWriter writer; 
	private PrintWriter exit;
	private String filePath;
	private String log;
	private boolean fail = false;
	private GregorianCalendar date;
	private String ftpAddress = "";
	private String htmlAddress = "";

	/** @return String containing the html address to the log file.*/
	public String getHtmlAddress() {
		return htmlAddress;
	}
	/** @param htmlAddress - set the html address to the log file.*/
	public void setHtmlAddress(String htmlAddress) {
		this.htmlAddress = htmlAddress;
	}
	/** @return String containing the ftp address to the log files.*/
	public String getFtpAddress() {
		return ftpAddress;
	}
	/** @param ftpAddress - set the ftp address to the log files.*/
	public void setFtpAddress(String ftpAddress) {
		this.ftpAddress = ftpAddress;
	}

	/**
	 *******************************************************************************************
	 * Read a file and add each line in a position of a linkedlist.
	 * @param fileName - location and name of the file to read.
	 * @return LinkedList containing in each position a entire line of the file.
	 *******************************************************************************************
	 **/

	public LinkedList<String> Reader(String fileName) 
	{
		LinkedList<String> list = new LinkedList<String>(); 
		String line = null;
		try	{
			FileReader in = new FileReader(fileName);
			BufferedReader buff = new BufferedReader(in);
			while((line = buff.readLine()) !=null) 
				list.add(line);		

		} catch( IOException ex ) {
			return list; 
		}
		return list;
	}

	/**
	 *******************************************************************************************
	 * Create a new file and directory (if doesn't exist already). 
	 * @param path - location where the file will be created.
	 * @param filename - name that the file will receive.
	 *******************************************************************************************
	 */
	public void newFile(String path, String filename)
	{
		try{
			boolean success = (new File(path)).mkdirs();
		}catch (Exception e){
			log = "Error creating directory.";
			log+= "<br>- " + e.getMessage();
		}
		this.filePath = path;
		String tmp = path+filename;

		try {
			writer = new FileWriter(tmp);
			exit = new PrintWriter(writer);
		} catch( IOException ex ) {
			log = "<br>Error creating log file.";
			log+= "<br>- " + ex.getMessage(); 
		}
	}

	/**
	 *******************************************************************************************
	 * Write a message in the file created by the method newFile().
	 * @param message - String containing the message that will be writed in the file.
	 *******************************************************************************************
	 **/
	public void writer(String message){
		if(!exit.checkError())
			this.exit.println(message);
		else System.out.println("Error writing log file.");
	}
	
	/**
	 *******************************************************************************************
	 *  @return String containing the current location where the application is being executed. 
	 * Note that this does not work using ssh.
	 *******************************************************************************************
	 **/
	public String getCurrentPath(){
		String currentPath = "";
		File f = new File("");
		currentPath = f.getAbsolutePath() + File.separator;
		return currentPath;
	} 

	/**
	 *******************************************************************************************
	 * Return the path to the file.
	 * @return string containing the path to the file created by the method newFile() .
	 ******************************************************************************************* 
	 **/
    public String getFilePath() {
		return filePath;
	}

	/**
	 *******************************************************************************************
	 * Contains the necessary information to be inserted at the begining of the HTML log.
	 * @param path - location where the file will be created.
	 * @param filename - name that the file will receive.
	 *******************************************************************************************
	 */
	public void startHTMLLog(String path, String filename){

		newFile(path, filename);

		writer("<HTML>");
		writer("<HEAD>");
		writer("<META HTTP-EQUIV="+(char)34+"charset"+(char)34+" CONTENT="+(char)34+"utf-8"+(char)34+">");
		writer("<TITLE>TEST SERVER LOG</TITLE>");
		writer("<STYLE TYPE="+(char)34+"text/css"+(char)34+">");
		writer("<!--");
		writer("table caption {color: darkblue; font-size: x-large; font-weight: bold}");
		writer("td {align: left; font-weight: bold}");
		writer("td.fail {color: darkred; width: 10% }");
		writer("td.warn {color: darkgoldenrod; width: 10% }");
		writer("td.pass {color: darkgreen; width: 10% }");
		writer("td.unkn {color: darkcyan; width: 10% }");
		writer("td.info {width: 10% }");
		writer("td.time {width: 30%; font-weight: normal}");
		writer("td.note {align: left; font-weight: bold}");
		writer("li {font-weight: normal}");
		writer("-->");
		writer("</STYLE>");
		writer("</HEAD>");
		writer("<TABLE WIDTH="+(char)34+"100%"+(char)34+"border="+(char)34+"1"+(char)34+"rules="+(char)34+"rows"+(char)34+" summary="+(char)34+"TEST SERVER LOG"+(char)34+"><BODY>");
	}

	/** Contains the necessary information to be inserted in the end of the HTML log.**/
	public void endHTMLLog(){
		writer("</TABLE> ");
		writer("</HTML> ");
		writer("");
	}

	/**
	 * ******************************************************************************************
	 * Write HTML log content.
	 * @param test brief test description 
	 * @param result test result (warn, pass, unknown, not executed, done and fail)
	 * @param additionalInfo detailed informations about the test
	 * ******************************************************************************************
	 */

	public void HTMLLogWriter(String test, String result, String additionalInfo){

		date = new GregorianCalendar();
		writer("<TR>");
		writer("<TD CLASS="+(char)34+"time"+(char)34+">"+date.getTime()+"</TD>");
		writer("<TD CLASS="+(char)34+"note"+(char)34+">"+test+"</TD>");

		if(result.equalsIgnoreCase("warn")){
			writer("<TD CLASS="+(char)34+"warn"+(char)34+">WARNING</TD>");
			fail = true;
		}
		else if(result.equalsIgnoreCase("pass")){
			writer("<TD CLASS="+(char)34+"pass"+(char)34+">PASS</TD>");
		}
		else if(result.equalsIgnoreCase("unknown")){
			writer("<TD CLASS="+(char)34+"unkn"+(char)34+">UNKNOWN</TD>");
		}
		else if(result.equalsIgnoreCase("not executed")){
			writer("<TD CLASS="+(char)34+"note"+(char)34+">NOT EXECUTED</TD>");
		}
		else if(result.equalsIgnoreCase("done")){
			writer("<TD CLASS="+(char)34+"note"+(char)34+">DONE</TD>");
		}
		else {
			writer("<TD CLASS="+(char)34+"fail"+(char)34+">FAIL</TD>");
			fail = true;
		}


		writer("</TR>");
		writer("<TR><TD COLSPAN=3><UL><LI><I>"+additionalInfo+" </I></LI></UL></TD></TR>");
		writer(" ");
	}

	/**
	 * ******************************************************************************************
	 * Read and write again the index.html file with the new log information.
	 * @param TestServerpath location where the log files are found
	 * @param LogFileName name of the log just created
	 * @param slist LinkedList containing, in each position, a line of the Web Server index file
	 * @param WebServerLogsPath Web Server address to the logs repository 
	 * ******************************************************************************************
	 */

	public void WebServerHome(String TestServerpath, String LogFileName, LinkedList slist, String WebServerLogsPath){

		LinkedList<String> combo = new LinkedList<String>();
		boolean list = false;
		String tmp = "";

		Iterator it = slist.iterator();
		while(it.hasNext()){

			tmp = it.next().toString();

			if(tmp.equalsIgnoreCase("<COMBO>")){
				while((it.hasNext())&&(list==false)){
					tmp = it.next().toString();
					if(tmp.contains("</COMBO>")){
						list=true;
					}else {
						combo.add(tmp);
					}
				}
			}
		}

		newFile(TestServerpath, "index.html");

		writer("<html>");
		writer("<head>");
		writer("<title>TEST SERVER</title>");
		writer("</head>");
		writer("<body>");
		writer("<p align="+(char)34+"left"+(char)34+"><big><big><u><strong>TEST SERVER HOME</strong></u></big></big></p>");
		writer("<hr>");
		writer("<p>&nbsp;</p>");
		writer("<script language="+(char)34+"javascript"+(char)34+">");
		writer("function gone() {");
		writer("location=document.comboLink.go.options[document.comboLink.go.selectedIndex].value }");
		writer("</script>");

		writer("<form name="+(char)34+"comboLink"+(char)34+">"); 
		writer("<p><font face="+(char)34+"Arial"+(char)34+"><font size="+(char)34+"2"+(char)34+"><label for="+(char)34+"all"+(char)34+"><strong>PREVIOUS TESTS: </strong></label></font>");
		writer("<select name="+(char)34+"go"+(char)34+" size="+(char)34+"1"+(char)34+" onChange="+(char)34+"gone()"+(char)34+">");
		writer("<option value="+(char)34+(char)34+"></option>");

		writer("<COMBO>");

		it = combo.iterator();
		while(it.hasNext()){
			writer(it.next().toString());
		}
		writer("<option value="+(char)34+WebServerLogsPath+"/"+LogFileName+".html"+(char)34+">"+LogFileName+"</option>");

		writer("</COMBO>");

		writer("</select></p></form>");

		//set link to html log
		setHtmlAddress(WebServerLogsPath+"/"+LogFileName+".html");
		writer("<form method="+(char)34+"POST"+(char)34+" action="+(char)34+"--WEBBOT-SELF--"+(char)34+">");  
		writer("<p><font face="+(char)34+"Arial"+(char)34+"><font size="+(char)34+"2"+(char)34+"><label for="+(char)34+"all"+(char)34+"><strong>LATEST TEST EXECUTED:</strong></label></font><font size="+(char)34+"-1"+(char)34+">");
		writer("<a href="+(char)34+WebServerLogsPath+"/"+LogFileName+".html"+(char)34+">"+LogFileName+"</a></font></font>");
		writer("</p></form>");

		tmp = WebServerLogsPath;
		if(tmp.contains("http"))
			tmp = tmp.replace("http", "ftp");

		//set link to ftp logs		
		setFtpAddress(tmp+"/"+LogFileName);
		writer("<form method="+(char)34+"POST"+(char)34+" action="+(char)34+"--WEBBOT-SELF--"+(char)34+">");  
		writer("<p><font face="+(char)34+"Arial"+(char)34+"><font size="+(char)34+"2"+(char)34+"><label for="+(char)34+"all"+(char)34+"><strong>LOGS OF LATEST TEST:</strong></label></font><font size="+(char)34+"-1"+(char)34+">");
		writer("<a href="+(char)34+tmp+"/"+LogFileName+(char)34+">"+LogFileName+"</a></font></font>");
		writer("</p></form>");

		writer("</body>");
		writer("</html>");
		writer("");
	}
    
	/**
	 * *******************************************************************************************
	 * Create a file containing the new VcemInputXml file information.
	 * @param VcemInputXml - contains the new informations to be Written in the VcemInputXml file.
	 * @param path - location where the file will be created.
	 * *******************************************************************************************
	 */
	public void writeVcemInputXML(VcemInputXML VcemInputXml, String path){

		newFile(path, "input.xml");

		writer("<?xml version="+(char)34+"1.0"+(char)34+" encoding="+(char)34+"UTF-8"+(char)34+"?>"); 
		writer("<MvcdInstallInput>");
		writer("<Action value="+(char)34+VcemInputXml.getActionValue()+(char)34+"/>");
		writer("<XMLOutput value="+(char)34+VcemInputXml.getXMLOutputValue()+(char)34+"/>");
		writer("<Lang id="+(char)34+VcemInputXml.getLangID()+(char)34+"/>");
		writer("<InstallType value="+(char)34+VcemInputXml.getInstallType()+(char)34+"/>");
		writer("</MvcdInstallInput>");
		writer("");

		exit.close();
	}
	/**
	 *******************************************************************************************
	 * Return the test result.
	 * @return false if any test fails | true if all tests pass.
	 ******************************************************************************************* 
	 **/
	public boolean isFail() {
		return fail;
	}
}
