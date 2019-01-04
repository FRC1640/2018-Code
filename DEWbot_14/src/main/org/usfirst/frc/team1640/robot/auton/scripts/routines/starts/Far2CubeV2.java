package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.detect.DetectInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.detect.DetectLiftHeight;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurveAndTimeout;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels.PointWheelsFieldCentric;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.OpenIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeDrive;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeExtending;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnAndMaintainDirection;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
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

public class Far2CubeV2 extends AutonScript {

	public Far2CubeV2(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		OcelotStrategy gyroCorrected = new GyroCorrectedOcelot(driveTrain, gyro, fieldCentric, 0.015, 0.00004, 0.0005, 0.9);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.02, 0.0, 0.0001, 0.9);
		ShortestAngleSunflower sunflower2CubeTurn = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0.00004, 0.0005, 0.9);
		ShortestAngleSunflower largeTurn = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.02, 0.00003, 0.0001, 0.9); //p = 0.012
		SunflowerStrategy sunflowerVision = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.05, 0, 0.001, 1.0);
		SteerStrategy defaultSteer = new DefaultSteer(driveTrain);
		ShortestAngleSunflower ocelotSunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.01, 0.00004, 0.0005, 0.9);
		ShortestAngleSunflower initialOcelotSunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0.00003, 0.0005, 0.9);
		ShortestAngleSunflower leftOcelotSunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.008, 0.00003, 0.0005, 0.9);

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
			
			addCommand("offset-gyro", new OffsetGyro(gyro, dir*90));
			
			if (dir != scSide) { // on opposite side of scale
				//drive toward alley
				double initialDistance = dir == 1 ? 208 : 211; //206 //220 or 217
				ShortestAngleSunflower initialOcelot = dir == 1 ? initialOcelotSunflower : leftOcelotSunflower;
				double initialAngle = dir == 1 ? 0 : -3;
				addCommand("approach-alley-PW-1", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, 0));
				addCommand("wait-approach-alley-PW-1", new WaitCommand(getCommand("approach-alley-PW-1")));
				addCommand("approach-alley-1", new DriveAlongTrapezoidalCurve(driveTrain, initialOcelot, initialDistance, initialAngle, 0.8, 0.5, 0.01));
//				addCommand("time-turn-alley-1", new TimeCommand(0.05));
//				addCommand("wait-time-turn-alley-1", new WaitCommand(getCommand("time-turn-alley-1")));
				addCommand("turn-to-alley-1", new TurnAndMaintainDirection(driveTrain, initialOcelotSunflower, gyro, dir*90, 3, 0.1, initialDistance));
				addCommand("wait-approach-alley-1", new WaitCommand(getCommand("approach-alley-1")));

				//drive through alley
//				addCommand("time-delay", new TimeCommand(2.5));
//				addCommand("wait-time-delay", new WaitCommand(getCommand("time-delay")));
				double alleyDistance = 190; //worlds - 188
				addCommand("through-alley-PW-1", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, dir*90));
				addCommand("wait-through-alley-PW-1", new WaitCommand(getCommand("through-alley-PW-1")));
				addCommand("through-alley-1", new DriveAlongTrapezoidalCurve(driveTrain, initialOcelotSunflower, alleyDistance, dir*90, 0.8, 1, 0.01));
				addCommand("detect-inches-lift-1", new DetectInches(robotContext, 80));
				addCommand("wait-detect-inches-lift-1", new WaitCommand(getCommand("detect-inches-lift-1")));
				addCommand("lift-to-top-1", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
				addCommand("turn-through-alley-1", new TurnAndMaintainDirection(driveTrain, initialOcelotSunflower, gyro, dir*90, 3, 0.1, alleyDistance));
				addCommand("wait-through-alley-1", new WaitCommand(getCommand("through-alley-1")));
				
				//turn/put arm over back
				addCommand("turn-to-scale-1", new TurnToDirection(largeTurn, gyro, dir*165, 6, 0.1, 3));
				addCommand("wait-turn-to-scale-1", new WaitCommand(getCommand("turn-to-scale-1")));

				//drive to scale
				addCommand("drive-to-scale-PW-1", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, dir*-10));
				addCommand("wait-drive-to-scale-PW-1", new WaitCommand(getCommand("drive-to-scale-PW-1")));
				addCommand("drive-to-scale-1", new DriveUntilInches(driveTrain, gyroCorrected, 35, dir*-10, 0.5));
				addCommand("lift-to-back-1", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleBack));
				addCommand("wait-drive-to-scale-1", new WaitCommand(getCommand("drive-to-scale-1")));
				addCommand("wait-lift-to-scale-1", new WaitCommand(getCommand("lift-to-back-1")));
				
				//place cube 1
				addCommand("outtake-1", new DriveIntakeTime(robotContext, -0.7, 0.5)); //-0.75
				addCommand("wait-outtake-1", new WaitCommand(getCommand("outtake-1")));
				
				//-----------------------2nd cube----------------
				
				//lower lift and turn towards cube
				double distanceToCube2 = 45;
				double angleToCube2 = -170;
				addCommand("lower-placer-2", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
				addCommand("lift-height-to-drive-2", new DetectLiftHeight(robotContext, 30));
				addCommand("turn-to-cube-2", new TurnToDirection(sunflower2CubeTurn, gyro, dir*angleToCube2, 6, 0.1, 3));
				addCommand("open-intake-2", new SetIntakeExtending(intakeStrat2, true));
				addCommand("start-intake-2", new SetIntakeDrive(intakeStrat2, 1.0));
				addCommand("wait-turn-to-cube-2", new WaitCommand(getCommand("turn-to-cube-2")));

				
				//drive towards cube
				addCommand("wait-lift-height-to-drive-2", new WaitCommand(getCommand("lift-height-to-drive-2")));
				addCommand("drive-to-cube-2", new DriveAlongTrapezoidalCurveAndTimeout(driveTrain, ocelotSunflower, distanceToCube2, dir*angleToCube2, 1.0, 2)); //LOWERED TIMEOUT
				addCommand("wait-drive-to-cube-2", new WaitCommand(getCommand("drive-to-cube-2")));
				
				//intake
				addCommand("close-intake-2", new SetIntakeExtending(intakeStrat2, false));
				addCommand("intake-time-2", new TimeCommand(0.1));
				addCommand("wait-intake-time-2", new WaitCommand(getCommand("intake-time-2")));
				
				//DONT USE 4/21 //back up
				double distanceLeaveCube2 = 27;
				double angleLeaveCube2 = 20;
				addCommand("leave-cube-2", new DriveUntilInches(driveTrain, ocelotSunflower, distanceLeaveCube2, dir*angleLeaveCube2, 0.95)); 
				addCommand("detect-inches-lift-2", new DetectInches(robotContext, 3));
				addCommand("wait-detect-inches-lift-2", new WaitCommand(getCommand("detect-inches-lift-2")));
				addCommand("lift-to-back-2", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleBack));
				addCommand("wait-leave-cube-2", new WaitCommand(getCommand("leave-cube-2")));
				
				//turn to scale and lift
				double angleToScale2 = 160;
//				addCommand("drive-to-scale-2", new DriveUntilInches(driveTrain, ocelotSunflower, 10, 0, 0.6));
//				addCommand("ocelot-time-2", new TimeCommand(0.1));
//				addCommand("wait-ocelot-time-2", new WaitCommand(getCommand("ocelot-time-2")));
				addCommand("turn-to-scale-2", new TurnToDirection(largeTurn, gyro, dir*angleToScale2, 6, 0.1, 3));
//				addCommand("stop-intake-2", new SetIntakeDrive(intakeStrat2, 0));
				addCommand("wait-turn-to-scale-2", new WaitCommand(getCommand("turn-to-scale-2")));
				
				//drive to scale
//				addCommand("wait-drive-to-scale-2", new WaitCommand(getCommand("drive-to-scale-2")));
//				addCommand("wait-lift-to-back-2", new WaitCommand(getCommand("lift-to-back-2")));
				
				//dont use 4/21//OCELOT
				double distanceToScale2 = 35;
//				addCommand("drive-to-scale-2", new DriveUntilInches(driveTrain, ocelotSunflower, distanceToScale2, 5, 0.6));
//				addCommand("wait-drive-to-scale-2", new WaitCommand(getCommand("drive-to-scale-2")));
//
//				addCommand("turn-to-scale-2", new TurnToDirection(largeTurn, gyro, dir*10, 6, 0.1, 3));
//				addCommand("stop-intake-2", new SetIntakeDrive(intakeStrat2, 0));
//				addCommand("lift-to-top-2", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
//				addCommand("wait-turn-to-scale-2", new WaitCommand(getCommand("turn-to-scale-2")));
//				addCommand("wait-lift-to-top-2", new WaitCommand(getCommand("lift-to-top-2")));
				//DONE OCELOT
				
				//OVER BACK
//				addCommand("lift-over-back-2")
//				addCommand("drive-to-scale-2", new DriveUntilInches(driveTrain, ocelotSunflower, distanceToScale2, 5, 0.6));
//				addCommand("wait-drive-to-scale-2", new WaitCommand(getCommand("drive-to-scale-2")));
				
				//outtake 2nd cube
//				addCommand("time-outtake2", new TimeCommand(0.75));
//				addCommand("wait-time-outtake2", new WaitCommand(getCommand("time-outtake2")));
				addCommand("wait-lift-to-back-2", new WaitCommand(getCommand("lift-to-back-2")));
				addCommand("outtake-2", new SetIntakeDrive(intakeStrat2, -0.9));
			}
		}
		
	}
	
}
