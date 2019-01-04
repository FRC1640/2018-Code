package main.org.usfirst.frc.team1640.robot.auton.commands.drive;

import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.intake.together.CurrentStopIntakeTogether;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.linear.LinearStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

public class DriveUntilIntakeAndBack implements AutonCommand {
	private IRobotContext robotContext;
	private LinearStrategy linearStrat;
	private CurrentStopIntakeTogether intakeStrat;
	private double angle;
	private double drive;
	
	private double distance;
	
	private boolean forward;
	private boolean isDone;
	private boolean isInit;
	
	public DriveUntilIntakeAndBack(IRobotContext robotContext, LinearStrategy linearStrat, CurrentStopIntakeTogether intakeStrat, double angle, double drive) {
		this.robotContext = robotContext;
		this.linearStrat = linearStrat;
		this.intakeStrat = intakeStrat;
		this.angle = angle;
		this.drive = MathUtilities.constrain(drive, 0, 1.0);
		
		distance = 0;
		
		forward = true;
		isDone = false;
		isInit = false;
	}

	@Override
	public void execute() {
		if (!isDone) {
			if (!isInit) {
				robotContext.getDriveTrain().resetPositions();
				linearStrat.init();
				intakeStrat.init();
				isInit = true;
			}
			if (forward) {
				
				linearStrat.setLateralDrive(MathUtilities.xFromPolar(angle, 0.5*drive));
				linearStrat.setLongitudinalDrive(MathUtilities.yFromPolar(angle, 0.5*drive));
				linearStrat.execute();
				
				intakeStrat.setWheelDrive(1.0);
				intakeStrat.execute();
				
				if (intakeStrat.isStopped()) {
					distance = Math.abs(robotContext.getDriveTrain().getPositionInches());
					robotContext.getDriveTrain().resetPositions();
					intakeStrat.setWheelDrive(0);
					intakeStrat.execute();
					forward = false;
					robotContext.getPlacer().setPreset(PlacerPreset.ScaleMax);
					robotContext.getPlacer().execute();
				}
			}
			else {
				System.out.println("Running back");
				
				linearStrat.setLateralDrive(MathUtilities.xFromPolar(angle, -drive));
				linearStrat.setLongitudinalDrive(MathUtilities.yFromPolar(angle, -drive));
				linearStrat.execute();
				
				if (Math.abs(robotContext.getDriveTrain().getPositionInches()) >= distance) {
					linearStrat.setLateralDrive(0);
					linearStrat.setLongitudinalDrive(0);
					linearStrat.execute();
					linearStrat.end();
					intakeStrat.end();
					isDone = true;
				}
			}
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
		forward = true;
		isInit = false;
		isDone = false;
		distance = 0;
		linearStrat.init();
		intakeStrat.init();
	}
	
	

}
