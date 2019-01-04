package main.org.usfirst.frc.team1640.placer.lift.motion;

import com.ctre.phoenix.motorcontrol.ControlMode;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;

public class LiftBangBang extends LiftMotion {	
	private final double LIMIT_BUFFER = 5;
	
	private final double UP_SPEED = 1;
	private final double SLOW_UP_SPEED = 0.5;
	private final double SLOW_UP_BUFFER = 5;
	private final double SETPOINT_BUFFER_UP = 1;

	private final double DOWN_SPEED = 0.5;
	private final double SLOW_DOWN_SPEED = 0.2;
	private final double SLOW_DOWN_BUFFER = 5;
	private final double SETPOINT_BUFFER_DOWN = 1;

	private double prevHeight = getHeight(), prevCounts;

	@Override
	public void execute() {
		double currentHeight = getHeight();
		double currentCounts = liftMotor.getSelectedSensorPosition(0);
		double speed = 0;
		if (setpoint < PlacerConstants.MAX_HEIGHT && 
			setpoint > PlacerConstants.FLOOR_BUFFER) {
			if(currentHeight > setpoint){ //moving down
				speed = -getSpeed(currentHeight, DOWN_SPEED, SLOW_DOWN_SPEED, SLOW_DOWN_BUFFER, SETPOINT_BUFFER_DOWN);		
			}
			else{ //moving up
				speed = getSpeed(currentHeight, UP_SPEED, SLOW_UP_SPEED, SLOW_UP_BUFFER, SETPOINT_BUFFER_UP);		
			}
		}
		
		liftMotor.set(ControlMode.PercentOutput, speed);
		liftMotor.configOpenloopRamp(.25, 20);
		System.out.println("Speed: " + speed + " setpoint: " + setpoint + " height: " + currentHeight);
		
		if(Math.abs(currentHeight - prevHeight) > 5){
			System.out.println("Major height jump. Old height: " + prevHeight + " new height: " + currentHeight);
			System.out.println("Prev count: " + prevCounts + " current counts: " + currentCounts);
		}
		prevHeight = currentHeight;
		prevCounts = currentCounts;
	}
		
	private double getSpeed(double currentHeight, double regularSpeed, double slowSpeed, double slowBuffer, double setpointBuffer){
		if(Math.abs(currentHeight - setpoint) < setpointBuffer){
			return 0;
		}
		else if ((currentHeight > PlacerConstants.MAX_HEIGHT - LIMIT_BUFFER && setpoint > currentHeight) || 
				(currentHeight < LIMIT_BUFFER + PlacerConstants.FLOOR_BUFFER && setpoint < currentHeight) ||
				Math.abs(currentHeight - setpoint) < slowBuffer){
			return slowSpeed;
		}
		else{
			return regularSpeed;
		}
	}

	@Override
	public boolean isDone() {
		return Math.abs(getHeight() - setpoint) < SETPOINT_BUFFER_UP;
	}
	
	@Override
	public void init() {
		
	}

}
