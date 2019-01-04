package main.org.usfirst.frc.team1640.drivetrain;

import main.org.usfirst.frc.team1640.drivetrain.cvtpivot.CVTPivot;
import main.org.usfirst.frc.team1640.drivetrain.cvtpivot.ICVTPivot;

public interface IDriveTrain {
	
	public double getPositionEncoderCounts();
	
	public double getPositionInches();
	
	public double getPositionFeet();
	
	public void setFavoritePivot(ICVTPivot pivot);
	
	public ICVTPivot getFavoritePivot();
	
	public ICVTPivot getFLPivot();
	
	public ICVTPivot getFRPivot();
	
	public ICVTPivot getBLPivot();
	
	public ICVTPivot getBRPivot();
	
	public void resetPositions();
	
	public void enable();
	
	public void disable();
}
