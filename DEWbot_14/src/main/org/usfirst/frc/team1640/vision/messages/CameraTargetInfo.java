package main.org.usfirst.frc.team1640.vision.messages;

import org.json.simple.JSONObject;

import main.org.usfirst.frc.team1640.constants.vision.MathConstants;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

/**
 * Class That Represents 2D Image Positions (In a homogeneous vector form) - Derived from Team 254 2017 Code
 */
public class CameraTargetInfo
{
	//Quick Static Sin and Cos Variables for Vision Math
	public static double cameraYawSin = Math.sin(Math.toRadians(MathConstants.DEFAULT_ANDROID_YAW));
	public static double cameraYawCos = Math.cos(Math.toRadians(MathConstants.DEFAULT_ANDROID_YAW));
	public static double cameraPitchSin = Math.sin(Math.toRadians(MathConstants.DEFAULT_ANDROID_PITCH));
	public static double cameraPitchCos = Math.cos(Math.toRadians(MathConstants.DEFAULT_ANDROID_PITCH));
	
	public static void setCameraYaw(double cameraYaw)
	{
		if(Double.isNaN(cameraYaw))
			return;
		cameraYawSin = Math.sin(Math.toRadians(cameraYaw));
		cameraYawCos = Math.cos(Math.toRadians(cameraYaw));
	}
	
	public static void setCameraPitch(double cameraPitch)
	{
		if(Double.isNaN(cameraPitch))
			return;
		cameraPitchSin = Math.sin(Math.toRadians(cameraPitch));
		cameraPitchCos = Math.cos(Math.toRadians(cameraPitch));
	}
	
	public static double calculateClosestDetectableDistance()
	{
		double y = 0;
		double z = (0 - (MathConstants.DEFAULT_ANDROID_IMAGE_HEIGHT / 2 - 0.5)) / MathConstants.DEFAULT_ANDROID_FOCAL_LENGTH;
		
		CameraTargetInfo info = new CameraTargetInfo(y, z, 0, 0);
		return info.getSimpleCameraDistance();
	}
	
	/**
	 * This Method Can Be Used For Blob Edge Based Turning, Given:
	 * @param distanceInInches - Target's Distance Away
	 * @param offsetAngleOfCubeDegrees - (-90, 90) of the Cube's Flat Face Angle From Robot Direction
	 * @param focalLength - Camera's Current Focal Length In Pixels
	 * @return The Number of Pixels From the Edge You Can Find the Middle At
	 */
	public static double pixelsOffsetFromBlobEdge(double distanceInInches, double offsetAngleOfCubeDegrees, double focalLength)
	{
		double apparentWidthFromAngle = MathConstants.CUBE_WIDTH * (1 + Math.abs(Math.sqrt(2) * Math.sin(2 * Math.toRadians(offsetAngleOfCubeDegrees))));
		double cubeWidthInPixels = focalLength * apparentWidthFromAngle / distanceInInches;
		return cubeWidthInPixels / 2;
	}
	
    protected double m_y;
    protected double m_z;
    protected int m_w;
    protected int m_h;

    /**
     * Class Coordinate Frame:
     * +x is out the camera's optical axis
     * +y is to the left of the image
     * +z is to the top of the image
     * We assume the x component of all targets is +1.0 (since this is homogeneous)
     * @param y - 'Y' of the Vector
     * @param z - 'Z' of the Vector
     */
    public CameraTargetInfo(double y, double z, int width, int height)
    {
        m_y = y;
        m_z = z;
        m_w = width;
        m_h = height;
    }
    
    /**
     * Reformats Double To Minimize Loss
     * @param value - Double to Format
     * @return Adjusted Double
     */
    private double doubleize(double value)
    {
        double leftover = value % 1;
        if (leftover < 1e-7)
        {
            value += 1e-7;
        }
        return value;
    }

    /**
     * Sets the Y of the Homogeneous Vector
     * @param newY - The New Y of the Vector
     */
    public void setY(double newY)
    {
    	m_y = newY;
    }
    
    /**
     * Sets the Z of the Homogeneous Vector
     * @param newZ - The New Z of the Vector
     */
    public void setZ(double newZ)
    {
    	m_z = newZ;
    }
    
    /**
     * Returns the X of the Homogeneous Vector
     * It is 1.0 because of the nature of this unit vector
     * @return The X of the Homogeneous Vector
     */
    public double getX()
    {
    	return 1.0;
    }
    
    /**
     * Returns the Y of the Homogeneous Vector
     * @return The Y of the Homogeneous Vector
     */
    public double getY()
    {
        return m_y;
    }

    /**
     * Returns the Z of the Homogeneous Vector
     * @return The Z of the Homogeneous Vector
     */
    public double getZ()
    {
        return m_z;
    }
    
    /**
     * Returns the Pixel Width of the Object
     * @return The Pixel Width of the Object
     */
    public int getWidth()
    {
    	return m_w;
    }
    
    /**
     * Returns the Pixel Height of the Object
     * @return The Pixel Height of the Object
     */
    public int getHeight()
    {
    	return m_h;
    }
    
    /**
     * Returns the Camera's Distance Measurement (Raw, Unadjusted)
     * @return The Camera's Distance Measurement (Raw, Unadjusted)
     */
    public double getSimpleCameraDistance()
    {
    	//When Yaw is 0, The Terms * cameraYawSin Reduce to 0
    	double xYaw = getX() * cameraYawCos + getY() * cameraYawSin;
    	double yYaw = getY() * cameraYawCos - getX() * cameraYawSin;
    	double zYaw = getZ();
    	
    	//When Pitch is 0, The Terms * cameraPitchSin Reduce to 0
    	double xR = zYaw * cameraPitchSin + xYaw * cameraPitchCos;
    	double yR = yYaw;
    	double zR = zYaw * cameraPitchCos - xYaw * cameraPitchSin;
    	
    	double scaling = MathConstants.DEFAULT_HEIGHT_DIFFERENCE / zR;
    	double distance = Math.hypot(xR, yR) * -scaling;
    	
    	//System.out.println("Distance " + distance + " X " + xR + " Y " + yR + " Z " + zR);
    	
    	if(distance >= 0)
    	{
    		return distance;
    	}
    	
    	return Double.NaN;
    }
    
    public double[] getWidthCameraDistance()
    {
		double distanceOptimal = MathConstants.CUBE_WIDTH * MathConstants.DEFAULT_ANDROID_FOCAL_LENGTH / getWidth();
		double distanceNonOptimal = distanceOptimal * Math.sqrt(2);
		
		double[] values = {distanceOptimal, distanceNonOptimal};
		return values;
    }
    
    public double[] getHeightCameraDistance()
    {
		double distanceOptimal = MathConstants.CUBE_HEIGHT * MathConstants.DEFAULT_ANDROID_FOCAL_LENGTH / getHeight();
		double distanceNonOptimal = distanceOptimal * Math.sqrt(2);
		
		double[] values = {distanceOptimal, distanceNonOptimal};
		return values;
    }
    
    public double getWidthBoundedCameraDistance()
    {
    	double simpleDistance = getSimpleCameraDistance();
    	
    	//TODO: Update Math with Stronger, Pitch Compensation Math
    	if(Double.isNaN(simpleDistance) && cameraPitchCos < 0.1)
    	{
    		double distanceOptimal = MathConstants.CUBE_WIDTH * MathConstants.DEFAULT_ANDROID_FOCAL_LENGTH / getWidth();
    		double distanceNonOptimal = distanceOptimal * Math.sqrt(2);
    		
    		if(!(distanceOptimal < simpleDistance * 1.5))
    		{
    			return distanceOptimal;
    		}
    		else if(!(simpleDistance * 1.5 > distanceNonOptimal))
    		{
    			return distanceNonOptimal;
    		}
    		else
    		{
    			return simpleDistance;
    		}
    	}
    	
    	return Double.NaN;
    }
    
    public double getHeightBoundedCameraDistance()
    {
    	double simpleDistance = getSimpleCameraDistance();
    	
    	//TODO: Update Math with Stronger, Pitch Compensation Math
    	if(Double.isNaN(simpleDistance) && cameraPitchCos < 0.1)
    	{
    		double distanceOptimal = MathConstants.CUBE_HEIGHT * MathConstants.DEFAULT_ANDROID_FOCAL_LENGTH / getHeight();
    		double distanceNonOptimal = distanceOptimal * Math.sqrt(2);
    		
    		if(!(distanceOptimal < simpleDistance * 1.5))
    		{
    			return distanceOptimal;
    		}
    		else if(!(simpleDistance * 1.5 > distanceNonOptimal))
    		{
    			return distanceNonOptimal;
    		}
    		else
    		{
    			return simpleDistance;
    		}
    	}
    	
    	return Double.NaN;
    }
    
    /**
     * Returns the Camera's Angle Measurement (Raw, Unadjusted)
     * @return The Camera's Angle Measurement (Raw, Unadjusted)
     */
    public double getSimpleCameraAngle()
    {
    	double xYaw = getX() * cameraYawCos + getY() * cameraYawSin;
    	double yYaw = getY() * cameraYawCos - getX() * cameraYawSin;
    	double zYaw = getZ();
    	
    	double xR = zYaw * cameraPitchSin + xYaw * cameraPitchCos;
    	double yR = yYaw;
    	//double zR = zYaw * cameraPitchCos - xYaw * cameraPitchSin;
    	
    	double angle = Math.toDegrees(Math.atan2(yR, xR));
    	if(angle < 60 && angle > -60)
    	{
    		return -angle;
    	}
    	
    	return Double.NaN;
    }

    /**
     * Creates JSON Object of the Camera Target Information Vector
     * @return The JSON Object of the Camera Target Information Vector
     */
    public JSONObject toJson()
    {
        JSONObject j = new JSONObject();
        j.put("y", doubleize(getY()));
        j.put("z", doubleize(getZ()));
        return j;
    }
}