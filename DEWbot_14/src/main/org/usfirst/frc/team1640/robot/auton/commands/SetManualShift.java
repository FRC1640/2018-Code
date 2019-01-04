package main.org.usfirst.frc.team1640.robot.auton.commands;

import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;

public class SetManualShift implements AutonCommand{
	private ManualShiftingDrive driveStrat;
	private double setting;
	private boolean isInit;
	
	public SetManualShift(ManualShiftingDrive driveStrat, double setting) {
		this.driveStrat = driveStrat;
		this.setting = setting;
		
		isInit = false;
	}
	
	@Override
	public void execute() {
		if (!isInit) {
			driveStrat.setTransmission(setting);
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
