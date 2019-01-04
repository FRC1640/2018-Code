package main.org.usfirst.frc.team1640.robot.auton.commands.detect;

import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;

public class DetectLiftHeight implements AutonCommand{
	private Placer placer;
	private double height;
	private boolean isDone, above, prevAbove, init;
	
	public DetectLiftHeight(IRobotContext robotContext, double height){
		placer = robotContext.getPlacer();
		this.height = height;
	}

	@Override
	public void execute() {
		if(!init){
			above = placer.getLiftHeight() > height;
			prevAbove = above;
			init = true;
		}
		if (!isDone) {
			above = placer.getLiftHeight() > height;
			isDone = above != prevAbove;
			prevAbove = above;
			if(isDone){
				System.out.println("Lift height done: " + placer.getLiftHeight());
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
		isDone = false;
	}

}
