package main.org.usfirst.frc.team1640.placer.lift.motion;

import com.ctre.phoenix.motorcontrol.ControlMode;

import main.org.usfirst.frc.team1640.utilities.MathUtilities;

public class LiftPercentOutput extends LiftMotion{
	private double speed;
	private final double REDUCTION = 0.5;
	
	@Override
	public void setLiftHeight(double speed){
		this.speed = MathUtilities.constrain(speed, -1, 1);
	}

	@Override
	public void init() {
		speed = 0;
	}

	@Override
	public void execute() {
//		System.out.println("po: " + speed * REDUCTION);
		liftMotor.set(ControlMode.PercentOutput, speed * REDUCTION);
	}
	
	@Override
	public boolean isDone(){
		return speed == 0;
	}

}
