package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.detect.DetectAngle;
import main.org.usfirst.frc.team1640.robot.auton.commands.detect.DetectInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeDrive;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeExtending;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.commands.vision.TurnToDirectionVision;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraightAndTimeout;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.robot.intake.together.CurrentStopIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.GyroCorrectedOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class Start2Sc2Cube2Sc extends AutonScript {

	public Start2Sc2Cube2Sc(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.0225, 0.0, 0.001, 0.9);
		SunflowerStrategy sunflowerVision = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.05, 0, 0.001, 1.0);
		ShortestAngleSunflower farTurn = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.02, 0.0, 0.001, 0.9);
		
		GyroCorrectedOcelot gyroCorrectedOcelot = new GyroCorrectedOcelot(driveTrain, gyro, ocelot);
		CurrentStopIntakeTogether intakeStrat = new CurrentStopIntakeTogether(new DefaultIntakeTogether(new DefaultIntakeSeparately(robotContext.getIntake()), 0.5, 1.0));
		
		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(robotContext.getIntake());
		IntakeTogetherStrategy intakeStrat2 = new DefaultIntakeTogether(intakeSeparately, 0.5, 1.0);
		
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = -1; break;
		case Right: dir = 1; break;
		default: System.out.println("Start2Sc2Cube2Sc: Invalid starting location: " + config.getStartPos().name());
		}
		
		int scSide = 0;
		
		switch(config.getScPos()) {
		case Left: scSide = -1; break;
		case Right: scSide = 1; break;
		default: System.out.println("Start2Sc2Cube2Sc: Invalid scale position: " + config.getScPos().name());
		}
		
		if (dir != 0) {
			
			addCommand("offset-gyro", new OffsetGyro(gyro, dir*90));
			
			if (dir == scSide) { // on same side as scale
				//drive to scale
				addCommand("approach-scale", new DriveStraight(robotContext, 270, 0, 0.8)); 
				
				//lift placer
				addCommand("time-before-lift", new TimeCommand(1)); 
				addCommand("wait-time-before-lift", new WaitCommand(getCommand("time-before-lift")));
				addCommand("lift-to-scale-height", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
				addCommand("wait-approach-scale", new WaitCommand(getCommand("approach-scale")));
				addCommand("wait-lift-to-scale-height", new WaitCommand(getCommand("lift-to-scale-height")));
				
				//outtake and drive away
				addCommand("place", new DriveIntakeTime(robotContext, -1, 0.5));
				addCommand("approach-cube", new DriveStraight(robotContext, 50, 155, 1.0));
				addCommand("wait-place", new WaitCommand(getCommand("place")));

				//lower arm
				addCommand("distance-before-lower-arm", new DetectInches(robotContext, 25));
				addCommand("wait-time-before-lower-arm", new WaitCommand(getCommand("distance-before-lower-arm")));
				addCommand("lift-to-floor", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
				addCommand("wait-approach-cube", new WaitCommand(getCommand("approach-cube")));
				
				//turn and begin intaking
				addCommand("turn-to-cube", new TurnToDirection(sunflower, gyro, dir*155, 6, 0.25, 3));
				addCommand("open-intake", new SetIntakeExtending(intakeStrat2, true));
				addCommand("start-intake", new SetIntakeDrive(intakeStrat2, 1.0));
				addCommand("wait-turn-to-cube", new WaitCommand(getCommand("turn-to-cube")));
				
				//vision turn
				addCommand("turn-vision", new TurnToDirectionVision(robotContext.getVisionServer(), sunflowerVision, gyro, 6.0, 0.1, 1));
				addCommand("wait-turn-vision", new WaitCommand(getCommand("turn-vision")));
				addCommand("wait-lift-to-floor", new WaitCommand(getCommand("lift-to-floor")));

				
				//go towards cube and close intake
				addCommand("drive-to-cube", new DriveStraightAndTimeout(robotContext, 48, dir*155, 1.0, 1.15)); //43 and 1.15s //LOWERED TIMEOUT
				addCommand("wait-drive-to-cube", new WaitCommand(getCommand("drive-to-cube")));
				addCommand("close-intake", new SetIntakeExtending(intakeStrat2, false));
				addCommand("intake-time", new TimeCommand(0.25));
				addCommand("wait-intake-time", new WaitCommand(getCommand("intake-time")));
				
				//drive away and lift placer
				addCommand("drive-back", new DriveUntilInches(driveTrain, sunflower, 80, -dir*25, 0.6)); 
				addCommand("time-close", new TimeCommand(0.15));
				addCommand("wait-time-close", new WaitCommand(getCommand("time-close")));
				addCommand("lift-to-scale", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
//				addCommand("turn-to-dunk", new TurnToDirection(sunflower, gyro, dir*50, 5, 0.05, 3)); //POTENTIAL OCELOT?
				addCommand("wait-drive-back", new WaitCommand(getCommand("drive-back")));
				
				//turn
				addCommand("turn-to-dunk", new TurnToDirection(farTurn, gyro, dir*90, 5, 0.05, 3)); //CHANGED ANGLE
				addCommand("stop-intake", new SetIntakeDrive(intakeStrat2, 0.0));
				addCommand("wait-lift-to-scale", new WaitCommand(getCommand("lift-to-scale")));
				addCommand("wait-turn-to-place", new WaitCommand(getCommand("turn-to-dunk")));
				
				//shoot
//				addCommand("angle-throw", new DetectAngle(gyro, 70, false));
//				addCommand("wait-throw", new WaitCommand(getCommand("angle-throw")));
				addCommand("place-cube-2", new DriveIntakeTime(robotContext, -0.7, 2.0));
				addCommand("wait-place-cube-2", new WaitCommand(getCommand("place-cube-2")));
				
			}
			else { // on opposite side of scale
				
				// FAR SCALE IS NOT WRITTEN YET
				
				addCommand("approach-alley", new DriveStraight(robotContext, 206, 0, 1.0)); //218
				addCommand("wait-approach-alley", new WaitCommand(getCommand("approach-alley")));
				addCommand("delay", new TimeCommand(2));
				addCommand("wait-delay", new WaitCommand(getCommand("delay")));
				addCommand("through-alley", new DriveStraight(robotContext, 178, dir*90, 1.0)); //184 //160;
				addCommand("lift-to-scale", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
				addCommand("wait-through-alley", new WaitCommand(getCommand("through-alley")));
				addCommand("turn-to-scale", new TurnToDirection(sunflower, gyro, -dir*20, 4, 0.2, 1.5));
				addCommand("wait-turn-to-scale", new WaitCommand(getCommand("turn-to-scale")));
				addCommand("drive-to-scale", new DriveStraight(robotContext, 48, 0, 1.0)); //62;
				addCommand("wait-drive-to-scale", new WaitCommand(getCommand("drive-to-scale")));
				addCommand("wait-lift-to-scale", new WaitCommand(getCommand("lift-to-scale")));
				addCommand("outtake", new DriveIntakeTime(robotContext, -0.5, 2));
			}
		}
		
	}
	
}
