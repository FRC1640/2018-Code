package main.org.usfirst.frc.team1640.robot.auton.commands.vision;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilInches;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.vision.VisionServer;

public class StrafeUntilAlignedVision extends DriveUntilInches{

	protected VisionServer visionServer;
	protected IGyro gyro;
	protected boolean hasSetup;
	protected boolean shouldRecheck;
	
	public StrafeUntilAlignedVision(VisionServer visionServer, LinearStrategy linearStrat, IDriveTrain driveTrain, IGyro gyro, double drive) {
		super(driveTrain, linearStrat, 0, 0, drive);
		this.visionServer = visionServer;
		this.gyro = gyro;
		hasSetup = false;
	}
	
	@Override
	public void execute() {
		if(!hasSetup) {
			double distanceHypotenuseToCube = visionServer.getBestGuessDistance();
			double angleHypotenuseToCube = visionServer.getBestGuessAngle();
			
			double distanceStrafeToCube = Math.sin(Math.toRadians(angleHypotenuseToCube)) * distanceHypotenuseToCube;
			double angleStrafeToCube = gyro.getYaw() - (90 * Math.signum(distanceStrafeToCube));
			
			this.distanceInInches = Math.abs(distanceStrafeToCube);
			this.angle = angleStrafeToCube;
			System.out.println("From D: " + distanceHypotenuseToCube + " A: " + angleHypotenuseToCube);
			System.out.println("Using D: " + distanceInInches + " A: " + angle);
			hasSetup = true;
		}
		
		super.execute();
	}
	
	@Override
	public void reset() {
		super.reset();
		hasSetup = false;
	}
}
