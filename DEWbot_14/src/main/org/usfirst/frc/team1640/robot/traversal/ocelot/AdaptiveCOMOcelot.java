package main.org.usfirst.frc.team1640.robot.traversal.ocelot;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;
import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DriveStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class AdaptiveCOMOcelot implements OcelotStrategy {
	
	private OcelotStrategy ocelotStrat;
	private Placer placer;
	private double k;
	private double multiplier;
	private double height;
	
	private double lateralDrive, longitudinalDrive, axialDrive;
	private double prevHeight;
	
	public AdaptiveCOMOcelot(OcelotStrategy ocelotStrat, Placer placer) {
		this.ocelotStrat = ocelotStrat;
		this.placer = placer;
		k = PlacerConstants.GROUND_TO_LIFT;
		multiplier = 0.4;
		height = k;
	}

	@Override
	public void setControlMode(ControlMode control) {
		ocelotStrat.setControlMode(control);
	}

	@Override
	public void init() {
		ocelotStrat.init();
	}

	@Override
	public void execute() {
		height = placer.getLiftHeight();
		if (height < 0) {
			height = 0;
		}
		
		if(height - prevHeight < -2) {
			System.out.println("Going down. Turning off COM");
			height = 0;
		}
		
		ocelotStrat.setLateralDrive(adaptDrive(lateralDrive));
		ocelotStrat.setLongitudinalDrive(adaptDrive(longitudinalDrive));
		ocelotStrat.setAxialDrive(adaptDrive(axialDrive));
		ocelotStrat.execute();
		
		prevHeight = height;
	}

	@Override
	public void end() {
		ocelotStrat.end();
	}
	
	private double adaptDrive(double drive) {
		double m = k/(multiplier*height+k);
		m = MathUtilities.constrain(m, 0.0, 1.0);
		return m*drive;
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
}
