package xml;

/**
 ****************************************
 *Class responsible to manage the VcemOutputXML informations.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 ****************************************
 **/

public class VcemOutputXML {
	
	private String status = "";
	private String errorCode = "";
	private String errorMessage = "";
	
	/**@return String containing the Error Code (this information should be found
	 * in the VcemOutput XML file).*/
	public String getErrorCode() {
		return errorCode;
	}
	/**@param errorCode - set the Error Code (this information should be found in the
	 * VcemOutput XML file)	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**@return String containing the Error Message (this information should be found
	 * in the VcemOutput XML file).*/
	public String getErrorMessage() {
		return errorMessage;
	}
	/**@param errorMessage - set the Error Message (this information should be found in the
	 * VcemOutput XML file)	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**@return String containing the Status (this information should be found
	 * in the VcemOutput XML file).*/
	public String getStatus() {
		return status;
	}
	/**@param status - set the Status (this information should be found in the
	 * VcemOutput XML file)	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
