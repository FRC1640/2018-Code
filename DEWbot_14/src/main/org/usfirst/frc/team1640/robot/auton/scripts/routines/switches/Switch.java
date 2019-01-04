package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.switches;

import main.org.usfirst.frc.team1640.constants.mechanical.DimensionConstants;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.PrintCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels.PointWheelsFieldCentric;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.ResetGyro;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class Switch extends AutonScript {
	
	public Switch(IRobotContext robotContext, boolean isLeft) {
		
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		
		DefaultSteer steer = new DefaultSteer(driveTrain);
		
		AutonCommand gyroOffset;
		if (isLeft) {
			gyroOffset = new OffsetGyro(gyro, -90);
		}
		else {
			gyroOffset = new OffsetGyro(gyro, 90);
		}
		
		addCommand("gyro-reset", new ResetGyro(gyro));
		addCommand("gyro-offset", gyroOffset);
		
		
		//TODO get PointWheelsFieldCentric working
//		addCommand("point-wheels1", new PointWheels(driveTrain, 90));
		addCommand("point-wheels1", new PointWheelsFieldCentric(driveTrain, gyro, steer, 0));
		addCommand("wait-point-wheels1", new WaitCommand(getCommand("point-wheels1")));
		
		addCommand("approach", new DriveStraight(robotContext, 168-DimensionConstants.ROBOT_WIDTH/2-DimensionConstants.BUMPER_WIDTH, 0, 1.0));
		addCommand("wait-approach", new WaitCommand(getCommand("approach")));
		AutonCommand pointWheels2;
		AutonCommand approach2;
		if (isLeft) {
//			pointWheels2 = new PointWheels(driveTrain, 0);
			pointWheels2 = new PointWheelsFieldCentric(driveTrain, gyro, steer, -90);
			approach2 = new DriveStraight(robotContext, 20, -90, 1.0);
		}
		else {
			pointWheels2 = new PointWheelsFieldCentric(driveTrain, gyro, steer, 90);
			approach2 = new DriveStraight(robotContext, 20, 90, 1.0);
		}
		
		addCommand("point-wheels2", pointWheels2);
		addCommand("wait-point-wheels2", getCommand("point-wheels2"));
		
		addCommand("approach2", approach2);
		addCommand("wait-approach2", getCommand("approach2"));
		
		addCommand("print", new PrintCommand("placing cube"));
		//addCommand("place-cube", new PlaceOnSwitch(robotContext)
	}
	
}
