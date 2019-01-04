package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts;

import main.org.usfirst.frc.team1640.constants.auton.AutonConstants;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.TurnToCubeScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class Start2Sc2Cube12Sc extends AutonScript {
	
	public Start2Sc2Cube12Sc(IRobotContext robotContext, FieldConfig config) {
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
		default: System.out.println("Invalid starting location");
		}
		
		addCommand("Start2Scale", new Start2Sc(robotContext, config));
		addCommand("drop-time", new TimeCommand(0.5));
		addCommand("wait-drop-time", new WaitCommand(getCommand("drop-time")));
		addCommand("wait-Start2Scale", new WaitCommand(getCommand("Start2Scale")));
		addCommand("drive-to-alley", new DriveStraight(robotContext, 50.25, 180, 1.0));
		addCommand("wait-drive-to-alley", new WaitCommand(getCommand("drive-to-alley")));
		if (AutonConstants.USE_VISION) {
			addCommand("intake", new DriveIntakeTime(robotContext, 1.0, 5));
			addCommand("move-to-cube", new TurnToCubeScript(robotContext));
			addCommand("wait-move-to-cube", new WaitCommand(getCommand("move-to-cube")));
		}
		else {
			addCommand("turn-to-cubes", new TurnToDirection(sunflower, gyro, dir*137.4, 2, 0.1, 1));
			addCommand("wait-turn-to-cubes", new WaitCommand(getCommand("turn-to-cubes")));
			addCommand("intake", new DriveIntakeTime(robotContext, 1.0, 3));
			addCommand("drive-into-alley", new DriveStraight(robotContext, 18, dir*137.4, 1.0));
			addCommand("wait-drive-into-alley", new WaitCommand(getCommand("drive-into-alley")));
		}
		addCommand("drive-outta-alley", new DriveStraight(robotContext, 6, -dir*15, 1.0));
		addCommand("wait-drive-outta-alley", new WaitCommand(getCommand("drive-outta-alley")));
		addCommand("turn-to-place", new TurnToDirection(sunflower, gyro, 180+dir*30, 2, 0.1, 2));
		addCommand("wait-turn-to-place", new WaitCommand(getCommand("turn-to-place")));
		addCommand("lift-to-scale-back", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleBack));
		addCommand("drive-to-scale", new DriveStraight(robotContext, 62, 0, 1.0));
		addCommand("wait-drive-to-scale", new WaitCommand(getCommand("drive-to-scale")));
		addCommand("outtake", new DriveIntakeTime(robotContext, -0.5, 1));
	}
}
