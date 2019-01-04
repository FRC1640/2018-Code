package main.org.usfirst.frc.team1640.placer.arm.motion;

import main.org.usfirst.frc.team1640.constants.ports.CAN_ID_Constants;
import main.org.usfirst.frc.team1640.constants.ports.PCM_Constants;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;

public abstract class ArmMotion {
	
	private final double minVoltage = 0.2;
	private final double maxVoltage = 4.8;
	private final double dVoltage = maxVoltage - minVoltage;
	private final double SETPOINT_BUFFER = 8;
	protected TalonSRX armMotorLeft;
	protected TalonSRX armMotorRight;
	protected AnalogInput armSensor;
	protected Solenoid armBrake;
	protected double setpoint;
	
	public ArmMotion(ISensorSet sensorSet) {
		armMotorLeft = new TalonSRX(CAN_ID_Constants.MOTOR_LEFT_ARM_ID);
		armMotorRight = new TalonSRX(CAN_ID_Constants.MOTOR_RIGHT_ARM_ID);
		armSensor = sensorSet.getArmSensor();
		armBrake = new Solenoid(0, PCM_Constants.SOLENOID_BRAKE);
		
	}
	public abstract void execute();
	
	public void setArmAngle(double angle){
		setpoint = angle;
	}
	
	protected double voltageToAngle(double voltage) {
		return ( (-(360.0 * (voltage - minVoltage) / dVoltage) + 360.0 - 0) % 360.0) - 27.33;
	}
	
	public double getAngle() {
		return voltageToAngle(armSensor.getVoltage());
	}
	
	public boolean isDone(){
		return Math.abs(setpoint - getAngle()) < SETPOINT_BUFFER;
	}
}
