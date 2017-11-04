package download;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 *******************************************************************************
 *Class responsible to identify the files on CruiseControl.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class HTTPUnit {

	private WebLink links[] = null;
	private LinkedList<String> buildList;
	private String log = "";
	private String buildNumber = "";
	private String URLPath = "";
	private boolean isIdentified = false;

	public HTTPUnit(){}

	/**
	 ****************************************************************************************************
	 * Take access to the URL informed by the user, selects the project that has the build to download,
	 * with the objective of identify the valid and available builds. <br>Identify and return a specific 
	 * build required.
	 * @param URL - address containing the direct link to the page with the builds to download.
	 * @param specificBuild - string containing the build number required. 
	 * @param project - project name (i.e. Yeti1.10).
	 * @param proxyHost - proxy host (optional)
	 * @param proxyPort - proxy port (optional)
	 * @return LinkedList String list containing the files information of the build specified. 
	 ****************************************************************************************************
	 **/

	public LinkedList<String> getSpecificBuild(String URL, String project, String specificBuild, String proxyHost, String proxyPort) {

		buildList = new LinkedList();

		try	{
			WebConversation wc = new WebConversation();
			if(!proxyHost.equalsIgnoreCase("")){
				System.setProperty("http.proxyHost",proxyHost);
				System.setProperty("http.proxyPort",proxyPort);
			}

			WebRequest req = new GetMethodWebRequest(URL);
			WebResponse respOrig = wc.getResponse(req);	
			WebResponse response = respOrig;

			/*listForms = response.getForms();
			for(WebForm form: listForms){} */ 

			links = response.getLinks();
			for(WebLink link: links) {	
				if(link == null) throw new HttpUnitException("Error, the component with name = "+link+" wasn't found!");
				else {
					if(link.asText().toString().equalsIgnoreCase(project)) {	
						//System.out.println("Yeti project link found: "+ link.asText().toString()+"\n");
						response = link.click();
					}
				}
			}
			links = response.getLinks();
			for(WebLink link: links) {	
				if(link == null) throw new HttpUnitException("Error, the component with name = "+link+" wasn't found!");
				else {
					if(link.asText().toString().contains(specificBuild)) {	
						//System.out.println("Yeti build link found: "+ link.asText().toString()+"\n");
						response = link.click();
						buildNumber = formatBuildNumber(link.asText().toString());
						break;
					}
				}
			}
			links = response.getLinks();
			for(WebLink link: links) {	
				if(link == null) throw new HttpUnitException("Error, the component with name = "+link+" wasn't found!");
				else {
					if(link.asText().toString().equalsIgnoreCase("Build Artifacts")) {	
						//System.out.println("Yeti build artifacts link found: "+ link.asText().toString()+"\n");
						response = link.click();
					}
				}
			}
			links = response.getLinks();

			boolean geturl = false;
			for(WebLink link: links) {	
				if(link == null) throw new HttpUnitException("Error, the component with name = "+link+" wasn't found!");
				else {
					if(geturl==false) {
						URLPath = response.getURL().toString();
						geturl=true;
					}

					//add the files names in the list
					if(link.asText().toString().contains(".")) { 
						//System.out.println("Yeti file download links found: "+ link.asText().toString()+"\n");
						buildList.add(link.asText().toString());
					}
				}
			}

			/*Iterator it = buildList.iterator();
			System.out.println("\nYETI BUILD FILES");
			while(it.hasNext())	{
				System.out.println(it.next().toString());
			}*/

			log+="Specific build correctly identified in CruiseControl page.<br>";
			log+="Build identified: "+buildNumber;
			isIdentified=true;

		}
		catch(UnknownHostException e){
			log = "Error accessing Cruise Control.<br>- "+e.getMessage();
			log += "<br>- "+e.getCause()+"<br>";;
			return buildList;
		}

		catch(NullPointerException e){
			log = "Error accessing Cruise Control.<br>- "+e.getMessage();
			log += "<br>- "+e.getCause()+"<br>";;
			return buildList;
		}
		catch(Exception e){
			log = "Error accessing Cruise Control.<br>- "+e.getMessage();
			log += "<br>- "+e.getCause()+"<br>";;
			return buildList;
		}
		return buildList;
	}

	/**
	 ****************************************************************************************************
	 * Take access to the URL informed by the user, selects the project that has the build to download 
	 * with the objective of identify the valid and available builds. <br>Identify and return the latest
	 * build found.
	 * @param URL - address containing the direct link to the page with the builds to download. 
	 * @param project - project name (i.e. Yeti1.10).
	 * @param proxyHost - proxy host (optional)
	 * @param proxyPort - proxy port (optional)
	 * @return LinkedList String list containing the files informations of the last valid and available 
	 * build. 
	 ****************************************************************************************************
	 **/
	public LinkedList<String> getLatestBuild(String URL, String project, String proxyHost, String proxyPort) {
		
		buildList = new LinkedList();

		try	{
			WebConversation wc = new WebConversation();
			if(!proxyHost.equalsIgnoreCase("")){
				System.setProperty("http.proxyHost",proxyHost);
				System.setProperty("http.proxyPort",proxyPort);
			}

			WebRequest req = new GetMethodWebRequest(URL);
			WebResponse respOrig = wc.getResponse(req);	
			WebResponse response = respOrig;

			/*listForms = response.getForms();
			for(WebForm form: listForms){} */ 

			links = response.getLinks();
			for(WebLink link: links) {	
				if(link == null) throw new HttpUnitException("Error, the component with name = "+link+" wasn't found!");
				else {
					if(link.asText().toString().equalsIgnoreCase(project)) {	
						//System.out.println("Yeti project link found: "+ link.asText().toString()+"\n");
						response = link.click();
					}
				}
			}

			boolean isBuild = false;
			links = response.getLinks();
			for(WebLink link: links) {
				if(link == null) throw new HttpUnitException("Error, the component with name = "+link+" wasn't found!");
				else {
					if(link.asText().toString().equalsIgnoreCase("")) 
						isBuild = false;
					
					if((isBuild)&&(link.asText().toString().contains("("))) { 
						response = link.click();
						buildNumber = formatBuildNumber(link.asText().toString());
						break;
					}
					if(link.asText().toString().equalsIgnoreCase("Latest Build")) 
						isBuild = true;
				}
			}
			links = response.getLinks();
			for(WebLink link: links) {	
				if(link == null) throw new HttpUnitException("Error, the component with name = "+link+" wasn't found!");
				else {
					if(link.asText().toString().equalsIgnoreCase("Build Artifacts")) {	
						//System.out.println("Yeti build artifacts link found: "+ link.asText().toString()+"\n");
						response = link.click();
					}
				}
			}
			boolean geturl=false;
			links = response.getLinks();
			for(WebLink link: links) {	
				if(link == null) throw new HttpUnitException("Error, the component with name = "+link+" wasn't found!");
				else {
					if(geturl==false) {
						URLPath = response.getURL().toString();
						geturl=true;
					}
					//add the files names in the list
					if(link.asText().toString().contains(".")) { 
						//System.out.println("Yeti file download links found: "+ link.asText().toString()+"\n");
						buildList.add(link.asText().toString());
					}
				}
			}

			/*Iterator it = buildList.iterator();
			System.out.println("\nYETI BUILD FILES");
			while(it.hasNext())	{
				System.out.println(it.next().toString());
			}*/
			log+="Latest build correctly identified in CruiseControl page.<br>";
			log+="Build identified: "+buildNumber;
			isIdentified = true;
		}
		catch(UnknownHostException e){
			log = "Error accessing Cruise Control.<br>- "+e.getMessage();
			log += "<br>- "+e.getCause()+"<br>";;
			return buildList;
		}

		catch(NullPointerException e){
			log = "Error accessing Cruise Control.<br>- "+e.getMessage();
			log += "<br>- "+e.getCause()+"<br>";;
			return buildList;
		}
		catch(Exception e){
			log = "Error accessing Cruise Control.<br>- "+e.getMessage();
			log += "<br>- "+e.getCause()+"<br>";
			return buildList;
		}
		return buildList;
	}

	private class HttpUnitException extends Exception {
		String message;

		public HttpUnitException(String s){
			super();
			log += s+"<br>";
		}

		public String getMessage(){
			return message;
		}
	}

	/**
	 *******************************************************************************************
	 * Return the saved information during the build search to be writed in the log file.
	 * @return String containing the build search results.
	 ******************************************************************************************* 
	 **/

	public String getLog() {
		return log;
	}

	/**
	 *******************************************************************************************
	 * Return the build number information.
	 * @return string containing the build number found.
	 ******************************************************************************************* 
	 **/
	public String getBuildNumber() {
		return buildNumber;
	}

	/**
	 *************************************************************************************************
	 * Get the string containing the buid link and change the format to identify the correct build 
	 * number.
	 * @param build Example = 04/09/2008 10:18:58 (1.10.0.276)
	 * @return Example = 1.10.0.276
	 *************************************************************************************************
	 **/

	public String formatBuildNumber(String build){
		String tmp="";
		try {

			StringTokenizer token = new StringTokenizer(build, "("); 
			while (token.hasMoreTokens()) { 
				tmp = token.nextToken().toString();
				if(tmp.contains(")"))
					build = tmp.replace(")","");
			}

		} catch (Exception e) {
			System.out.println("Error formating build number "+e.getMessage());
		}
		return build;
	}
	
	/**
	 *******************************************************************************************
	 * Return the URL path to the files.
	 * @return path to the files where the files of the build identified are found.
	 ******************************************************************************************* 
	 **/

	public String getURLPath() {
		return URLPath;
	}

	/**
	 *******************************************************************************************
	 * Return the build identification result.
	 * @return false if cannot identify build | true if the build was identified correctly
	 ******************************************************************************************* 
	 **/
	public boolean isIdentified() {
		return isIdentified;
	}
}
