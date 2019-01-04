package main.org.usfirst.frc.team1640.robot.intake.together;

import main.org.usfirst.frc.team1640.robot.intake.IntakeStrategy;

public interface IntakeTogetherStrategy extends IntakeStrategy {
	// An intake strategy that makes it easy to drive wheels together
	
	public void setWheelDrive(double drive);
}
