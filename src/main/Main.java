package main;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import mail.PostMail;
import testManager.Executer;
import testManager.PostInstallVerifications;
import testManager.PostUninstallVerifications;
import testManager.TimeController;
import xml.ConfigXML;
import xml.EmailXML;
import xml.QTPResultsXML;
import xml.RegKey;
import xml.RegKeyString;
import xml.TestsXML;
import xml.VcemInputXML;
import xml.VcemOutputXML;
import download.Download;
import download.HTTPUnit;
import fileManager.CopyFile;
import fileManager.LogManager;
import fileManager.XMLReader;

/**
 *******************************************************************************
 *Class responsible to manage Test Server.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/
public class Main 
{
	private LinkedList<String> stringlist = new LinkedList<String>();
	private LogManager file = new LogManager();
	private LogManager html = new LogManager();
	private LogManager mail = new LogManager();
	private LogManager webServer = new LogManager();
	private LogManager vcemInput = new LogManager();

	private TestsXML testsXML = new TestsXML();
	private EmailXML emailXML = new EmailXML();
	private ConfigXML config = new ConfigXML();
	private VcemInputXML vcemInputXML = new VcemInputXML();
	private VcemOutputXML vcemOutputXML = new VcemOutputXML();
	private QTPResultsXML qtpresults = new QTPResultsXML();

	private PostMail postMail = new PostMail();
	private TimeController timecontroller = new TimeController();
	private HTTPUnit http = new HTTPUnit();
	private Download download = new Download();
	private Executer exec = new Executer();
	private PostInstallVerifications postinstallver = new PostInstallVerifications();
	private PostUninstallVerifications postuninstallver = new PostUninstallVerifications();
	private CopyFile cpy = new CopyFile();
	private LinkedList<RegKey> regKeylist = new LinkedList<RegKey>();

	private boolean isDownloaded;
	private boolean isCopied;

	private static String slash = File.separator;
	private static String INSTALL_LOG_NAME = "VCEMInstallation.log";
	private static String UNINSTALL_LOG_NAME = "VCEMUninstallation.log";

	private boolean specificBuild;
	private String log = "";
	private String WEBS = "";
	private String FTPS = "";
	private String QTPS = "";
	private String QTPSLog = "";
	private String QTPResults = "";
	private String TS_LOG_NAME = "";

	public Main(){}

	/** Controll and manage the Test Server methods and conditions.
	 * @param commandlist LinkedList containing the necessary parameters to execute the Test Server (this
	 * parameter should contain the path to the config.xml file). **/
	public void execute(LinkedList<String> commandlist){

		timecontroller.startTime();
		boolean isInstalled = false, xmlinput = false;;
		String configPath = "";

		try {

			if(!commandlist.isEmpty()){
				Iterator i = commandlist.iterator();
				while(i.hasNext()){
					configPath = i.next().toString();
					if((configPath.contains("-help"))||(configPath.contains("-h"))){
						System.out.println("Test Server help: ");
						System.out.println("/XMLINPUT=<XML_input_file>");
						System.out.println("-help <show help>");
						System.exit(0);
					}
				}

				i = commandlist.iterator();
				while(i.hasNext()){
					configPath = i.next().toString();
					if(configPath.contains("/XMLINPUT=")){
						configPath = configPath.replace("/XMLINPUT=", "");
						xmlinput = true;
					}
				}
			}
			if(xmlinput==false){
				System.out.println("/XMLINPUT parameter missing!\n");
				System.out.println("Test Server help: ");
				System.out.println("/XMLINPUT=<XML_input_file>");
				System.out.println("-help <show help>");
				System.exit(1);
			}


			/*does not work using ssh*/
			//XMLReader xml = new XMLReader(file.getCurrentPath()+"config.xml");

			XMLReader xml = new XMLReader(configPath);
			config = xml.readConfigXML();

			if(config.getBUILD().equalsIgnoreCase("latestBuild"))
				specificBuild = false;
			else specificBuild = true;

			TS_LOG_NAME = config.getTS_NAME();

			GregorianCalendar gregDate = new GregorianCalendar();
			int month = gregDate.get(Calendar.MONTH)+1;
			int day = gregDate.get(Calendar.DAY_OF_MONTH);
			int year = gregDate.get(Calendar.YEAR);
			int hour = gregDate.get(Calendar.HOUR);
			int minute = gregDate.get(Calendar.MINUTE);
			int second = gregDate.get(Calendar.SECOND);

			TS_LOG_NAME = 
				TS_LOG_NAME + 
				+month+
				+day
				+year+
				"-"
				+hour+"_"+
				+minute+"_"+
				+second;

			setWebServerFolders();
			setQTPFolders();

			file.newFile(config.getTS_PATH()+"logs"+slash,TS_LOG_NAME+".log");
			html.startHTMLLog(config.getTS_PATH()+"logs"+slash,TS_LOG_NAME+".html");

			if(!specificBuild){
				if(!getLatestBuild()){
					logWriter("Error during build identification, cannot proceed!");
					html.endHTMLLog();
					logWriter("");
					System.exit(1);
				}
			}else {
				if(!getSpecificBuild(config.getBUILD())){
					logWriter("Error during build identification, cannot proceed!");
					html.endHTMLLog();
					logWriter("");
					System.exit(1);
				}
			}

			if(!download()){
				logWriter("Error during download, cannot proceed!");
				html.endHTMLLog();
				logWriter("");
				System.exit(1);
			}
			else {

				XMLReader testsReader = new XMLReader(config.getTS_PATH()+"tests.xml");
				testsXML = testsReader.readTestsXML();

				if(testsXML.getInstallation().equalsIgnoreCase("yes"))
					isInstalled = Install();
				else HTMLLogWriter("Installation...", "Installation not executed.","not executed");

				if(testsXML.getDeployedFiles().equalsIgnoreCase("yes"))
					verifyDeployedFiles();
				else HTMLLogWriter("Deployed files verification...", "Deployed files verification not executed","not executed"); 

				if(testsXML.getInstLog().equalsIgnoreCase("yes"))
					verifyInstallLog();
				else HTMLLogWriter("Installation Log verification...", "Installation Log verification not executed","not executed");

				if(testsXML.getInstRegKeys().equalsIgnoreCase("yes"))
					verifyInstallRegistry();
				else HTMLLogWriter("Installation Registry key verification...", "Installation Registry key verification not executed","not executed");

				if(testsXML.getInstVCEMOutput().equalsIgnoreCase("yes"))
					readVCEMOutput();
				else HTMLLogWriter("VcemOutput XML file verification...", "VcemOutput XML file verification not executed","not executed");

				if(testsXML.getInstEnvVars().equalsIgnoreCase("yes"))
					verifyEnvVars(0);
				else HTMLLogWriter("Checking Environment Variables...", "Checking Environment Variables after install not executed. There is a problem executing this test using SSH. The VCEM environment variable is not shown if the request is executed by a ssh client. ","not executed");

				if(isInstalled){
					if(testsXML.getQTPTests().equalsIgnoreCase("yes")){
						executeQTPTests();
						QTPResultsLogger();
					} else HTMLLogWriter("QTP Tests...", "QTP Tests not executed","not executed");
				}
			}

			if(!html.isFail()){
				if(testsXML.getUninstallation().equalsIgnoreCase("yes"))
					Uninstall();
				else HTMLLogWriter("Uninstallation...", "Uninstallation not executed","not executed");

				if(testsXML.getRemovedFiles().equalsIgnoreCase("yes"))
					verifyRemovedFiles();
				else HTMLLogWriter("Removed files verification...", "Removed files verification not executed","not executed");

				if(testsXML.getUninstLog().equalsIgnoreCase("yes"))
					verifyUninstallLog();
				else HTMLLogWriter("Uninstallation Log verification...", "Uninstallation Log verification not executed","not executed");

				if(testsXML.getUninstRegKeys().equalsIgnoreCase("yes"))
					verifyUninstallRegistry();
				else HTMLLogWriter("Uninstallation Registry key verification...", "Uninstallation Registry key verification not executed","not executed");

				if(testsXML.getUninstEnvVars().equalsIgnoreCase("yes"))
					verifyEnvVars(1);
				else HTMLLogWriter("Checking Environment Variables ...", "Checking Environment Variables after uninstall not executed. There is a problem executing this test using SSH. The VCEM environment variable is not shown if the request is executed by a ssh client. ","not executed");
			}


			WebServerHome();
			copyFile();
			html.endHTMLLog();

			if(((webServer.getHtmlAddress()!=null)&&(!webServer.getHtmlAddress().equalsIgnoreCase("")))
					&&((webServer.getFtpAddress()!=null)&&(!webServer.getFtpAddress().equalsIgnoreCase("")))){

				/*does not work using ssh*/
				//XMLReader mailReader = new XMLReader(file.getCurrentPath()+"email.xml");

				XMLReader mailReader = new XMLReader(config.getTS_PATH()+"email.xml");
				emailXML = mailReader.readEmailXML();

				try{
					//VM test server does not have access to mail server
					postMail.postMail(emailXML, TS_LOG_NAME, webServer.getHtmlAddress(),webServer.getFtpAddress());
				}catch (Exception e) {
					logWriter("Error - Cannot send mail: "+ e.getMessage());
					HTMLLogWriter("Sending mail... ", "Error sending mail!<br>- "+e.getMessage(),"fail");
				}
			}

			double duration = timecontroller.stopTime();
			logWriter("\nTotal time: "+duration);
			logWriter("");
			System.exit(0);

		}catch (Exception e) {
			HTMLLogWriter("Test Server execution.. ", "Error during Test Server execution!<br>- "+e.getMessage(),"fail");
			logWriter("Test Server execution fails!\n- "+e.getMessage());
			System.out.println("Error during Test Server execution.");
			System.out.println("\n"+e.getMessage());
		}
	}
	
	/** This method gets the WEBSERVER_ADDRESS parameter from ConfigXML and, sets the FTP address and 
	 * the WEBSERVER address in locals variables.*/	
	public void setWebServerFolders(){

		String tmp = config.getWEBSERVER_ADDRESS();
		if(tmp.contains("http:")) tmp = tmp.replace("http:", "");
		if(tmp.contains("/")) tmp = tmp.replace("/", "");

		FTPS = slash+slash+tmp+slash+"c$"+slash+"Inetpub"+slash+"ftproot"+slash+TS_LOG_NAME+slash;
		WEBS = slash+slash+tmp+slash+"c$"+slash+"apache"+slash+"htdocs"+slash;
	}
	
	/** This method gets the QTP files locations and, sets the local variables containing the correct
	 * path to the file in the machine where the files are found (i.e. \\QTP Server IP\drive$\qtpLogFile) */
	public void setQTPFolders(){

		String qtpLog = config.getQTPLOGFILE();
		String qtpResults = config.getQTPRESULTSFILE();
		String qtpssh = config.getQTPSERVER_SSH();

		StringTokenizer token = new StringTokenizer(qtpssh, "@"); 
		if(token.hasMoreTokens())token.nextToken().toString();
		if(token.hasMoreTokens()){
			QTPS = token.nextToken().toString();
		}

		String tmp = "";
		token = new StringTokenizer(qtpLog, ":"); 
		if(token.hasMoreTokens())
			tmp = token.nextToken().toString();

		qtpLog = qtpLog.replace(tmp+":"+slash, "");
		QTPSLog = slash+slash+QTPS+slash+tmp+"$"+slash+qtpLog;

		tmp = "";
		token = new StringTokenizer(qtpResults, ":"); 
		if(token.hasMoreTokens())
			tmp = token.nextToken().toString();

		qtpResults = qtpResults.replace(tmp+":"+slash, "");
		QTPResults = slash+slash+QTPS+slash+tmp+"$"+slash+qtpResults;
	}

	/** Perform the Environment Variables tests.
	 * @param check Integer parameter determining what test needs to be executed.
	 * <br>'0' (zero) to verify the environment variable after install; 
	 * <br>'1' (one) to verify the environment variable after uninstall;  */
	public void verifyEnvVars(int check){

		log = "";
		if(check==0){
			log = postinstallver.VerifyEnvVariables(config.getENV_VAR_NAME(), config.getENV_VAR_VALUE());

			/*There is a problem executing this test using SSH. The VCEM environment variable is not shown 
			 * if the request is executed by a ssh client.
			 */

			HTMLLogWriter("Checking Environment Variables...", "There is a problem executing this test using SSH. The VCEM environment variable is not shown if the request is executed by a ssh client. "+log,"");
		}



		if(check==1){
			log = postuninstallver.VerifyEnvVariables(config.getENV_VAR_NAME(), config.getENV_VAR_VALUE());

			/*There is a problem executing this test using SSH. The VCEM environment variable is not shown 
			 * if the request is executed by a ssh client.
			 */

			HTMLLogWriter("Checking Environment Variables...", "There is a problem executing this test using SSH. The VCEM environment variable is not shown if the request is executed by a ssh client."+log,"");
		}
	}
	/**	Method responsible to copy the QTP files for the Test Server machine, read the file content
	 * and write in the HTML log file.  */
	public void QTPResultsLogger(){
		isCopied = true;
		logWriter("\nWriting qtp results on log file started...");

		isCopied = cpy.copy(QTPSLog,config.getTS_PATH()+"logs"+slash, "qtpresults.xml");
		if(!isCopied) isCopied = false;

		isCopied =  cpy.copy(QTPResults,config.getTS_PATH()+"logs"+slash, "qtpresults.txt");
		if(!isCopied) isCopied = false;

		if(isCopied){

			boolean del = true;
			File f = new File(QTPResults);
			del = f.delete();
			if(del==false) logWriter("Cannot remove file: "+QTPResults);
			f = new File(QTPSLog);
			del = f.delete();
			if(del==false) logWriter("Cannot remove file: "+QTPSLog);

			HTMLLogWriter("Copying QTP Log File...", cpy.getLog(),"done");
		} else HTMLLogWriter("Copying QTP Log File...", cpy.getLog()+"<br>Error during QTP log file copy!","fail");

		logWriter(cpy.getLog());
		logWriter("QTP Log file copy finished...");

		log = "";
		XMLReader qtpXML = new XMLReader(config.getTS_PATH()+"logs"+slash+"qtpresults.xml");
		LinkedList<QTPResultsXML> qptlist = new LinkedList<QTPResultsXML>(); 
		qptlist = qtpXML.readQTPResultsXML();

		if(qtpXML.getLog().contains("Error")){
			HTMLLogWriter("Error Performing QTP Tests...", qtpXML.getLog().toString(), "fail");
		}
		else {

			LinkedList<String> stringlist = new LinkedList<String>();

			Iterator it = qptlist.iterator();
			while(it.hasNext())	{
				log = "";
				qtpresults = (QTPResultsXML) it.next();

				stringlist = qtpresults.getCommentList();
				Iterator icom = stringlist.iterator();
				while(icom.hasNext())	{
					log += "<br>"+icom.next().toString();
				}
				HTMLLogWriter(qtpresults.getName(), qtpresults.getDescription()+log, qtpresults.getResult());
			}
		}
		logWriter("Writing qtp results on log file finished...");
	}

	/** Method responsible to call the WebServerHome method that Reads and writes again the 
	 * index.html file with the updated information.*/
	public void WebServerHome(){

		logWriter("\nUpdating web server home started...");
		LinkedList<String> slist = new LinkedList<String>();
		slist = webServer.Reader(WEBS+"index.html");

		if(!config.getWEBSERVER_EXT_ADDRESS().equalsIgnoreCase("")){
			webServer.WebServerHome(config.getTS_PATH()+"logs"+slash,TS_LOG_NAME, slist, config.getWEBSERVER_EXT_ADDRESS());
		}
		else webServer.WebServerHome(config.getTS_PATH()+"logs"+slash,TS_LOG_NAME, slist, config.getWEBSERVER_ADDRESS());
		logWriter("Updating web server home finished...");
	}

	/** Method responsible to copy the FTP server files and the WEBSERVER files to the Machine with the WebServer installed.*/
	public void copyFile(){

		logWriter("\nLog file copy started...");

		isCopied = true;

		isCopied = cpy.copy(config.getTS_PATH()+"logs"+slash+TS_LOG_NAME+".html",FTPS,TS_LOG_NAME+".html");
		if(!isCopied) isCopied = false;

		isCopied = cpy.copy(config.getTS_PATH()+"logs"+slash+"qtpresults.txt",FTPS, "qtpresults.txt");
		if(!isCopied) isCopied = false;

		isCopied = cpy.copy(config.getTS_PATH()+"logs"+slash+INSTALL_LOG_NAME,FTPS,INSTALL_LOG_NAME);
		if(!isCopied) isCopied = false;

		isCopied = cpy.copy(config.getTS_PATH()+"logs"+slash+TS_LOG_NAME+".html",WEBS,TS_LOG_NAME+".html");
		if(!isCopied) isCopied = false;

		isCopied = cpy.copy(config.getTS_PATH()+"logs"+slash+"index.html",WEBS,"index.html");
		if(!isCopied) isCopied = false;

		if(isCopied) 
			HTMLLogWriter("Copying QTP Log File...", cpy.getLog(),"done");
		else HTMLLogWriter("Copying QTP Log File...", cpy.getLog(),"fail");

		cpy.copy(config.getTS_PATH()+"logs"+slash+TS_LOG_NAME+".log",FTPS,TS_LOG_NAME+".log");

		logWriter(cpy.getLog());
		logWriter("Log file copy finished...");

	}
	/** Method responsible to start the QTP tests execution*/
	public void executeQTPTests(){

		logWriter("\nQTP Tests started...");

		log = postinstallver.executeQTPTests(config.getQTPFILE(), config.getQTPSERVER_SSH());	
		logWriter(log);
		logWriter("QTP Tests finished...");
		HTMLLogWriter("Starting QTP Tests...", log,"");

	}
	/** Method responsible to start the latest build identification.  
	 * @return true if the build was identified correctly and, false if not.*/
	public boolean getLatestBuild(){

		logWriter("\nIdentify latest build started...");
		stringlist = http.getLatestBuild(config.getURL(),config.getPROJECT(), config.getPROXY_HOST(),config.getPROXY_PORT());

		config.setBUILD(http.getBuildNumber());

		if(stringlist.isEmpty()){
			logWriter("Error - Cannot access url: "+config.getURL());
			logWriter(http.getLog().toString());
			HTMLLogWriter("Identifying Latest Build in Cruise Control...", "Error during build identification, cannot proceed!<br>Cannot access url: "+config.getURL()+"<br>"+http.getLog().toString(),"fail");
			return false;
		}else 
			if(http.isIdentified()){
				logWriter(http.getLog());
				logWriter("Identify latest build finished...");
				HTMLLogWriter("Identifying latest Build in Cruise Control...", http.getLog(),"done");
				return http.isIdentified();
			}else {
				logWriter("Error - Cannot access url: "+config.getURL());
				logWriter(http.getLog().toString());
				HTMLLogWriter("Identifying Latest Build in Cruise Control...", "Error during build identification, cannot proceed!<br>Cannot access url: "+config.getURL()+"<br>"+http.getLog().toString(),"fail");
				return http.isIdentified();
			} 
	}

	/** Method responsible to start the build identification.  
	 * @param build build number to be identified.
	 * @return true if the build was identified correctly and, false if not. */
	public boolean getSpecificBuild(String build){
		logWriter("\nIdentify specific build started...");
		stringlist = http.getSpecificBuild(config.getURL(),config.getPROJECT(), build, config.getPROXY_HOST(),config.getPROXY_PORT());

		if(stringlist.isEmpty()){
			logWriter("Error - Cannot access url: "+config.getURL());
			logWriter(http.getLog().toString());
			HTMLLogWriter("Identifying Specific Build in Cruise Control...", "Error during build identification, cannot proceed!<br>Cannot access url: "+config.getURL()+"<br>"+http.getLog().toString(),"fail");
			return false;
		}else 
			if(http.isIdentified()){
				logWriter(http.getLog());
				logWriter("Identify specific build finished...");
				HTMLLogWriter("Identifying Specific Build in Cruise Control...", http.getLog(),"done");
				return http.isIdentified();
			}else {
				logWriter("Error - Cannot access url: "+config.getURL());
				logWriter(http.getLog().toString());
				HTMLLogWriter("Identifying Specific Build in Cruise Control...", "Error during build identification, cannot proceed!<br>Cannot access url: "+config.getURL()+"<br>"+http.getLog().toString(),"fail");
				return http.isIdentified();
			} 
	}

	/** Method responsible to start the download of the build identified.
	 * @return true if the download was executed correctly and, false if not. */
	 public boolean download(){
		logWriter("\nDownload process started...");

		try {
			Iterator it = stringlist.iterator();
			while(it.hasNext()){
				isDownloaded = download.downloadFile(http.getURLPath(),config.getTS_PATH()+http.getBuildNumber(),it.next().toString(), config.getPROXY_HOST(),config.getPROXY_PORT());
			}
		}
		catch (NullPointerException e) {
			logWriter("\nError during download.");
			logWriter(e.getMessage()+"\n");
			HTMLLogWriter("Download...", "Error during download, cannot proceed!"+e.getMessage()+"<br>","fail");
			return false;
		}
		catch (Exception e) {
			logWriter("\nError during download.\n");
			logWriter(e.getMessage()+"\n");
			HTMLLogWriter("Download...", "Error during download, cannot proceed!"+e.getMessage()+"<br>","fail");
			return false;
		}

		logWriter("Download process finished...");

		if(isDownloaded) 
			HTMLLogWriter("Download...", download.getLog(),"done");
		else HTMLLogWriter("Download...", download.getLog(),"fail");

		return isDownloaded;
	}

	/** Perform the VCEM installation with as following parameters:
	 * 	<pre>
	 * VCEM140.exe /VERYSILENT /SUPPRESSMSGBOXES
	 * /XMLINPUT=< VCEMXML Input file location >
	 * /DIR=< location where the VCEM should be installed >
	 * /LOGFILE= < log file location > 
	 * /DBPWD=< password >  
	 * /INSTALLTYPE=< standalone|plugin >
	 * </pre>
	 * @return true if the installation was executed correctly and, false if not. */
	public boolean Install(){
		logWriter("\nInstallation process started...");

		XMLReader vceminput = new XMLReader(download.getdownloadPath()+"input.xml");
		vcemInputXML = vceminput.ReadVcemInputXML();
		vcemInputXML.setLangID(config.getLANGID());
		vcemInputXML.setXMLOutputValue(config.getTS_PATH()+"logs"+slash+"VcemOutput.xml");
		vcemInputXML.setInstallType(config.getINSTALLTYPE());
		vcemInput.writeVcemInputXML(vcemInputXML, download.getdownloadPath());

		String command = 
			"VCEM140.exe"+
			" /VERYSILENT"+
			" /SUPPRESSMSGBOXES"+
			" /XMLINPUT="+(char)34+download.getdownloadPath()+"input.xml"+(char)34+
			" /DIR="+(char)34+config.getVCEM_INSTALL_PATH()+(char)34+
			" /LOG="+(char)34+config.getTS_PATH()+"logs"+slash+INSTALL_LOG_NAME+(char)34+
			" /CHECK_SIM=false";
		
		

		log = exec.install(download.getdownloadPath(), command);
		log += vceminput.getLog();
		logWriter(log);
		logWriter("Installation process finished...");
		HTMLLogWriter("Installation...", log,"");


		if((log.contains("error"))||(log.contains("Error")))return false;
		else return true;
	}

	/** Perform the VCEM installation with as following parameters:
	 * 	<pre>
	 *  unins000.exe
	 *  /VERYSILENT
	 *  /SUPPRESSMSGBOXES
	 *  /LOG= < log file location >
	 **/
	public void Uninstall(){
		logWriter("\nUninstallation process started...");
		String command = 
			" /VERYSILENT"+
			" /SUPPRESSMSGBOXES"+
			" /LOG="+config.getTS_PATH()+"logs"+slash+UNINSTALL_LOG_NAME;
		log = exec.unistall(config.getVCEM_INSTALL_PATH(),command);
		logWriter(log);
		logWriter("Uninstallation process finished...");

		HTMLLogWriter("Uninstallation...", log,"");
	}

	/** Perform the Deployed Files test.*/
	public void verifyDeployedFiles(){
		logWriter("\nDeployed files verification started...");
		log = postinstallver.verifyDeployedFiles(config.getVCEM_INSTALL_PATH(), config.getHPSIM_INSTALL_PATH(),config.getFILE_LIST());
		logWriter(log);
		logWriter("Deployed files verification finished...");

		HTMLLogWriter("Deployed files verification...", log,"");
	}

	/** Perform the Removed Files test.*/
	public void verifyRemovedFiles(){
		logWriter("\nRemoved files verification started...");
		log = postuninstallver.verifyRemovedFiles(config.getVCEM_INSTALL_PATH(), config.getHPSIM_INSTALL_PATH(),config.getFILE_LIST());
		logWriter(log);
		logWriter("Removed files verification finished...");

		HTMLLogWriter("Removed files verification...", log,"");
	}
	
	/** Perform the VcemOutput XML test.*/
	public void readVCEMOutput(){
		logWriter("\nVcemOutput XML file verification started...");

		XMLReader rs = new XMLReader(config.getTS_PATH()+"logs"+slash+"VcemOutput.xml");
		vcemOutputXML = rs.ReadVcemOutputXML();

		log = postinstallver.verifyVcemXML(vcemOutputXML);

		log += rs.getLog();
		logWriter(log);
		logWriter("VcemOutput XML file verification finished...");

		HTMLLogWriter("VcemOutput XML file verification...", log,"");

	}
	/** Perform the Installation Log test.*/
	public void verifyInstallLog() {
		logWriter("\nInstallation Log verification started...");
		log = postinstallver.InstallLogReader(config.getTS_PATH()+"logs"+slash+INSTALL_LOG_NAME);
		logWriter(log);
		logWriter("Installation Log verification finished...");

		HTMLLogWriter("Installation Log verification...", log,"");
	}
	/** Perform the Uninstallation Log test.*/
	public void verifyUninstallLog() {

		isCopied = true;
		logWriter("\nInstallation Log verification started...");
		log = postuninstallver.UninstallLogReader(config.getTS_PATH()+"logs"+slash+UNINSTALL_LOG_NAME);
		logWriter(log);
		logWriter("Installation Log verification finished...");

		HTMLLogWriter("Uninstallation Log verification...", log,"");

		isCopied = cpy.copy(config.getTS_PATH()+"logs"+slash+UNINSTALL_LOG_NAME,FTPS,UNINSTALL_LOG_NAME);
		if(!isCopied) isCopied = false;

		if(isCopied) 
			HTMLLogWriter("Copying Uninstallation Log File...", cpy.getLog(),"done");
		else HTMLLogWriter("Copying Uninstallation Log File...", cpy.getLog(),"fail");
	}

	/** Perform the installation registry key test.*/
	public void verifyInstallRegistry(){
		logWriter("\nRegistry key verification started...");

		/*does not work using ssh
		XMLReader regKey = new XMLReader(file.getCurrentPath()+"registry.xml");*/

		XMLReader regKey = new XMLReader(config.getTS_PATH()+"registry.xml");
		regKeylist = regKey.ReadRegistryXML();

		Iterator it = regKeylist.iterator();
		while(it.hasNext()){
			RegKey rk = (RegKey) it.next();
			LinkedList<RegKeyString> t = rk.getKeyStringList();

			Iterator it2 = t.iterator();
			while(it2.hasNext()){
				RegKeyString rks = (RegKeyString) it2.next();
				if(rks.getData().contains("latestBuild"))
					rks.setData(rks.getData().replace("latestBuild", config.getBUILD()));
			}
		}

		log = postinstallver.verifyRegKey(regKeylist);
		log += regKey.getLog();
		logWriter(log);
		HTMLLogWriter("Installation Registry key verification...", log,"");
		logWriter("\nRegistry key verification finished...");
	}
	/** Perform the uninstallation registry key test.*/
	public void verifyUninstallRegistry(){
		logWriter("\nRegistry key verification started...");

		/*does not work using ssh
		XMLReader regKey = new XMLReader(file.getCurrentPath()+"registry.xml");*/

		XMLReader regKey = new XMLReader(config.getTS_PATH()+"registry.xml");
		regKeylist = regKey.ReadRegistryXML();

		log = postuninstallver.verifyRegKey(regKeylist);
		log += regKey.getLog();

		logWriter(log);
		HTMLLogWriter("Uninstallation Registry key verification...", log,"");
		logWriter("\nRegistry key verification finished...");
	}


	/** Method responsible to write messages in the log file.
	 * @param str String containing a message to be written in the Test Server '.log' file. */
	public void logWriter(String str) {
		file.writer(str);
	}
	/**
	 * Method responsible to write messages in the HTML log file.
	 * @param test - test description.
	 * @param additionalInfo - test details (result details, comments, etc...)
	 * @param result - test result (warn, pass, unknown, not executed, done and fail), 
	 * If this parameter is not informed the method searches for key words, to determine the result.*/
	public void HTMLLogWriter(String test, String additionalInfo, String result) {

		if(!result.equalsIgnoreCase("")){
			html.HTMLLogWriter(test, result, additionalInfo);
		}
		else if((additionalInfo.contains("error"))
				||(additionalInfo.contains("Error"))
				||(additionalInfo.contains("ERROR"))
				||(additionalInfo.contains("fail"))
				||(additionalInfo.contains("Fail"))
				||(additionalInfo.contains("FAIL"))){
			html.HTMLLogWriter(test, "fail", additionalInfo);
		}
		else if((additionalInfo.contains("warning"))
				||(additionalInfo.contains("Warning"))
				||(additionalInfo.contains("WARNING"))){
			html.HTMLLogWriter(test, "warn", additionalInfo);
		} 
		else if((additionalInfo.contains("not executed"))
				||(additionalInfo.contains("Not Executed"))
				||(additionalInfo.contains("NOT EXECUTED"))){
			html.HTMLLogWriter(test, "not executed", additionalInfo);
		} 
		else html.HTMLLogWriter(test, "pass", additionalInfo); 
	}
	/** Start the Test Server execution by calling the method main.execute(commandList). 
	 * The commandList should contain the following information:
	 * <br>/XMLINPUT= < path to the Test Server config.xml file >
	 * @param args String containing the following parameter: /XMLINPUT= < path to the Test Server config.xml file >
	 */
	public static void main(String[] args) {

		LinkedList<String> commandList = new LinkedList<String>();
		int i=0;

		try {
			while(args[i]!=null){
				commandList.add(args[i]);
				i++;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}

		Main main = new Main();
		main.execute(commandList);
	}
}
