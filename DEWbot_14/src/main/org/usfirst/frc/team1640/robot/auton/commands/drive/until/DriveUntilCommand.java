package main.org.usfirst.frc.team1640.robot.auton.commands.drive.until;

import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

public abstract class DriveUntilCommand implements AutonCommand {
	private boolean initialized = false;
	private boolean done = false;
	protected double angle;
	private double drive;
	
	private LinearStrategy linearStrat;
	
	public DriveUntilCommand(LinearStrategy linearStrat, double angleInDegrees, double drive) {
		
		this.linearStrat = linearStrat;
		
		// make sure angle is a valid angle
		this.angle = angleInDegrees%360;
		
		// make sure drive is valid value
		this.drive = MathUtilities.constrain(drive, -1.0, 1.0);
	}

	@Override
	public void execute() {
		if(!initialized){
			initialized = true;
			done = false;
			linearStrat.init();
			initEndCondition();
		}
		if(!done){
			linearStrat.setLateralDrive(MathUtilities.xFromPolar(angle, drive));
			linearStrat.setLongitudinalDrive(MathUtilities.yFromPolar(angle, drive));
			linearStrat.execute();
//			System.out.println("driving");
		}
		if(isEndConditionMet()){
			done = true;
			linearStrat.setLateralDrive(0.0);
			linearStrat.setLongitudinalDrive(0.0);
			linearStrat.execute();
//			linearStrat.end();
		}
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
		initEndCondition();
	}
	
	public abstract void initEndCondition();
	
	public abstract boolean isEndConditionMet();

}
