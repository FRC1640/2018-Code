package main.org.usfirst.frc.team1640.robot.traversal.ocelot;

import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.sensors.gyroscope.Gyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.PIDOutputDouble;
import main.org.usfirst.frc.team1640.utilities.PIDSourceDouble;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.PIDController;

public class GyroCorrection implements OcelotStrategy {
	private OcelotStrategy ocelot;
	private IGyro gyro;
	
	private double lateralDrive, longitudinalDrive, axialDrive;
	private double prevAxialDrive;
	
	private PIDSourceDouble shortestAngle;
	private PIDOutputDouble correctedAxialDrive;
	private PIDController gyroPID;
	private double gyroSetpoint;
	private ElapsedTimer timer;
	private int kTurnDelayInMilliseconds = 400;
	private double kP, kI, kD;
	private double driveRange;
	
	public GyroCorrection(OcelotStrategy ocelot, Gyro gyro, double kP, double kI, double kD, double driveRange) {
		this.ocelot = ocelot;
		this.gyro = gyro;
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.driveRange = driveRange;
		
		lateralDrive = 0;
		longitudinalDrive = 0;
		axialDrive = 0;
	}
	
	public void init() {
		ocelot.init();
		
		shortestAngle = new PIDSourceDouble();
		correctedAxialDrive = new PIDOutputDouble();
		final double kPeriod = 0.02;
		gyroPID = new PIDController(kP, kI, kD, shortestAngle, correctedAxialDrive, kPeriod);
		gyroPID.setOutputRange(-Math.abs(driveRange), Math.abs(driveRange));
		gyroPID.enable();
		
		gyroSetpoint = gyro.getYaw();
	}
	
	public void end() {
		gyroPID.reset();
		ocelot.end();
	}
	
	public void execute() {
		double speed = axialDrive;
		if (axialDrive == 0) {
			if (timer.getElapsedMilliseconds() < kTurnDelayInMilliseconds) {
				gyroSetpoint = gyro.getYaw();
				speed = 0;
			}
			else {
				shortestAngle.setValue(MathUtilities.shortestAngleBetween(gyro.getYaw(), gyroSetpoint));
				speed = calcSpeed();
			}
		}
		else {
			timer.restart(); // restart the timer
		}
		ocelot.setLateralDrive(lateralDrive);
		ocelot.setLongitudinalDrive(longitudinalDrive);
		ocelot.setAxialDrive(speed);
		ocelot.execute();
		
		prevAxialDrive = axialDrive;
	}
	
	private double calcSpeed() {
		double buffer = 0.1;
		
		if (lateralDrive < 0.2 && longitudinalDrive < 0.2) {
			buffer = 0.01;
		}
		else {
			buffer = 0.005;
		}
		
		return (Math.abs(correctedAxialDrive.getValue()) < buffer ? axialDrive : correctedAxialDrive.getValue());
	}
	
	public void setStrategy(OcelotStrategy ocelot) {
		this.ocelot = ocelot;
	}
	
	public void setLateralDrive(double drive) {
		lateralDrive = drive;
	}
	
	public void setLongitudinalDrive(double drive) {
		longitudinalDrive = drive;
	}
	
	public void setAxialDrive(double drive) {
		axialDrive = drive;
	}

	@Override
	public void setControlMode(ControlMode control) {
		ocelot.setControlMode(control);
	}
}
