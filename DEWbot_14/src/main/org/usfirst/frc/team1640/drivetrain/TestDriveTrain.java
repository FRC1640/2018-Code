package main.org.usfirst.frc.team1640.drivetrain;

import main.org.usfirst.frc.team1640.drivetrain.cvtpivot.ICVTPivot;
import main.org.usfirst.frc.team1640.drivetrain.cvtpivot.TestCVTPivot;

public class TestDriveTrain implements IDriveTrain {
	public static enum PivotPosition {
		kFrontLeft, kFrontRight, kBackLeft, kBackRight
	}
	private ICVTPivot flPivot, frPivot, blPivot, brPivot;
	
	public TestDriveTrain() {
		flPivot = new TestCVTPivot();
		frPivot = new TestCVTPivot();
		blPivot = new TestCVTPivot();
		brPivot = new TestCVTPivot();
	}
	
	public void setFavoritePivot(ICVTPivot pivot) {
	}
	
	public ICVTPivot getFLPivot() {
		return flPivot;
	}
	
	public ICVTPivot getFRPivot() {
		return frPivot;
	}
	
	public ICVTPivot getBLPivot() {
		return blPivot;
	}
	
	public ICVTPivot getBRPivot() {
		return brPivot;
	}
	
	public void resetPositions() {
		flPivot.resetPosition();
		frPivot.resetPosition();
		blPivot.resetPosition();
		brPivot.resetPosition();
	}
	
	public void enable() {
		flPivot.enable();
		frPivot.enable();
		blPivot.enable();
		brPivot.enable();
	}
	
	public void disable() {
		flPivot.disable();
		frPivot.disable();
		blPivot.disable();
		brPivot.disable();
	}

	@Override
	public double getPositionEncoderCounts() {
		
		return 0;
	}

	@Override
	public double getPositionInches() {
		
		return 0;
	}

	@Override
	public double getPositionFeet() {
		
		return 0;
	}

	@Override
	public ICVTPivot getFavoritePivot() {
		// TODO Auto-generated method stub
		return null;
	}
}
