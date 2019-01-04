package main.org.usfirst.frc.team1640.placer.lift.motion;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;

public class TestLiftMotion extends LiftMotion {
	
	private double setPointHeight;
	private final double REG_SPEED = 0.5;
	private final double SLOW_SPEED = 0.3;
	private final double SETPOINT_BUFFER = 5;
	private final double LIMIT_BUFFER = 15;

	@Override
	public void setLiftHeight(double height) {
		if(height != setPointHeight){
			System.out.println("Prev height " + setPointHeight);
			System.out.println("New height " + height);
		}
		setPointHeight = height;
	}

	@Override
	public void execute() {
//		double currentHeight = getHeight();
//		double speed = 0;
//		if (Math.abs(currentHeight - setPointHeight) > SETPOINT_BUFFER && setPointHeight < PlacerConstants.MAX_HEIGHT && setPointHeight > PlacerConstants.FLOOR_BUFFER) {
//			if (currentHeight > PlacerConstants.MAX_HEIGHT - LIMIT_BUFFER || currentHeight < LIMIT_BUFFER + PlacerConstants.FLOOR_BUFFER) {
//				speed = SLOW_SPEED;
//			}
//			
//			else {
//				speed = REG_SPEED;
//				
//			}
//			
//			if (currentHeight > setPointHeight) {
//				speed = -speed;
//			}
//		}
//		
//		System.out.println("lift speed: " + liftMotor.getSelectedSensorPosition(0) + " lift height: " + currentHeight);
//		armBrake.set(true);
		System.out.println("Lift Height: " + getHeight());
	}

	@Override
	public boolean isDone() {
		return false;
	}
	
	@Override
	public void init() {
		
	}

}
