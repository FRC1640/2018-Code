package main.org.usfirst.frc.team1640.robot.intake.together;

import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class CurrentStopIntakeTogether implements IntakeTogetherStrategy {
	
	private IntakeTogetherStrategy intakeStrat;
	private ElapsedTimer timer; // keeps track of how long the current exceeds a threshold
	private boolean currentTooHigh; // if the current exceeds a threshold
	private boolean prevCurrentTooHigh;
	private boolean stop; // whether to stop intaking
	
	private final double kMaxCurrent = 15; // the threshold current that will trigger the timer
	private final double kTime = 0.3; // the amount of time in seconds the intake will operate above threshold current before stopping
	
	public CurrentStopIntakeTogether(IntakeTogetherStrategy intakeStrat) {
		this.intakeStrat = intakeStrat;
		timer = new ElapsedTimer();
		
		currentTooHigh = false;
		prevCurrentTooHigh = currentTooHigh;
		stop = false;
	}

	@Override
	public void setExtending(boolean isExtending) {
		intakeStrat.setExtending(isExtending);
	}

	@Override
	public boolean getExtending() {
		return intakeStrat.getExtending();
	}

	@Override
	public double getLeftWheelCurrent() {
		return intakeStrat.getLeftWheelCurrent();
	}

	@Override
	public double getRightWheelCurrent() {
		return intakeStrat.getRightWheelCurrent();
	}

	@Override
	public void init() {
		currentTooHigh = false;
		prevCurrentTooHigh = currentTooHigh;
		intakeStrat.init();
	}

	@Override
	public void execute() {
		intakeStrat.execute();
	}

	@Override
	public void end() {
		intakeStrat.end();
	}

	@Override
	public void setWheelDrive(double drive) {
		double leftCurrent = intakeStrat.getLeftWheelCurrent();
		double rightCurrent = intakeStrat.getRightWheelCurrent();
		
		currentTooHigh = MathUtilities.maximum(leftCurrent, rightCurrent) > kMaxCurrent;
		
		// if out-taking, un-stop the intake
		if (drive <= 0) {
			stop = false;
		}
		else if (currentTooHigh) {
			if (!prevCurrentTooHigh) {
				timer.restart(); // the instant the current becomes too high, restart the timer
			}
			
			// if enough time has passed with the current too high, stop intaking
			if (timer.getElapsedSeconds() > kTime) {
				stop = true;
			}
		}
		
		// if intaking is stopped, set the wheel drive to 0
		if (stop) {
			drive = 0;
		}
		
		intakeStrat.setWheelDrive(drive);
		
		prevCurrentTooHigh = currentTooHigh; // update prev variables
	}
	
	public boolean isStopped() {
		return stop;
	}
	
}
