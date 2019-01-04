package main.org.usfirst.frc.team1640.robot.traversal.sunflower;

import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class WheelsMaintainDirectionSunflower implements SunflowerStrategy {
	private SunflowerStrategy sunflowerStrat;
	private IGyro gyro;
	
	private double lateralDrive;
	private double longitudinalDrive;
	private boolean prevMoving;
	private double direction;
	private double directionWhenStopped;
	private boolean acceptNewDirections;
	
	private final double kMinDrive = 0.1; // the speed under which the axial drive will turn off
	
	public WheelsMaintainDirectionSunflower(SunflowerStrategy sunflowerStrat, IGyro gyro) {
		this.sunflowerStrat = sunflowerStrat;
		this.gyro = gyro;
	}

	@Override
	public void setDirection(double direction) {
		this.direction = direction;
	}

	@Override
	public void init() {
		sunflowerStrat.init();
		direction = gyro.getYaw();
		directionWhenStopped = direction;
	}

	@Override
	public void execute() {
		boolean moving = MathUtilities.magnitude(lateralDrive, longitudinalDrive) > kMinDrive;
		
		if (!moving && prevMoving) {
			directionWhenStopped = direction;
		}
		if (moving && !prevMoving || gyro.hasRecentlyOffset()) {
			direction = gyro.getYaw();
		}
		if (!moving && direction == directionWhenStopped) {
			sunflowerStrat.acceptNewDirections(false);
		}
		else {
			sunflowerStrat.acceptNewDirections(acceptNewDirections);
		}

		sunflowerStrat.setDirection(direction);
		sunflowerStrat.setLateralDrive(lateralDrive);
		sunflowerStrat.setLongitudinalDrive(longitudinalDrive);
		sunflowerStrat.execute();
		
		prevMoving = moving;
	}

	@Override
	public void end() {
		sunflowerStrat.end();
	}

	@Override
	public void setLateralDrive(double drive) {
		lateralDrive = drive;
	}
	
	@Override
	public void setLongitudinalDrive(double drive) {
		longitudinalDrive = drive;
	}

	@Override
	public void setControlMode(ControlMode control) {
		sunflowerStrat.setControlMode(control);
	}

	@Override
	public void acceptNewDirections(boolean acceptNewDirections) {
		this.acceptNewDirections = acceptNewDirections;
	}

}
