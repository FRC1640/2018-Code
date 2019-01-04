package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.detect.DetectInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilSeconds;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeDrive;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeExtending;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnAndMaintainDirection;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class DoubleSideSwitch extends AutonScript {

	public DoubleSideSwitch(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.0225, 0.0, 0.001, 0.9);
		SunflowerStrategy sunflowerVision = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.05, 0, 0.001, 1.0);
		ShortestAngleSunflower ocelotSunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.01, 0.000028, 0.0005, 0.9);
		ShortestAngleSunflower ocelotSunflowerTurn = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.02, 0.000028, 0.0005, 0.9);

		
		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(robotContext.getIntake());
		IntakeTogetherStrategy intakeStrat2 = new DefaultIntakeTogether(intakeSeparately, 0.5, 1.0);
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = -1; break;
		case Right: dir = 1; break;
		default: System.out.println("DoubleSideSwitch: Invalid starting location: " + config.getStartPos().name());
		}
		
		int scSide = 0;
		int swSide = 0;
		
		switch(config.getScPos()) {
		case Left: scSide = -1; break;
		case Right: scSide = 1; break;
		default: System.out.println("DoubleSideSwitch: Invalid scale position: " + config.getScPos().name());
		}
		switch(config.getSwPos()) {
		case Left: swSide = -1; break;
		case Right: swSide = 1; break;
		default: System.out.println("DoubleSideSwitch: Invalid switch position: " + config.getSwPos().name());
		}
		
		if (dir != 0) {
			
			addCommand("offset-gyro", new OffsetGyro(gyro, dir*90));
			
			if (dir == scSide) { // on same side as scale
//				addCommand("approach-scale", new DriveStraight(robotContext, 290, 0, 1.0)); //274 //264.5
//				addCommand("time-before-lift", new TimeCommand(1)); // wait a bit of time before raising the lift to the scale height
//				addCommand("wait-time-before-lift", new WaitCommand(getCommand("time-before-lift")));
//				addCommand("lift-to-scale-height", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
//				addCommand("wait-approach-scale", new WaitCommand(getCommand("approach-scale")));
////				addCommand("turn", new TurnToDirection(sunflower, gyro, dir*35, 5, 0.05, 3));
////				addCommand("wait-turn", new WaitCommand(getCommand("turn")));
//				addCommand("open-intake", new OpenIntakeTime(robotContext, 1));
//				addCommand("wait-lift-to-scale-height", new WaitCommand(getCommand("lift-to-scale-height")));
//				addCommand("wait-open-intake", new WaitCommand(getCommand("open-intake")));
////				addCommand("outtake", new DriveIntakeTime(robotContext, -0.75, 1.5));
////				addCommand("outtake", new SetIntakeExtending(robotContext, true)); is this necessary?
			}
			else if(dir == swSide){ // on opposite side of scale
				double initialDistance = 235;
				double strafeAngle = dir==1 ? 5 : 6;
				addCommand("approach-alley", new DriveAlongTrapezoidalCurve(driveTrain, ocelotSunflower, initialDistance, dir*strafeAngle, 1.0)); //218
				addCommand("lift-placer-switch", new PlacerPresetCommand(robotContext, PlacerPreset.Switch));
				addCommand("time-turn-1", new TimeCommand(0.25));
				addCommand("wait-time-turn-1", new WaitCommand(getCommand("time-turn-1")));
				addCommand("turn-to-scale-1", new TurnAndMaintainDirection(driveTrain, ocelotSunflower, gyro, dir*86, 3, 0.1, initialDistance));
				addCommand("wait-lift", new WaitCommand(getCommand("lift-placer-switch")));
				
				
				addCommand("detect-spit", new DetectInches(robotContext, 100));
				addCommand("wait-detect-spit", new WaitCommand(getCommand("detect-spit")));
				
				addCommand("spit", new DriveIntakeTime(robotContext, -1, 1));
				addCommand("wait-approach-alley", new WaitCommand(getCommand("approach-alley")));
				
				addCommand("lift-floor", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));

				addCommand("turn-to-cube", new TurnToDirection(ocelotSunflower, gyro, dir*155, 6, 0.25, 3));
				addCommand("open-intake", new SetIntakeExtending(intakeStrat2, true));
				addCommand("start-intake", new SetIntakeDrive(intakeStrat2, 1.0));
				addCommand("time-drive", new TimeCommand(0.2));
				addCommand("wait-time-drive", new WaitCommand(getCommand("time-drive")));
//				addCommand("wait-turn-to-cube", new WaitCommand(getCommand("turn-to-cube")));
				addCommand("wait-lift", new WaitCommand(getCommand("lift-floor")));
				
				addCommand("drive-to-cube", new DriveUntilSeconds(ocelotSunflower, 0.9, dir*155, 0.7)); //LOWERED TIMEOUT
				addCommand("wait-drive-to-cube", new WaitCommand(getCommand("drive-to-cube")));
				addCommand("close-intake", new SetIntakeExtending(intakeStrat2, false));
				
				//!!!!!NEW!!!!!!
				
				//backup and lift arm
				addCommand("drive-back", new DriveUntilInches(driveTrain, sunflower, 9, -dir*15, 0.6)); 
				addCommand("time-close", new TimeCommand(0.3));
				addCommand("wait-time-close", new WaitCommand(getCommand("time-close")));
				addCommand("lift-to-transport", new PlacerPresetCommand(robotContext, PlacerPreset.CubeTravel));
				addCommand("wait-drive-back", new WaitCommand(getCommand("drive-back")));
				addCommand("time-get-cube", new TimeCommand(0.25));
				addCommand("wait-time-get-cube", new WaitCommand(getCommand("time-get-cube")));
				addCommand("drive-forward", new DriveUntilSeconds(sunflower, 1.1, -dir*205, 0.6));
				addCommand("wait-lift-transport", new WaitCommand(getCommand("lift-to-transport")));
				addCommand("wait-drive-forward", new WaitCommand(getCommand("drive-forward")));

				addCommand("outtake", new SetIntakeDrive(intakeStrat2, -1));
				
				//NEW CODE
				double cube3Distance = dir == 1 ? 30 : 25;
				addCommand("drive-back-to-alley", new DriveUntilInches(driveTrain, sunflower, cube3Distance, dir*40, 0.6));
				addCommand("time-turn", new TimeCommand(0.05));
				addCommand("wait-time-turn", new WaitCommand(getCommand("time-turn")));
				addCommand("turn-to-cube3", new TurnAndMaintainDirection(driveTrain, ocelotSunflowerTurn, gyro, dir*180, 6, 0.1, 7));
				addCommand("wait-turn-to-cube3", new WaitCommand(getCommand("turn-to-cube3")));
				addCommand("wait-drive-back-to-alley", new WaitCommand(getCommand("drive-back-to-alley")));
				
				addCommand("lift-to-floor", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
				addCommand("open-intake-2", new SetIntakeExtending(intakeStrat2, true));
				addCommand("start-intake-2", new SetIntakeDrive(intakeStrat2, 1.0));
				addCommand("wait-lift-floor", new WaitCommand(getCommand("lift-to-floor")));
				
				addCommand("approach-cube3", new DriveUntilSeconds(ocelotSunflower, 1.3, dir*180, 0.6));
				addCommand("wait-approach-cube3", new WaitCommand(getCommand("approach-cube3")));
				addCommand("close-intake", new SetIntakeExtending(intakeStrat2, false));
				addCommand("wait-close-intake", new WaitCommand(getCommand("close-intake")));
				
				addCommand("backup", new DriveUntilInches(driveTrain, sunflower, 5, dir*0, 0.6));
				addCommand("wait-backup", new WaitCommand(getCommand("backup")));
				
				//OLD CODE
//				addCommand("drive-back-to-alley", new DriveUntilInches(driveTrain, sunflower, 17, -dir*25, 0.6));
//				addCommand("wait-alley", new WaitCommand(getCommand("drive-back-to-alley")));
//
//				addCommand("lift-to-floor", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
//				addCommand("open-intake-2", new SetIntakeExtending(intakeStrat2, true));
//				addCommand("start-intake-2", new SetIntakeDrive(intakeStrat2, 1.0));
//				addCommand("wait-lift-floor", new WaitCommand(getCommand("lift-to-floor")));
//				
//				double cube3Angle = 135;
//				addCommand("approach-scale-1", new DriveUntilSeconds(ocelotSunflower, 1.3, dir*cube3Angle, 0.6)); 
//				addCommand("time-turn-1", new TimeCommand(0.05));
//				addCommand("wait-time-turn-1", new WaitCommand(getCommand("time-turn-1")));
//				addCommand("turn-to-scale-1", new TurnAndMaintainDirection(driveTrain, ocelotSunflowerTurn, gyro, cube3Angle, 6, 0.1, 10));
//				addCommand("wait-turn", new WaitCommand(getCommand("approach-scale-1")));
//				addCommand("wait-drive", new WaitCommand(getCommand("turn-to-scale-1")));
//				addCommand("close-intake-2", new SetIntakeExtending(intakeStrat2, false));
//				addCommand("wait-close-intake-2", new WaitCommand(getCommand("close-intake-2")));

				
				
				//turn to alley
//				addCommand("turn-to-alley", new TurnToDirection(sunflower, gyro, dir*90, 5, 0.05, 3)); //CHANGED ANGLE
//				addCommand("stop-intake", new SetIntakeDrive(intakeStrat2, 0.0));
//				addCommand("wait-turn-to-place", new WaitCommand(getCommand("turn-to-alley")));

				//drive through alley
//				addCommand("drive-alley", new DriveStraight(robotContext, 184, dir*90, 1.0)); 
//				addCommand("wait-drive-alley", new WaitCommand(getCommand("drive-alley")));
//				addCommand("wait-lift-to-transport", new WaitCommand(getCommand("lift-to-transport")));
				
				//place cube
//				addCommand("lift-to-scale", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
//				addCommand("turn-to-scale", new TurnToDirection(sunflower, gyro, -dir*20, 4, 0.2, 1.5));
//				addCommand("wait-turn-to-scale", new WaitCommand(getCommand("turn-to-scale")));
//				addCommand("wait-lift-to-scale", new WaitCommand(getCommand("lift-to-scale")));
//				addCommand("drive-to-scale", new DriveStraight(robotContext, 48, 0, 1.0)); //62;
//				addCommand("wait-drive-to-scale", new WaitCommand(getCommand("drive-to-scale")));
//				addCommand("outtake", new DriveIntakeTime(robotContext, -0.6, 1.5));
//				addCommand("wait-outtake", new WaitCommand(getCommand("outtake")));

				

				
////				addCommand("delay", new TimeCommand(2));
////				addCommand("wait-delay", new WaitCommand(getCommand("delay")));
//				addCommand("through-alley", new DriveStraight(robotContext, 184, dir*90, 1.0)); //184 //160;
//				addCommand("lift-to-scale", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
//				addCommand("wait-through-alley", new WaitCommand(getCommand("through-alley")));
//				addCommand("turn-to-scale", new TurnToDirection(sunflower, gyro, -dir*20, 4, 0.2, 1.5));
//				addCommand("wait-turn-to-scale", new WaitCommand(getCommand("turn-to-scale")));
//				addCommand("drive-to-scale", new DriveStraight(robotContext, 48, 0, 1.0)); //62;
//				addCommand("wait-drive-to-scale", new WaitCommand(getCommand("drive-to-scale")));
//				addCommand("wait-lift-to-scale", new WaitCommand(getCommand("lift-to-scale")));
//				addCommand("outtake", new DriveIntakeTime(robotContext, -0.6, 1.5));
//				addCommand("wait-outtake", new WaitCommand(getCommand("outtake")));
//				
//				addCommand("drive-away", new DriveStraight(robotContext, -48, 0, 1.0));
//				addCommand("wait-drive-away", new WaitCommand(getCommand("drive-away")));
			}
		}
		
	}
	
}
