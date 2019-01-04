package main.org.usfirst.frc.team1640.robot.auton.commands.drive.until;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class DriveUntilInchesAndTimeout extends DriveUntilCommand {
	private IDriveTrain driveTrain;
	protected double distanceInInches;
	private double time;
	private ElapsedTimer timer;

	public DriveUntilInchesAndTimeout(IDriveTrain driveTrain, LinearStrategy linearStrat, double distanceInInches, double angleInDegrees, double drive, double timeInSeconds) {
		super(linearStrat, angleInDegrees, drive);
		
		this.driveTrain = driveTrain;
		
		this.distanceInInches = distanceInInches;
		this.time = timeInSeconds;
		
		timer = new ElapsedTimer(); 
	}

	@Override
	public void initEndCondition() {
		driveTrain.resetPositions();
		timer.restart();
	}

	@Override
	public boolean isEndConditionMet() {
		System.out.println("drive until inches isEndConditionMet: " + Math.abs(driveTrain.getPositionInches()));
		return (Math.abs(driveTrain.getPositionInches()) > distanceInInches || timer.getElapsedSeconds() > time);
	}

}
