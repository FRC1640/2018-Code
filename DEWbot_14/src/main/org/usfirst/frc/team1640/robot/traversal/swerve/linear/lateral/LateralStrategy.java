package main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.lateral;

import main.org.usfirst.frc.team1640.robot.traversal.swerve.controlmode.ControlModeStrategy;

public interface LateralStrategy extends ControlModeStrategy {
	
	public void setLateralDrive(double drive);
}
