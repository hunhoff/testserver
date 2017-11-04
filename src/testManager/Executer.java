package testManager;
import java.io.File;

/**
 *******************************************************************************
 *Class responsible to execute the installation/uninstallation.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/
public class Executer 
{
	private int exitValue;
	private String log;

	public Executer(){}

	/**
	 *******************************************************************************************
	 * Perform the VCEM build installation and verifies if the installation finishes 
	 * successfully checking the process exit value. 
	 * @param file - address containing the direct link to the setup file. 
	 * @param command - command with the parameters to perform the installation.
	 * @return String containing the installation results.
	 *******************************************************************************************
	 **/
	public String install(String file, String command) {

		log = "";
		exitValue = 1;
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		
		command = file+File.separator+command;
		log+= "Command Line executed: "+command+"<br>";
		try {
			p = rt.exec(command);
			p.waitFor();
			exitValue = p.exitValue();
		} catch (Exception ex) { 
			log += "Error during the execution of the VCEM build installation. Process exit value equals zero.";
			log += "<br>- "+ex.getMessage();
		}
		if(exitValue==0) log += "VCEM build Installation Completed.";
		else log += "Error installing VCEM build. Process exit value is different of zero.";
		return log;
	}


	/**
	 *******************************************************************************************
	 * Perform the VCEM build uninstallation and verifies if the uninstallation finishes 
	 * successfully checking the process exit value. 
	 * @param file - address containing the direct link to the uninstall file. 
	 * @param command - command with the parameters to perform the uninstallation.
	 * @return String containing the uninstallation results.
	 *******************************************************************************************
	 **/
	public String unistall(String file, String command) {

		exitValue = 1;
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		
		file = (char)34+file+"unins000.exe"+(char)34;
		command = file+" "+command;
		log = "Command Line executed: "+command+"<br>";
		try {

			p = rt.exec(command);
			p.waitFor();
			exitValue = p.exitValue();
		} catch (Exception ex) { 
			log += "Error during the execution of the VCEM build uninstallation.";
			log += "<br>- "+ex.getMessage();
		}
		if(exitValue==0) log += "VCEM build Uninstallation Completed. Process exit value equals zero.";
		else log += "Error uninstalling VCEM build. Process exit value is different of zero.";
		return log;
	}

	/**
	 *******************************************************************************************
	 * Start new VCEM browser.
	 * The method just open a new browser in the VCEM url address. 
	 *******************************************************************************************
	 **/
	public void startVCEM() {

		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			p = rt.exec("rundll32 url.dll,FileProtocolHandler https://localhost:50000");
			p.wait();
		} catch (Exception ex) { 
			ex.getMessage(); 
		}
	}
}
