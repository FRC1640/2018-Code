package main.org.usfirst.frc.team1640.robot.intake;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.controllers.rumblers.DefaultRumbler;
import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.together.CurrentStopIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class IntakeController implements Strategy {
	private Intake intake;
	private IController driverController;
	
	private CurrentStopIntakeTogether intakeStrat;
	
	private DefaultRumbler intakeRumbler;
	
	public IntakeController(IRobotContext robotContext) {
		intake = robotContext.getIntake();
		driverController = robotContext.getDriverController();
		
		DefaultIntakeSeparately intakeSeparately = new DefaultIntakeSeparately(intake);
		DefaultIntakeTogether intakeTogether = new DefaultIntakeTogether(intakeSeparately, 0.5, 1.0);
		intakeStrat = new CurrentStopIntakeTogether(intakeTogether);
		
		intakeRumbler = driverController.createDefaultRumbler();
	}

	@Override
	public void init() {
		intakeStrat.init();
	}

	@Override
	public void execute() {
		driverController.setDeadband(0.01);
		double intakeSpeed = driverController.getRightTrigger()-driverController.getLeftTrigger();
		
		//New code
//		boolean extending = driverController.getRightBumper();
//		if(extending) {
//			intakeStrat.setWheelDrive(intakeSpeed);
//		}
//		else {
//			if(intakeSpeed != 0) {
//				intakeStrat.setWheelDrive(intakeSpeed);
//			}
//			else {
//				intakeStrat.setWheelDrive(0.3);
//			}
//		}
//		intakeStrat.setExtending(extending);
//		intakeStrat.execute();

		final double minSpeed = 0.75;
		
		if (intakeSpeed < 0) {
			intakeSpeed = -minSpeed + (1-minSpeed)*intakeSpeed;
		}
		
		//Old code
		intakeStrat.setWheelDrive(intakeSpeed);
		intakeStrat.setExtending(driverController.getRightBumper());
		intakeStrat.execute();
		
		if (intakeSpeed > 0 && !intakeStrat.isStopped()) {
			intakeRumbler.setRightRumble(1.0);
			intakeRumbler.setLeftRumble(1.0);
		}
		else {
			intakeRumbler.setRightRumble(0.0);
			intakeRumbler.setLeftRumble(0.0);
		}
	}

	@Override
	public void end() {
		intakeStrat.end();
	}

}
