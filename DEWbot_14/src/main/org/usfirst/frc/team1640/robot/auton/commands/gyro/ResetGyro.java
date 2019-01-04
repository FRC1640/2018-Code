package main.org.usfirst.frc.team1640.robot.auton.commands.gyro;

import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class ResetGyro implements AutonCommand {
	private IGyro gyro;
	
	private boolean isInit;
	
	public ResetGyro(IGyro gyro) {
		this.gyro = gyro;
		
		isInit = false;
	}

	@Override
	public void execute() {
		if (!isInit) {
			gyro.resetYaw();
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