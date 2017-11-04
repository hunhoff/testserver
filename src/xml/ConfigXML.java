package xml;

/**
 *******************************************************************************
 *Class responsible to manage the Test Server configuration properties.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class ConfigXML {

	private String BUILD = "";
	private String PROXY_HOST = "";
	private String PROXY_PORT = "";
	private String VCEM_INSTALL_PATH = "";
	private String HPSIM_INSTALL_PATH = "";
	private String DBPWD = "";
	private String INSTALLTYPE = "";
	private String LANGID = "";
	private String ENV_VAR_NAME = "";
	private String ENV_VAR_VALUE = "";
	private String TS_NAME = "";
	private String TS_PATH = "";
	private String URL = "";
	private String PROJECT = "";
	private String FILE_LIST = "";
	private String WEBSERVER_ADDRESS = "";
	private String QTPSERVER_SSH = "";
	private String QTPFILE = "";
	private String QTPLOGFILE = "";
	private String QTPRESULTSFILE = "";
	private String WEBSERVER_EXT_ADDRESS = "";
	
	/** @return String containing the environment variable name*/
	public String getENV_VAR_NAME() {
		return ENV_VAR_NAME;
	}
	/** @param env_var_name - set the environment variable name*/
	public void setENV_VAR_NAME(String env_var_name) {
		ENV_VAR_NAME = env_var_name;
	}
	/** @return String containing the environment variable value*/
	public String getENV_VAR_VALUE() {
		return ENV_VAR_VALUE;
	}
	/** @param env_var_value - set the environment variable value*/
	public void setENV_VAR_VALUE(String env_var_value) {
		ENV_VAR_VALUE = env_var_value;
	}
	/** @return String containing the path to the QTP results file (this file contain all QTP
	 * low level result details and, is just saved in FTP to verification)*/
	public String getQTPRESULTSFILE() {
		return QTPRESULTSFILE;
	}
	/** @param qtpresultsfile - set the QTP results file path*/
	public void setQTPRESULTSFILE(String qtpresultsfile) {
		QTPRESULTSFILE = qtpresultsfile;
	}
	/** @return String containing the Test Server name*/
	public String getTS_NAME() {
		return TS_NAME;
	}
	/** @param ts_name - set the Test Server name*/
	public void setTS_NAME(String ts_name) {
		TS_NAME = ts_name;
	}
	/** @return String containing the webserver external address (with access to the corp net)*/
	public String getWEBSERVER_EXT_ADDRESS() {
		return WEBSERVER_EXT_ADDRESS;
	}
	/** @param webserver_ext_address - set the webserver external address (with access to the corp net)*/
	public void setWEBSERVER_EXT_ADDRESS(String webserver_ext_address) {
		WEBSERVER_EXT_ADDRESS = webserver_ext_address;
	}
	/** @return String containing the language ID (needed in the vcemInput XML file)*/
	public String getLANGID() {
		return LANGID;
	}
	/** @param langid - set the language ID (needed in the vcemInput XML file*/
	public void setLANGID(String langid) {
		LANGID = langid;
	}
	/** @return String containing the path to the QTP XML log file (this file contain all QTP 
	 * results that should be displayed in the HTML log file)*/
	public String getQTPLOGFILE() {
		return QTPLOGFILE;
	}
	/** @param qtplogfile - set the path to the QTP XML log file (this file contain all QTP 
	 * results that should be displayed in the HTML log file)*/
	public void setQTPLOGFILE(String qtplogfile) {
		QTPLOGFILE = qtplogfile;
	}
	/** @return String containing the database password (needed in the installation command line)*/
	public String getDBPWD() {
		return DBPWD;
	}
	/** @param dbpwd -  set the database password (needed in the installation command line) */
	public void setDBPWD(String dbpwd) {
		DBPWD = dbpwd;
	}
	/** @return String containing the path to the File list (this file contain all file that will 
	 * be checked after install and uninstall) */
	public String getFILE_LIST() {
		return FILE_LIST;
	}
	/** @param file_list - set the path to the File list (this file contain all file that will 
	 * be checked after install and uninstall)
	 */
	public void setFILE_LIST(String file_list) {
		FILE_LIST = file_list;
	}
	/** @return String containing the path where the HPSIM is installed*/
	public String getHPSIM_INSTALL_PATH() {
		return HPSIM_INSTALL_PATH;
	}
	/** @param hpsim_install_path - set the path where the HPSIM is installed*/
	public void setHPSIM_INSTALL_PATH(String hpsim_install_path) {
		HPSIM_INSTALL_PATH = hpsim_install_path;
	}
	/** @return String containing the installation type (standalone or plugin)*/
	public String getINSTALLTYPE() {
		return INSTALLTYPE;
	}
	/** @param installtype - set the installation type (standalone or plugin)*/
	public void setINSTALLTYPE(String installtype) {
		INSTALLTYPE = installtype;
	}
	/** @return String containing the CruiseControl project name*/
	public String getPROJECT() {
		return PROJECT;
	}
	/** @param project - set the CruiseControl project name */
	public void setPROJECT(String project) {
		PROJECT = project;
	}
	/** @return String containing the proxy host if exist*/
	public String getPROXY_HOST() {
		return PROXY_HOST;
	}
	/** @param proxy_host - the proxy host*/
	public void setPROXY_HOST(String proxy_host) {
		PROXY_HOST = proxy_host;
	}
	/** @return String containing the proxy port if exist*/
	public String getPROXY_PORT() {
		return PROXY_PORT;
	}
	/** @param proxy_port - set the proxy port*/
	public void setPROXY_PORT(String proxy_port) {
		PROXY_PORT = proxy_port;
	}
	/** @return String containing the path to the .VBS file that will call the QTP script to be executed*/
	public String getQTPFILE() {
		return QTPFILE;
	}
	/** @param qtpfile - set the path to the .VBS file that will call the QTP script to be executed*/
	public void setQTPFILE(String qtpfile) {
		QTPFILE = qtpfile;
	}
	/** @return String containing the path where the Test Server files are located*/
	public String getTS_PATH() {
		return TS_PATH;
	}
	/** @param ts_path - set the path where the Test Server files are located*/
	public void setTS_PATH(String ts_path) {
		TS_PATH = ts_path;
	}
	/** @return String containing the CruiseControl URL address*/
	public String getURL() {
		return URL;
	}
	/** @param url - set the CruiseControl URL address*/
	public void setURL(String url) {
		URL = url;
	}
	/** @return String containing the path where the VCEM will be installed*/
	public String getVCEM_INSTALL_PATH() {
		return VCEM_INSTALL_PATH;
	}
	/** @param vcem_install_path - set the path where the VCEM will be installed*/
	public void setVCEM_INSTALL_PATH(String vcem_install_path) {
		VCEM_INSTALL_PATH = vcem_install_path;
	}
	/** @return String containing the webserver local address (with access to the 
	 * Virtual Machine that will execute the test)*/
	public String getWEBSERVER_ADDRESS() {
		return WEBSERVER_ADDRESS;
	}
	/** @param webserver_address - set the webserver local address (with access to the 
	 * Virtual Machine that will execute the test)*/
	public void setWEBSERVER_ADDRESS(String webserver_address) {
		WEBSERVER_ADDRESS = webserver_address;
	}
	/** @return String containing the build that will be tested ('LatestBuild' for automatically 
	 * identify the latest build in CruiseControl or, inform the build number to be tested)*/
	public String getBUILD() {
		return BUILD;
	}
	/** @param build - set the build that will be tested ('LatestBuild' for automatically 
	 * identify the latest build in CruiseControl or, inform the build number to be tested)*/
	public void setBUILD(String build) {
		BUILD = build;
	}
	/** @return String containing the ssh command to start ssh communication with the server that 
	 * will execute the QTP tests (i.e. 'ssh username@IPAddress')*/
	public String getQTPSERVER_SSH() {
		return QTPSERVER_SSH;
	}
	/** @param qtpserver_ssh - set the ssh command to start ssh communication with the server that 
	 * will execute the QTP tests (i.e. 'ssh username@IPAddress')*/
	public void setQTPSERVER_SSH(String qtpserver_ssh) {
		QTPSERVER_SSH = qtpserver_ssh;
	}
}
