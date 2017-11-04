package xml;

/**
 *******************************************************************************
 *Class responsible to manage the informations of the Registry Key Strings.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class RegKeyString {

	private String name = "";
	private String data = "";
	
	/** @return String containing the data of the registry key String.*/
	public String getData() {
		return data;
	}
	/** @param data	- set the data of the registry key String. */
	public void setData(String data) {
		this.data = data;
	}
	/** @return String containing the name of the registry key String.*/
	public String getName() {
		return name;
	}
	/** @param name - set the name of the registry key String.*/
	public void setName(String name) {
		this.name = name;
	}
}
