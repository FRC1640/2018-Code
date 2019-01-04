package main.org.usfirst.frc.team1640.robot.traversal.sunflower;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.OcelotStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.PIDOutputDouble;
import main.org.usfirst.frc.team1640.utilities.PIDSourceDouble;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.PIDController;

public class ShortestAngleSunflower implements SunflowerStrategy {
	private IGyro gyro;
	
	private OcelotStrategy ocelotStrat;
	private double direction;
	private boolean acceptNewDirections;
	private PIDSourceDouble shortestAngleSource;
	private PIDOutputDouble axialDriveOutput;
	private PIDController gyroPID;
	

	public ShortestAngleSunflower(IDriveTrain driveTrain, IGyro gyro, OcelotStrategy ocelotStrat, double kP,
			double kI, double kD, double driveRange) {
		this.gyro = gyro;
		
		this.ocelotStrat = ocelotStrat;
		axialDriveOutput = new PIDOutputDouble();
		shortestAngleSource = new PIDSourceDouble();
		final double kPeriod = 0.02;
		gyroPID = new PIDController(kP, kI, kD, shortestAngleSource, axialDriveOutput, kPeriod);
		gyroPID.setOutputRange(-Math.abs(driveRange), Math.abs(driveRange));
	}

	@Override
	public void init() {
		ocelotStrat.init();
		direction = gyro.getYaw();
		
		gyroPID.enable();
		gyroPID.setSetpoint(0.0);
	}

	@Override
	public void execute() {
		if (gyro.hasRecentlyOffset()) {
			System.out.println("Gyro reset in shortest angle");
			direction = gyro.getYaw();
		}
		
		shortestAngleSource.setValue(MathUtilities.shortestAngleBetween(gyro.getYaw(), direction));
		gyroPID.setSetpoint(0.0);
	
		if (acceptNewDirections) {
//			System.out.println("axial drive:" + axialDriveOutput.getValue() + " dir: " + shortestAngleSource.pidGet());
			ocelotStrat.setAxialDrive(axialDriveOutput.getValue());
		}
		else {
			ocelotStrat.setAxialDrive(0.0);
		}
		ocelotStrat.execute();
	}

	@Override
	public void end() {
		System.out.println("strategy has ended");
		gyroPID.reset();
		ocelotStrat.end();
	}

	@Override
	public void setDirection(double direction) {
		this.direction = direction;
	}

	@Override
	public void setLateralDrive(double drive) {
		ocelotStrat.setLateralDrive(drive);
	}

	@Override
	public void setLongitudinalDrive(double drive) {
		ocelotStrat.setLongitudinalDrive(drive);
//		System.out.println("long: " + drive);
	}

	@Override
	public void setControlMode(ControlMode control) {
		ocelotStrat.setControlMode(control);
	}

	@Override
	public void acceptNewDirections(boolean acceptNewDirections) {
		this.acceptNewDirections = acceptNewDirections;
	}
	
	public void setOcelotStrategy(OcelotStrategy ocelotStrat) {
		this.ocelotStrat.end();
		this.ocelotStrat = ocelotStrat;
		this.ocelotStrat.init();
	}
	
	public boolean atDirection() {
		return Math.abs(gyroPID.get()) < 0.1;
	}

}
