package main.org.usfirst.frc.team1640.robot.traversal.steer;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.drivetrain.pivot.IPivot;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class DefaultSteer implements SteerStrategy {
	private IPivot flPivot, frPivot, blPivot, brPivot;
	private double flDirection, frDirection, blDirection, brDirection;
	
	public DefaultSteer(IDriveTrain driveTrain) {	
		flPivot = driveTrain.getFLPivot();
		frPivot = driveTrain.getFRPivot();
		blPivot = driveTrain.getBLPivot();
		brPivot = driveTrain.getBRPivot();
	}

	@Override
	public void init() {
		flDirection = flPivot.getAngle();
		frDirection = frPivot.getAngle();
		blDirection = blPivot.getAngle();
		brDirection = brPivot.getAngle();
	}

	@Override
	public void execute() {
		flPivot.setTargetAngle(flDirection);
		frPivot.setTargetAngle(frDirection);
		blPivot.setTargetAngle(blDirection);
		brPivot.setTargetAngle(brDirection);
	}

	@Override
	public void end() {
		flPivot.setTargetAngle(flPivot.getAngle());
		frPivot.setTargetAngle(frPivot.getAngle());
		blPivot.setTargetAngle(blPivot.getAngle());
		brPivot.setTargetAngle(brPivot.getAngle());
	}

	@Override
	public void setFLDirection(double direction) {
		flDirection = direction;
	}

	@Override
	public void setFRDirection(double direction) {
		frDirection = direction;
	}

	@Override
	public void setBLDirection(double direction) {
		blDirection = direction;
	}

	@Override
	public void setBRDirection(double direction) {
		brDirection = direction;
	}
}
