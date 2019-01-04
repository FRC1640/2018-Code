package main.org.usfirst.frc.team1640.robot.traversal.swerve.axial;

import main.org.usfirst.frc.team1640.robot.traversal.swerve.controlmode.ControlModeStrategy;

public interface AxialStrategy extends ControlModeStrategy {
	
	public void setAxialDrive(double drive);
}
