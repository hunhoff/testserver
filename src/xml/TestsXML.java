package xml;

/**
 *******************************************************************************
 *Class responsible to manage the Tests that should be executed.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class TestsXML {
	
	private String installation;
	private String DeployedFiles;
	private String InstLog;
	private String InstRegKeys;
	private String InstVCEMOutput;
	private String InstEnvVars;
	private String QTPTests;
	private String Uninstallation;
	private String RemovedFiles;
	private String UninstLog;
	private String UninstRegKeys;
	private String UninstEnvVars;
	
	/** @return String informing if the Deployed Files verification should be executed.*/
	public String getDeployedFiles() {
		return DeployedFiles;
	}
	/** @param deployedFiles - set if the Deployed Files verification should be executed.*/
	public void setDeployedFiles(String deployedFiles) {
		DeployedFiles = deployedFiles;
	}
	/** @return String informing if the Installation should be executed.*/
	public String getInstallation() {
		return installation;
	}
	/** @param installation - set if the Installation should be executed. */
	public void setInstallation(String installation) {
		this.installation = installation;
	}
	/** @return String informing if the 'Installation Environment Variables' verification should be executed.*/
	public String getInstEnvVars() {
		return InstEnvVars;
	}
	/** @param instEnvVars - set if the 'Installation Environment Variables' verification should be executed.*/
	public void setInstEnvVars(String instEnvVars) {
		InstEnvVars = instEnvVars;
	}
	
	/** @return String informing if the 'Installation log' verification should be executed.*/
	public String getInstLog() {
		return InstLog;
	}
	/** @param instLog - set if the 'Installation log' verification should be executed.*/
	public void setInstLog(String instLog) {
		InstLog = instLog;
	}
	/** @return String informing if the 'Installation Registry Keys' verification should be executed.*/
	public String getInstRegKeys() {
		return InstRegKeys;
	}
	/** @param instRegKeys - set if the 'Installation Registry Keys' verification should be executed.*/
	public void setInstRegKeys(String instRegKeys) {
		InstRegKeys = instRegKeys;
	}
	/** @return String informing if the 'Installation VCEM Output' verification should be executed.*/
	public String getInstVCEMOutput() {
		return InstVCEMOutput;
	}
	/** @param instVCEMOutput - set if the 'Installation VCEM Output' verification should be executed.*/
	public void setInstVCEMOutput(String instVCEMOutput) {
		InstVCEMOutput = instVCEMOutput;
	}
	/** @return String informing if the 'QTP Tests' should be executed.*/
	public String getQTPTests() {
		return QTPTests;
	}
	/** @param tests - set if the 'QTP Tests' should be executed.*/
	public void setQTPTests(String tests) {
		QTPTests = tests;
	}
	/** @return String informing if the 'Removed Files' verification should be executed after the uninstall.*/
	public String getRemovedFiles() {
		return RemovedFiles;
	}
	/** @param removedFiles - set if the 'Removed Files' verification should be executed after the uninstall.*/
	public void setRemovedFiles(String removedFiles) {
		RemovedFiles = removedFiles;
	}
	/** @return String informing if the 'Uninstallation' should be executed.*/
	public String getUninstallation() {
		return Uninstallation;
	}
	/** @param uninstallation - set if the 'Uninstallation' should be executed.*/
	public void setUninstallation(String uninstallation) {
		Uninstallation = uninstallation;
	}
	/** @return String informing if the 'Uninstallation Environment Variables' verification should be executed.*/
	public String getUninstEnvVars() {
		return UninstEnvVars;
	}
	/** @param uninstEnvVars - set if the 'Uninstallation Environment Variables' verification should be executed.*/
	public void setUninstEnvVars(String uninstEnvVars) {
		UninstEnvVars = uninstEnvVars;
	}
	/** @return String informing if the 'Uninstallation log' verification should be executed.*/
	public String getUninstLog() {
		return UninstLog;
	}
	/** @param uninstLog - set if the 'Uninstallation log' verification should be executed.*/
	public void setUninstLog(String uninstLog) {
		UninstLog = uninstLog;
	}
	/** @return String informing if the 'Uninstallation Registry Keys' verification should be executed.*/
	public String getUninstRegKeys() {
		return UninstRegKeys;
	}
	/** @param uninstRegKeys - set if the 'Uninstallation Registry Keys' verification should be executed.*/
	public void setUninstRegKeys(String uninstRegKeys) {
		UninstRegKeys = uninstRegKeys;
	}
}
