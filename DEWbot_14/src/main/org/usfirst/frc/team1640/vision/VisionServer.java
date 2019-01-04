package main.org.usfirst.frc.team1640.vision;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionRunner.Listener;
import edu.wpi.first.wpilibj.vision.VisionThread;
import main.org.usfirst.frc.team1640.vision.AndroidDebugBridgeIO.CONNECTION_STATE;
import main.org.usfirst.frc.team1640.vision.messages.CameraTargetInfo;
import main.org.usfirst.frc.team1640.vision.messages.VisionUpdate;
import main.org.usfirst.frc.team1640.constants.vision.ConnectionConstants;
import main.org.usfirst.frc.team1640.constants.vision.MathConstants;
import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

/**
 * The Class That Manages All Connections And Data Between the Android Phone
 */
public class VisionServer extends CatchingThread implements Listener<BlobVisionPipeline>
{	
	private Object synchronizedObject;
	private IRobotContext robotContext;
	private ArrayList<VisionUpdate> visionUpdates;
	private ServerSocket serverSocket;
	private ArrayList<ServerThread> serverThreads;
	private boolean isRunning;
	private boolean isConnected;
	private boolean shouldUseJavaTime;
	private boolean shouldCheckConnection;
	private boolean hasReceivedFirstVisionUpdate;
	private boolean isUsingLowerUsb;
	private double lastMessageReceivedTime;
	
	private int printNum;
	
	//Way of Selectively Picking Correct Vision Target
	//Currently, None are Implemented
	public enum VISION_CRITERION {FIELD, STRAFE, INTAKE};
	
	private UsbCamera usbCamera;
	private UsbCamera usbCamera2;
	private MjpegServer mjpegServer;
	
	private BlobVisionPipeline onBoardVisionPipeline;
	private VisionRunner<BlobVisionPipeline> onBoardVisionRunner;
	private VisionThread onBoardVisionThread;
	private MjpegServer cvMjpegServer;
	
	/**
	 * Tries to Create the Vision Server and Starts the Thread
	 */
	public VisionServer(IRobotContext robotContext)
	{	
		this.robotContext = robotContext;
		visionUpdates = new ArrayList<>();
		shouldUseJavaTime = ConnectionConstants.SHOULD_USE_JAVA_TIME;
		
		if(ConnectionConstants.SHOULD_USE_ANDROID)
		{
			try
			{
				boolean deviceAttached = true;
				
				if(ConnectionConstants.SHOULD_USE_ADB)
				{
					System.out.println("Starting ADB");
					AndroidDebugBridgeIO adbIO = AndroidDebugBridgeIO.getInstance();
					
					//adbIO.startADB();
					
					String currentDevices = adbIO.listDevicesADB();
					if(!adbIO.isDeviceAvailable(currentDevices))
					{
						deviceAttached = false;
						System.out.println("Device Not Found!");
					}
					
					adbIO.restartApp();
						
					adbIO.reverseADB(ConnectionConstants.DEFAULT_PORT, ConnectionConstants.DEFAULT_PORT);
					
					if(ConnectionConstants.SHOULD_STREAM_VIDEO)
					{
						adbIO.fowardADB(ConnectionConstants.DEFAULT_VIDEO_PORT, ConnectionConstants.DEFAULT_VIDEO_PORT);
					}
					System.out.println("Finished ADB");
				}
				
				synchronizedObject = new Object();
				serverSocket = new ServerSocket(ConnectionConstants.DEFAULT_PORT);
				serverThreads = new ArrayList<>();
				isRunning = deviceAttached;
				shouldCheckConnection = false;
				isConnected = false;
				hasReceivedFirstVisionUpdate = false;
				lastMessageReceivedTime = getTimestamp();
				printNum = 0;
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
				isRunning = false;
			}
		}
		
		if(ConnectionConstants.SHOULD_USE_USB_CAM)
		{
			isUsingLowerUsb = true;
			
			try
			{
				//CameraServer.getInstance().startAutomaticCapture();
				usbCamera = new UsbCamera("cam0", 0);
				usbCamera.setBrightness(50);
				usbCamera.setExposureHoldCurrent();
				usbCamera.setWhiteBalanceHoldCurrent();
				usbCamera.setFPS(25);
				usbCamera.setResolution(160, 120);
				
				mjpegServer = new MjpegServer("server_USB Camera 0", ConnectionConstants.DEFAULT_WEBCAM_PORT);
				mjpegServer.setSource(usbCamera);
				
				if(ConnectionConstants.IS_MORE_THAN_ONE_USB_CAM)
				{
					try
					{
						usbCamera2 = new UsbCamera("cam1", 1);
						usbCamera2.setBrightness(50);
						usbCamera2.setFPS(25);
						usbCamera.setExposureHoldCurrent();
						usbCamera.setWhiteBalanceHoldCurrent();
						usbCamera.setFPS(25);
						usbCamera.setResolution(160, 120);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				
				if(ConnectionConstants.SHOULD_USE_ONBOARD_VISION && ConnectionConstants.SHOULD_STREAM_ONBOARD_VISION)
				{
					cvMjpegServer = new MjpegServer("server_USB CV", 5801);
				}

			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		this.start();
	}

	/**
	 * Main Thread Loop - Creates ServerThreads and Determines Overall Connection Status
	 */
	@Override
	protected void runWithCatch() 
	{
		while (true)
		{
			if(ConnectionConstants.SHOULD_USE_ANDROID)
			{
				if(isRunning)
				{
					try
					{
						Socket socket = serverSocket.accept();
						ServerThread serverThread = new ServerThread(this, socket, shouldUseJavaTime);
						serverThread.start();
						serverThreads.add(serverThread);
					}
					catch(IOException ioe)
					{
						ioe.printStackTrace();
					}
					finally
					{
						try
						{
							sleep(ConnectionConstants.DEFAULT_SLEEP_TIME);
						}
						catch(InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					isRunning = AndroidDebugBridgeIO.getInstance().setUpDeviceADB();
				}
				
				if(shouldCheckConnection)
				{
					for(ServerThread serverThread : serverThreads)
						if(lastMessageReceivedTime < serverThread.getLastMessageTimestamp())
							lastMessageReceivedTime = serverThread.getLastMessageTimestamp();
					
					if(getTimestamp() - lastMessageReceivedTime > MathConstants.MAX_TIME_DIFF_MILLIS)
					{
						isConnected = false;
						if(printNum % 10 == 0)
						{
							System.out.println("Camera Connection Lost");
						}
						printNum++;
						
					}
					else
					{
						isRunning = AndroidDebugBridgeIO.getInstance().setUpDeviceADB();
						if(!isConnected)
						{
							System.out.println("Vision Connected");
						}
						
						isConnected = true;
						printNum = 0;
					}
					
					if(!isRunning)
					{
						try
						{
							sleep(ConnectionConstants.DEFAULT_SLEEP_TIME);
						}
						catch(InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
				
				shouldCheckConnection = !shouldCheckConnection;
			}
			
			if(ConnectionConstants.SHOULD_USE_USB_CAM)
			{
				if(ConnectionConstants.IS_MORE_THAN_ONE_USB_CAM)
				{
					Placer placer = robotContext.getPlacer();
					
					//System.out.println("USB Camera Update");
					if(placer != null)
					{
						boolean shouldBeUsingLowerUsb = placer.getLiftHeight() < 36;
						
						if(shouldBeUsingLowerUsb && !isUsingLowerUsb)
						{
							mjpegServer.setSource(usbCamera);
							isUsingLowerUsb = !isUsingLowerUsb;
						}
						else if(!shouldBeUsingLowerUsb && isUsingLowerUsb)
						{
							mjpegServer.setSource(usbCamera2);
							isUsingLowerUsb = !isUsingLowerUsb;
						}
					}
					
					try
					{
						if(!ConnectionConstants.SHOULD_USE_ANDROID)
							sleep(ConnectionConstants.DEFAULT_SLEEP_TIME);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				if(ConnectionConstants.SHOULD_USE_ONBOARD_VISION && onBoardVisionPipeline == null && usbCamera.getLastFrameTime() != 0)
				{
					System.out.println("Starting Onboard Camera");
					if(ConnectionConstants.SHOULD_STREAM_ONBOARD_VISION)
					{
						CvSource source = new CvSource("CVFrames", PixelFormat.kBGR, 160, 120, 25);
						onBoardVisionPipeline = new BlobVisionPipeline(usbCamera, source);
						cvMjpegServer.setSource(source);
					}
					else
					{
						onBoardVisionPipeline = new BlobVisionPipeline(usbCamera);
					}
					
					onBoardVisionRunner = new VisionRunner<BlobVisionPipeline>(usbCamera, onBoardVisionPipeline, this);
					onBoardVisionThread = new VisionThread(onBoardVisionRunner);
					onBoardVisionThread.start();
				}
				
				if(!ConnectionConstants.SHOULD_USE_ANDROID && !ConnectionConstants.IS_MORE_THAN_ONE_USB_CAM)
				{
					try
					{
						sleep(ConnectionConstants.DEFAULT_SLEEP_TIME);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Returns Pipeline Outputs for Local Processing
	 */
	@Override
	public void copyPipelineOutputs(BlobVisionPipeline pipeline) 
	{
		//TODO: Complete Pipeline Conversion
		ArrayList<CameraTargetInfo> latestTargets = pipeline.targets;
		double frameTime = pipeline.imageTimeTargets;
		
		System.out.println("Camera Frame Time " + frameTime);
		for(CameraTargetInfo latestTarget : latestTargets)
		{
			System.out.println("Target Height" + latestTarget.getHeight() + " Width " + latestTarget.getWidth());
		}
		
		addVisionUpdate(new VisionUpdate(latestTargets, frameTime));
	}
	
	/**
	 * Adds a VisionUpdate to the Queue of VisionUpdates
	 * @param visionUpdate - VisionUpdate to Be Added
	 */
	public void addVisionUpdate(VisionUpdate visionUpdate)
	{
		synchronized (synchronizedObject)
		{
			if(visionUpdates.size() >= ConnectionConstants.VISION_MEMORY_LENGTH)
				visionUpdates.remove(ConnectionConstants.VISION_MEMORY_LENGTH - 1);
			
			visionUpdates.add(0, visionUpdate);
		}
		
		if(!hasReceivedFirstVisionUpdate && ConnectionConstants.SHOULD_USE_PHONE_GYRO)
		{
			hasReceivedFirstVisionUpdate = true;
			setBaselinePhoneOrientation();
		}
		
		//double[] grav = getLatestPhoneGravity();
		//System.out.println("Gravity X " + grav[0] + " Y " + grav[1] +  " Z " + grav[2] + " T " + Math.toDegrees(Math.atan2(Math.hypot(grav[0], grav[1]), grav[2])));
	}
	
	/**
	 * Returns the Current List of VisionUpdates
	 * @return visionUpdates - the Current List of VisionUpdates
	 */
	public ArrayList<VisionUpdate> getCurrentVisionUpdates()
	{
		synchronized (synchronizedObject)
		{
			ArrayList<VisionUpdate> tempArray = new ArrayList<>();
			for(VisionUpdate update : visionUpdates)
			{
				tempArray.add(update);
			}
			return tempArray;
		}
	}
	
	/**
	 * Retrieves the Latest Phone Gravity Calculation (If None, Null)
	 * @return An Array of the Latest Phone Gravity Calculation
	 */
	public double[] getLatestPhoneGravity()
	{
		double[] gravity = null;
		ArrayList<VisionUpdate> latestUpdates = getCurrentVisionUpdates();
		
		for(int i = 0; i < latestUpdates.size() && gravity == null; i++)
		{
			VisionUpdate update = latestUpdates.get(i);
			if(update != null && update.getCapturedAgoMs() < MathConstants.MAX_TIME_DIFF_MILLIS * 1.5)
			{
				double[] rawGravity = update.getGravity();
				if(rawGravity != null)
				{
					gravity = rawGravity;
				}
			}
		}
		
		if(gravity == null)
		{
			return gravity;
		}
		
		double adjustedPitchX = gravity[0] * CameraTargetInfo.cameraPitchSin - gravity[2] * CameraTargetInfo.cameraPitchCos;
		double adjustedPitchY = gravity[1];
		double adjustedPitchZ = gravity[2]  * CameraTargetInfo.cameraPitchSin + gravity[0] * CameraTargetInfo.cameraPitchCos;
		
		//System.out.println("Gravity X " + adjustedPitchX + " Y " + adjustedPitchY +  " Z " + adjustedPitchZ);
		
		double adjustedYawX = adjustedPitchX * CameraTargetInfo.cameraYawCos + adjustedPitchY * CameraTargetInfo.cameraYawSin;
		double adjustedYawY = adjustedPitchY * CameraTargetInfo.cameraYawCos - adjustedPitchX * CameraTargetInfo.cameraYawSin;
		double adjustedYawZ = adjustedPitchZ;
		
		double[] adjustedGravity = {adjustedYawX, adjustedYawY, adjustedYawZ};
		
		return adjustedGravity;
	}
	
	/**
	 * If Within Certain Ranges, Will Update the CameraTargetInfo Classes Static Pitch and Yaw
	 * @param pitch - New Pitch (Conditional)
	 * @param yaw - New Yaw (Conditional)
	 */
	public void setBaselinePhoneOrientation(double pitch, double yaw)
	{
		if(Math.abs(pitch - MathConstants.DEFAULT_ANDROID_PITCH) < 8 &&
				Math.abs(pitch - MathConstants.DEFAULT_ANDROID_PITCH) > 2)
		{
			CameraTargetInfo.setCameraPitch(pitch);
		}
		else
		{
			System.out.println("Vision Server - Camera Pitch WAY OFF");
		}
		
		if(Math.abs(yaw - MathConstants.DEFAULT_ANDROID_YAW) < 8 &&
				Math.abs(yaw - MathConstants.DEFAULT_ANDROID_YAW) > 2)
		{
			CameraTargetInfo.setCameraYaw(yaw);
		}
		else
		{
			System.out.println("Vision Server - Camera Yaw WAY OFF");
		}
	}
	
	/**
	 * Finds the Latest Android Vision Update with Vision and Calls setBaselinePhoneOrientation
	 * Draws from the Whole List Automatically - Does Nothing if a proper visionupdate is not found
	 */
	public void setBaselinePhoneOrientation()
	{
		ArrayList<VisionUpdate> latestUpdates = getCurrentVisionUpdates();
		
		for(int i = 0; i < latestUpdates.size(); i++)
		{
			VisionUpdate update = latestUpdates.get(i);
			if(update != null && update.getCapturedAgoMs() < MathConstants.MAX_TIME_DIFF_MILLIS * 1.5)
			{
				double pitch = update.getPitch();
				double yaw = update.getYaw();
				if(!Double.isNaN(pitch) && !Double.isNaN(yaw))
				{
					setBaselinePhoneOrientation(pitch, yaw);
					return;
				}
			}
		}
		
		System.out.println("Vision Server - No Data Found!");
	}
	
	/**
	 * Returns the Simple Distance Measurement of the 'Best Guess' CameraTargetInfo
	 * @return the Simple Distance Measurement of the 'Best Guess' CameraTargetInfo
	 */
	public double getBestGuessDistance()
	{
		CameraTargetInfo info = getBestGuessTarget(VISION_CRITERION.FIELD);
		
		if(info != null)
		{
			double distance = info.getSimpleCameraDistance();
			System.out.println("Distance Found: " + distance);
			return (distance);
		}
		
		return Double.NaN;
	}
	
	/**
	 * Returns the Simple Angle Measurement of the 'Best Guess' CameraTargetInfo
	 * @return the Simple Angle Measurement of the 'Best Guess' CameraTargetInfo
	 */
	public double getBestGuessAngle()
	{
		CameraTargetInfo info = getBestGuessTarget(VISION_CRITERION.FIELD);
		
		if(info != null)
		{
			double angle = info.getSimpleCameraAngle();
			System.out.println("Angle Found: " + angle);
			return angle;
		}
		
		return Double.NaN;
	}
	
	/**
	 * Method Returns the 'Best Guess' Target From the List of Targets Given A Long Data Set
	 * @return the 'Best Guess' Target From the List of Targets Given A Long Data Set
	 */
	public ArrayList<CameraTargetInfo> getBestGuessTargets(VISION_CRITERION criterion)
	{
		switch(criterion)
		{
			case STRAFE:
				return getBestGuessTargetStrafe();
			case INTAKE:
				return getBestGuessTargetField();
			case FIELD:
			default:
				return getBestGuessTargetField();
		}
	}
	
	public CameraTargetInfo getBestGuessTarget(VISION_CRITERION criterion)
	{
		ArrayList<CameraTargetInfo> targets = getBestGuessTargets(criterion);
		if(targets != null && targets.size() > 0)
		{
			return targets.get(0);
		}
		return null;
	}
	
	/**
	 * Method Returns the 'Best Guess' Target From the List of Targets Given A Long Data Set
	 * @param minDistance - Minimum Distance of a Qualified Target
	 * @param maxDistance - Maximum Distance of a Qualified Target
	 * @param minAngle - Minimum Angle of a Qualified Target
	 * @param maxAngle - Maximum Angle of a Qualified Target
	 * @param topIndexesN - Number of Indexes (VisionUpdate Priority List [Down to Index 5]) to Descend 
	 * @return the 'Best Guess' Target From the List of Targets Given A Long Data Set
	 */
	public ArrayList<CameraTargetInfo> getBestGuessTargets(double minDistance, double maxDistance, double minAngle, double maxAngle, int topIndexesN)
	{
		ArrayList<VisionUpdate> updates = getCurrentVisionUpdates();
		ArrayList<CameraTargetInfo> listedTargetInfo = new ArrayList<>();
		double currentTimeMillis = getTimestamp();
		boolean reachedEndOfUsefulTargets = false;
		
		for(int i = 0; i < updates.size() && !reachedEndOfUsefulTargets; i++)
		{
			VisionUpdate update = updates.get(i);
			
			if(currentTimeMillis - update.getCapturedAtTimestamp() > MathConstants.MAX_TIME_DIFF_MILLIS)
			{
				//System.out.println("Captured " + update.getCapturedAtTimestamp());
				reachedEndOfUsefulTargets = true;
				continue;
			}
			else
			{
				List<CameraTargetInfo> infos = update.getTargets();
				
				if(infos == null || infos.size() <= 0)
					continue;
				
				//System.out.println("Target's List Size " + infos.size());
				
				for(int j = 0; j < topIndexesN && j < infos.size(); j++)
				{
					CameraTargetInfo info = infos.get(j);
					
					if(info == null)
					{
						continue;
					}
					
					double simpleCameraAngle = info.getSimpleCameraAngle();
					double simpleCameraDistance = info.getSimpleCameraDistance();
					
					if(Double.isNaN(simpleCameraAngle))
					{
						//System.out.println("Camera Angle NaN");
						continue;
					}
					else if(Double.isNaN(simpleCameraDistance))
					{
						//System.out.println("Camera Distance NaN");
						continue;
					}
					else if(simpleCameraAngle > minAngle && simpleCameraAngle < maxAngle)
					{
						continue;
					}
					else if(simpleCameraDistance > minDistance && simpleCameraDistance < maxDistance)
					{
						continue;
					}
					else
					{
						listedTargetInfo.add(info);
					}
				}
			}
		}
		
		if(!(listedTargetInfo.size() > 0))
		{
			System.out.println("No Targets Found!");
		}

		return listedTargetInfo;
	}
	
	/**
	 * Subset of the Vision Target Searching Algorithm for When Strafing
	 * Has Greater Location Thresholds and Lower Temporal Thresholds
	 */
	private ArrayList<CameraTargetInfo> getBestGuessTargetStrafe()
	{
		ArrayList<VisionUpdate> updates = getCurrentVisionUpdates();
		ArrayList<CameraTargetInfo> listedTargetInfo = new ArrayList<>();
		double currentTimeMillis = getTimestamp();
		boolean reachedEndOfUsefulTargets = false;
		
		for(int i = 0; i < updates.size() && !reachedEndOfUsefulTargets; i++)
		{
			VisionUpdate update = updates.get(i);
			
			if(currentTimeMillis - update.getCapturedAtTimestamp() > MathConstants.MAX_TIME_DIFF_MILLIS)
			{
				//System.out.println("Captured " + update.getCapturedAtTimestamp());
				reachedEndOfUsefulTargets = true;
				continue;
			}
			else
			{
				List<CameraTargetInfo> infos = update.getTargets();
				
				if(infos == null || infos.size() <= 0)
					continue;
				
				//System.out.println("Target's List Size " + infos.size());
				
				for(int j = 0; j < (infos.size() / 1.3) + 1 && j < infos.size(); j++)
				{
					CameraTargetInfo info = infos.get(j);
					
					if(info == null)
					{
						continue;
					}
					
					double simpleCameraAngle = info.getSimpleCameraAngle();
					double simpleCameraDistance = info.getSimpleCameraDistance();
					
					if(Double.isNaN(simpleCameraAngle))
					{
						//System.out.println("Camera Angle NaN");
						continue;
					}
					else if(Double.isNaN(simpleCameraDistance))
					{
						//System.out.println("Camera Distance NaN");
						continue;
					}
					
					double widthBasedCameraDistance = info.getWidthBoundedCameraDistance();
					double heightBasedCameraDistance = info.getHeightBoundedCameraDistance();
					
					if(Math.abs(widthBasedCameraDistance - simpleCameraDistance) > MathConstants.ACCEPTABLE_VISION_DISTANCE_ERROR)
					{
						System.out.println("Excluding Target at D:" + simpleCameraDistance + " Because of Width");
						continue;
					}
					else if(Math.abs(heightBasedCameraDistance - simpleCameraDistance) > MathConstants.ACCEPTABLE_VISION_DISTANCE_ERROR)
					{
						System.out.println("Excluding Target at D:" + simpleCameraDistance + " Because of Height");
						continue;
					}
					else
					{
						listedTargetInfo.add(info);
					}
				}
			}
		}
		
		if(!(listedTargetInfo.size() > 0))
		{
			System.out.println("No Targets Found!");
		}
		
		return listedTargetInfo;
	}
	
	private ArrayList<CameraTargetInfo> getBestGuessTargetField()
	{
		ArrayList<VisionUpdate> updates = getCurrentVisionUpdates();
		ArrayList<CameraTargetInfo> listedTargetInfo = new ArrayList<>();
		double currentTimeMillis = getTimestamp();
		boolean reachedEndOfUsefulTargets = false;
		
		//TODO: Get Rid of i < 1 - IT SHOULD NOT BE NECESSARY
		for(int i = 0; i < updates.size() && !reachedEndOfUsefulTargets && i < 1; i++)
		{
			VisionUpdate update = updates.get(i);
			
			if(currentTimeMillis - update.getCapturedAtTimestamp() > MathConstants.MAX_TIME_DIFF_MILLIS)
			{
				//System.out.println("Captured " + update.getCapturedAtTimestamp());
				reachedEndOfUsefulTargets = true;
				continue;
			}
			else
			{
				List<CameraTargetInfo> infos = update.getTargets();
				
				if(infos == null || infos.size() <= 0)
					continue;
				
				//System.out.println("Target's List Size " + infos.size());
				
				for(int j = 0; j < infos.size(); j++)
				{
					CameraTargetInfo info = infos.get(j);
					
					if(info == null)
					{
						continue;
					}
					else if(Double.isNaN(info.getSimpleCameraAngle()))
					{
						//System.out.println("Camera Angle NaN");
						continue;
					}
					else if(Double.isNaN(info.getSimpleCameraDistance()))
					{
						//System.out.println("Camera Distance NaN");
						continue;
					}
					else if(info.getSimpleCameraDistance() > (12 * 25))
					{
						//System.out.println("Camera Distance To Big");
						continue;
					}
					else
					{
						listedTargetInfo.add(info);
					}
				}
			}
		}
		
		if(!(listedTargetInfo.size() > 0))
		{
			//System.out.println(String.format("Best Target - D: %f A: %f", listedTargetInfo.get(0).getSimpleCameraDistance(), listedTargetInfo.get(0).getSimpleCameraAngle()));
			System.out.println("No Targets Found!");
		}
		
		return listedTargetInfo;
	}
	
	/**
	 * Returns Current Connection Status
	 * @return isConnected - Current Connection Status
	 */
	public boolean isConnected()
	{
		return isConnected;
	}
	
    /**
     * Gets a Timestamp from Either Java or the roboRIO FPGA
     * @return Timestamp as a double
     */
    private double getTimestamp() 
    {
    	if (shouldUseJavaTime)
        	return System.currentTimeMillis();
        else
        	return Timer.getFPGATimestamp() * 1000;
    }
}
