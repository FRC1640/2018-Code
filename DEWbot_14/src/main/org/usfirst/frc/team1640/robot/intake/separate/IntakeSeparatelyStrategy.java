package main.org.usfirst.frc.team1640.robot.intake.separate;

import main.org.usfirst.frc.team1640.robot.intake.IntakeStrategy;

public interface IntakeSeparatelyStrategy extends IntakeStrategy {
	//An intake strategy that allows each intake wheel to be driven separately

	public void setLeftWheelDrive(double drive);
	
	public void setRightWheelDrive(double drive);
	
}
