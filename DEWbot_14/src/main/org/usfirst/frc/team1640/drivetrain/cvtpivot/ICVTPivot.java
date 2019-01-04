package main.org.usfirst.frc.team1640.drivetrain.cvtpivot;

import main.org.usfirst.frc.team1640.drivetrain.pivot.IPivot;


public interface ICVTPivot extends IPivot {
	
	public void setTransmission(double transmission);
	
	public double getTransmission();
}
