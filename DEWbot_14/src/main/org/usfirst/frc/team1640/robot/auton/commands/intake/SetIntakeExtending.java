package main.org.usfirst.frc.team1640.robot.auton.commands.intake;

import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;

public class SetIntakeExtending implements AutonCommand {
	private Intake intake;
	private boolean extending;
	
	private IntakeTogetherStrategy intakeStrat;

	private boolean isDone;
	
	public SetIntakeExtending(IntakeTogetherStrategy intakeTogether, boolean extending) {
		this.extending = extending;
		
		this.intakeStrat = intakeTogether;
		
//		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(intake);
//		intakeStrat = new DefaultIntakeTogether(intakeSeparately, 1.0, 1.0);
		
		isDone = false;
	}

	@Override
	public void execute() {
		if (!isDone) {
			System.out.println("Set extending: " + extending);
			intakeStrat.setExtending(extending);
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
