package main.org.usfirst.frc.team1640.placer.arm.motion;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;

public class TestArmMotion extends ArmMotion {
	private final double REG_SPEED = 0.5;
	private final double SLOW_SPEED = 0.3;
	private final double SETPOINT_BUFFER = 5;
	private final double LIMIT_BUFFER = 25;
	private double setPointAngle;
	public TestArmMotion(ISensorSet sensorSet) {
		super(sensorSet);
	}

	@Override
	public void setArmAngle(double angle) {
		if(angle != setPointAngle){
			System.out.println("Prev angle " + setPointAngle);
			System.out.println("New angle " + angle);
		}
		setPointAngle = angle;
	}

	@Override
	public void execute() {
		double currentAngle = getAngle();
		double speed = 0;
		if (Math.abs(currentAngle - setPointAngle) > SETPOINT_BUFFER && 
				setPointAngle < PlacerConstants.ARM_UPPER_LIMIT && 
				setPointAngle > PlacerConstants.ARM_LOWER_LIMIT) {
			if ((currentAngle > PlacerConstants.MAX_HEIGHT - LIMIT_BUFFER && setPointAngle > currentAngle) || 
					(currentAngle < LIMIT_BUFFER + PlacerConstants.FLOOR_BUFFER && setPointAngle < currentAngle)) {
				speed = SLOW_SPEED;
			}
			
			else {
				speed = REG_SPEED;
			}
			
			if (currentAngle > setPointAngle) {
				speed = -speed;
			}
			
		}
		
		
		armBrake.set(true);
		System.out.println("arm angle: " + getAngle());
//		System.out.println("arm Speed: " + speed + " arm angle: " + currentAngle + " setpoint " + setPointAngle);
	}

	@Override
	public boolean isDone() {
		return false;
	}

}
