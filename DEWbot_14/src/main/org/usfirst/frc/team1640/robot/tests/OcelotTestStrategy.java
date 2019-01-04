package main.org.usfirst.frc.team1640.robot.tests;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ControllerShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.GyroCorrectedOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.OcelotStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.Strategy;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class OcelotTestStrategy implements Strategy {
	IController controller;
	IGyro gyro;
	IDriveTrain driveTrain;
	OcelotStrategy myStrat;

	public OcelotTestStrategy(IController controller, IGyro gyro, IDriveTrain driveTrain) {
		this.controller = controller;
		this.gyro = gyro;
		this.driveTrain = driveTrain;
		
//		myStrat = new DefaultOcelot(driveTrain);
		ControllerShiftingDrive driveStrat = new ControllerShiftingDrive(driveTrain);
		DefaultSteer steerStrat = new DefaultSteer(driveTrain);
		OcelotStrategy ocelot = new DefaultOcelot(driveTrain, steerStrat, driveStrat);
		SunflowerStrategy sunflowerStrat = new ShortestAngleSunflower(driveTrain, gyro, ocelot, 0.007, 0.0, 0.001, 1.0);
		OcelotStrategy ocelotStrat = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		myStrat = new GyroCorrectedOcelot(driveTrain, gyro, ocelotStrat);
//		ocelotStrat = new GyroCorrectedOcelot(driveTrain, gyro, 0.014, 0.0, 0.001, 1.0);
	}
	
	@Override
	public void init() {
		myStrat.init();
	}

	@Override
	public void execute() {
		controller.setDeadband(0.2);
		double x1 = controller.getLeftX();
		double y1 = controller.getLeftY();
		double x2 = controller.getRightX();
		double y2 = controller.getRightY();
		
//		System.out.println(driveTrain.getPositionInches());
//		System.out.println(driveTrain.getFLPivot().getVelocity());
		
		myStrat.setLateralDrive(x1);
		myStrat.setLongitudinalDrive(y1);
		myStrat.setAxialDrive(x2);
		myStrat.execute();
		
		if (controller.getStartButtonPressed()) {
			gyro.resetYaw();
		}
		
		if (controller.getAButtonPressed()) {
			myStrat.setControlMode(ControlMode.Velocity);
		}
		if (controller.getBButtonPressed()) {
			myStrat.setControlMode(ControlMode.PercentOutput);
		}
	}

	@Override
	public void end() {
		myStrat.end();
	}

}
