package main.org.usfirst.frc.team1640.controllers.rumblers;

public class DefaultRumbler extends Rumbler {
	
	DefaultRumbler() {
		
	}
	
	public void setLeftRumble(double rumble) {
		leftRumble = rumble;
	}
	
	public void setRightRumble(double rumble) {
		rightRumble = rumble;
	}

	@Override
	public void update() {
	}
	
	public void cancel() {
		leftRumble = 0;
		rightRumble = 0;
	}
	
}
