package main.org.usfirst.frc.team1640.robot.traversal.ocelot;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class GyroCorrectedOcelot implements OcelotStrategy {
	private OcelotStrategy ocelotStrat;
	private ShortestAngleSunflower sunflowerStrat;
	private LinearStrategy myStrat;
	
	private boolean isAxialDriveNegligible;
	private boolean isPrevAxialDriveNegligible;
	private boolean isCoasting = false;
	
	private final double kAxialDriveBuffer = 0.1;
	private final double kTurnDelayInSeconds = 0.5;
	
	private ElapsedTimer timer;
	
	public GyroCorrectedOcelot(IDriveTrain driveTrain, IGyro gyro, OcelotStrategy ocelotStrat) {
		this.ocelotStrat = ocelotStrat;
		sunflowerStrat = new ShortestAngleSunflower(driveTrain, gyro, ocelotStrat, 0.015, 0.0, 0.0001, 1.0);
		
		myStrat = sunflowerStrat; // Default strategy
		
		isAxialDriveNegligible = true;
		isPrevAxialDriveNegligible = isAxialDriveNegligible;
		
		timer = new ElapsedTimer();
	}
	
	public GyroCorrectedOcelot(IDriveTrain driveTrain, IGyro gyro, OcelotStrategy ocelotStrat, double kP, double kI, double kD, double driveRange){
		this(driveTrain, gyro, ocelotStrat);
		sunflowerStrat = new ShortestAngleSunflower(driveTrain, gyro, ocelotStrat, kP, kI, kD, driveRange);
		myStrat = sunflowerStrat;
	}

	@Override
	public void init() {
		myStrat.init();
	}

	@Override
	public void execute() {
		
		sunflowerStrat.acceptNewDirections(true);
		
		if (isAxialDriveNegligible) { // The operator isn't trying to turn
			
			// Rising edge
			if (!isPrevAxialDriveNegligible) {
				timer.restart();
				isCoasting = true;
			}
			
			if (timer.getElapsedSeconds() > kTurnDelayInSeconds && isCoasting) {
				myStrat.end();
				myStrat = sunflowerStrat; // In sunflower mode, the robot should maintain its heading
				myStrat.init();
				isCoasting = false;
			}
		}
		else { // The operator is trying to turn
			
			// Rising edge
			if (isPrevAxialDriveNegligible) {
				myStrat.end();
				myStrat = ocelotStrat; // In ocelot mode, the robot should drive normally
				myStrat.init();
			}
			
		}
		
		myStrat.execute();
		
		isPrevAxialDriveNegligible = isAxialDriveNegligible;
	}

	@Override
	public void end() {
		ocelotStrat.end();
		sunflowerStrat.end();
	}
	
	@Override
	public void setLateralDrive(double drive) {
		myStrat.setLateralDrive(drive);
	}

	@Override
	public void setLongitudinalDrive(double drive) {
		myStrat.setLongitudinalDrive(drive);
	}

	@Override
	public void setAxialDrive(double drive) {
		ocelotStrat.setAxialDrive(drive);
		isAxialDriveNegligible = MathUtilities.inRange(drive, -kAxialDriveBuffer, kAxialDriveBuffer);
	}
	
	@Override
	public void setControlMode(ControlMode control) {
		ocelotStrat.setControlMode(control);
		sunflowerStrat.setControlMode(control);
	}
	
	public void setOcelotStrategy(OcelotStrategy ocelotStrat) {
		ocelotStrat.end();
		this.ocelotStrat = ocelotStrat;
		sunflowerStrat.setOcelotStrategy(ocelotStrat);
		ocelotStrat.init();
	}

}
