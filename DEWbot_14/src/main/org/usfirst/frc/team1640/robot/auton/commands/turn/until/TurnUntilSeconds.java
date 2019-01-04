package main.org.usfirst.frc.team1640.robot.auton.commands.turn.until;

import main.org.usfirst.frc.team1640.robot.traversal.swerve.axial.AxialStrategy;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class TurnUntilSeconds extends TurnUntilCommand {
	private double timeInSeconds;
	
	private ElapsedTimer timer;

	public TurnUntilSeconds(AxialStrategy axialStrat, double timeInSeconds, double drive) {
		super(axialStrat, drive);
		
		timer = new ElapsedTimer();
		
		this.timeInSeconds = timeInSeconds;
	}

	@Override
	public void initEndCondition() {
		timer.restart();
	}

	@Override
	public boolean isEndConditionMet() {
		return (timer.getElapsedSeconds() > timeInSeconds);
	}

}