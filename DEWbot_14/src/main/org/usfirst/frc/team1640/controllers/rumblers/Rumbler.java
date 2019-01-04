package main.org.usfirst.frc.team1640.controllers.rumblers;

public abstract class Rumbler {
	protected double leftRumble;
	protected double rightRumble;
	
	public double getLeftRumble() {
		return leftRumble;
	}
	
	public double getRightRumble() {
		return rightRumble;
	}
	
	public abstract void update();
	
	public abstract void cancel();
}
