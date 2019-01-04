package main.org.usfirst.frc.team1640.robot.auton.commands.vision;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilInches;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;
import main.org.usfirst.frc.team1640.vision.VisionServer;
import main.org.usfirst.frc.team1640.vision.VisionServer.VISION_CRITERION;
import main.org.usfirst.frc.team1640.vision.messages.CameraTargetInfo;

public class DriveUntilInchesVision extends DriveUntilInches {

	protected VisionServer visionServer;
	protected IGyro gyro;
	protected ElapsedTimer timer;
	protected double updatesPerSecond;
	protected boolean hasSetup;
	protected boolean closeEnough;
	
	public DriveUntilInchesVision(VisionServer visionServer, LinearStrategy linearStrat, IDriveTrain driveTrain, IGyro gyro, double drive) {
		super(driveTrain, linearStrat, 0, 0, drive);
		
		this.visionServer = visionServer;
		this.gyro = gyro;
		updatesPerSecond = 0;
		
		timer = null;
		hasSetup = false;
		closeEnough = false;
	}
	
	public DriveUntilInchesVision(VisionServer visionServer, double updatesPerSecond, LinearStrategy linearStrat, IDriveTrain driveTrain, IGyro gyro, double drive) {
		super(driveTrain, linearStrat, 0, 0, drive);
		
		this.visionServer = visionServer;
		this.gyro = gyro;
		this.updatesPerSecond = updatesPerSecond;
		
		timer = new ElapsedTimer();
		hasSetup = false;
		closeEnough = false;
	}
	
	@Override
	public void execute() {
		if(hasSetup && !closeEnough && updatesPerSecond > 0 && timer.getElapsedSeconds() > (1 / updatesPerSecond)) {
			hasSetup = false;
		}
		
		if(!hasSetup && !closeEnough) {
			CameraTargetInfo target = visionServer.getBestGuessTarget(VISION_CRITERION.FIELD);
			if(target == null) {
				return;
			}
			else {
				this.distanceInInches = target.getSimpleCameraDistance();
				this.angle = gyro.getYaw() - target.getSimpleCameraAngle();
				
				if(distanceInInches <= CameraTargetInfo.calculateClosestDetectableDistance()) {
					closeEnough = true;
				}
				
				System.out.println("Using D: " + distanceInInches + " A: " + angle);
				hasSetup = true;
			
				if(updatesPerSecond > 0) {
					timer.start();
				}
			}
		}
		
		super.execute();
	}
	
	@Override
	public void reset() {
		super.reset();
		hasSetup = false;
		closeEnough = false;
	}
}
