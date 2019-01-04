package main.org.usfirst.frc.team1640.vision;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.org.usfirst.frc.team1640.constants.vision.ConnectionConstants;
import main.org.usfirst.frc.team1640.vision.AndroidDebugBridgeIO.CONNECTION_STATE;

/**
 * Class that interfaces with the Android Phone over and ADB Connection
 * Inspired by Team 254 Code
 */
public class AndroidDebugBridgeIO 
{
	private static AndroidDebugBridgeIO adbIO;
	
	/**
	 * Singleton AndroidDebugBridgeIO getter
	 * @return The Only Instance of AndroidDebugBridgeIO
	 */
	public static AndroidDebugBridgeIO getInstance()
	{
		if(adbIO == null)
			adbIO = new AndroidDebugBridgeIO();
		return adbIO;
	}
	
	public enum CONNECTION_STATE {DISCONNECTED, UNAUTHORIZED, CONNECTED};
	
	//Commands for the Android Debug Bridge
	//https://developer.android.com/studio/command-line/adb.html
	/*
	private static final String START_ADB = "start-server";
	private static final String STOP_ADB = "kill-server";
	*/
	private static final String START_ADB = "start";
	private static final String STOP_ADB = "stop";
	
	private static final String FORWARD_ADB = "forward";
	private static final String REVERSE_ADB = "reverse";
	private static final String TCP_REF_ADB = " tcp:";
	private static final String PORT_REMOVE_ADB = " --remove-all";
	
	private static final String RESTART_DEVICE = "reboot";
	private static final String DEVICES_ADB = "devices";
	private static final String DEVICES_ADB_SPLITTER = "attached";
	private static final String DEVICES_ADB_RESPONSE_CONNECTED = "device";
	private static final String DEVICES_ADB_RESPONSE_UNAUTHORIZED = "authorize";
	
	private static final String SHELL_ADB = "shell";
	private static final String SHELL_START_APP = " am start ";
	private static final String SHELL_STOP_APP = " am force-stop "; 
	
	private Path androidPath;
	
	/**
	 * Instantiates the AndroidDebugBridgeIO and Sets the Path
	 */
	private AndroidDebugBridgeIO()
	{
		androidPath = Paths.get(ConnectionConstants.RIO_ADB_LOCATION);
	}
	
	/**
	 * Generic Java Command With Try-Catch
	 * @see "https://github.com/team8/RIOdroid/blob/master/src/org/spectrum3847/RIOdroid/RIOdroid.java"
	 * @param command - 'Command-Line' Command to Execute
	 * @return The Response of the Command
	 */
	private String command(String command)
	{
		try
		{
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
			
			StringBuffer commandResponse = new StringBuffer();
			BufferedReader commandResponseReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line = "";
			while((line = commandResponseReader.readLine()) != null)
			{
				commandResponse.append(line);
				commandResponse.append("\n");
			}
			
			return commandResponse.toString();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Base Command Method for ADB (All other commands pass strings to this)
	 * @param commandAppend - Android Debug Bridge Command to be run
	 * @return Any Command Response
	 */
	public String commandADB(String commandAppend)
	{
		return command(androidPath.toString() + " " + commandAppend);
	}
	
	/**
	 * Special Command Method for a roboRIO Root Access Script (So that adb can have access to usb)
	 * @param commandAppend - ADB Root Script Command to be run
	 * @return Any Command Response
	 */
	public String commandScript(String commandAppend)
	{
		return command(ConnectionConstants.ROOT_ADB_LOCATION + " " + commandAppend);
	}
	
	/**
	 * Starts the Android Debug Bridge Server
	 */
	public void startADB()
	{
		commandScript(START_ADB);
	}
	
	/**
	 * Kills the Android Debug Bridge Server
	 */
	public void stopADB()
	{
		commandScript(STOP_ADB);
	}
	
	/**
	 * Gets the Current Device List from ADB
	 * @return Current Device Status
	 */
	public String listDevicesADB()
	{
		return commandADB(DEVICES_ADB);
	}
	
	/**
	 * Parses a Response to listDevicesADB to See If It is Connected
	 * @param listDevicesOutput - a Response to listDevicesADB
	 * @return Whether or Not the Device Is Connected
	 */
	public boolean isDeviceAvailable(String listDevicesOutput)
	{
		if(listDevicesOutput == null)
		{
			return false;
		}
		
		int titleIndex = listDevicesOutput.indexOf(DEVICES_ADB_SPLITTER, 0);
		
		if(titleIndex < 0)
		{
			return false;
		}
		
		String newString = listDevicesOutput.substring(titleIndex, listDevicesOutput.length());
		return newString.contains(DEVICES_ADB_RESPONSE_CONNECTED);
	}
	
	/**
	 * Parses a Response to listDevicesADB to See If It is Unauthorized
	 * @param listDevicesOutput - a Response to listDevicesADB
	 * @return Whether or Not the Device Is Authorized (false)
	 */
	public boolean isDeviceUnauthorized(String listDevicesOutput)
	{
		if(listDevicesOutput == null)
		{
			return false;
		}
		
		int titleIndex = listDevicesOutput.indexOf(DEVICES_ADB_SPLITTER, 0);
		
		if(titleIndex < 0)
		{
			return false;
		}
		
		String newString = listDevicesOutput.substring(titleIndex, listDevicesOutput.length());
		return newString.contains(DEVICES_ADB_RESPONSE_UNAUTHORIZED);
	}
	
	/**
	 * Calls the List of ADB Devices and Parses it For Its Connection State
	 * @return
	 */
	public CONNECTION_STATE getConnectionState()
	{
		return getConnectionState(listDevicesADB());
	}
	
	/**
	 * Manages the Parsing of List Devices ADB Output For Its Connection State
	 * @param deviceOutput - a Response to listDevicesADB
	 * @return What Condition the ADB Connection Status Is
	 */
	public CONNECTION_STATE getConnectionState(String deviceOutput)
	{
		if(isDeviceAvailable(deviceOutput))
		{
			return CONNECTION_STATE.CONNECTED;
		}
		else if(isDeviceUnauthorized(deviceOutput))
		{
			return CONNECTION_STATE.UNAUTHORIZED;
		}
		else
		{
			return CONNECTION_STATE.DISCONNECTED;
		}
	}
	
	/**
	 * Makes a local port send data to a phone port
	 * @param local - RoboRIO port
	 * @param remote - Android port
	 */
	public void fowardADB(int local, int remote)
	{
		commandADB(FORWARD_ADB + TCP_REF_ADB + local + TCP_REF_ADB + remote);
	}
	
	/**
	 * Closes all 'forwarded' ADB port
	 */
	public void stopForwardADB()
	{
		commandADB(FORWARD_ADB + PORT_REMOVE_ADB);
	}
	
	/**
	 * Makes a phone port send data to a local port
	 * @param local - RoboRIo port
	 * @param remote - Android port
	 */
	public void reverseADB(int local, int remote)
	{
		commandADB(REVERSE_ADB + TCP_REF_ADB + remote + TCP_REF_ADB + local);
	}
	
	/**
	 * Closes all 'reversed' ADB ports
	 */
	public void stopReverseADB()
	{
		commandADB(REVERSE_ADB + PORT_REMOVE_ADB);
	}
	
	/**
	 * Starts the Specified Application
	 */
	public void startApp()
	{
		commandADB(SHELL_ADB + SHELL_START_APP + ConnectionConstants.ANDROID_APP_NAME 
				+ "/" + ConnectionConstants.ANDROID_APP_NAME + ConnectionConstants.ANDROID_APP_ACTIVITY);
	}
	
	/**
	 * Stops the Specified Application
	 */
	public void stopApp()
	{
		commandADB(SHELL_ADB + SHELL_STOP_APP + ConnectionConstants.ANDROID_APP_NAME);
	}
	
	/**
	 * Restarts the Specified Application
	 */
	public void restartApp()
	{
		stopApp();
		startApp();
	}
	
	/**
	 * Kills and Then Starts the ADB
	 */
	public void restartADB()
	{
		
		stopADB();
		startADB();
	}
	
	/**
	 * Sends a Restart Android Phone Command
	 */
	public void restartAndroid()
	{
		commandADB(RESTART_DEVICE);
	}
	
	/**
	 * Checks the ADB Device Connection Status
	 * @return Whether or Not the Device Is Properly Connected
	 */
	public boolean setUpDeviceADB()
	{
		String currentDevices = listDevicesADB();
		CONNECTION_STATE status = getConnectionState(currentDevices);
		
		switch(status)
		{
			case CONNECTED:
				System.out.println("Device Found");
				break;
			case UNAUTHORIZED:
				//adbIO.stopADB();
				System.out.println("Device Unauthorized");
				return false;
			case DISCONNECTED:
//				System.out.println("Device Not Found");
				return false;
		}
		
		restartApp();
		
		reverseADB(ConnectionConstants.DEFAULT_PORT, ConnectionConstants.DEFAULT_PORT);
		
		if(ConnectionConstants.SHOULD_STREAM_VIDEO)
		{
			fowardADB(ConnectionConstants.DEFAULT_VIDEO_PORT, ConnectionConstants.DEFAULT_VIDEO_PORT);
		}
		return true;
	}
}
