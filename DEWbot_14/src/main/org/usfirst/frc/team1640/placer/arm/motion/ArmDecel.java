package main.org.usfirst.frc.team1640.placer.arm.motion;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class ArmDecel extends ArmMotion {
	
	private final double MAX_SPEED = 0.55;
	private final double SETPOINT_BUFFER = 8;
	private final double RETRACTING_SPEED = 0.45; //0.35
	private final double DOWN_SPEED = 0.35;
	private final double OVER_BACK_UP_SPEED = 0.375;
	private final double OVER_BACK_DOWN_SPEED = 0.4;
	private final double OVER_BACK_BRAKE_BUFFER = 15;
	private final double OVER_BACK_RAMP = 0.4;
	private final double BRAKE_BUFFER = 20;
	private final double REGULAR_RAMP = 0.25;
	private final double RETRACTING_RAMP = 0;
	private boolean retracting = false, prevBrake;
	private double prevSpeed;
	private ElapsedTimer timer;
	private boolean prevRetracting;
	private double prevSetpoint;
	
	public ArmDecel(ISensorSet sensorSet) {
		super(sensorSet);
		timer = new ElapsedTimer();
		setRampRate(REGULAR_RAMP);
	}

	@Override
	public void execute() {
		double currentAngle = getAngle();
		double speed = 0;
		double ramp = REGULAR_RAMP;
		double brakeBuffer = BRAKE_BUFFER;
		if (getError() > SETPOINT_BUFFER && 
				setpoint <= PlacerConstants.ARM_UPPER_LIMIT && 
				setpoint >= PlacerConstants.ARM_LOWER_LIMIT) {
			if(currentAngle < setpoint){
				if(currentAngle > 85){ //over back
					speed = OVER_BACK_UP_SPEED;
					ramp = OVER_BACK_RAMP;
				}
				else{
					speed = MAX_SPEED;
				}			
			}
			else{
				if(currentAngle > 85){ //over back
					speed = -OVER_BACK_DOWN_SPEED;
					ramp = OVER_BACK_RAMP;
					brakeBuffer = OVER_BACK_BRAKE_BUFFER;
				}
				else{
					speed = -DOWN_SPEED;
				}
			}
		}
		
		boolean brake = brake(brakeBuffer);
		
		armBrake.set(!brake); //true - brake not engaged

		
		if(!brake && prevBrake && !retracting){
			timer.restart();
			retracting = true;
		}
		
		if(retracting){
			if(timer.getElapsedMilliseconds() > 90){ //90
				retracting = false;
				timer.restart();
			}
//			System.out.println("retracting arm brake");
			speed = RETRACTING_SPEED;
		}
		
		if(!retracting) {
			if(!isDone() && timer.getElapsedMilliseconds() > 1000) {
				retracting = true;
			}
		}
		
		if(getError() < SETPOINT_BUFFER){
			setRampRate(RETRACTING_RAMP);
		}
		else{
			setRampRate(ramp);
		}
		
//		System.out.println("speed: " + speed + " setpoint: " + setPointAngle + " current angle: " + currentAngle);
//		System.out.println("speed: " + speed + " brake: " + brake + " retracting: " + retracting);

		
		armMotorRight.set(ControlMode.PercentOutput, -speed);
		armMotorLeft.set(ControlMode.PercentOutput, speed);

		if(setpoint != prevSetpoint){
			System.out.println("New setpont angle: " + setpoint );
		}
//		
		prevSpeed = speed;
		prevBrake = brake;
		prevRetracting = retracting;
		prevSetpoint = setpoint;
		
//		System.out.println("brake: " + brake + " speed: " + speed + " angle: " + getAngle());
	}
	
	private boolean brake(double brakeBuffer){
//		return (setpoint != prevSetpoint) && (getError() < BRAKE_BUFFER && setpoint != -4.6);
		return getError() < brakeBuffer && setpoint != -4.6; //TODO set to constant
	}
	
	private double getError(){
		return Math.abs(setpoint - getAngle());
	}
	
	private void setRampRate(double secondsToFull){
		armMotorLeft.configOpenloopRamp(secondsToFull, 20);
		armMotorRight.configOpenloopRamp(secondsToFull, 20);
	}
}
