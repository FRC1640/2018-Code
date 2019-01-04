package main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.longitudinal;

import main.org.usfirst.frc.team1640.robot.traversal.swerve.controlmode.ControlModeStrategy;

public interface LongitudinalStrategy extends ControlModeStrategy {

	public void setLongitudinalDrive(double drive);
}
