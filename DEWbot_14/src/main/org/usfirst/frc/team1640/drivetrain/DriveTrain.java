package main.org.usfirst.frc.team1640.drivetrain;

import main.org.usfirst.frc.team1640.constants.ports.AnalogInputConstants;
import main.org.usfirst.frc.team1640.constants.ports.CAN_ID_Constants;
import main.org.usfirst.frc.team1640.constants.ports.PWM_Constants;
import main.org.usfirst.frc.team1640.drivetrain.cvtpivot.CVTPivot;
import main.org.usfirst.frc.team1640.drivetrain.cvtpivot.ICVTPivot;

public class DriveTrain implements IDriveTrain {
	public static enum PivotPosition {
		kFrontLeft, kFrontRight, kBackLeft, kBackRight
	}
	private ICVTPivot flPivot, frPivot, blPivot, brPivot;
	
	private ICVTPivot kFavoritePivot;
	
	public DriveTrain() {
		flPivot = new CVTPivot(CAN_ID_Constants.MOTOR_FLD_ID, CAN_ID_Constants.MOTOR_FLS_ID, AnalogInputConstants.RESOLVER_FL, PWM_Constants.SERVO_FL, 
				0.20019529200000002, 4.737548343 , 0.0, "fl");
		frPivot = new CVTPivot(CAN_ID_Constants.MOTOR_FRD_ID, CAN_ID_Constants.MOTOR_FRS_ID, AnalogInputConstants.RESOLVER_FR, PWM_Constants.SERVO_FR, 
				0.20385740100000002, 4.729003422 , 0.0, "fr");
		blPivot = new CVTPivot(CAN_ID_Constants.MOTOR_BLD_ID, CAN_ID_Constants.MOTOR_BLS_ID, AnalogInputConstants.RESOLVER_BL, PWM_Constants.SERVO_BL, 
				 0.209960916, 4.742431155, 180.0, "bl");
		brPivot = new CVTPivot(CAN_ID_Constants.MOTOR_BRD_ID, CAN_ID_Constants.MOTOR_BRS_ID, AnalogInputConstants.RESOLVER_BR, PWM_Constants.SERVO_BR, 
				0.205078104, 4.739989749 , 180.0, "br");
		
		kFavoritePivot = frPivot;
	}
	
	public void setFavoritePivot(ICVTPivot pivot) {
		kFavoritePivot = pivot;
	}
	
	public double getPositionEncoderCounts() {
		return kFavoritePivot.getPosition();
	}
	
	public double getPositionInches() {
		return kFavoritePivot.getInches();
	}
	
	public double getPositionFeet() {
		return kFavoritePivot.getFeet();
	}
	
	public ICVTPivot getFavoritePivot() {
		return kFavoritePivot;
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
}
