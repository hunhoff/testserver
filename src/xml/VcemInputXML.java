package xml;

/**
 *******************************************************************************
 *Class responsible to manage the VcemInputXML informations.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class VcemInputXML {
	
	private String ActionValue = "";
	private String XMLOutputValue = "";
	private String LangID = "";
	private String InstallType ="";
	
	/** @return VcemInputXML InstallType Value*/
	public String getInstallType() {
		return InstallType;
	}
	/** @param actionValue - set VcemInputXML InstallType Value */
	public void setInstallType(String installType) {
		InstallType = installType;
	}
	
	/** @return VcemInputXML Action Value*/
	public String getActionValue() {
		return ActionValue;
	}
	/** @param actionValue - set VcemInputXML Action Value */
	public void setActionValue(String actionValue) {
		ActionValue = actionValue;
	}
	/** @return VcemInputXML Language ID*/
	public String getLangID() {
		return LangID;
	}
	/** @param langID - set VcemInputXML Language ID*/
	public void setLangID(String langID) {
		LangID = langID;
	}
	/** @return VcemInputXML OutputValue (this parameter specifies the 
	 * location where the VcemOutputFile will be created)*/
	public String getXMLOutputValue() {
		return XMLOutputValue;
	}
	/** @param outputValue - set VcemInputXML OutputValue (this parameter specifies the 
	 * location where the VcemOutputFile will be created*/
	public void setXMLOutputValue(String outputValue) {
		XMLOutputValue = outputValue;
	}
}
