package main.org.usfirst.frc.team1640.robot.states;

import main.org.usfirst.frc.team1640.compressor.AirCompressor;
import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.placer.PlacerController;
import main.org.usfirst.frc.team1640.robot.tests.SendEncoderValuesToNetworkTables;
import main.org.usfirst.frc.team1640.utilities.Strategy;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class DisabledRobotState extends RobotState {
	private ElapsedTimer timer;
	private IController driverController;
	private AirCompressor compressor;
	
	private Strategy testStrat;
	
	private PlacerController placerController;
	
	public DisabledRobotState(IRobotContext robotContext) {
		timer = new ElapsedTimer();
		driverController = robotContext.getDriverController();
		compressor = robotContext.getAirCompressor();
		
		testStrat = new SendEncoderValuesToNetworkTables(robotContext);
	}

	@Override
	public void init() {
		timer.start();
		driverController.pauseRumble();
		testStrat.init();
	}

	@Override
	public void update() {
		testStrat.execute();
	}
	
}
