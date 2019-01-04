package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.scales;

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

public class Scale extends AutonScript {
	private IRobotContext robotContext;
	private boolean isLeft;
	
	public Scale(IRobotContext robotContext, boolean isLeft) {
		this.robotContext = robotContext;
		this.isLeft = isLeft;
		
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		
		DefaultSteer steer = new DefaultSteer(driveTrain);
		
		addCommand("reset-gyro", new ResetGyro(gyro));
		
		AutonCommand offsetGyro;
		if (isLeft) {
			offsetGyro = new OffsetGyro(gyro, -90);
		}
		else {
			offsetGyro = new OffsetGyro(gyro, 90);
		}
		
		addCommand("offset-gyro", offsetGyro);
		
		addCommand("point-wheels1", new PointWheelsFieldCentric(driveTrain, gyro, steer, 0));
		addCommand("wait-point-wheels1", new WaitCommand(getCommand("point-wheels1")));
		
		addCommand("approach1", new DriveStraight(robotContext, 324-DimensionConstants.ROBOT_WIDTH/2-DimensionConstants.BUMPER_WIDTH, 0, 1.0));
		addCommand("wait-approach1", new WaitCommand(getCommand("approach1")));
		
		addCommand("print1", new PrintCommand("approach 1 distance: " + driveTrain.getFLPivot().getInches()));
		
		addCommand("point-wheels2", new PointWheelsFieldCentric(driveTrain, gyro, steer, 90));
		addCommand("wait-point-wheels2", new WaitCommand(getCommand("point-wheels2")));
		
		AutonCommand approach2;
		if (isLeft) {
			approach2 = new DriveStraight(robotContext, 6, -90, 1.0);
		}
		else {
			approach2 = new DriveStraight(robotContext, 6, 90, 1.0);
		}
		
		addCommand("approach2", approach2);
		addCommand("wait-approach2", getCommand("approach2"));
		
		addCommand("print", new PrintCommand("place cube"));
		//addCommand("place", new PlaceOnScale());
	}
}
