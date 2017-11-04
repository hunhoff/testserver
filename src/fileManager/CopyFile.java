package fileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 ****************************************
 *Class responsible to copy the files.
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 ****************************************
 **/

public class CopyFile {

	public CopyFile(){}

	private String log = "";
	private boolean isCopied = false;
	
	/**
	 *******************************************************************************************
	 * Perform a file copy from a source to a destination (path + filename).
	 * @param source - location and name of the file to copy.
	 * @param path - location where the file will be copied.
	 * @param fileName - name that the copied file will receive.
	 * @return 'true' if the file was copied correctly <br>'false' if a error ocurr during the
	 * process.
	 *******************************************************************************************
	 */
	public boolean copy(String source, String path, String fileName){

		try{
			boolean success = (new File(path)).mkdirs();
		}catch (Exception e){
			log += "Error creating directory.";
			log += "<br>- " + e.getMessage();
		}
		
		String destination = path+fileName;
		
		InputStream IS = null;
		OutputStream OS = null;

		try{
			int read = 0;
			File s = new File(source);
			IS = new FileInputStream(s);
			File d = new File(destination);
			OS = new FileOutputStream(d);

			byte[] buffer = new byte[1024];
			while ((read = IS.read(buffer)) != -1) {
				OS.write(buffer, 0, read);
			}
		}

		catch(FileNotFoundException ex){
			log += "Error executing file copy from '"+source+"' to '"+destination+"'.";
			log += "<br>- "+ex.getMessage();
			log += "<br>- "+ex.getCause()+"<br>";
			return isCopied;
		}
		catch(IOException ex){
			log += "Error executing file copy from '"+source+"' to '"+destination+"'.";
			log += "<br>- "+ex.getMessage();
			log += "<br>- "+ex.getCause()+"<br>";
			return isCopied;
		}
		catch(Exception ex){
			log += "Error executing file copy from '"+source+"' to '"+destination+"'.";
			log += "<br>- "+ex.getMessage();
			log += "<br>- "+ex.getCause()+"<br>";
			return isCopied;
		}

		finally {
			try {
				if (IS != null) { IS.close(); }
				if (OS != null) { OS.close(); }
			} catch (IOException e) {
				log += "Warning, the connection to the copy process could not be closed.";
				log += "<br>- "+e.getMessage();
				log += "<br>- "+e.getCause()+"<br>";
			}
		}
		log += "Log file copy finished successfully from '"+source+"' to '"+destination+"'<br>";
		isCopied = true;
		return isCopied;
	}

	/**
	 *******************************************************************************************
	 * Return the results saved during the files copy.
	 * @return String containing the copy results.
	 ******************************************************************************************* 
	 **/
	public String getLog() {
		return log;
	}
}
