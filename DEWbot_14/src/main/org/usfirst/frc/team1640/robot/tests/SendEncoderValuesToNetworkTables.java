package main.org.usfirst.frc.team1640.robot.tests;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.utilities.Strategy;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class SendEncoderValuesToNetworkTables implements Strategy {
	private NetworkTable table;
	private IDriveTrain driveTrain;
	private IRobotContext robotContext;
	private IController controller;
	private ElapsedTimer timer;
	
	public SendEncoderValuesToNetworkTables(IRobotContext robotContext) {
		driveTrain = robotContext.getDriveTrain();
		controller = robotContext.getDriverController();
		timer = new ElapsedTimer();
		table = NetworkTableInstance.getDefault().getTable("Plot");
		this.robotContext = robotContext;
	}

	@Override
	public void init() {
		timer.start();
	}

	@Override
	public void execute() {
		table.getEntry("_time").setDouble(timer.getElapsedSeconds());
		table.getEntry("i_flInches").setDouble(Math.abs(driveTrain.getFLPivot().getInches()));
		table.getEntry("i_frInches").setDouble(Math.abs(driveTrain.getFRPivot().getInches()));
		table.getEntry("i_blInches").setDouble(Math.abs(driveTrain.getBLPivot().getInches()));
		table.getEntry("i_brInches").setDouble(Math.abs(driveTrain.getBRPivot().getInches()));
		table.getEntry("height").setDouble(robotContext.getPlacer().getLiftHeight());
		table.getEntry("a_flAngle").setDouble(driveTrain.getFLPivot().getAngle());
		table.getEntry("a_frAngle").setDouble(driveTrain.getFRPivot().getAngle());
		table.getEntry("a_blAngle").setDouble(driveTrain.getBLPivot().getAngle());
		table.getEntry("a_brAngle").setDouble(driveTrain.getBRPivot().getAngle());
		table.getEntry("armAngle").setDouble(robotContext.getPlacer().getArmAngle());
		table.getEntry("gyroYaw").setDouble(robotContext.getSensorSet().getGyro().getYaw());
		table.getEntry("time").setDouble(timer.getElapsedSeconds());
		table.getEntry("y1").setDouble(Math.abs(driveTrain.getFLPivot().getVelocity()));
		table.getEntry("y2").setDouble(Math.abs(driveTrain.getFRPivot().getVelocity()));
		table.getEntry("y3").setDouble(Math.abs(driveTrain.getBLPivot().getVelocity()));
		table.getEntry("y4").setDouble(Math.abs(driveTrain.getBRPivot().getVelocity()));
//		if (controller.getRightBumper()) {
//			driveTrain.resetPositions();
//			timer.restart();
//		}
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
