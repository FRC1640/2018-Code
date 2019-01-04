package main.org.usfirst.frc.team1640.placer.arm.motion;

import main.org.usfirst.frc.team1640.sensors.ISensorSet;
import main.org.usfirst.frc.team1640.utilities.PIDOutputDouble;
import main.org.usfirst.frc.team1640.utilities.PIDSourceDouble;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.PIDController;

public class ArmPID extends ArmMotion {
	private PIDController pid;
	private PIDOutputDouble speed;
	private PIDSourceDouble angle;
	private double setpoint, p = 0, i = 0, d = 0;
	
	public ArmPID(ISensorSet sensorSet){
		super(sensorSet);
		speed = new PIDOutputDouble();
		angle = new PIDSourceDouble();
		pid = new PIDController(p, i, d, angle, speed);
		pid.enable();
	}

	@Override
	public void execute() {
		pid.setSetpoint(setpoint);
		angle.setValue(getAngle());
		armMotorLeft.set(ControlMode.PercentOutput, speed.getValue());
		armMotorRight.set(ControlMode.PercentOutput, -speed.getValue());
	}
}
