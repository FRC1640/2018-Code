package main.org.usfirst.frc.team1640.robot.auton.scripts.drive;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.SetManualShift;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.ChangeControlMode;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.ResetEncoders;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels.PointWheelsFieldCentric;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.steer.SteerStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

import com.ctre.phoenix.motorcontrol.ControlMode;


public class DriveStraightSunflower extends AutonScript {
	
	public DriveStraightSunflower(IRobotContext robotContext, ShortestAngleSunflower sunflower, double distanceInInches, double angle, double maxDrive) {
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		IGyro gyro = robotContext.getSensorSet().getGyro();
		
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		SteerStrategy steerStrat = new DefaultSteer(driveTrain);
		
		addCommand("reset-encoders", new ResetEncoders(robotContext));
		addCommand("normal-transmission", new SetManualShift(driveStrat, 0));
		addCommand("velocity-control", new ChangeControlMode(driveStrat, ControlMode.PercentOutput));
		addCommand("point-wheels", new PointWheelsFieldCentric(driveTrain, gyro, steerStrat, angle));
		addCommand("wait-point-wheels", new WaitCommand(getCommand("point-wheels")));
		addCommand("drive1", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, distanceInInches, angle-gyro.getYaw(), maxDrive));
		addCommand("wait-drive1", new WaitCommand(getCommand("drive1")));
	}
}