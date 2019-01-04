package main.org.usfirst.frc.team1640.robot.traversal.drive;

import java.util.ArrayList;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.traversal.transmission.DefaultTransmission;
import main.org.usfirst.frc.team1640.robot.traversal.transmission.TransmissionStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class ControllerShiftingDrive implements DriveStrategy {
	private IDriveTrain driveTrain;
	
	private DriveStrategy driveStrat;
	private TransmissionStrategy transmissionStrat;
	private double flDrive, frDrive, blDrive, brDrive;
	
	private final double kMaxMotor = (1.0/3);
	private final double kMaxServo = (2.0/3);
	private final double kIdealMotor = 0.5;
	private final double kMinThreshold = 45;
	private final double kMinCurrentThreshold = 10;
	private final int kMinSpeedThreshold = 10000;
	
	private ArrayList<Double> minVelocities;
	
	public ControllerShiftingDrive(IDriveTrain driveTrain) {
		this.driveTrain = driveTrain;
		driveStrat = new DefaultDrive(driveTrain);
		transmissionStrat = new DefaultTransmission(driveTrain);
		
		minVelocities = new ArrayList<Double>();
	}
	
	@Override
	public void init() {
		driveStrat.init();
		transmissionStrat.init();
	}

	@Override
	public void execute() {
		
//		System.out.println("*FL* Drive: " + calculateMotorSpeed(flDrive) + ", CVT: " + calculateServoAngle(flDrive) + ", Speed: " + driveTrain.getFLPivot().getVelocity());
//		System.out.println("*FR* Drive: " + calculateMotorSpeed(frDrive) + ", CVT: " + calculateServoAngle(frDrive) + ", Speed: " + driveTrain.getFRPivot().getVelocity());
//		System.out.println("*BL* Drive: " + calculateMotorSpeed(blDrive) + ", CVT: " + calculateServoAngle(blDrive) + ", Speed: " + driveTrain.getBLPivot().getVelocity());
//		System.out.println("*BR* Drive: " + calculateMotorSpeed(brDrive) + ", CVT: " + calculateServoAngle(brDrive) + ", Speed: " + driveTrain.getBRPivot().getVelocity());
		
		driveStrat.setFLDrive(calculateMotorSpeed(flDrive));
		driveStrat.setFRDrive(calculateMotorSpeed(frDrive));	
		driveStrat.setBLDrive(calculateMotorSpeed(blDrive));
		driveStrat.setBRDrive(calculateMotorSpeed(brDrive));
		
		transmissionStrat.setFLTransmission(calculateServoAngle(flDrive));
		transmissionStrat.setFRTransmission(calculateServoAngle(frDrive));
		transmissionStrat.setBLTransmission(calculateServoAngle(blDrive));
		transmissionStrat.setBRTransmission(calculateServoAngle(brDrive));
		
		driveStrat.execute();
		transmissionStrat.execute();
	}

	@Override
	public void end() {
		driveStrat.end();
		transmissionStrat.end();
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
		driveStrat.setControlMode(mode);;
	}
	
	private double calculateServoAngle(double drive) {		
		drive = Math.abs(drive);
		// if(getCurrent() > MIN_CURRENT_THRESHOLD){
		
		//TODO reimplement minspeed
		//TODO get this working
		if (getMinSpeed() < kMinSpeedThreshold) {
			return 0.0;
		}
		if (drive <= kMaxMotor)
			return 0.0;
//			return kNormalAngle;
		else if (drive >= kMaxServo)
			return -1.0;
//			return kMaxAngle;
		else
			//TODO make this readable
			return -(drive - kMaxMotor)/(kMaxServo - kMaxMotor);
//			return (drive - kMaxMotor) * (kMaxAngle - kNormalAngle)
//					/ (kMaxServo - kMaxMotor) + kNormalAngle;
	}
	
	private double calculateMotorSpeed(double drive){
		double direction = Math.signum(drive);
		drive = Math.abs(drive);
		if(drive <= kMaxMotor)
			return direction*drive*(kIdealMotor/kMaxMotor);
		else if(drive <= kMaxServo)
			return direction*kIdealMotor;
		else 
			return (drive-kMaxServo)*kIdealMotor/(1-kMaxServo) + direction*kIdealMotor;
	}
	
	private double getMinSpeed() {
		minVelocities.clear();
		
		minVelocities.add(Math.abs(driveTrain.getFLPivot().getVelocity()));
		minVelocities.add(Math.abs(driveTrain.getFRPivot().getVelocity()));
		minVelocities.add(Math.abs(driveTrain.getBLPivot().getVelocity()));
		minVelocities.add(Math.abs(driveTrain.getBRPivot().getVelocity()));
		
		for (int i = 0; i < minVelocities.size(); i++) {
			if (minVelocities.get(i).equals(0.0)) {
				minVelocities.remove(i);
			}
		}
		
		double min;
		if (minVelocities.size() > 0) {
			min = minVelocities.get(0);
			for (Double vel : minVelocities) {
				if (vel < min) {
					min = vel;
				}
			}
		}
		else {
			min = 0;
		}
		
		return min;
	}

}
