package xml;
import java.util.LinkedList;

/**
 *******************************************************************************
 *Class responsible to manage the QTP results.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class QTPResultsXML {
	
	private String name = "";
	private String description = "";
	private String result = "";
	private LinkedList<String> commentList = new LinkedList<String>();
	
	public QTPResultsXML(){}
	
	/**Add comment to the comment list.
	 * @param comment - comment to be added.*/
	public void addComment(String comment){
		this.commentList.add(comment);
	}
	/** @return LinkedList containing the comment list.*/
	public LinkedList<String> getCommentList() {
		return commentList;
	}
	/** @return String containing the test description.*/
	public String getDescription() {
		return description;
	}
	/** @param description - set the test description.*/
	public void setDescription(String description) {
		this.description = description;
	}
	/** @return String containing the test name.*/
	public String getName() {
		return name;
	}
	/** @param name - set the test name.*/
	public void setName(String name) {
		this.name = name;
	}
	/** @return String containing the test result.*/
	public String getResult() {
		return result;
	}
	/** @param result - set the test result (warn, pass, unknown, not executed, done, fail)*/
	public void setResult(String result) {
		this.result = result;
	}
}
