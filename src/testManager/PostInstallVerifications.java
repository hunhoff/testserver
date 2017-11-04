package testManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import xml.RegKey;
import xml.RegKeyString;
import xml.VcemOutputXML;

import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

import fileManager.LogManager;

/**
 *******************************************************************************
 *Class responsible to all Post Install Verifications.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class PostInstallVerifications {

	private String log = "";
	private RegistryKey iceRegkey;
	private int exitValue;

	public PostInstallVerifications(){}


	/** 
	 *********************************************************************************
	 * Verify in windows registry keys if the VCEM key and strings was created
	 * successfully. 
	 * @param regKeylist LinkedList containing the registry keys and strings to
	 * verify.
	 * ******************************************************************************* 					
	 **/	

	public String verifyRegKey(LinkedList<RegKey> regKeylist){

		LinkedList<RegKeyString> keyStringList = new LinkedList<RegKeyString>();
		log = "";
		String tmp = "";
		iceRegkey=null;

		Iterator it = regKeylist.iterator();
		while(it.hasNext())	{

			RegKey regK = (RegKey) it.next();
			//System.out.println(regK.getKey());
			tmp = "";

			try {
				log += regK.getKey()+": ";

				//HKEY_LOCAL_MACHINE (HKLM)
				if(regK.getKey().contains("HKLM"+File.separator)){
					tmp = regK.getKey().replace("HKLM"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_LOCAL_MACHINE,tmp,0);
				}
				//HKEY_LOCAL_MACHINE (HKLM)
				if(regK.getKey().contains("HKEY_LOCAL_MACHINE"+File.separator)){
					tmp = regK.getKey().replace("HKEY_LOCAL_MACHINE"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_LOCAL_MACHINE,tmp,0);
				}

				//HKEY_CURRENT_USER (HKCU)
				if(regK.getKey().contains("HKCU"+File.separator)){
					tmp = regK.getKey().replace("HKCU"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_CURRENT_USER,tmp,0);
				}
				//HKEY_CURRENT_USER (HKCU)
				if(regK.getKey().contains("HKEY_CURRENT_USER"+File.separator)){
					tmp = regK.getKey().replace("HKEY_CURRENT_USER"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_CURRENT_USER,tmp,0);
				}

				//HKEY_CLASSES_ROOT (HKCR)
				if(regK.getKey().contains("HKCR"+File.separator)){
					tmp = regK.getKey().replace("HKCR"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_CLASSES_ROOT,tmp,0);
				}
				//HKEY_CLASSES_ROOT (HKCR)
				if(regK.getKey().contains("HKEY_CLASSES_ROOT"+File.separator)){
					tmp = regK.getKey().replace("HKEY_CLASSES_ROOT"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_CLASSES_ROOT,tmp,0);
				}

				//HKEY_USERS (HKU)
				if(regK.getKey().contains("HKU"+File.separator)){
					tmp = regK.getKey().replace("HKU"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_USERS,tmp,0);
				}
				//HKEY_USERS (HKU)
				if(regK.getKey().contains("HKEY_USERS"+File.separator)){
					tmp = regK.getKey().replace("HKEY_USERS"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_USERS,tmp,0);
				}
				//HKEY_PERFORMANCE_DATA
				if(regK.getKey().contains("HKEY_PERFORMANCE_DATA"+File.separator)){
					tmp = regK.getKey().replace("HKEY_PERFORMANCE_DATA"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_PERFORMANCE_DATA,tmp,0);
				}
				//HKEY_DYN_DATA
				if(regK.getKey().contains("HKEY_DYN_DATA"+File.separator)){
					tmp = regK.getKey().replace("HKEY_DYN_DATA"+File.separator, "");
					iceRegkey = Registry.openSubkey(Registry.HKEY_DYN_DATA,tmp,0);
				}

				if(!(iceRegkey==null)) log += "key was successfully found. \n<br>";
				else log += "Error - key not found \n<br>";
			} 
			catch (Exception e) {
				log += "Error - key not found \n<br>";
				return log;
			}

			if(!(iceRegkey==null)){

				keyStringList = regK.getKeyStringList();
				Iterator ksl = keyStringList.iterator();
				while(ksl.hasNext()){
					RegKeyString rks = (RegKeyString) ksl.next();
					//System.out.println(rks.getName());
					//System.out.println(rks.getData());
					try {
						log += rks.getName()+": ";
						if((iceRegkey.getStringValue(rks.getName()))!=null) {
							if((iceRegkey.getStringValue(rks.getName()).equalsIgnoreCase(rks.getData()))){ 
								log += "String value and data was successfully found.\n<br>";
							}else {
								log += "Warning - The string '"+iceRegkey.getStringValue(rks.getName())+"' is different of expected: '"+rks.getData()+"'.\n<br>";
							}
						}
					} 
					catch (RegistryException e) {
						log += "Error, string value not found \n<br>";
					}
					catch (NullPointerException e) {
						log += "Error, string value not found \n<br>";
					}
				}
			}
		}
		return log;
	}	


	/**
	 ***************************************************************************************************************
	 * Verify if the VCEM files was correctly deployed by VCEM installer after the installation.
	 * @param VCEMInstallPath - VCEM installation path (i.e. C:\Program Files\HP\Virtual Connect Enterprise Manager). 
	 * @param HPSIMInstallPath - VCEM installation path (i.e. C:\Program Files\HP\Systems Insight Manager).
	 * @param file - local and name where is the file containing the list of deployed files.
	 * @return String containing all files that could not be found.
	 ***************************************************************************************************************
	 **/

	public String verifyDeployedFiles(String VCEMInstallPath, String HPSIMInstallPath, String file){

		LogManager fm = new LogManager();
		LinkedList<String> list = new LinkedList<String>();
		list = fm.Reader(file);
		File f;

		boolean isMissing = false;
		String tmp = "";

		try {
			if(!list.isEmpty()) {
				Iterator i = list.iterator();
				while(i.hasNext()) {
					tmp = i.next().toString();
					if(tmp.contains("VCEMPath/")) {
						tmp = tmp.replace("VCEMPath/", VCEMInstallPath);
						f = new File(tmp);
						if(!f.exists()){
							if(isMissing==false) log = "Error - Files not found: ";
							isMissing = true;
							log += "<br>"+tmp;
						}
					}
					else if(tmp.contains("HPSIMPath/")) {
						tmp = tmp.replace("HPSIMPath/", HPSIMInstallPath);
						f = new File(tmp);
						if(!f.exists()){
							if(isMissing==false) log = "Error - Files not found: ";
							isMissing = true;
							log += "<br>"+tmp;
						}
					}else {
						f = new File(tmp);
						if(!f.exists()){
							if(isMissing==false) log = "Error - Files not found: ";
							isMissing = true;
							log += "<br>"+tmp;
						}
					}

				}
				if(isMissing==false) log="Installation files verification was executed successfully.";
			}else log="Error - Cannot find files list.";

		}catch (Exception e) {
			log="Error - Cannot read files list.<br>";
			log+="- "+e.getMessage();
		}
		return log;
	}

	/**
	 *******************************************************************************************
	 * Perform the log verification, searching for errors and warnings in the text. 
	 * The lines containing this 'tags' are saved in a string (except the default lines) and,
	 * returned to the user. 
	 * @param fileName - address containing the direct link to the installation log file.
	 * @return String containing the log verification results.
	 *******************************************************************************************
	 **/
	public String InstallLogReader(String fileName) {
		log = ""; 
		boolean error=false;		
		String line = null;
		try	{
			FileReader in = new FileReader(fileName);
			BufferedReader buff = new BufferedReader(in);
			while((line = buff.readLine()) !=null) {
				if(((line.contains("ERROR"))
						||(line.contains("error"))
						||(line.contains("Error"))
						||(line.contains("Fail"))
						||(line.contains("fail"))
						||(line.contains("FAIL")))
						&&((!(line.contains("Extracting a temporary file: errors.xml")))
								&&(!(line.contains("Loading the error messages from XML errors file...")))
								&&(!(line.contains("was copyied with success to")))
								&&(!(line.contains("11001")))
								&&(!(line.contains("error code - 0")))
								&&(!(line.contains("The XML error file was loaded with success.")))
								&&(!(line.contains("GUID Server installation errorcode: 0")))
								&&(!(line.contains("Dest filename:")))
								&&(!(line.contains("Setting permissions on file:")))
								&&(!(line.contains("Creating the XML report file"))))) {
					log+=line+"<br>";
					error=true;
				}

				if((line.contains("warning"))||(line.contains("WARNING"))) { 
					log+=line+"<br>";
					error=true;
				}
			}

			if(error==false) log ="Installation log file verification was executed successfully."; 
		} catch( IOException ex ) {
			log += "Error during the verification of the installation log file.";
			log += "<br>Reading File Error: "+ex.getMessage();
			return log; 
		}
		return log;
	}

	/**
	 *******************************************************************************************
	 * Perform the QTP Tests execution (execute script).
	 * @param qtpfile address containing the direct link to the QTP start script file (*.vbs).
	 * @return String containing the QTP Tests starting script result.
	 *******************************************************************************************
	 **/

	public String executeQTPTests(String qtpfile, String qtpServerSSH) {

		log = "";
		exitValue = 1;
		Runtime rt = Runtime.getRuntime();
		Process p = null;

		String command = qtpServerSSH+" CScript.exe "+qtpfile;
		command = command.replace(File.separator, "/");

		try {

			p = rt.exec(command);
			p.waitFor();
			exitValue = p.exitValue();

		} catch (Exception ex) { 
			log += "Error during VCEM QTP Tests initialization.";
			log += "<br>- "+ex.getMessage();
		}

		if(exitValue==0) log += "VCEM QTP Tests initiated successfully. Process exit value equals zero";
		else log += "Error initiating QTP Tests. Process exit value is different of zero.";

		log += "\nCommand executed: "+command;
		return log;
	}

	/**
	 *******************************************************************************************
	 *  Perform the VcemOutput XML file verification. 
	 *  The following results shoud be found in the file:
	 * <pre>
	 * - Status should be equal to SUCCESS.
	 * - Error Code should be equal to '0'.
	 * - Error Message should be equal to 'Action completed'.
	 * </pre>
	 * @param vcemXML VcemOutputXML containing the VcemOutputXML file results.
	 * @return String containing the VcemOutputXML verification results.
	 *******************************************************************************************
	 */
	public String verifyVcemXML(VcemOutputXML vcemXML){
		log = "";
		boolean test = true;

		if(!vcemXML.getStatus().equalsIgnoreCase("SUCCESS")){ 
			log = "The VcemOutput XML status '"+vcemXML.getStatus()+"' is different of expected `SUCCESS`<br>";
			test = false;
		}

		if(!vcemXML.getErrorCode().equalsIgnoreCase("0")){
			log += "The VcemOutput XML error code '"+vcemXML.getErrorCode()+"' is different of expected `0`<br>";
			test = false;
		}

		if(!vcemXML.getErrorMessage().equalsIgnoreCase("Virtual Connect Enterprise Manager installation was successfully completed.")){ 
			log += "The VcemOutput XML ErrorMessage '"+vcemXML.getErrorMessage()+"' is different of expected 'Virtual Connect Enterprise Manager installation was successfully completed.'<br>";
			test = false;
		}

		if(test==false) log += "Error during the verification of the VcemOutput XML file.";
		else log = "VcemOutput XML file verification was executed successfully.";

		return log;
	}
	/**
	 *******************************************************************************************
	 * Perform the Environment Variables verification. 
	 * @param name Environment Variable name.
	 * @param value Environment Variable value.
	 * @return String containing the Environment Variables verification results.
	 *******************************************************************************************
	 */
	public String VerifyEnvVariables(String name, String value) {

		log = "";
		boolean envName = false;
		boolean envValue = false;

		Map<String, String> variables = System.getenv();  
		for (Map.Entry<String, String> entry : variables.entrySet())  
		{  
			String entryName = entry.getKey();  
			String entryValue = entry.getValue();  

			if(name.equalsIgnoreCase(entryName)) envName = true;
			if(value.equalsIgnoreCase(entryValue)) envValue = true;
		}
		if(envName==false) log += "Error - Cannot find environment variable name: "+name;
		if(envValue==false) log += "<br>Error - Cannot find environment variable value: "+value;

		if((envName==true)&&(envValue==true))
			log = "Environment variable name '"+name+"' with value '"+value+"' was successfully found.";

		return log;
	}
}