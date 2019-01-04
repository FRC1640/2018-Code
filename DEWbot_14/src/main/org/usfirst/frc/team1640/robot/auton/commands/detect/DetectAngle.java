package main.org.usfirst.frc.team1640.robot.auton.commands.detect;

import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class DetectAngle implements AutonCommand{
	private IGyro gyro;
	private double angle;
	private boolean isDone, greater;
	
	
	public DetectAngle(IGyro gyro, double angle, boolean greater){
		this.gyro = gyro;
		this.angle = angle;
		this.greater = greater;
	}

	@Override
	public void execute() {
		if (!isDone) {
			if(greater){
				isDone = gyro.getYaw() > angle;
			}
			else{
				System.out.println("yaw: " + gyro.getYaw());
				isDone = gyro.getYaw() < angle;
			}
		}
	}

	@Override
	public boolean isRunning() {
		return !isDone;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void reset() {
		isDone = false;
	}

}
