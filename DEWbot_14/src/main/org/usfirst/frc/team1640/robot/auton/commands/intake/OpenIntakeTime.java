package main.org.usfirst.frc.team1640.robot.auton.commands.intake;

import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class OpenIntakeTime implements AutonCommand {
	private Intake intake;
	private double timeInSeconds;
	
	private IntakeTogetherStrategy intakeStrat;
	
	private ElapsedTimer timer;
	
	private boolean isInit;
	private boolean isDone;
	
	public OpenIntakeTime(IRobotContext robotContext, double timeInSeconds) {
		intake = robotContext.getIntake();
		this.timeInSeconds = timeInSeconds;
		
		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(intake);
		intakeStrat = new DefaultIntakeTogether(intakeSeparately, 1.0, 1.0);
		
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
			System.out.println("Running");
			if (timer.getElapsedSeconds() < timeInSeconds) {
				intakeStrat.setExtending(true);
				intakeStrat.execute();
			}
			else {
				intakeStrat.setExtending(false);
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
