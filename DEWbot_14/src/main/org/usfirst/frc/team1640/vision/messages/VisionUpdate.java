package main.org.usfirst.frc.team1640.vision.messages;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * VisionUpdate contains the various attributes outputted by the vision system, 
 * namely a list of targets and the timestamp at which it was captured
 * Derived from Team 254 2017 Code 
 */
public class VisionUpdate 
{
	//String Constants for the JSON References
	private static final String JSON_CAPTURED_AGO_MS = "capturedAgoMs";
	private static final String JSON_PITCH = "pitch";
	private static final String JSON_YAW = "yaw";
	private static final String JSON_TARGETS = "targets";
	private static final String JSON_Y = "y";
	private static final String JSON_Z = "z";
	private static final String JSON_W = "w";
	private static final String JSON_H = "h";
	private static final String JSON_GX = "gX";
	private static final String JSON_GY = "gY";
	private static final String JSON_GZ = "gZ";
	
	
	//A JSON Parser for the VisionUpdates
	private static JSONParser parser = new JSONParser();
	
	//Vision Update State Variables
    protected boolean valid = false;
    protected long capturedAgoMs;
    protected List<CameraTargetInfo> targets;
    protected double capturedAtTimestamp = 0;
    protected double cameraPitch;
    protected double cameraYaw;
    protected double cameraGravityX;
    protected double cameraGravityY;
    protected double cameraGravityZ;
    
    public VisionUpdate()
    {
    	
    }
    
    public VisionUpdate(List<CameraTargetInfo> targets, double capturedAtTimestamp)
    {
    	this.targets = targets;
    	this.capturedAtTimestamp = capturedAtTimestamp;
    	this.valid = true;
    	this.cameraGravityX = Double.NaN;
    	this.cameraGravityY = Double.NaN;
    	this.cameraGravityZ = Double.NaN;
    	this.cameraPitch = Double.NaN;
    	this.cameraYaw = Double.NaN;
    }
    
    /**
     * Returns the double value from the Optional Object or the Default Value
     * @param n - Object with Double
     * @param defaultValue - The Value to Return if n==null
     * @return The Optional Double Value
     */
    private static double getOptDouble(Object n, double defaultValue) 
    {
        if (n == null) 
        {
            return defaultValue;
        }
        return (double) n;
    }
    
    /**
     * Returns the long value from the Optional Object or the Default Value
     * @param n - Object with Long
     * @param defaultValue - The Value to Return if n==null
     * @return The Optional Long Value
     */
    private static long getOptLong(Object n, long defaultValue) 
    {
        if (n == null) 
        {
            return defaultValue;
        }
        return (long) n;
    }

    /**
     * Returns the Object-Wrapped Double from a JSONObject at a certain Key
     * @param j - The JSONObject
     * @param key - The String Key to get a Value
     * @return - An 'Optional' (Object-Wrapped) Double from JSON
     * @throws ClassCastException
     */
    private static Optional<Double> parseDouble(JSONObject j, String key) throws ClassCastException 
    {
        Object d = j.get(key);
        if (d == null) 
        {
            return Optional.empty();
        } 
        else
        {
            return Optional.of((double) d);
        }
    }
    
    /**
     * Returns the Object-Wrapped Integer from a JSONObject at a certain Key
     * @param j - The JSONObject
     * @param key - The String Key to get a Value
     * @return - An 'Optional' (Object-Wrapped) Integer from JSON
     * @throws ClassCastException
     */
    public static Optional<Long> parseLong(JSONObject j, String key) throws ClassCastException 
    {
        Object d = j.get(key);
        if (d == null) 
        {
            return Optional.empty();
        } 
        else 
        {
            return Optional.of((long) d);
        }
    }

    /**
     * Generates a VisionUpdate object given a JSON blob and a timestamp.
     * @param Capture
     *            timestamp
     * @param JSON
     *            blob with update string, example: { "capturedAgoMs" : 100, "targets": [{"y": 5.4, "z": 5.5}] }
     * @return VisionUpdate object
     */
    public static VisionUpdate generateFromJsonString(double current_time, String updateString) 
    {
        VisionUpdate update = new VisionUpdate();
        try 
        {
            JSONObject j = (JSONObject) parser.parse(updateString);
            long capturedAgoMs = getOptLong(j.get(JSON_CAPTURED_AGO_MS), 0);
            if (capturedAgoMs == 0) 
            {
                update.valid = false;
                return update;
            }
            update.capturedAgoMs = capturedAgoMs;
            update.capturedAtTimestamp = current_time - capturedAgoMs / 1000.0;
			update.cameraPitch = getOptDouble(j.get(JSON_PITCH), Double.NaN);
			update.cameraYaw = getOptDouble(j.get(JSON_YAW), Double.NaN);
            update.cameraGravityX = getOptDouble(j.get(JSON_GX), Double.NaN);
            update.cameraGravityY = getOptDouble(j.get(JSON_GY), Double.NaN);
            update.cameraGravityZ = getOptDouble(j.get(JSON_GZ), Double.NaN);
            JSONArray targets = (JSONArray) j.get(JSON_TARGETS);
            
            ArrayList<CameraTargetInfo> targetInfos = new ArrayList<>(targets.size());
            for (Object targetObj : targets) 
            {
                JSONObject target = (JSONObject) targetObj;
                Optional<Double> y = parseDouble(target, JSON_Y);
                Optional<Double> z = parseDouble(target, JSON_Z);
                Optional<Long> width = parseLong(target, JSON_W);
                Optional<Long> height = parseLong(target, JSON_H);
                if (!(y.isPresent() && z.isPresent() && width.isPresent() && height.isPresent())) 
                {
                    update.valid = false;
                    return update;
                }
                targetInfos.add(new CameraTargetInfo(y.get(), z.get(), (int) (long) width.get(), (int) (long) height.get()));
            }
            update.targets = targetInfos;
            update.valid = true;
        } 
        catch (ParseException e) 
        {
            System.err.println("Parse error: " + e);
            System.err.println(updateString);
        } 
        catch (ClassCastException e) 
        {
            System.err.println("Data type error: " + e);
            System.err.println(updateString);
        }
        return update;
    }

    /**
     * Returns the List of Targets Contained in the VisionUpdate
     * @return The List of Targets Contained in the VisionUpdate
     */
    public List<CameraTargetInfo> getTargets() 
    {
        return targets;
    }

    /**
     * If the the VisionUpdate was properly formatted and contained all of the components
     * @return value - Was VisionUpdate was properly formatted and contains all of the components
     */
    public boolean isValid() 
    {
        return valid;
    }

    /**
     * Gets the Time Since the Picture Was Captured on the Android
     * @return - The Time Since the Picture Was Captured on the Android
     */
    public long getCapturedAgoMs() 
    {
        return capturedAgoMs;
    }

    /**
     * Gets the Absolute Time that the Picture from the Android was Taken 'At'
     * @return - The Absolute Time that the Picture from the Android was Taken 'At'
     */
    public double getCapturedAtTimestamp() 
    {
        return capturedAtTimestamp;
    }
    
    /**
     * Gets the Last Android Pitch Update
     * @return The Last Android Pitch Update
     */
    public double getPitch()
    {
    	return cameraPitch;
    }
    
    /**
     * Gets the Last Android Yaw Update
     * @return The Last Android Yaw Update
     */
    public double getYaw()
    {
    	return cameraYaw;
    }
    
    public double[] getGravity()
    {
    	if(Double.isNaN(cameraGravityX) || Double.isNaN(cameraGravityY) || Double.isNaN(cameraGravityZ))
    	{
    		return null;
    	}
    	double[] gravity = {cameraGravityX, cameraGravityY, cameraGravityZ};
    	return gravity;
    }
}
