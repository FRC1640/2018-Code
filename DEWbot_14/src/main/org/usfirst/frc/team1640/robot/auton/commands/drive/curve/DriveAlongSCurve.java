package main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.curves.SCurve;
import main.org.usfirst.frc.team1640.utilities.curves.TrapezoidalCurve;

public class DriveAlongSCurve implements AutonCommand {
	private IDriveTrain driveTrain;
	private LinearStrategy linearStrat;
	private double distanceInInches;
	private double angle;
	private double maxDrive;
	
	private boolean initialized;
	private boolean done;
	private SCurve curve;
	
	private final double kMinDrive = 0.23;
	private final double kPercentX1 = 0.1;
	private final double kPercentX2 = 0.3;
	
	public DriveAlongSCurve(IDriveTrain driveTrain, LinearStrategy linearStrat, double distanceInInches, double angle, double maxDrive){
		this.driveTrain = driveTrain;
		this.linearStrat = linearStrat;
		this.distanceInInches = distanceInInches;
		this.angle = angle;
		
		curve = new SCurve(distanceInInches, kMinDrive, maxDrive, kPercentX1, kPercentX2);
		
		initialized = false;
		done = false;
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
			linearStrat.setLateralDrive(MathUtilities.xFromPolar(angle, drive));
			linearStrat.setLongitudinalDrive(MathUtilities.yFromPolar(angle, drive));
			linearStrat.execute();
		}
		if(getPosition() > distanceInInches){
			linearStrat.setLateralDrive(MathUtilities.xFromPolar(angle, 0.0));
			linearStrat.setLongitudinalDrive(MathUtilities.yFromPolar(angle, 0.0));
			linearStrat.execute();
			linearStrat.end();
			done = true;
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
