package main.org.usfirst.frc.team1640.controllers.rumblers;

import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class PulsingRumbler extends Rumbler {
	private final double kPulsePeriod = 0.25;
	private ElapsedTimer leftTimer;
	private ElapsedTimer rightTimer;
	
	private boolean isLeftPulsing;
	private boolean isLeftRumbling;
	private boolean isRightPulsing;
	private boolean isRightRumbling;
	private int leftPulses;
	private int myLeftPulses;
	private int rightPulses;
	private int myRightPulses;
	
	private boolean prevIsLeftRumbling;
	private boolean prevIsRightRumbling;
	
	PulsingRumbler() {
		leftTimer = new ElapsedTimer();
		rightTimer = new ElapsedTimer();
		
		isLeftPulsing = false;
		isLeftRumbling = true;
		leftPulses = 0;
		myLeftPulses = 0;
		
		isRightPulsing = false;
		isRightRumbling = true;
		rightPulses = 0;
		myRightPulses = 0;
		
		prevIsLeftRumbling = true;
		prevIsRightRumbling = true;
	}
	
	@Override
	public void update() {

		if (isLeftPulsing) {
			isLeftRumbling = (leftTimer.getElapsedSeconds() % kPulsePeriod < kPulsePeriod/2);
			
			if (isLeftRumbling && !prevIsLeftRumbling) {
				myLeftPulses++;
			}
			if (leftPulses == -1 || myLeftPulses < leftPulses) {
			
				if (isLeftRumbling) {
					leftRumble = 1.0;
				}
				else {
					leftRumble = 0.0;
				}
			}
			else {
				isLeftPulsing = false;
				leftRumble = 0;
			}
		}
		else {
			leftRumble = 0;
		}
		
		if (isRightPulsing) {
			isRightRumbling = (rightTimer.getElapsedSeconds() % kPulsePeriod < kPulsePeriod/2);
			
			if (isRightRumbling && !prevIsRightRumbling) {
				myRightPulses++;
			}
			if (rightPulses == -1 || myRightPulses < rightPulses) {
				
				if (isRightRumbling) {
					rightRumble = 1.0;
				}
				else {
					rightRumble = 0;
				}
			}
			else {
				rightRumble = 0;
				isRightPulsing = false;
			}
		}
		else {
			rightRumble = 0;
		}
		
		prevIsLeftRumbling = isLeftRumbling;
		prevIsRightRumbling = isRightRumbling;
	}
	
	// set the number of pulses to -1 to pulse indefinitely
	public void pulseLeft(int numberOfPulses) {
		if (numberOfPulses < -1) numberOfPulses = -1;
		leftPulses = numberOfPulses;
		myLeftPulses = 0;
		
		isLeftPulsing = true;
		isLeftRumbling = true;
		prevIsLeftRumbling = true;
		leftTimer.start();
	}
	
	// set the number of pulses to -1 to pulse indefinitely
	public void pulseRight(int numberOfPulses) {
		if (numberOfPulses < -1) numberOfPulses = -1;
		rightPulses = numberOfPulses;
		myRightPulses = 0;
		
		isRightPulsing = true;
		isRightRumbling = true;
		prevIsRightRumbling = true;
		rightTimer.start();
	}
	
	public void cancelLeft() {
		isLeftPulsing = false;
		leftRumble = 0;
	}
	
	public void cancelRight() {
		isRightPulsing = false;
		rightRumble = 0;
	}
	
	public void cancel() {
		cancelLeft();
		cancelRight();
	}

}
