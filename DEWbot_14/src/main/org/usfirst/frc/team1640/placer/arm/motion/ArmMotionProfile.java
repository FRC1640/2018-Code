package main.org.usfirst.frc.team1640.placer.arm.motion;

import main.org.usfirst.frc.team1640.sensors.ISensorSet;

public class ArmMotionProfile extends ArmMotion {

	public ArmMotionProfile(ISensorSet sensorSet) {
		super(sensorSet);
	}
	
	@Override
	public double getAngle() {
		//Returns current angle of arm
		
		return 0;
	}

	@Override
	public void execute() {
		
	}
}
