package main.org.usfirst.frc.team1640.controllers.rumblers;

import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class BurstRumbler extends Rumbler {
	private ElapsedTimer leftRumbleTimer;
	private ElapsedTimer rightRumbleTimer;
	private double leftTime;
	private double rightTime;

	BurstRumbler() {
		leftRumbleTimer = new ElapsedTimer();
		rightRumbleTimer = new ElapsedTimer();
	}
	
	public void burstLeftRumble(double rumble, double timeInSeconds) {
		leftRumble = rumble;
		leftTime = timeInSeconds;
		leftRumbleTimer.restart();
	}
	
	public void burstRightRumble(double rumble, double timeInSeconds) {
		rightRumble = rumble;
		rightTime = timeInSeconds;
		rightRumbleTimer.restart();
	}
	
	@Override
	public void update() {
		if (leftRumbleTimer.getElapsedSeconds() > leftTime) {
			leftRumble = 0;
		}
		
		if (rightRumbleTimer.getElapsedSeconds() > rightTime) {
			rightRumble = 0;
		}
	}
	
	public void cancelLeftRumble() {
		leftRumble = 0;
		leftTime = 0;
	}
	
	public void cancelRightRumble() {
		rightRumble = 0;
		rightTime = 0;
	}
	
	public void cancel() {
		cancelLeftRumble();
		cancelRightRumble();
	}
}
