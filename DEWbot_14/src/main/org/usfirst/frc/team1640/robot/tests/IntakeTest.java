package main.org.usfirst.frc.team1640.robot.tests;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.robot.intake.together.CurrentStopIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class IntakeTest implements Strategy {
	private IController controller;
	
	private Intake intake;
	
	private IntakeTogetherStrategy intakeTogetherStrat;
	private IntakeSeparatelyStrategy intakeSeparatelyStrat;
	
	public IntakeTest(IRobotContext robotContext) {
		
		controller = robotContext.getDriverController();
		intake = robotContext.getIntake();
		
		intakeSeparatelyStrat = new DefaultIntakeSeparately(intake);
		DefaultIntakeTogether defaultIntakeTogetherStrat = new DefaultIntakeTogether(intakeSeparatelyStrat, 0.5, 1.0);
		intakeTogetherStrat = new CurrentStopIntakeTogether(defaultIntakeTogetherStrat);
	}
	
	@Override
	public void init() {
		intakeTogetherStrat.init();
	}

	@Override
	public void execute() {
		controller.setDeadband(0.2);
		intakeTogetherStrat.setWheelDrive(controller.getRightTrigger()-controller.getLeftTrigger());
		intakeTogetherStrat.setExtending(controller.getRightBumper());
		intakeTogetherStrat.execute();
		System.out.println(intake.getLeftWheelCurrent());
	}

	@Override
	public void end() {
		intakeTogetherStrat.end();
	}

}
