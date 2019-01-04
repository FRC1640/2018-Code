package main.org.usfirst.frc.team1640.placer.lift.motion;

import com.ctre.phoenix.motorcontrol.ControlMode;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;

public class LiftProportional extends LiftMotion {	
	private final double LIMIT_BUFFER = 5;
	
	private final double UP_SPEED = 1.0;
	private final double UP_SPEED_SCALAR = 1.15;
	private final double MIN_UP_SPEED = 0.5;
	private final double SLOW_UP_BUFFER = 10;
	private final double SETPOINT_BUFFER_UP = 3;

	private final double DOWN_SPEED = 0.5;
	private final double DOWN_SPEED_SCALAR = 1.32;
	private final double MIN_DOWN_SPEED = 0.2;
	private final double SLOW_DOWN_BUFFER = 17;
	private final double SETPOINT_BUFFER_DOWN = 1;

	private double prevHeight = getHeight(), prevCounts, prevSpeed;
	
	@Override
	public void execute() {
		double currentHeight = getHeight();
		double currentCounts = liftMotor.getSelectedSensorPosition(0);
		double speed = 0;
		if (setpoint < PlacerConstants.MAX_HEIGHT && 
			setpoint > PlacerConstants.FLOOR_BUFFER) {
			if(currentHeight > setpoint){ //moving down
				speed = -getSpeed(currentHeight, DOWN_SPEED, MIN_DOWN_SPEED, DOWN_SPEED_SCALAR, SLOW_DOWN_BUFFER, SETPOINT_BUFFER_DOWN);		
			}
			else{ //moving up
				speed = getSpeed(currentHeight, UP_SPEED, MIN_UP_SPEED, UP_SPEED_SCALAR, SLOW_UP_BUFFER, SETPOINT_BUFFER_UP);		
			}
		}
		
		if(speed == 0 && prevSpeed != 0){
			System.out.println("Done. Current Height: " + currentHeight + " Setpoint height: " + setpoint);
		}
		liftMotor.set(ControlMode.PercentOutput, -speed);
		liftMotor.configOpenloopRamp(0.35, 20);
		
		if(Math.abs(currentHeight - prevHeight) > 5){
			System.out.println("Major height jump. Old height: " + prevHeight + " new height: " + currentHeight);
			System.out.println("Prev count: " + prevCounts + " current counts: " + currentCounts);
		}
		prevHeight = currentHeight;
		prevCounts = currentCounts;
		prevSpeed = speed;
		
		System.out.println("Enc Vel: " + liftMotor.getSelectedSensorVelocity(0) + " Motor output: " + liftMotor.getMotorOutputVoltage() / liftMotor.getBusVoltage());
	}
		
	private double getSpeed(double currentHeight, double regularSpeed, double minSpeed, double speedScalar, double slowBuffer, double setpointBuffer){
		if(Math.abs(currentHeight - setpoint) < setpointBuffer){
			return 0;
		}
		else if ((currentHeight > PlacerConstants.MAX_HEIGHT - LIMIT_BUFFER && setpoint > currentHeight) || 
				(currentHeight < LIMIT_BUFFER + PlacerConstants.FLOOR_BUFFER && setpoint < currentHeight) ||
				Math.abs(currentHeight - setpoint) < slowBuffer){
			double proportionalSpeed = regularSpeed - speedScalar / Math.abs(currentHeight - setpoint);
//			double selectedSpeed = proportionalSpeed > 0 ? Math.max(minSpeed, proportionalSpeed) : minSpeed;
//			System.out.println("Speed: " +  selectedSpeed + " up? " + (currentHeight < setPointHeight));
			return proportionalSpeed > 0 ? Math.max(minSpeed, proportionalSpeed) : minSpeed;
		}
		else{
			return regularSpeed;
		}
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return Math.abs(getHeight()-setpoint) < 5;
	}
	
	@Override
	public void init() {
		
	}

}
