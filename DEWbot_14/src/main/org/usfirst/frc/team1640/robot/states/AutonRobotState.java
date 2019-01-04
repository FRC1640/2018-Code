package main.org.usfirst.frc.team1640.robot.states;

import main.org.usfirst.frc.team1640.robot.auton.ScriptController2;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.ScPos;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.StartPos;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.SwPos;
import main.org.usfirst.frc.team1640.robot.placer.PlacerController;
import main.org.usfirst.frc.team1640.robot.tests.SendEncoderValuesToNetworkTables;
import main.org.usfirst.frc.team1640.utilities.Strategy;


public class AutonRobotState extends RobotState {
	private IRobotContext robotContext;
	private FieldConfig config;
	
	private PlacerController placerController;
	
	private ScriptController2 scriptController;
	private Strategy networkTables;
	
	public AutonRobotState(IRobotContext robotContext) {
		this.robotContext = robotContext;
		
		config = new FieldConfig();
		config.setStartPos(StartPos.Right);
		config.setSwPos(SwPos.Right);
		config.setScPos(ScPos.Right);
		
		scriptController = new ScriptController2(robotContext);
		networkTables = new SendEncoderValuesToNetworkTables(robotContext);
	}

	@Override
	public void init() {
		robotContext.getAirCompressor().start();
		robotContext.getSensorSet().getGyro().resetYaw();
		
		robotContext.getDriveTrain().getFLPivot().setTargetAngle(90);
		robotContext.getDriveTrain().getFRPivot().setTargetAngle(90);
		robotContext.getDriveTrain().getBLPivot().setTargetAngle(90);
		robotContext.getDriveTrain().getBRPivot().setTargetAngle(90);
		
		scriptController.init();
		networkTables.init();
	}

	@Override
	public void update() {
		scriptController.execute();
		robotContext.getPlacer().execute();
		networkTables.execute();
	}

}
