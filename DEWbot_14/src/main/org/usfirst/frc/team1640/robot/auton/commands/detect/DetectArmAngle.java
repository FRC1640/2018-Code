package main.org.usfirst.frc.team1640.robot.auton.commands.detect;

import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;

public class DetectArmAngle implements AutonCommand{
	private Placer placer;
	private double angleThreshold;
	private boolean isDone, above, prevAbove;
	
	public DetectArmAngle(IRobotContext robotContext, double angle){
		placer = robotContext.getPlacer();
		angleThreshold = angle;
	}

	@Override
	public void execute() {
		if (!isDone) {
			above = placer.getArmAngle() > angleThreshold;
			isDone = above != prevAbove;
			prevAbove = above;
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
