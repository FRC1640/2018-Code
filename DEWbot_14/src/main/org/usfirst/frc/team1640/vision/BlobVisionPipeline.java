package main.org.usfirst.frc.team1640.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import main.org.usfirst.frc.team1640.constants.vision.MathConstants;
import main.org.usfirst.frc.team1640.vision.messages.CameraTargetInfo;

public class BlobVisionPipeline implements VisionPipeline
{
	//Example HSV Parameters - DO NOT USE
	private static final int hMin = 20;
	private static final int hMax = 71;
	private static final int sMin = 215;
	private static final int sMax = 255;
	private static final int vMin = 120;
	private static final int vMax = 165;
	
	private static final double minTargetWidth = 0.0625;
	private static final double maxTargetWidth = 0.9;
	private static final double minTargetHeight = 0.0625;
	private static final double maxTargetHeight = 0.8333333;
	
	private static final double minWideness = 3.0;
	private static final double maxWideness = 0.25;
	
	private static final double minFullness = 0.45;
	private static final double maxFullness = 0.95;
	
	private static final int maxTargetsPerUpdate = 6;
	private static final boolean shouldLog = false;
	
	public volatile ArrayList<CameraTargetInfo> targets;
	public volatile long imageTimeTargets;
	
	private UsbCamera camera;
	private CvSource processedFrame;
	
	public BlobVisionPipeline(UsbCamera camera)
	{
		this.camera = camera;
	}
	
	public BlobVisionPipeline(UsbCamera camera, CvSource processedFrame)
	{
		this(camera);
		this.processedFrame = processedFrame;
	}
	
	@Override
	public void process(Mat image) 
	{
		long timestamp = camera.getLastFrameTime();
		
		Mat hsvCopy = new Mat(image.height(), image.width(), image.channels());
		Imgproc.cvtColor(image, hsvCopy, Imgproc.COLOR_BGR2HSV);
		
		//Mat threshCopy = new Mat(image.height(), image.width(), image.channels());
		Mat threshCopy = new Mat();
		Core.inRange(hsvCopy, new Scalar(hMin, sMin, vMin), new Scalar(hMax, sMax, vMax), threshCopy);
		hsvCopy.release();
		
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(threshCopy, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_KCOS);
		
		ArrayList<CameraTargetInfo> tempFoundTargets = new ArrayList<>();
		
		if(shouldLog)
			System.out.println("Number of Raw Contours: " + contours.size());
		
		for(MatOfPoint contour : contours)
		{
			if(Imgproc.isContourConvex(contour))
			{
				if(shouldLog)
					System.out.println("Convex Contour Found");
				
				Rect boundingRect = Imgproc.boundingRect(contour);
				
				int centroidX = boundingRect.x + (boundingRect.width / 2);
				int centroidY = boundingRect.y;
				int width = boundingRect.width;
				int height = boundingRect.height;
				
				if(width < minTargetWidth * image.width() || width > maxTargetWidth * image.width() ||
		                height < minTargetHeight * image.height() || height > maxTargetHeight * image.height())
				{
					if(shouldLog)
						System.out.println("Eliminated Based on Size");
					continue;
				}
				
				double wideness = width * 1.0 / height;
				
				if(wideness < minWideness || wideness > maxWideness)
				{
					if(shouldLog)
						System.out.println("Eliminated Based on Width/Height");
					continue;
				}
				
				double contourArea = Imgproc.contourArea(contour);
				double rectangleArea = width * height;
				double fullness = contourArea / rectangleArea;
				
				if(fullness < minFullness || fullness > maxFullness)
				{
					if(shouldLog)
						System.out.println("Eliminated Based on Fullness");
					continue;
				}
				
				double diagonalFOVRad = Math.toRadians(MathConstants.DEFAULT_USB_FOV_D);
				double horizontalFOV = 2 * Math.atan(Math.tan(diagonalFOVRad) * Math.cos(Math.atan2(height, width)));
				double focalLength = (width * 0.5) / Math.tan(horizontalFOV * 0.5);
				
				double centerCol = ((double) image.width()) / 2.0 - 0.5;
				double centerRow = ((double) image.height()) / 2.0 - 0.5;
				double y = -(centroidX - centerCol) / focalLength;
				double z = (centroidY - centerRow) / focalLength;
				
				tempFoundTargets.add(new CameraTargetInfo(y, z, width, height));
			}
			//convexContours = new ArrayList<>();
			//Imgproc.convexHull(contour, hull, false);
		}
		
		threshCopy.release();
		
		for(int i = 0; i < tempFoundTargets.size(); i++)
		{
			for(int j = tempFoundTargets.size() - 1; j > i; j--)
			{
				CameraTargetInfo targetA = tempFoundTargets.get(i);
				CameraTargetInfo targetB = tempFoundTargets.get(j);
				
				if(targetB.getWidth() * targetB.getHeight() > targetA.getWidth() * targetA.getHeight())
				{
					tempFoundTargets.set(i, targetB);
					tempFoundTargets.set(j, targetA);
				}
			}
		}
		
		ArrayList<CameraTargetInfo> finalTargets = new ArrayList<>();
		
		for(int i = 0; i < maxTargetsPerUpdate && i < tempFoundTargets.size(); i++)
		{
			finalTargets.add(tempFoundTargets.get(i));
		}
		
		if(processedFrame != null)
		{
			Imgproc.polylines(image, contours, true, new Scalar(0, 112, 255), 3);
			Mat bgrCopy = new Mat(image.height(), image.width(), image.channels());
			Imgproc.cvtColor(image, bgrCopy, Imgproc.COLOR_RGB2BGR);
			processedFrame.putFrame(bgrCopy);
		}
		
		targets = finalTargets;
		imageTimeTargets = timestamp;
	}
}
