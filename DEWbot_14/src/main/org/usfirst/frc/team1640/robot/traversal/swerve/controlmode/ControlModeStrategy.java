package main.org.usfirst.frc.team1640.robot.traversal.swerve.controlmode;

import main.org.usfirst.frc.team1640.robot.traversal.TraversalStrategy;

import com.ctre.phoenix.motorcontrol.ControlMode;

public interface ControlModeStrategy extends TraversalStrategy {
	public void setControlMode(ControlMode control);
}
