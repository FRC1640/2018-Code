package main.org.usfirst.frc.team1640.robot.tests;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DefaultDrive;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DriveStrategy;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class DriveTest implements Strategy {
	private IController controller;
	private IDriveTrain driveTrain;
	private DriveStrategy driveStrat;
	
	public DriveTest(IRobotContext robotContext) {
		controller = robotContext.getDriverController();
		driveTrain = robotContext.getDriveTrain();
		
		driveStrat = new DefaultDrive(driveTrain);
	}

	@Override
	public void init() {
		driveStrat.init();
	}

	@Override
	public void execute() {
		double d = controller.getLeftY();
		
		driveStrat.setFLDrive(d);
		driveStrat.setFRDrive(d);
		driveStrat.setBLDrive(d);
		driveStrat.setBRDrive(d);
		driveStrat.execute();
	}

	@Override
	public void end() {
		driveStrat.end();
	}

}
