package main.org.usfirst.frc.team1640.robot.intake;

import main.org.usfirst.frc.team1640.utilities.Strategy;

public interface IntakeStrategy extends Strategy {

	public void setExtending(boolean isExtending);
	
	public boolean getExtending();
	
	public double getLeftWheelCurrent();
	
	public double getRightWheelCurrent();
	
}
