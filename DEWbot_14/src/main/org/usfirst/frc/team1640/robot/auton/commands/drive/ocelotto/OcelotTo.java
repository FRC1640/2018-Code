package main.org.usfirst.frc.team1640.robot.auton.commands.drive.ocelotto;

import main.org.usfirst.frc.team1640.constants.mechanical.DimensionConstants;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.Vector;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class OcelotTo implements AutonCommand {
	
	private FieldCentricOcelot ocelotStrat;
	private IGyro gyro;
	private double angle;
	
	private boolean isInit;
	protected boolean isDone;
	private boolean isPointed;
	
	private double lateralDrive;
	private double longitudinalDrive;
	private double axialDrive;
	
	protected double goalAngle;
	
	protected double kP = 1.0;
	
	private final double kBuffer = 2;
	
	private ElapsedTimer timer;
	
	public OcelotTo(FieldCentricOcelot ocelotStrat, IGyro gyro, Vector distanceInInches, double angleInDegrees) {
		this.ocelotStrat = ocelotStrat;
		this.gyro = gyro;
		this.angle = angleInDegrees;
		
		double d = distanceInInches.magnitude();
		double a = Math.abs(Math.toRadians(angleInDegrees));
		double kR = DimensionConstants.ROBOT_DIAGONAL/2; // distance from the center of the robot to each pivot
		
		// k is the perfect ratio that allows the linear and rotational motions to finish at the exact same time
		// L = k*R (where L is the linear drive speed and R is the rotational drive speed)
		double k = d/(a*kR);
		
		// determine which should be bigger (axial or linear drive) using k and scale everything based on that
		if (k > 1) { // if k is bigger than 1, L must be bigger
			lateralDrive = distanceInInches.getX()/d;
			longitudinalDrive = distanceInInches.getY()/d;
			axialDrive = -Math.signum(angleInDegrees)/k; //negative because ocelot uses left-hand rule
		}
		else { // if k is less than 1, R must be bigger
			axialDrive = -Math.signum(angleInDegrees); //negative because ocelot uses left-hand rule;
			lateralDrive = distanceInInches.getX()*k/d;
			longitudinalDrive = distanceInInches.getY()*k/d;
		}
		
		timer = new ElapsedTimer();
		
		isInit = false;
		isDone = false;
		isPointed = false;
	}

	@Override
	public void execute() {
		if (!isInit) {
			goalAngle = ((gyro.getYaw() + angle)%360+360)%360;
			init();
			isInit = true;
			
			// Start pointing wheels in correct direction
			timer.restart();
			ocelotStrat.setLateralDrive(lateralDrive*0.01);
			ocelotStrat.setLongitudinalDrive(longitudinalDrive*0.01);
			ocelotStrat.setAxialDrive(axialDrive*0.01);
			ocelotStrat.execute();
			isPointed = false;
		}
		
		if (!isDone) {
			if (!isPointed) {
				if (timer.getElapsedSeconds() > 0.25) {
					isPointed = true;
				}
			}
			else {
				ocelotStrat.setLateralDrive(kP*lateralDrive);
				ocelotStrat.setLongitudinalDrive(kP*longitudinalDrive);
				ocelotStrat.setAxialDrive(kP*axialDrive);
				ocelotStrat.execute();
				
				if (Math.abs(MathUtilities.shortestAngleBetween(gyro.getYaw(), goalAngle)) < kBuffer) {
					ocelotStrat.setLateralDrive(0);
					ocelotStrat.setLongitudinalDrive(0);
					ocelotStrat.setAxialDrive(0);
					ocelotStrat.execute();
					isDone = true;
				}
			}
		}
		else {
			ocelotStrat.setLateralDrive(0);
			ocelotStrat.setLongitudinalDrive(0);
			ocelotStrat.setAxialDrive(0);
			ocelotStrat.execute();
		}
	}
	
	protected void init() {
		
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
	}

}
