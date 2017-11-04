package testManager;

/**
 *******************************************************************************
 *Class responsible to calculate the Test Server execution time. 
 *@author      Alessandro Hunhoff 
 *@author 	   alessandro.hunhoff@hp.com 
 *@since       1.0.0
 *@version     1.0.23
 *******************************************************************************
 **/

public class TimeController {
	
	private double startTime;
	private double stopTime;

	/**
	 *************************************************************************************************
	 * Starts a timer.
	 *************************************************************************************************
	 **/
	public void startTime(){
		this.startTime = 0.0;
		this.startTime = (double)System.currentTimeMillis() / 1000D;
	}
	
	/**
	 *************************************************************************************************
	 * Stops the timer, calculates the time expended between the start and stop of the process. 
	 * @return double value containing the time expended.
	 *************************************************************************************************
	 **/
	public double stopTime(){
		this.stopTime = 0.0;
		this.stopTime = (double)System.currentTimeMillis() / 1000D;
        double time = (this.stopTime - this.startTime);
        if(time >= 3600.0) time = time/3600.0;
        return time;
	}
}
