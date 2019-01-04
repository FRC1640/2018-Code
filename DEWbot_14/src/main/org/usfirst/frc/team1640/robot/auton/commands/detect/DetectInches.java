package main.org.usfirst.frc.team1640.robot.auton.commands.detect;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;

public class DetectInches implements AutonCommand{
	private IDriveTrain driveTrain;
	private double inchThreshold;
	private boolean isDone;
	
	public DetectInches(IRobotContext robotContext, double inches){
		driveTrain = robotContext.getDriveTrain();
		inchThreshold = inches;
	}

	@Override
	public void execute() {
		if (!isDone) {
			isDone = Math.abs(driveTrain.getPositionInches()) >= inchThreshold;
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
