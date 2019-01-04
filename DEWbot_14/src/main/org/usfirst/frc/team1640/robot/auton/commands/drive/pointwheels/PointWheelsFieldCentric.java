package main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.steer.SteerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class PointWheelsFieldCentric implements AutonCommand {
	private IDriveTrain driveTrain;
	private IGyro gyro;
	private double direction;
	
	private double flDirection;
	private double frDirection;
	private double blDirection;
	private double brDirection;
	
	private SteerStrategy steerStrat;
	private ElapsedTimer timer;
	
	private boolean isInit;
	private boolean isDone;
	private boolean hasEnded;
	
	private final double kAngleBuffer = 4;
	private final double kTimeoutInSeconds = 0.5;
	
	public PointWheelsFieldCentric(IDriveTrain driveTrain, IGyro gyro, SteerStrategy steerStrat, double direction) {
		this.driveTrain = driveTrain;
		this.gyro = gyro;
		this.direction = direction;
		
		this.steerStrat = steerStrat;
		timer = new ElapsedTimer();
		
		isInit = false;
		isDone = false;
		hasEnded = false;
	}
	
	public void execute() {
		if (!isInit) {
			initialize();
		}
		
		if (!isDone) {
			steerStrat.setFLDirection(flDirection);
			steerStrat.setFRDirection(frDirection);
			steerStrat.setBLDirection(blDirection);
			steerStrat.setBRDirection(brDirection);
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
		flDirection = direction-gyro.getYaw();
		frDirection = direction-gyro.getYaw();
		blDirection = direction-gyro.getYaw();
		brDirection = direction-gyro.getYaw();
		steerStrat.init();
		isInit = true;
		timer.start();
	}
	
	private boolean checkPivotWithinBuffer(double pivotAngle, double angle) {
		boolean flipped = Math.abs(MathUtilities.shortestAngleBetween((pivotAngle+180)%360, angle)) < kAngleBuffer;
		boolean notFlipped = Math.abs(MathUtilities.shortestAngleBetween(pivotAngle, angle)) < kAngleBuffer;
		return flipped || notFlipped;
	}
	
	private boolean areAllPivotsFinished() {
		boolean flReady = checkPivotWithinBuffer(driveTrain.getFLPivot().getAngle(), flDirection);
		boolean frReady = checkPivotWithinBuffer(driveTrain.getFRPivot().getAngle(), frDirection);
		boolean blReady = checkPivotWithinBuffer(driveTrain.getBLPivot().getAngle(), blDirection);
		boolean brReady = checkPivotWithinBuffer(driveTrain.getBRPivot().getAngle(), brDirection);
		
		return flReady && frReady && blReady && brReady;
	}

	public boolean isRunning() {
		return !isDone;
	}

	public boolean isInitialized() {
		return true;
	}

	public void reset() {
		isInit = false;
		isDone = false;
		hasEnded = false;
	}

}
