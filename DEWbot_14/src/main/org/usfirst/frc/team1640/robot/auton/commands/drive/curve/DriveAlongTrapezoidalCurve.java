package main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.curves.TrapezoidalCurve;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class DriveAlongTrapezoidalCurve implements AutonCommand {
	private IDriveTrain driveTrain;
	private LinearStrategy linearStrat;
	private double distanceInInches;
	private double angle;
	private double maxDrive;
	
	private boolean initialized;
	private boolean done;
	private TrapezoidalCurve curve;
	
	private final double kSlopeUp = 0.5, kSlopeDown = 0.005; //0.01105836205568892402188508946708;
	private final double kMinDrive = 0.23;
	
	public DriveAlongTrapezoidalCurve(IDriveTrain driveTrain, LinearStrategy linearStrat, double distanceInInches, double angle, double maxDrive){
		this.driveTrain = driveTrain;
		this.linearStrat = linearStrat;
		this.distanceInInches = distanceInInches;
		if (maxDrive < 0) {
			this.angle = (angle + 180)%360;
		}
		else {
			this.angle = angle;
		}
		this.maxDrive = Math.abs(maxDrive);
		
		curve = new TrapezoidalCurve(kSlopeUp, kSlopeDown, distanceInInches, this.maxDrive, kMinDrive, kMinDrive);
		
		initialized = false;
		done = false;
	}
	
	public DriveAlongTrapezoidalCurve(IDriveTrain driveTrain, LinearStrategy linearStrat, double distanceInInches, double angle, double maxDrive, double slopeUp, double slopeDown){
		this(driveTrain, linearStrat, distanceInInches, angle, maxDrive);
		curve = new TrapezoidalCurve(slopeUp, slopeDown, distanceInInches, this.maxDrive, kMinDrive, kMinDrive);

	}

	@Override
	public void execute() {
		
		if(!initialized){
			driveTrain.resetPositions();
			linearStrat.init();
			initialized = true;
		}
		if(!done) {
			double drive = curve.getY(getPosition());
			if(Math.abs(drive) < Math.abs(kMinDrive)){
				drive = kMinDrive;
			}
			if(getPosition() > distanceInInches) {
				linearStrat.setLateralDrive(0);
				linearStrat.setLongitudinalDrive(0);
				linearStrat.execute();
//				linearStrat.end();
				done = true;
			}
			else {
				linearStrat.setLateralDrive(MathUtilities.xFromPolar(angle, drive));
				linearStrat.setLongitudinalDrive(MathUtilities.yFromPolar(angle, drive));
				linearStrat.execute();
			}
		}
	}
	
	private double getPosition(){
		return Math.abs(driveTrain.getPositionInches());
	}

	@Override
	public boolean isRunning() {
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void reset() {
		initialized = false;
		done = false;
	}

}
