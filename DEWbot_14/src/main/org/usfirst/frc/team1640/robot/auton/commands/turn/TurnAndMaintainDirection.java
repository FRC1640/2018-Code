package main.org.usfirst.frc.team1640.robot.auton.commands.turn;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.directional.DirectionalStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class TurnAndMaintainDirection implements AutonCommand {
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
	
	public TurnAndMaintainDirection(IDriveTrain driveTrain, DirectionalStrategy directionalStrat, IGyro gyro, double direction, double angleBuffer, double secondsInBuffer, double encCountsToMaintain) {
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
	}
	
	@Override
	public void execute() {
		if (!isDone) {
			// Initialize the directionalStrat on first iteration
			if (!isInit) {
				directionalStrat.init();
				isInit = true;
				timeoutTimer.start();
			}
			
			boolean isInBuffer = Math.abs(MathUtilities.shortestAngleBetween(gyro.getYaw(), direction))
					< angleBuffer;
			
//			System.out.println("Angle: " + Math.abs(MathUtilities.shortestAngleBetween(gyro.getYaw(), direction)));
			
			directionalStrat.setDirection(direction);
			directionalStrat.acceptNewDirections(true);
			directionalStrat.execute();
			
//			if (isInBuffer) {
//				if (!prevIsInBuffer) { // if it just entered the angle buffer
//					timer.restart(); // start keeping track of how long it has been in the buffer
//				}
//				else if (timer.getElapsedSeconds() > secondsInBuffer && Math.abs(driveTrain.getPositionInches()) > encCounts) {
//					// if the robot has been pointing the right direction for long enough, we are done
//					System.out.println("Did not timeout");
////					directionalStrat.end();
//					isDone = true;
//				}
//			}
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
