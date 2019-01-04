package main.org.usfirst.frc.team1640.robot.auton.commands.drive.until;

import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class DriveUntilSeconds extends DriveUntilCommand {
	private double timeInSeconds;
	
	private ElapsedTimer timer;
	
	public DriveUntilSeconds(LinearStrategy linearStrat, double timeInSeconds, double angleInDegrees, double drive) {
		super(linearStrat, angleInDegrees, drive);
		
		// make sure time is a valid number
		this.timeInSeconds = Math.abs(timeInSeconds);
		
		timer = new ElapsedTimer();
	}

	@Override
	public void initEndCondition() {
		timer.start();
	}

	@Override
	public boolean isEndConditionMet() {
		return (timer.getElapsedSeconds() > timeInSeconds);
	}

}
