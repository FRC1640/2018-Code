package main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.curves.TrapezoidalCurve;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class DriveAlongTrapezoidalCurveAndTimeout implements AutonCommand {
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
	
	private ElapsedTimer timer;
	private double timeoutInSeconds;
	
	public DriveAlongTrapezoidalCurveAndTimeout(IDriveTrain driveTrain, LinearStrategy linearStrat, double distanceInInches, double angle, double maxDrive, double timeoutInSeconds){
		this.driveTrain = driveTrain;
		this.linearStrat = linearStrat;
		this.distanceInInches = distanceInInches;
		this.timeoutInSeconds = timeoutInSeconds;
		if (maxDrive < 0) {
			this.angle = (angle + 180)%360;
		}
		else {
			this.angle = angle;
		}
		this.maxDrive = Math.abs(maxDrive);
		
		curve = new TrapezoidalCurve(kSlopeUp, kSlopeDown, distanceInInches, this.maxDrive, kMinDrive, kMinDrive);
		
		timer = new ElapsedTimer();
		
		initialized = false;
		done = false;
	}

	@Override
	public void execute() {
		
		if(!initialized){
			driveTrain.resetPositions();
			linearStrat.init();
			initialized = true;
			timer.restart();
		}
		if(!done) {
			double drive = curve.getY(getPosition());
			if(Math.abs(drive) < Math.abs(kMinDrive)){
				drive = kMinDrive;
			}
			if(getPosition() > distanceInInches || timer.getElapsedSeconds() > timeoutInSeconds){
				linearStrat.setLateralDrive(0);
				linearStrat.setLongitudinalDrive(0);
				linearStrat.execute();
				linearStrat.end();
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
