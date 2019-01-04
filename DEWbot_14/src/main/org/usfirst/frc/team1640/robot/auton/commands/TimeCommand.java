package main.org.usfirst.frc.team1640.robot.auton.commands;

import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class TimeCommand implements AutonCommand {
	private double timeInSeconds;
	
	private ElapsedTimer timer;
	
	private boolean isInit;
	private boolean isDone;
	
	public TimeCommand(double timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
		
		timer = new ElapsedTimer();
		
		isInit = false;
		isDone = false;
	}

	@Override
	public void execute() {
		if (!isInit) {
			timer.start();
			isInit = true;
		}
		
		if (!isDone) {
			if (timer.getElapsedSeconds() > timeInSeconds) {
				isDone = true;
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
		isInit = false;
		isDone = false;
	}

}
