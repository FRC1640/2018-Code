package main.org.usfirst.frc.team1640.robot.auton.commands.intake;

import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class SetIntakeDrive implements AutonCommand {
	private Intake intake;
	private double drive;
	
	private IntakeTogetherStrategy intakeStrat;
	
	private boolean isDone;
	
	public SetIntakeDrive(IntakeTogetherStrategy intakeTogether, double drive) {
		this.drive = drive;
		
		this.intakeStrat = intakeTogether;
		
//		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(intake);
//		intakeStrat = new DefaultIntakeTogether(intakeSeparately, 0.5, 1.0);

		isDone = false;
	}

	@Override
	public void execute() {
		if (!isDone) {
			intakeStrat.setWheelDrive(drive);
			intakeStrat.execute();
			isDone = true;
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
		isDone = false;
	}
	
}
