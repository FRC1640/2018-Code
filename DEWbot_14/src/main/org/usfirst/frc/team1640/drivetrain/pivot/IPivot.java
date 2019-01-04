package main.org.usfirst.frc.team1640.drivetrain.pivot;

import com.ctre.phoenix.motorcontrol.ControlMode;


public interface IPivot {
	
	public void setTargetAngle(double angle);

	public void setDrive(double drive);
	
	public void setControlMode(ControlMode controlMode);
	
	public void enable();
	
	public void disable();
	
	public double getAngle();
	
	public double getVoltage();

	public double getVelocity();
	
	public int getPosition();
	
	public void resetPosition();
	
	public double getInches();
	
	public double getFeet();
	
	public ControlMode getControlMode();
	
	public double getSetpoint();
	
	public double getTargetAngle();
	
	public double getDrive();
	
	public boolean getFlipDrive();
}
