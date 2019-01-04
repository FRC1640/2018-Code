package main.org.usfirst.frc.team1640.robot.auton.commands.drive.ocelotto;

import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.Vector;
import main.org.usfirst.frc.team1640.utilities.curves.TrapezoidalCurve;

public class TrapezoidalOcelotTo extends OcelotTo {
	private IGyro gyro;
	private TrapezoidalCurve curve;
	private double angle;

	public TrapezoidalOcelotTo(FieldCentricOcelot ocelotStrat, IGyro gyro,
			Vector distanceInInches, double angleInDegrees) {
		super(ocelotStrat, gyro, distanceInInches, angleInDegrees);
		this.gyro = gyro;
		
		curve = new TrapezoidalCurve(2, -2.5, 1.0, 0.5, 0.24, 0);
		
		angle = angleInDegrees;
	}
	
	public void execute() {
		super.execute();
		
		double progress = Math.abs(MathUtilities.shortestAngleBetween(gyro.getYaw(), goalAngle)/angle);
		if (progress < 1) {
			kP = curve.getY(progress);
		}
		else {
			kP = 0;
		}
	}
	
	@Override
	protected void init() {
		kP = curve.getY(0);
	}

}
