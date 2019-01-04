package main.org.usfirst.frc.team1640.robot.states;

import main.org.usfirst.frc.team1640.compressor.AirCompressor;
import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.intake.IntakeController;
import main.org.usfirst.frc.team1640.robot.placer.PlacerController;
import main.org.usfirst.frc.team1640.robot.tests.SendEncoderValuesToNetworkTables;
import main.org.usfirst.frc.team1640.robot.traversal.TraversalController;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.Strategy;


public class TeleopRobotState extends RobotState {
	private IController driverController;
	private IController operatorController;
	private IGyro gyro;
	
	private TraversalController traversalController;
	private IntakeController intakeController;
	private PlacerController placerController;
	private Strategy testStrat, testStrat2;
	
	private AirCompressor compressor;

	public TeleopRobotState(IRobotContext robotContext) {
		driverController = robotContext.getDriverController();
		operatorController = robotContext.getOperatorController();
		gyro = robotContext.getSensorSet().getGyro();
		compressor = robotContext.getAirCompressor();
		
		testStrat = new SendEncoderValuesToNetworkTables(robotContext);
		
		traversalController = new TraversalController(robotContext);
		intakeController = new IntakeController(robotContext);
		placerController = new PlacerController(robotContext);
	}
	
	@Override
	public void init() {
		testStrat.init();
		
		placerController.init();
		traversalController.init();
		intakeController.init();
		driverController.resumeRumble();
		compressor.start();
	}

	@Override
	public void update() {
		testStrat.execute();
		
		if (driverController.getStartButtonReleased()) {
			gyro.resetYaw();
		}
		
		traversalController.execute();
		intakeController.execute();
		placerController.execute();
		
		
	}

}
