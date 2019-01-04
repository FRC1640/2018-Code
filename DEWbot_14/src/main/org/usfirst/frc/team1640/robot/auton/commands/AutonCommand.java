package main.org.usfirst.frc.team1640.robot.auton.commands;

public interface AutonCommand { //interface for all auton commands (ex. drive, intake)
	
	void execute();
	
	boolean isRunning();
	
	boolean isInitialized();
	
	void reset();
}