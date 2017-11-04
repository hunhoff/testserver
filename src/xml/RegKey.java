package xml;
import java.util.LinkedList;


/**
 *******************************************************************************
 *Class responsible to manage the Registry Keys informations.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class RegKey {

	private String key = "";
	private LinkedList<RegKeyString> keyStringList = new LinkedList<RegKeyString>();

	/** Add RegKeyString to a LinkedList of RegKeyString. 
	 * @param keyString - < RegKeyString >  to be added. */
	public void addRegString(RegKeyString keyString){
		keyStringList.add(keyString);
	}
	/** @return LinkedList containing a list of RegKeyString.*/
	public LinkedList<RegKeyString> getKeyStringList() {
		return keyStringList;
	}
	/** @return String containing the registry key location and name.*/
	public String getKey() {
		return key;
	}
	/** @param key - set the registry key location and name.*/
	public void setKey(String key) {
		this.key = key;
	}
}