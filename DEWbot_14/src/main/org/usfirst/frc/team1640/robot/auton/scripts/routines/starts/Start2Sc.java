package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.OpenIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class Start2Sc extends AutonScript {

	public Start2Sc(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.02, 0.0, 0.0001, 0.9);
		
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = -1; break;
		case Right: dir = 1; break;
		default: System.out.println("Start2Sc: Invalid starting location: " + config.getStartPos().name());
		}
		
		int scSide = 0;
		
		switch(config.getScPos()) {
		case Left: scSide = -1; break;
		case Right: scSide = 1; break;
		default: System.out.println("Start2Sc: Invalid scale position: " + config.getScPos().name());
		}
		
		if (dir != 0) {
			
			addCommand("offset-gyro", new OffsetGyro(gyro, dir*90));
			
			if (dir == scSide) { // on same side as scale
				addCommand("approach-scale", new DriveStraight(robotContext, 290, 0, 1.0)); //274 //264.5
				addCommand("time-before-lift", new TimeCommand(1)); // wait a bit of time before raising the lift to the scale height
				addCommand("wait-time-before-lift", new WaitCommand(getCommand("time-before-lift")));
				addCommand("lift-to-scale-height", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
				addCommand("wait-approach-scale", new WaitCommand(getCommand("approach-scale")));
//				addCommand("turn", new TurnToDirection(sunflower, gyro, dir*35, 5, 0.05, 3));
//				addCommand("wait-turn", new WaitCommand(getCommand("turn")));
				addCommand("open-intake", new OpenIntakeTime(robotContext, 1));
				addCommand("wait-lift-to-scale-height", new WaitCommand(getCommand("lift-to-scale-height")));
				addCommand("wait-open-intake", new WaitCommand(getCommand("open-intake")));
//				addCommand("outtake", new DriveIntakeTime(robotContext, -0.75, 1.5));
//				addCommand("outtake", new SetIntakeExtending(robotContext, true)); is this necessary?
			}
			else { // on opposite side of scale
				addCommand("approach-alley", new DriveStraight(robotContext, 206, 0, 1.0)); //218
				addCommand("wait-approach-alley", new WaitCommand(getCommand("approach-alley")));
//				addCommand("delay", new TimeCommand(2));
//				addCommand("wait-delay", new WaitCommand(getCommand("delay")));
				addCommand("through-alley", new DriveStraight(robotContext, 184, dir*90, 1.0)); //184 //160;
				addCommand("lift-to-scale", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
				addCommand("wait-through-alley", new WaitCommand(getCommand("through-alley")));
				addCommand("turn-to-scale", new TurnToDirection(sunflower, gyro, -dir*20, 4, 0.2, 1.5));
				addCommand("wait-turn-to-scale", new WaitCommand(getCommand("turn-to-scale")));
				addCommand("drive-to-scale", new DriveStraight(robotContext, 48, 0, 1.0)); //62;
				addCommand("wait-drive-to-scale", new WaitCommand(getCommand("drive-to-scale")));
				addCommand("wait-lift-to-scale", new WaitCommand(getCommand("lift-to-scale")));
				addCommand("outtake", new DriveIntakeTime(robotContext, -0.6, 1.5));
				addCommand("wait-outtake", new WaitCommand(getCommand("outtake")));
				
				addCommand("drive-away", new DriveStraight(robotContext, -48, 0, 1.0));
				addCommand("wait-drive-away", new WaitCommand(getCommand("drive-away")));
			}
		}
		
	}
	
}
