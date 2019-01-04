package main.org.usfirst.frc.team1640.robot.traversal.ocelot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import main.org.usfirst.frc.team1640.sensors.gyroscope.Gyro;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class AntiTip implements OcelotStrategy {
	
	private ControlMode controlMode;
	private double longitudinal;
	private double axial;
	private double lateral;
	private DefaultOcelot ocelot;
	private IGyro gyro;
	private final double FRONT_TIPPING_ANGLE = 7.0;
	private final double SIDE_TIPPING_ANGLE = 7.0;
	//X-Axis is side tipping, Y-Axis is front tipping
	//X is pitch and Y is roll
	public AntiTip(DefaultOcelot ocelot, IGyro gyro) {
		this.ocelot = ocelot;
		this.gyro = gyro;
	}

	@Override
	public void setLateralDrive(double drive) {
		lateral = drive;
	}

	@Override
	public void setControlMode(ControlMode control) {
		controlMode = control;
	}

	@Override
	public void init() {
		ocelot.init();
	}

	@Override
	public void execute() {
		
		if (isSideTipping()) {
			ocelot.setAxialDrive(0);
			ocelot.setLongitudinalDrive(0);
			double lateral = gyro.getPitch() > 0 ? -1 : 1;
			ocelot.setLateralDrive(lateral);
			ocelot.setControlMode(ControlMode.PercentOutput);
			System.out.println("Anti tip side engaged. Lateral: " + lateral);
		}
		
		else if (isFrontTipping()) {
			ocelot.setAxialDrive(0);
			ocelot.setLateralDrive(0);
			double longitudinal = gyro.getRoll() > 0 ? 1 : -1;
			ocelot.setLongitudinalDrive(longitudinal);
			ocelot.setControlMode(ControlMode.PercentOutput);
			System.out.println("Anti tip front engaged. Long: " + longitudinal);
		}
		
		else {
			ocelot.setLateralDrive(lateral);
			ocelot.setAxialDrive(axial);
			ocelot.setLongitudinalDrive(longitudinal);
			ocelot.setControlMode(controlMode);
		}
		ocelot.execute();
	}

	@Override
	public void end() {
		ocelot.end();
	}

	@Override
	public void setLongitudinalDrive(double drive) {
		longitudinal = drive;
	}

	@Override
	public void setAxialDrive(double drive) {
		axial = drive;
	}
	
	private boolean isFrontTipping() {
		return Math.abs(gyro.getRoll()) > FRONT_TIPPING_ANGLE;
	}
	private boolean isSideTipping() {
		return Math.abs(gyro.getPitch()) > SIDE_TIPPING_ANGLE;
	}

}
