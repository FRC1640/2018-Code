package main.org.usfirst.frc.team1640.robot.tests;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.DriveUntilIntakeAndBack;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.Start2Sc2Cube2Sc;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.ScPos;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.StartPos;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.SwPos;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.together.CurrentStopIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ControllerShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.OcelotStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class SequencesAlongsideRealtimeTest implements Strategy {
	private IRobotContext robotContext;
	private OcelotStrategy ocelotStrat;
	private IController controller;
	private IGyro gyro;
	private AutonCommand script;
	
	private boolean isRunningScript;
	
	public SequencesAlongsideRealtimeTest(IRobotContext robotContext) {
		controller = robotContext.getDriverController();
		gyro = robotContext.getSensorSet().getGyro();
		this.robotContext = robotContext;
		
		
		ControllerShiftingDrive driveStrat = new ControllerShiftingDrive(robotContext.getDriveTrain());
		DefaultSteer steerStrat = new DefaultSteer(robotContext.getDriveTrain());
		DefaultOcelot ocelot = new DefaultOcelot(robotContext.getDriveTrain(), steerStrat, driveStrat);
		ocelotStrat = new FieldCentricOcelot(robotContext.getDriveTrain(), robotContext.getSensorSet().getGyro(), ocelot);
		
		// set your field configuration here
		FieldConfig config = new FieldConfig();
		config.setStartPos(StartPos.Right);
		config.setSwPos(SwPos.Right);
		config.setScPos(ScPos.Right);
		
		// PRESS B BUTTON TO RUN SCRIPT
		// PRESS A BUTTON TO SWITCH BACK TO NORMAL DRIVING
//		script = new DriveUntilIntakeAndBack(robotContext, new FieldCentricOcelot(robotContext.getDriveTrain(), robotContext.getSensorSet().getGyro(), ocelot), new CurrentStopIntakeTogether(new DefaultIntakeTogether(new DefaultIntakeSeparately(robotContext.getIntake()), 0.5, 1.0)), robotContext.getSensorSet().getGyro().getYaw(), 0.5);

		script = new Start2Sc2Cube2Sc(robotContext, config);
		
		
		isRunningScript = false;
	}

	@Override
	public void init() {
		ocelotStrat.init();
	}

	@Override
	public void execute() {
		if (controller.getAButtonPressed()) {
			ocelotStrat.init();
			isRunningScript = false;
		}
		if (controller.getBButtonPressed()) {
			ocelotStrat.end();
			script.reset();
			isRunningScript = true;
		}
		
		if (isRunningScript) {
			script.execute();
		}
		else {
			ocelotStrat.execute();
		}
		
		if (controller.getStartButtonPressed()) {
			gyro.resetYaw();
		}
		
		controller.setDeadband(0.2);
		ocelotStrat.setLateralDrive(controller.getLeftX());
		ocelotStrat.setLongitudinalDrive(controller.getLeftY());
		ocelotStrat.setAxialDrive(controller.getRightX());
	}

	@Override
	public void end() {
		ocelotStrat.end();
		script.reset();
	}
	
}
