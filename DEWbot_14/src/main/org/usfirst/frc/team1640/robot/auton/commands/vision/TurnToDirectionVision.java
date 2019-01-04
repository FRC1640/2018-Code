package main.org.usfirst.frc.team1640.robot.auton.commands.vision;

import java.util.ArrayList;

import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.directional.DirectionalStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.vision.VisionServer;
import main.org.usfirst.frc.team1640.vision.VisionServer.VISION_CRITERION;
//import main.org.usfirst.frc.team1640.vision.VisionServer;
import main.org.usfirst.frc.team1640.vision.messages.CameraTargetInfo;

public class TurnToDirectionVision extends TurnToDirection {
	
	protected VisionServer visionServer;
	protected double visionAngleBuffer;
	protected boolean isLeft;
	
	public TurnToDirectionVision(VisionServer visionServer, DirectionalStrategy directionalStrat, IGyro gyro, double angleBuffer, double secondsInBuffer, 
			double timeoutInSeconds) {
		super(directionalStrat, gyro, 0, angleBuffer, secondsInBuffer, timeoutInSeconds);
		this.visionServer = visionServer;
		this.visionAngleBuffer = Double.NaN;
		this.isLeft = true;
	}
	
	public TurnToDirectionVision(VisionServer visionServer, DirectionalStrategy directionalStrat, IGyro gyro, double angleBuffer, double secondsInBuffer, 
			double timeoutInSeconds, boolean isLeft) {
		super(directionalStrat, gyro, 0, angleBuffer, secondsInBuffer, timeoutInSeconds);
		this.visionServer = visionServer;
		this.visionAngleBuffer = Double.NaN;
		this.isLeft = isLeft;
	}
	
	public TurnToDirectionVision(VisionServer visionServer, DirectionalStrategy directionalStrat, IGyro gyro, 
			double defaultAngle, double angleBuffer, double secondsInBuffer, double timeoutInSeconds) {
		super(directionalStrat, gyro, defaultAngle, angleBuffer, secondsInBuffer, timeoutInSeconds);
		this.visionServer = visionServer;
		this.visionAngleBuffer = Double.NaN;
		this.isLeft = true;
	}
	
	/*
	public TurnToDirectionVision(VisionServer visionServer, DirectionalStrategy directionalStrat, IGyro gyro, 
			double defaultAngle, double visionAngleBuffer, double angleBuffer, double secondsInBuffer, double timeoutInSeconds) {
		super(directionalStrat, gyro, defaultAngle, angleBuffer, secondsInBuffer, timeoutInSeconds);
		this.visionServer = visionServer;
		this.visionAngleBuffer = visionAngleBuffer;
	}
	*/
	
	
	@Override
	public void execute() {
		
		if(isRunning() && !isInit) {
			//double guessAngle = visionServer.getBestGuessAngle();
			ArrayList<CameraTargetInfo> targets = visionServer.getBestGuessTargets(VISION_CRITERION.FIELD);
			
			if(targets == null || targets.size() <= 0) {
				System.out.println("Bad Vision Target");
				isDone = true;
				return;
			}
			
			int index = 0;
			double mostValue = targets.get(0).getSimpleCameraAngle();
			for(int i = 0; i < targets.size() && i < 3; i++) {
				if(targets.get(i).getSimpleCameraAngle() < mostValue && isLeft) {
					index = i;
					mostValue = targets.get(i).getSimpleCameraAngle();
				}
				if(targets.get(i).getSimpleCameraAngle() > mostValue && !isLeft) {
					index = i;
					mostValue = targets.get(i).getSimpleCameraAngle();
				}
				System.out.println("Current Value" + targets.get(i).getSimpleCameraAngle());
				System.out.println("Most Value " + mostValue + " @ " + index);
			}
			
			System.out.println("Targets Length" + targets.size());
			double guessAngle = targets.get(index).getSimpleCameraAngle();
			
			if(Double.isNaN(guessAngle)) {
				isDone = true;
				System.out.println("Bad Vision Angle");
				return;
			}
			
			this.direction = gyro.getYaw() - guessAngle;
			System.out.println("Getting Angle to Turn " + guessAngle);
		}
		
		/*
		if(!Double.isNaN(visionAngleBuffer))
		{
			boolean isInBuffer = Math.abs(MathUtilities.shortestAngleBetween(gyro.getYaw(), direction))
					< visionAngleBuffer;
			if(isInBuffer)
			{
				
			}
		}
		*/

		super.execute();
	}
}
