package main.org.usfirst.frc.team1640.robot.auton.commands;

public class WaitCommand implements AutonCommand { //auton command to pause script until another command has been completed
	//this class allows for multitasking in auton scripts
	private AutonCommand command; //command to wait for
	
	public WaitCommand(AutonCommand command){
		//record inputs
		this.command = command;
	}

	@Override
	public void execute() { }

	@Override
	public boolean isRunning() {
		return command.isRunning(); //this command only finishes when the command it holds is finished
	}

	@Override
	public boolean isInitialized() {
		return !command.isRunning(); //this command only initializes when the command it holds is finished 
		//(this prevents the script from moving to a new command)
	}
	
	@Override 
	public void reset() {}
}
