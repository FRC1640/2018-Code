package main.org.usfirst.frc.team1640.robot.auton.commands;

import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class TimeoutCommand implements AutonCommand {
	private AutonCommand command;
	
	private ElapsedTimer timer;
	private double timeout;
	
	private boolean isInit;
	private boolean isDone;
	
	public TimeoutCommand(AutonCommand command, double timeout) {
		this.command = command;
		
		timer = new ElapsedTimer();
		this.timeout = timeout;
		
		isInit = false;
		isDone = false;
	}

	@Override
	public void execute() {
		if (!isInit) {
			timer.start();
			isInit = true;
		}
		else if (!isDone && command.isRunning() && timer.getElapsedSeconds() < timeout) {
			command.execute();
		}
		else {
			isDone = true;
		}
	}

	@Override
	public boolean isRunning() {
		return !isDone;
	}

	@Override
	public boolean isInitialized() {
		return command.isInitialized();
	}

	@Override
	public void reset() {
		command.reset();
		isInit = false;
		isDone = false;
	}

}
