package main.org.usfirst.frc.team1640.drivetrain.cvtpivot;

import main.org.usfirst.frc.team1640.drivetrain.pivot.Pivot;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

import edu.wpi.first.wpilibj.Servo;

public class CVTPivot extends Pivot implements ICVTPivot {
	private Servo servo;
	private final int kNormalAngle = 120;
	private final int kMinAngle = 50;
	private final int kMaxAngle = 170;

	public CVTPivot(int driveChannel, int steerChannel, int resolverChannel, int servoChannel, double minVoltage, double maxVoltage, double offset, String name) {
		super(driveChannel, steerChannel, resolverChannel, minVoltage, maxVoltage, offset, name);
		
		servo = new Servo(servoChannel);
	}
	
	// TODO change this method so it sets transmission to a percentage of the max angle
	public void setTransmission(double transmission) {
		transmission = MathUtilities.constrain(transmission, -1.0, 1.0);
		double angle = 0;
		if (transmission < 0) {
			angle = transmission*Math.abs(kNormalAngle-kMinAngle);
		}
		else {
			angle = transmission*Math.abs(kMaxAngle - kNormalAngle);
		}
		servo.setAngle(kNormalAngle + angle);
	}
	
	public double getTransmission() {
		return servo.getAngle();
	}
}
