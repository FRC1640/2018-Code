package main.org.usfirst.frc.team1640.robot.tests;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ControllerShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.OcelotStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.Strategy;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class SunflowerTestStrategy implements Strategy {
	IController controller;
	IGyro gyro;
	SunflowerStrategy sunflowerStrat;

	public SunflowerTestStrategy(IRobotContext robotContext) {
		this.controller = robotContext.getDriverController();
		this.gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ControllerShiftingDrive driveStrat = new ControllerShiftingDrive(driveTrain);
		DefaultSteer steerStrat = new DefaultSteer(driveTrain);
		OcelotStrategy ocelot = new DefaultOcelot(driveTrain, steerStrat, driveStrat);
		OcelotStrategy fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		sunflowerStrat = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.007, 0, 0.001, 1.0);
//		sunflowerStrat = new WheelsMaintainDirectionSunflower(new ShortestAngleSunflower(driveTrain, gyro, 0.007, 0, 0.001, 1.0), gyro);
	}
	
	@Override
	public void init() {
		sunflowerStrat.init();
	}

	@Override
	public void execute() {
		controller.setDeadband(0.2);
		double x1 = controller.getLeftX();
		double y1 = controller.getLeftY();
		double x2 = controller.getRightX();
		double y2 = controller.getRightY();

		if (MathUtilities.magnitude(x2, y2) > 0.9) {
			sunflowerStrat.setDirection(MathUtilities.angle(x2, y2));
			sunflowerStrat.acceptNewDirections(true);
		}
		else if (controller.getPOVNorth()) {
			sunflowerStrat.setDirection(0);
			sunflowerStrat.acceptNewDirections(true);
		}
		else if (controller.getPOVWest()) {
			sunflowerStrat.setDirection(90);
			sunflowerStrat.acceptNewDirections(true);
		}
		else if (controller.getPOVSouth()) {
			sunflowerStrat.setDirection(180);
			sunflowerStrat.acceptNewDirections(true);
		}
		else if (controller.getPOVEast()) {
			sunflowerStrat.setDirection(270);
			sunflowerStrat.acceptNewDirections(true);
		}
		else {
			sunflowerStrat.acceptNewDirections(false);
		}
		sunflowerStrat.setControlMode(ControlMode.PercentOutput);
		sunflowerStrat.setLateralDrive(x1);
		sunflowerStrat.setLongitudinalDrive(y1);
		sunflowerStrat.execute();
		
		if (controller.getStartButtonPressed()) {
			gyro.resetYaw();
		}
	}

	@Override
	public void end() {
		sunflowerStrat.end();
	}

}