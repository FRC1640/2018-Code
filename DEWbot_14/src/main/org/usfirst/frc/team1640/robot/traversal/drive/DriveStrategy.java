package main.org.usfirst.frc.team1640.robot.traversal.drive;

import main.org.usfirst.frc.team1640.robot.traversal.swerve.controlmode.ControlModeStrategy;

public interface DriveStrategy extends ControlModeStrategy {
	public void setFLDrive(double drive);
	public void setFRDrive(double drive);
	public void setBLDrive(double drive);
	public void setBRDrive(double drive);
}
