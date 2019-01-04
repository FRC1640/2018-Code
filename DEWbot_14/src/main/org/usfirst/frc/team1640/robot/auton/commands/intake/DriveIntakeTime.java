package main.org.usfirst.frc.team1640.robot.auton.commands.intake;

import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class DriveIntakeTime implements AutonCommand {
	private Intake intake;
	private double drive;
	private double timeInSeconds;
	
	private IntakeTogetherStrategy intakeStrat;
	
	private ElapsedTimer timer;
	
	private boolean isInit;
	private boolean isDone;
	
	public DriveIntakeTime(IRobotContext robotContext, double drive, double timeInSeconds) {
		intake = robotContext.getIntake();
		this.drive = drive;
		this.timeInSeconds = timeInSeconds;
		
		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(intake);
		intakeStrat = new DefaultIntakeTogether(intakeSeparately, 0.4, 1.0);
		
		timer = new ElapsedTimer();
		
		isInit = false;
		isDone = false;
	}

	@Override
	public void execute() {
		if (!isInit) {
			timer.start();
			isInit = true;
		}
		if (!isDone) {
			if (timer.getElapsedSeconds() < timeInSeconds) {
				intakeStrat.setWheelDrive(drive);
				intakeStrat.execute();
			}
			else {
				intakeStrat.setWheelDrive(0);
				intakeStrat.execute();
				intakeStrat.end();
				isDone = true;
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
		isInit = false;
		isDone = false;
	}
	
}
