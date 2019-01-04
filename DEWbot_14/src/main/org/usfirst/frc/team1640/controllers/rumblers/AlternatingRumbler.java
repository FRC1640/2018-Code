package main.org.usfirst.frc.team1640.controllers.rumblers;

import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class AlternatingRumbler extends Rumbler {
	private ElapsedTimer timer;
	private boolean isRumbling;
	private boolean isRumblingLeft;
	private double rumble;
	private double period;
	private int times;
	
	private int myTimes = 0;
	private boolean prevIsRumblingLeft;

	AlternatingRumbler() {
		timer = new ElapsedTimer();
		
		isRumbling = false;
		isRumblingLeft = true;
		rumble = 0;
		period = 0;
		times = 0;
		
		myTimes = 0;
		prevIsRumblingLeft = true;
	}
	
	@Override
	public void update() {
		if (isRumbling) {
			
			if (times == -1 || myTimes < times) {
			
				// left side rumbles during the first half of the period
				// and the right side rumbles during the second half
				isRumblingLeft = (timer.getElapsedSeconds() % period < period/2);
				
				if (isRumblingLeft && !prevIsRumblingLeft) myTimes++;
				
				if (isRumblingLeft) {
					leftRumble = rumble;
					rightRumble = 0;
				}
				else {
					rightRumble = rumble;
					leftRumble = 0;
				}
				
			}
			else {
				leftRumble = 0;
				rightRumble = 0;
			}
		}
		else {
			leftRumble = 0;
			rightRumble = 0;
		}
		
		prevIsRumblingLeft = isRumblingLeft;
	}
	
	// set times to -1 to continue indefinitely
	public void rumble(double rumbleAmount, double periodInSeconds, int times) {
		this.rumble = rumbleAmount;
		period = periodInSeconds;
		this.times = times;
		
		myTimes = 0;
		isRumblingLeft = true;
		prevIsRumblingLeft = true;
		isRumbling = true;
		timer.restart();
	}
	
	public void cancel() {
		leftRumble = 0;
		rightRumble = 0;
		myTimes = times;
		isRumbling = false;
	}

}
