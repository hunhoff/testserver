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
 *Class responsible to all Post Uninstall Verifications.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class PostUninstallVerifications {

	private String log = "";
	private RegistryKey iceRegkey;
	private int exitValue;

	public PostUninstallVerifications(){}


	/** 
	 *********************************************************************************
	 * Verify in windows registry keys if the VCEM key and strings was created
	 * successfully. 
	 * @param regKeylist LinkedList containing the registry keys and strings to
	 * verify.
	 * ******************************************************************************* 					
	 **/	

	public String verifyRegKey(LinkedList<RegKey> regKeylist){

		log = "";
		String tmp = "";
		iceRegkey = null;

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

				if(!(iceRegkey==null)) log += "Error - key was not removed.\n<br>";
				else log += "key was successfully removed.\n<br>";
			} 
			catch (Exception e) {
				log += "key was successfully removed.\n<br>";
				return log;
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

	public String verifyRemovedFiles(String VCEMInstallPath, String HPSIMInstallPath, String file){

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
						if(f.exists()){
							if(isMissing==false) log = "Error - Files not removed: ";
							isMissing = true;
							log += "<br>"+tmp;
						}
					}
					else if(tmp.contains("HPSIMPath/")) {
						tmp = tmp.replace("HPSIMPath/", HPSIMInstallPath);
						f = new File(tmp);
						if(f.exists()){
							if(isMissing==false) log = "Error - Files not removed: ";
							isMissing = true;
							log += "<br>"+tmp;
						}
					}else {
						f = new File(tmp);
						if(f.exists()){
							if(isMissing==false) log = "Error - Files not removed: ";
							isMissing = true;
							log += "<br>"+tmp;
						}
					}

				}
				if(isMissing==false) log="Uninstallation files verification was executed successfully.";
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
	 * @param fileName - address containing the direct link to the uninstallation log file.
	 * @return String containing the log verification results.
	 *******************************************************************************************
	 **/
	public String UninstallLogReader(String fileName) {
		
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
						&&((!(line.contains("Loading the error messages from")))
						&&(!(line.contains("Language to be used:")))
						&&(!(line.contains("mvcd_CLI_Failover.xml- executed")))
						&&(!(line.contains("Deleting file:"))))){
					log+=line+"<br>";
					error=true;
				}

				if((line.contains("warning"))||(line.contains("WARNING"))) { 
					log+=line+"<br>";
					error=true;
				}
			}

			if(error==false) log ="Uninstallation log file verification was executed successfully."; 
		} catch( IOException ex ) {
			log += "Error during the verification of the uninstallation log file.";
			log += "<br>Reading File Error: "+ex.getMessage();
			return log; 
		}
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
		boolean envName = true;
		boolean envValue = true;

		Map<String, String> variables = System.getenv();  
		for (Map.Entry<String, String> entry : variables.entrySet())  
		{  
			String entryName = entry.getKey();  
			String entryValue = entry.getValue();  

			if(name.equalsIgnoreCase(entryName)) envName = false;
			if(value.equalsIgnoreCase(entryValue)) envValue = false;
		}
		if(envName==false) log += "Error - variable name: '"+name+"' was not removed";
		if(envValue==false) log += "Error - variable value: '"+value+"' was not removed";
		
		if((envName==true)&&(envValue==true))
		log = "Environment variable name '"+name+"' with value '"+value+"' was successfully removed.";

		return log;
	}
}
