package main.org.usfirst.frc.team1640.robot.auton.commands.drive.ocelotto;

import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.Vector;
import main.org.usfirst.frc.team1640.utilities.curves.ReverseAtEndCurve;
import main.org.usfirst.frc.team1640.utilities.curves.TrapezoidalCurve;

public class OcelotToReverseAtEnd extends OcelotTo {
	private IGyro gyro;
	private ReverseAtEndCurve curve;
	private double angle;
	
	private double progress;
	private double prevProgress;

	public OcelotToReverseAtEnd(FieldCentricOcelot ocelotStrat, IGyro gyro,
			Vector distanceInInches, double angleInDegrees) {
		super(ocelotStrat, gyro, distanceInInches, angleInDegrees);
		this.gyro = gyro;
		
		curve = new ReverseAtEndCurve(0, 1, 1.0, -1.0, 0.75);
		
		angle = angleInDegrees;
		
		progress = 0;
	}
	
	public void execute() {
		
		double progress = MathUtilities.constrain(1-Math.abs(MathUtilities.shortestAngleBetween(gyro.getYaw(), goalAngle)/angle), 0, 1);
		System.out.println("delta: " + Double.toString(progress - prevProgress));
		if (progress < 1 && progress - prevProgress > -0.001) {
			kP = curve.getY(progress);
		}
		else {
			kP = 0;
			isDone = true;
		}
		
		super.execute();
		
		prevProgress = progress;
	}
	
	@Override
	protected void init() {
		kP = curve.getY(0);
	}

}
