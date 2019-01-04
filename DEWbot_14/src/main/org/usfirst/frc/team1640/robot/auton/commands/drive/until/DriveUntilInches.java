package main.org.usfirst.frc.team1640.robot.auton.commands.drive.until;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;

public class DriveUntilInches extends DriveUntilCommand {
	private IDriveTrain driveTrain;
	protected double distanceInInches;

	public DriveUntilInches(IDriveTrain driveTrain, LinearStrategy linearStrat, double distanceInInches, double angleInDegrees, double drive) {
		super(linearStrat, angleInDegrees, drive);
		
		this.driveTrain = driveTrain;
		
		this.distanceInInches = distanceInInches;
	}

	@Override
	public void initEndCondition() {
		driveTrain.resetPositions();
	}

	@Override
	public boolean isEndConditionMet() {
		System.out.println("drive until inches isEndConditionMet: " + Math.abs(driveTrain.getPositionInches()));
		return (Math.abs(driveTrain.getPositionInches()) > distanceInInches);
	}

}
