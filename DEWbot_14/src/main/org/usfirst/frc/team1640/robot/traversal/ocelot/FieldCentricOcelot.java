package main.org.usfirst.frc.team1640.robot.traversal.ocelot;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class FieldCentricOcelot implements OcelotStrategy {
	private OcelotStrategy ocelotStrat;
	private IGyro gyro;
	private double lateralDrive, longitudinalDrive, axialDrive;
	
	public FieldCentricOcelot(IDriveTrain driveTrain, IGyro gyro, OcelotStrategy ocelotStrat) {
		this.gyro = gyro;
		
		this.ocelotStrat = ocelotStrat;
		
		lateralDrive = 0.0;
		longitudinalDrive = 0.0;
		axialDrive = 0.0;
	}
	
	public void init() {
		ocelotStrat.init();
	}
	
	public void execute() {
//		double temp = lateralDrive*Math.cos(Math.toRadians(gyro.getYaw())) + longitudinalDrive*Math.sin(Math.toRadians(gyro.getYaw()));
//		longitudinalDrive =  -lateralDrive*Math.sin(Math.toRadians(gyro.getYaw())) + longitudinalDrive*Math.cos(Math.toRadians(gyro.getYaw()));
//		lateralDrive = temp;
		
		//potential fix for oceloting in auton
		double temp = this.lateralDrive*Math.cos(Math.toRadians(gyro.getYaw())) + this.longitudinalDrive*Math.sin(Math.toRadians(gyro.getYaw()));
		double longitudinalDrive =  -lateralDrive*Math.sin(Math.toRadians(gyro.getYaw())) + this.longitudinalDrive*Math.cos(Math.toRadians(gyro.getYaw()));
		double lateralDrive = temp;

		ocelotStrat.setLateralDrive(lateralDrive);
		ocelotStrat.setLongitudinalDrive(longitudinalDrive);
		ocelotStrat.setAxialDrive(axialDrive);
		ocelotStrat.execute();
	}
	
	public void end() {
		ocelotStrat.end();
	}
	
	public void setOcelot(double lateralDrive, double longitudinalDrive, double axialDrive) {
		this.lateralDrive = lateralDrive;
		this.longitudinalDrive = longitudinalDrive;
		this.axialDrive = axialDrive;
	}

	@Override
	public void setLateralDrive(double drive) {
		lateralDrive = drive;
	}

	@Override
	public void setLongitudinalDrive(double drive) {
		longitudinalDrive = drive;
	}

	@Override
	public void setAxialDrive(double drive) {
		axialDrive = drive;
	}

	@Override
	public void setControlMode(ControlMode control) {
		ocelotStrat.setControlMode(control);
	}
	
	public void setOcelotStrategy(OcelotStrategy ocelotStrat) {
		this.ocelotStrat.end();
		this.ocelotStrat = ocelotStrat;
		this.ocelotStrat.init();
	}
}
