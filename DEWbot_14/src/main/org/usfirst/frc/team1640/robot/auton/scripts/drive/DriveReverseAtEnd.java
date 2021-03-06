package main.org.usfirst.frc.team1640.robot.auton.scripts.drive;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.SetManualShift;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.ChangeControlMode;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.ResetEncoders;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels.PointWheelsFieldCentric;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilSeconds;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.GyroCorrectedOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.OcelotStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.steer.SteerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class DriveReverseAtEnd extends AutonScript {
	
	public DriveReverseAtEnd(IRobotContext robotContext, double distanceInInches, double angle, double maxDrive, double reverseDrive, double timeToReverseInSeconds) {
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		IGyro gyro = robotContext.getSensorSet().getGyro();
		
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		SteerStrategy steerStrat = new DefaultSteer(driveTrain);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, steerStrat, driveStrat);
		OcelotStrategy ocelotStrat = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		OcelotStrategy myStrat = new GyroCorrectedOcelot(driveTrain, gyro, ocelotStrat);
		
		addCommand("reset-encoders", new ResetEncoders(robotContext));
		addCommand("normal-transmission", new SetManualShift(driveStrat, 0));
		addCommand("velocity-control", new ChangeControlMode(driveStrat, ControlMode.PercentOutput));
		addCommand("point-wheels", new PointWheelsFieldCentric(driveTrain, gyro, steerStrat, angle));
		addCommand("wait-point-wheels", new WaitCommand(getCommand("point-wheels")));
		double angleToDrive = angle-gyro.getYaw();
		addCommand("drive", new DriveUntilInches(driveTrain, myStrat, distanceInInches, angleToDrive, maxDrive));
		addCommand("wait-drive", new WaitCommand(getCommand("drive")));
		addCommand("reverse", new DriveUntilSeconds(myStrat, timeToReverseInSeconds, angleToDrive, reverseDrive));
		addCommand("wait-reverse", new WaitCommand(getCommand("reverse")));
	}

}
