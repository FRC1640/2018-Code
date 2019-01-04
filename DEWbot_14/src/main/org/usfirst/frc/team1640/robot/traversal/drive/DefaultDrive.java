package main.org.usfirst.frc.team1640.robot.traversal.drive;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.drivetrain.pivot.IPivot;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class DefaultDrive implements DriveStrategy {
	private IPivot flPivot, frPivot, blPivot, brPivot;
	private double flDrive, frDrive, blDrive, brDrive;
	private ControlMode controlMode;
	
	public DefaultDrive(IDriveTrain driveTrain) {
		
		flPivot = driveTrain.getFLPivot();
		frPivot = driveTrain.getFRPivot();
		blPivot = driveTrain.getBLPivot();
		brPivot = driveTrain.getBRPivot();
	}

	@Override
	public void init() {
	}

	@Override
	public void execute() {
		flPivot.setControlMode(controlMode);
		frPivot.setControlMode(controlMode);
		blPivot.setControlMode(controlMode);
		brPivot.setControlMode(controlMode);
		
		flPivot.setDrive(flDrive);
		frPivot.setDrive(frDrive);
		blPivot.setDrive(blDrive);
		brPivot.setDrive(brDrive);
	}

	@Override
	public void end() {
		flPivot.setDrive(0);
		frPivot.setDrive(0);
		blPivot.setDrive(0);
		brPivot.setDrive(0);
	}

	@Override
	public void setFLDrive(double drive) {
		flDrive = drive;
	}

	@Override
	public void setFRDrive(double drive) {
		frDrive = drive;
	}

	@Override
	public void setBLDrive(double drive) {
		blDrive = drive;
	}

	@Override
	public void setBRDrive(double drive) {
		brDrive = drive;
	}
	
	@Override
	public void setControlMode(ControlMode mode) {
		controlMode = mode;
	}
}
