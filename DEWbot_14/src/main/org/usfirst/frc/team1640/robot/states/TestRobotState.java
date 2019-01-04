package main.org.usfirst.frc.team1640.robot.states;

import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.placer.PlacerController;
import main.org.usfirst.frc.team1640.robot.tests.SendEncoderValuesToNetworkTables;
import main.org.usfirst.frc.team1640.robot.tests.SequencesAlongsideRealtimeTest;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class TestRobotState extends RobotState {
	private Strategy testStrat;
	private Strategy testStrat2;
	private IRobotContext robotContext;
	
	public TestRobotState(IRobotContext robotContext) {
		this.robotContext = robotContext;
		testStrat = new SequencesAlongsideRealtimeTest(robotContext);
		testStrat2 = new SendEncoderValuesToNetworkTables(robotContext);
	}

	@Override
	public void init() {
		testStrat.init();
		testStrat2.init();
		robotContext.getAirCompressor().start();
	}

	@Override
	public void update() {
		testStrat.execute();
		testStrat2.execute();
	}

}
