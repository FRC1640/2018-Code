package main.org.usfirst.frc.team1640.constants.vision;

public final class MathConstants {
	public static final double DEFAULT_ANDROID_FOCAL_LENGTH = 476.19050071198666;
	public static final double DEFAULT_ANDROID_IMAGE_WIDTH = 640;
	public static final double DEFAULT_ANDROID_IMAGE_HEIGHT = 480;
	public static final double DEFAULT_ANDROID_YAW = 0;
	public static final double DEFAULT_ANDROID_PITCH = 19; //New Camera Tilt Angle From 6
	public static final double DEFAULT_CAMERA_HEIGHT = 22;
	public static final double DEFAULT_USB_FOV_D = 60;
	public static final double DEFAULT_USB_FOCAL_LENGTH_M = 4.0e-3;
	public static final double CAMERA_DISTANCE_FROM_FRONT = 8;
	public static final double CUBE_WIDTH = 13;
	public static final double CUBE_HEIGHT = 11;
	public static final double CUBE_CIRCULAR_RADIUS = (CUBE_WIDTH + CUBE_HEIGHT) / 4;
	public static final double DEFAULT_HEIGHT_DIFFERENCE = DEFAULT_CAMERA_HEIGHT;// - CUBE_CIRCULAR_RADIUS;
	public static final double CAMERA_Y_DEADBAND = 0.001;
	public static final double ACCEPTABLE_VISION_DISTANCE_ERROR = 10;
	public static final double ACCEPTABLE_VISION_ANGLE_ERROR = 7;
	public static final int MAX_TIME_DIFF_MILLIS = 500;
}
