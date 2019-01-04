package main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.steer.SteerStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class PointWheels implements AutonCommand {
	protected IDriveTrain driveTrain;
	protected double direction;
	
	private SteerStrategy steerStrat;
	private ElapsedTimer timer;
	
	private boolean isInit;
	private boolean isDone;
	private boolean hasEnded;
	
	private final double kAngleBuffer = 4;
	private final double kTimeoutInSeconds = 0.5;
	
	public PointWheels(IDriveTrain driveTrain, SteerStrategy steerStrat, double direction) {
		this.driveTrain = driveTrain;
		this.direction = direction;
		
		this.steerStrat = steerStrat;
		timer = new ElapsedTimer();
		
		isInit = false;
		isDone = false;
		hasEnded = false;
	}

	@Override
	public void execute() {
		if (!isInit) {
			initialize();
		}
		
		if (!isDone) {
			steerStrat.setFLDirection(direction);
			steerStrat.setFRDirection(direction);
			steerStrat.setBLDirection(direction);
			steerStrat.setBRDirection(direction);
			steerStrat.execute();
			
			boolean timeout = timer.getElapsedSeconds() > kTimeoutInSeconds;
			
			isDone = areAllPivotsFinished() || timeout;
		}
		else if (!hasEnded){
			steerStrat.end();
			hasEnded = true;
		}
	}
	
	private void initialize() {
		steerStrat.init();
		isInit = true;
		timer.start();
	}
	
	private boolean checkPivotWithinBuffer(double pivotAngle) {
		boolean flipped = Math.abs(MathUtilities.shortestAngleBetween((pivotAngle+180)%360, direction)) < kAngleBuffer;
		boolean notFlipped = Math.abs(MathUtilities.shortestAngleBetween(pivotAngle, direction)) < kAngleBuffer;
		return flipped || notFlipped;
	}
	
	private boolean areAllPivotsFinished() {
		boolean flReady = checkPivotWithinBuffer(driveTrain.getFLPivot().getAngle());
		boolean frReady = checkPivotWithinBuffer(driveTrain.getFRPivot().getAngle());
		boolean blReady = checkPivotWithinBuffer(driveTrain.getBLPivot().getAngle());
		boolean brReady = checkPivotWithinBuffer(driveTrain.getBRPivot().getAngle());
		
		return flReady && frReady && blReady && brReady;
	}

	@Override
	public boolean isRunning() {
		return !isDone;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void reset() {
		isInit = false;
		isDone = false;
		hasEnded = false;
	}

}
