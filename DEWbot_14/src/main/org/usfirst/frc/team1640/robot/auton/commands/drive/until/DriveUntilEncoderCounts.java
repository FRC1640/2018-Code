package main.org.usfirst.frc.team1640.robot.auton.commands.drive.until;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;

public class DriveUntilEncoderCounts extends DriveUntilCommand {
	private IDriveTrain driveTrain;
	private int distanceInEncoderCounts;
	
	public DriveUntilEncoderCounts(IDriveTrain driveTrain, LinearStrategy linearStrat, int distanceInEncoderCounts, double angleInDegrees, double drive) {
		super(linearStrat, angleInDegrees, drive);
		
		this.driveTrain = driveTrain;
		
		this.distanceInEncoderCounts = distanceInEncoderCounts;
	}

	@Override
	public void initEndCondition() {
		driveTrain.resetPositions();
	}

	@Override
	public boolean isEndConditionMet() {
		System.out.println("Position: " + driveTrain.getPositionEncoderCounts());
		return (Math.abs(driveTrain.getPositionEncoderCounts()) > distanceInEncoderCounts);
	}

}
