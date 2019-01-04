package main.org.usfirst.frc.team1640.robot.traversal.drive;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.traversal.transmission.DefaultTransmission;
import main.org.usfirst.frc.team1640.robot.traversal.transmission.TransmissionStrategy;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class ManualShiftingDrive implements DriveStrategy {
	private DriveStrategy driveStrat;
	private TransmissionStrategy transmissionStrat;
	
	public ManualShiftingDrive(IDriveTrain driveTrain) {
		driveStrat = new DefaultDrive(driveTrain);
		transmissionStrat = new DefaultTransmission(driveTrain);
	}
	
	@Override
	public void init() {
		driveStrat.init();
		transmissionStrat.init();
	}

	@Override
	public void execute() {
		driveStrat.execute();
		transmissionStrat.execute();
	}

	@Override
	public void end() {
	}

	@Override
	public void setFLDrive(double drive) {
		driveStrat.setFLDrive(drive);
	}

	@Override
	public void setFRDrive(double drive) {
		driveStrat.setFRDrive(drive);
	}

	@Override
	public void setBLDrive(double drive) {
		driveStrat.setBLDrive(drive);
	}

	@Override
	public void setBRDrive(double drive) {
		driveStrat.setBRDrive(drive);
	}
	
	@Override
	public void setControlMode(ControlMode mode) {
		driveStrat.setControlMode(mode);
	}
	
	public void setTransmission(double transmission) {
		transmissionStrat.setFLTransmission(transmission);
		transmissionStrat.setFRTransmission(transmission);
		transmissionStrat.setBLTransmission(transmission);
		transmissionStrat.setBRTransmission(transmission);
	}

}
