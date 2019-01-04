package main.org.usfirst.frc.team1640.robot.auton.commands.drive;

import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DriveStrategy;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class ChangeControlMode implements AutonCommand {
	private DriveStrategy driveStrat;
	private ControlMode controlMode;
	private boolean isInit;
	
	public ChangeControlMode(DriveStrategy driveStrat, ControlMode controlMode) {
		this.driveStrat = driveStrat;
		this.controlMode = controlMode;
		
		isInit = false;
	}

	@Override
	public void execute() {
		if (!isInit) {
			driveStrat.setControlMode(controlMode);
			driveStrat.execute();
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
