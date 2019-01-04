package main.org.usfirst.frc.team1640.robot.auton.commands;

public class PrintCommand implements AutonCommand {
	private boolean isDone;
	private String stringToPrint;
	
	public PrintCommand(String stringToPrint) {
		this.stringToPrint = stringToPrint;
		isDone = false;
	}

	@Override
	public void execute() {
		if (!isDone) {
			isDone = true;
			System.out.println(stringToPrint);
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
		System.out.println("Restting print command");
		isDone = false;
	}

}
