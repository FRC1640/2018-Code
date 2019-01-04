package main.org.usfirst.frc.team1640.robot.auton.commands.drive;

import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;

public class ResetEncoders implements AutonCommand {
	private IRobotContext robotContext;
	
	private boolean isDone;
	
	public ResetEncoders(IRobotContext robotContext) {
		this.robotContext = robotContext;
		
		isDone = false;
	}
	
	@Override
	public void execute() {
		if (!isDone) {
			robotContext.getDriveTrain().resetPositions();
			isDone = true;
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
