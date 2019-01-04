package main.org.usfirst.frc.team1640.robot.auton.commands.turn;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.directional.DirectionalStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class DelayedTurnAndMaintainDirection implements AutonCommand {
	private DirectionalStrategy directionalStrat;
	protected IGyro gyro;
	protected double direction;
	private double angleBuffer;
	private double secondsInBuffer;
	private double timeoutInSeconds;
	
	protected boolean isInit;
	protected boolean isDone;
	private boolean prevIsInBuffer;
	
	private ElapsedTimer timer;
	private ElapsedTimer timeoutTimer;
	private double encCounts;
	private IDriveTrain driveTrain;
	
	private double initialAngle;
	private double delayedInches;
	
	public DelayedTurnAndMaintainDirection(IDriveTrain driveTrain, DirectionalStrategy directionalStrat, IGyro gyro, double direction, double angleBuffer, double secondsInBuffer, double encCountsToMaintain, double initialAngle, double delayedInches) {
		this.directionalStrat = directionalStrat;
		this.gyro = gyro;
		this.direction = direction;
		this.angleBuffer = angleBuffer;
		this.secondsInBuffer = secondsInBuffer;
		this.driveTrain = driveTrain;
		encCounts = encCountsToMaintain;
		isInit = false;
		isDone = false;
		prevIsInBuffer = false;
		
		
		timer = new ElapsedTimer();
		timeoutTimer = new ElapsedTimer();
		
		this.initialAngle = initialAngle;
		this.delayedInches = delayedInches;
	}
	
	@Override
	public void execute() {
		if (!isDone) {
			// Initialize the directionalStrat on first iteration
			if (!isInit) {
				directionalStrat.init();
				isInit = true;
				timeoutTimer.start();
				directionalStrat.setDirection(initialAngle);
			}
			
			boolean isInBuffer = Math.abs(MathUtilities.shortestAngleBetween(gyro.getYaw(), direction))
					< angleBuffer;
						
			if(Math.abs(driveTrain.getPositionInches()) > delayedInches){
				directionalStrat.setDirection(direction);
			}
			directionalStrat.acceptNewDirections(true);
			directionalStrat.execute();
			
			if(Math.abs(driveTrain.getPositionInches()) > encCounts){
				isDone = true;
			}
			
			prevIsInBuffer = isInBuffer;
		}
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
		prevIsInBuffer = false;
	}

}
