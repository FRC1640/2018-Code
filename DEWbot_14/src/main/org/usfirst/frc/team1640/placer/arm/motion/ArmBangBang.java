package main.org.usfirst.frc.team1640.placer.arm.motion;

import com.ctre.phoenix.motorcontrol.ControlMode;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;

public class ArmBangBang extends ArmMotion {
	
	public ArmBangBang(ISensorSet sensorSet) {
		super(sensorSet);
	}

	private final double REG_SPEED = 1;
	private final double SLOW_SPEED = 1;
	private final double SETPOINT_BUFFER = 5;
	private final double LIMIT_BUFFER = 25;
	
	@Override
	public void execute() {
		double currentAngle = getAngle();
		double speed = 0;
		if (Math.abs(currentAngle - setpoint) > SETPOINT_BUFFER && 
				setpoint < PlacerConstants.ARM_UPPER_LIMIT && 
				setpoint > PlacerConstants.ARM_LOWER_LIMIT) {
			if ((currentAngle > PlacerConstants.MAX_HEIGHT - LIMIT_BUFFER && setpoint > currentAngle) 
					|| (currentAngle < LIMIT_BUFFER + PlacerConstants.FLOOR_BUFFER && setpoint < currentAngle)) {				
				speed = SLOW_SPEED;
			}
			
			else {
				speed = REG_SPEED;
			}
			
			if (currentAngle > setpoint) {
				speed = -speed;
			}
			
		}
		
		if (speed == 0) {
//			armBrake.set(true);
		}
		
		else {
//			armBrake.set(false);
		}
		
		armMotorLeft.set(ControlMode.PercentOutput, speed);
		armMotorRight.set(ControlMode.PercentOutput, -speed);
		
		System.out.println("arm Speed: " + speed + " arm angle: " + currentAngle + " setpoint " + setpoint);
	

	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return Math.abs(getAngle() - setpoint) < SETPOINT_BUFFER;
	}

}
