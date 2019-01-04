package main.org.usfirst.frc.team1640.placer.lift.motion;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;
import main.org.usfirst.frc.team1640.constants.ports.CAN_ID_Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public abstract class LiftMotion {
	
	private double offset;
	protected double setpoint;
	protected final double SETPOINT_BUFFER = 2;
	protected TalonSRX liftMotor;
	private TalonSRX liftMotor2;
	
	public LiftMotion() {
		liftMotor = new TalonSRX(CAN_ID_Constants.MOTOR_LIFT_ID);
		liftMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 20);
		
		liftMotor2 = new TalonSRX(CAN_ID_Constants.MOTOR_LIFT_2_ID);
		liftMotor2.set(ControlMode.Follower, CAN_ID_Constants.MOTOR_LIFT_ID);
		liftMotor2.setInverted(true);
		liftMotor.getSensorCollection().setQuadraturePosition(0, 20);
	}
	public abstract void init();
	public abstract void execute();
	
	public boolean isDone(){
		return Math.abs(getHeight() - setpoint) < SETPOINT_BUFFER;
	}
	
	public void setLiftHeight(double height){
		setpoint = height;
	}
	
	
	protected double countsToInches(double counts) {
		return counts / PlacerConstants.COUNTS_PER_REV * PlacerConstants.SPROCKET_TEETH / 4;
	}
	
	protected double inchesToCounts(double inches){
		return inches * 4 * PlacerConstants.COUNTS_PER_REV / PlacerConstants.SPROCKET_TEETH;
	}
	
	public double getHeight() {
		return countsToInches(liftMotor.getSelectedSensorPosition(0)) - offset;
	}
	
	public void reset() {
		System.out.println("Resetting. Previous height: " + getHeight());
		liftMotor.setSelectedSensorPosition(0, 0, 20);
		offset = getHeight();
		System.out.println("New height: " + getHeight() + " offset: " + offset);
	}
}
