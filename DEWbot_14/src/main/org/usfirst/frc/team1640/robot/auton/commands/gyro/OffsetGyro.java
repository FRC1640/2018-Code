package main.org.usfirst.frc.team1640.robot.auton.commands.gyro;

import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class OffsetGyro implements AutonCommand {
	private IGyro gyro;
	private double offset;
	
	private boolean isInit;
	
	public OffsetGyro(IGyro gyro, double offset) {
		this.gyro = gyro;
		this.offset = offset;
		
		isInit = false;
	}

	@Override
	public void execute() {
		if (!isInit) {
			gyro.setYawOffset(offset);
			isInit = true;
		}
	}

	@Override
	public boolean isRunning() {
		return !isInit;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void reset() {
		isInit = false;
	}
}
