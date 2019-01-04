package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.simple;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeoutCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels.PointWheelsFieldCentric;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.ResetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnAndMaintainDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DefaultDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.GyroCorrectedOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class CenterSwitch extends AutonScript {
	//Start this script at the center position
	// 6 in. to the right of the outside edge of the exchange line
	// 6 in. away from the center line of the field
	
	public CenterSwitch(IRobotContext robotContext, FieldConfig config) {
		
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), new DefaultDrive(driveTrain));
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		GyroCorrectedOcelot myStrat = new GyroCorrectedOcelot(driveTrain, gyro, fieldCentric);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.017, 0.0, 0.0001, 0.9);
		
		DefaultSteer steer = new DefaultSteer(driveTrain);
		
		addCommand("reset-gyro", new ResetGyro(gyro));
		addCommand("lift-to-switch-height", new PlacerPresetCommand(robotContext, PlacerPreset.Switch));
		
		AutonCommand pointWheels;
		AutonCommand approach;
		double distance = 0;
		if (config.getSwPos() == FieldConfig.SwPos.Left) {
			double angle = 32.5; //32.5;
			distance = 130;
			pointWheels = new PointWheelsFieldCentric(driveTrain, gyro, steer, angle);
			approach = new TimeoutCommand(new DriveAlongTrapezoidalCurve(driveTrain, sunflower, distance, angle, 1.0, 1, 0.0075), 3);
//			approach = new DriveUntilSeconds(myStrat, 2.75, angle, 0.6);
		}
		else {
			double angle = -25; //-21
			distance = 106;
			pointWheels = new PointWheelsFieldCentric(driveTrain, gyro, steer, angle);
			approach = new TimeoutCommand(new DriveAlongTrapezoidalCurve(driveTrain, sunflower, distance, angle, 1.0, 1, 0.0075), 2.5);
//			approach = new DriveUntilSeconds(myStrat, 2.75, angle, 0.6);
		}
		
		addCommand("point-wheels", pointWheels);
		addCommand("wait-point-wheels", getCommand("point-wheels"));
		addCommand("approach", approach);
		addCommand("maintain-angle", new TurnAndMaintainDirection(driveTrain, sunflower, gyro, 0, 7, 0.1, distance));
				
		addCommand("wait-approach", new WaitCommand(getCommand("approach")));
		addCommand("place", new DriveIntakeTime(robotContext, -1.0, 0.25));
		addCommand("wait-lift-to-switch-height", new WaitCommand(getCommand("lift-to-switch-height")));
		addCommand("wait-place", new WaitCommand(getCommand("place")));
	}
	
}