package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.unused;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.detect.DetectInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.GetCube;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class Start2Sw2Cube1 extends AutonScript {

	public Start2Sw2Cube1(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0.0, 0.0001, 0.9);
		
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = -1; break;
		case Right: dir = 1; break;
		default: System.out.println("Start2Sw2Cube1: Invalid starting location");
		}
		
		if (dir != 0) {
			
			addCommand("offset-gyro", new OffsetGyro(gyro, dir*90));
			addCommand("approach-alley", new DriveStraight(robotContext, 223.75, 0, 1.0));
			addCommand("lift-to-switch-height", new PlacerPresetCommand(robotContext, PlacerPreset.Switch));
			addCommand("detect-near-switch", new DetectInches(robotContext, 128)); // check encoders exceed certain value
			addCommand("wait-detect-near-switch", new WaitCommand(getCommand("detect-near-switch")));
			addCommand("outtake-cube1", new DriveIntakeTime(robotContext, -1.0, 0.5));
			addCommand("wait-approach-alley", new WaitCommand(getCommand("approach-alley")));
			addCommand("lift-to-floor", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
			addCommand("turn-to-cubes", new TurnToDirection(sunflower, gyro, 180, 2, 0.1, 1));
			addCommand("wait-turn-to-cubes", new WaitCommand(getCommand("turn-to-cubes")));
			addCommand("drive-into-alley", new DriveStraight(robotContext, 30, dir*90, 1.0));
			addCommand("GetCube", new GetCube(robotContext));
		}
		
	}
}
