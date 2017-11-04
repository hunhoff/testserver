package download;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.ice.jni.registry.RegistryKey;

/**
 *******************************************************************************
 *Class responsible to download the files.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class Download {

	private int read;
	private long write;
	private String log="";
	private String downloadPath="";

	public Download(){}

	/**
	 *******************************************************************************************
	 * Perform a file download from a web page to a local machine.
	 * @param urlPath - url address containing the location and name of the file to download.
	 * @param localPath - location where the file will be downloaded. 
	 * @param filename - name that the downloaded file will receive.
	 * @param proxyHost - proxy host (optional)
	 * @param proxyPort - proxy port (optional)
	 * @return 'true' if the file was downloaded correctly 
	 * <br>'false' if a error ocurr during the process.
	 *******************************************************************************************
	 **/
	
	public boolean downloadFile(String urlPath, String localPath, String filename, String proxyHost, String proxyPort) 
	{
		write = 0;
		read = 0;
		OutputStream OS = null;
		URLConnection connection = null;
		InputStream  IS = null;
	
		/**
		 * Creates local directory if doesn't exists already.
		 **/
		try{
			boolean success = (new File(localPath)).mkdirs();
		}catch (Exception e){
			log+= "Error creating directory.";
			log+= "<br>- " + e.getMessage();
			log+= "<br>- " + e.getCause();
			return false;
		}
		
		String urlFileAddress = "";
		urlFileAddress = urlPath+""+filename;
		
		String localFilePath = "";
		localFilePath = localPath+File.separator+filename;

		downloadPath = localPath+File.separator;

		try {
			URL url = new URL(urlFileAddress);

			if(proxyHost!=null){
				System.setProperty("http.proxyHost",proxyHost);
				System.setProperty("http.proxyPort",proxyPort);
			}

			connection = url.openConnection();
			IS = connection.getInputStream();
			OS = new BufferedOutputStream(new FileOutputStream(localFilePath));
			byte[] buffer = new byte[1024];
			while ((read = IS.read(buffer)) != -1){
				/**
				 * Writes [read] bytes from the specified byte array [buffer] starting at offset [0] to 
				 * this output stream.
				 **/
				OS.write(buffer, 0, read);
				write += read;
			}

			log+= "Download completed successfully. See details below:" +
					"<br>- from '"+urlFileAddress+"' " +
					"<br>- to '"+localFilePath+"' " +
					"<br>- file size = "+write+" bytes.<br>";

		} catch (Exception e) {
			log = "Error executing download from '"+urlFileAddress+"'.";
			log += "<br>- "+e.getMessage();
			log += "<br>- "+e.getCause();
			return false;
		} finally {
			try {
				if (IS != null) { IS.close(); }
				if (OS != null) { OS.close(); }
			} catch (IOException e) {
				log += "<br>Warning, the connection could not be closed.";
				log += "<br>- "+e.getMessage();
				log += "<br>- "+e.getCause();
			}
		}
		return true;
	}
	/**
	 *******************************************************************************************
	 * Return the results saved during the files download.
	 * @return String containing the download results.
	 ******************************************************************************************* 
	 **/

	public String getLog() {
		return log;
	}

	/**
	 *******************************************************************************************
	 * Return the path to the files downloaded.
	 * @return String containing the path to the files downloaded.
	 ******************************************************************************************* 
	 **/

	public String getdownloadPath() {
		return downloadPath;
	}
}
