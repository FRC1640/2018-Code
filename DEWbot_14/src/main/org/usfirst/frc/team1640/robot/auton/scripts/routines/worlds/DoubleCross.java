package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.worlds;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.OpenIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeDrive;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.worlds.subscripts.FarScaleAndCube;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.GyroCorrectedOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.OcelotStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.steer.SteerStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class DoubleCross extends AutonScript {

	public DoubleCross(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		OcelotStrategy gyroCorrected = new GyroCorrectedOcelot(driveTrain, gyro, fieldCentric, 0.015, 0.00004, 0.0005, 0.9);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.02, 0.0, 0.0001, 0.9);
		ShortestAngleSunflower sunflower2CubeTurn = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.013, 0.0001, 0, 0.9);
		ShortestAngleSunflower largeTurn = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.013, 0, 0, 0.9);
		SunflowerStrategy sunflowerVision = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.05, 0, 0.001, 1.0);
		SteerStrategy defaultSteer = new DefaultSteer(driveTrain);
		ShortestAngleSunflower ocelotSunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0.00004, 0.0005, 0.9);
		
		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(robotContext.getIntake());
		IntakeTogetherStrategy intakeStrat2 = new DefaultIntakeTogether(intakeSeparately, 0.5, 1.0);
		
		
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
						
			if (dir != scSide) { // on opposite side of scale
				//drive toward alley
				addCommand("far-scale-and-cube", new FarScaleAndCube(robotContext, config));
				
				//back up
//				double distanceLeaveCube2 = 20;
//				double angleLeaveCube2 = 20;
//				addCommand("leave-cube-2", new DriveUntilInches(driveTrain, ocelotSunflower, distanceLeaveCube2, dir*angleLeaveCube2, 0.95)); 
//				addCommand("detect-inches-lift-2", new DetectInches(robotContext, 3));
//				addCommand("wait-detect-inches-lift-2", new WaitCommand(getCommand("detect-inches-lift-2")));
//				addCommand("lift-to-back-2", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleBack));
//				addCommand("wait-leave-cube-2", new WaitCommand(getCommand("leave-cube-2")));
				
				//turn to scale and lift
//				double angleToScale2 = 150;
//				addCommand("drive-to-scale-2", new DriveUntilInches(driveTrain, ocelotSunflower, 10, 0, 0.6));
//				addCommand("ocelot-time-2", new TimeCommand(0.1));
//				addCommand("wait-ocelot-time-2", new WaitCommand(getCommand("ocelot-time-2")));
//				addCommand("turn-to-scale-2", new TurnToDirection(ocelotSunflower, gyro, dir*angleToScale2, 10, 0.1, 3));
//				addCommand("stop-intake-2", new SetIntakeDrive(intakeStrat2, 0));
//				addCommand("wait-turn-to-scale-2", new WaitCommand(getCommand("turn-to-scale-2")));
				
				//drive to scale
//				addCommand("wait-drive-to-scale-2", new WaitCommand(getCommand("drive-to-scale-2")));
//				addCommand("wait-lift-to-back-2", new WaitCommand(getCommand("lift-to-back-2")));
				
				//OCELOT
				double distanceToScale2 = 20;
				addCommand("drive-to-scale-2", new DriveUntilInches(driveTrain, ocelotSunflower, distanceToScale2, 5, 0.6));
				addCommand("ocelot-time-2", new TimeCommand(0.1));
				addCommand("wait-ocelot-time-2", new WaitCommand(getCommand("ocelot-time-2")));
				addCommand("turn-to-scale-2", new TurnToDirection(ocelotSunflower, gyro, dir*-10, 6, 0.1, 3));
				addCommand("stop-intake-2", new SetIntakeDrive(intakeStrat2, 0));
				addCommand("lift-to-top-2", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
				addCommand("wait-turn-to-scale-2", new WaitCommand(getCommand("turn-to-scale-2")));
				addCommand("wait-drive-to-scale-2", new WaitCommand(getCommand("drive-to-scale-2")));
				addCommand("wait-lift-to-top-2", new WaitCommand(getCommand("lift-to-top-2")));
				
				addCommand("drive-to-drop-2", new DriveUntilInches(driveTrain, ocelotSunflower, 5, 0, 0.6));
				addCommand("wait-drive-to-drop-2", new WaitCommand(getCommand("drive-to-drop-2")));
				//DONE OCELOT
				
				//outtake 2nd cube
				addCommand("outtake-2", new SetIntakeDrive(intakeStrat2, -0.6));
			}
		}
		
	}
	
}
