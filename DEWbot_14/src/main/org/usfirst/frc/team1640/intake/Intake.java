package main.org.usfirst.frc.team1640.intake;

import main.org.usfirst.frc.team1640.constants.ports.CAN_ID_Constants;
import main.org.usfirst.frc.team1640.constants.ports.PCM_Constants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;

public class Intake {

	private Solenoid solenoidRetract;
	private Solenoid solenoidExtend;
	private WPI_TalonSRX leftWheel;
	private WPI_TalonSRX rightWheel;
	
	private boolean isExtending;
	
	public Intake() {
		solenoidRetract = new Solenoid(PCM_Constants.SOLENOID_RETRACT);
		solenoidExtend = new Solenoid(PCM_Constants.SOLENOID_EXTEND);
		
		leftWheel = new WPI_TalonSRX(CAN_ID_Constants.MOTOR_INTAKE_LEFT_ID);
		rightWheel = new WPI_TalonSRX(CAN_ID_Constants.MOTOR_INTAKE_RIGHT_ID);
		leftWheel.setNeutralMode(NeutralMode.Brake);
		rightWheel.setNeutralMode(NeutralMode.Brake);
		isExtending = false;
	}
	
	public void setLeftWheelDrive(double drive) {
		leftWheel.set(-drive);
	}
	
	public void setRightWheelDrive(double drive) {
		rightWheel.set(drive);
	}
	
	public void setExtending(boolean isExtending) {
		solenoidExtend.set(!isExtending);
		solenoidRetract.set(!isExtending);
	}
	
	public double getLeftWheelCurrent() {
		return leftWheel.getOutputCurrent();
	}
	
	public double getRightWheelCurrent() {
		return rightWheel.getOutputCurrent();
	}
	
	public boolean getExtending() {
		return isExtending;
	}
}
