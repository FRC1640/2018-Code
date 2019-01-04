package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.worlds.subscripts;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.detect.DetectInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels.PointWheelsFieldCentric;
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
import main.org.usfirst.frc.team1640.robot.intake.together.CurrentStopIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.GyroCorrectedOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.steer.SteerStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class DoubleCloseScaleAndCube extends AutonScript {

	public DoubleCloseScaleAndCube(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.02, 0.0, 0.0001, 0.9);
		ShortestAngleSunflower ocelotSunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0.00004, 0.0005, 0.9);
		ShortestAngleSunflower ocelotSunflowerRough = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0, 0.0005, 0.9);

		SteerStrategy defaultSteer = new DefaultSteer(driveTrain);
		SunflowerStrategy sunflowerVision = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.038, 0, 0.001, 1.0);
		
		GyroCorrectedOcelot gyroCorrectedOcelot = new GyroCorrectedOcelot(driveTrain, gyro, ocelot);
		CurrentStopIntakeTogether intakeStrat = new CurrentStopIntakeTogether(new DefaultIntakeTogether(new DefaultIntakeSeparately(robotContext.getIntake()), 0.5, 1.0));
		
		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(robotContext.getIntake());
		IntakeTogetherStrategy intakeStrat2 = new DefaultIntakeTogether(intakeSeparately, 0.5, 1.0);
		
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = -1; break;
		case Right: dir = 1; break;
		default: System.out.println("DoubleCloseAndCube: Invalid starting location: " + config.getStartPos().name());
		}
		
		int scSide = 0;
		
		switch(config.getScPos()) {
		case Left: scSide = -1; break;
		case Right: scSide = 1; break;
		default: System.out.println("DoubleCloseAndCube: Invalid scale position: " + config.getScPos().name());
		}
		
		if (dir != 0) {
			
			addCommand("offset-gyro", new OffsetGyro(gyro, dir*90));
			
			if (dir == scSide) { // on same side as scale
				
				//approach scale, lift placer, outtake cube
				double initialDistance = 260;
				addCommand("approach-scale-PW-1", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, 0));
				addCommand("wait-approach-scale-PW-1", new WaitCommand(getCommand("approach-scale-PW-1")));
				addCommand("approach-scale-1", new DriveAlongTrapezoidalCurve(driveTrain, ocelotSunflower, initialDistance, 0, 1, 0.7, 0.01)); 
				addCommand("time-turn-1", new TimeCommand(0.1));
				addCommand("wait-time-turn-1", new WaitCommand(getCommand("time-turn-1")));
				addCommand("turn-to-scale-1", new TurnAndMaintainDirection(driveTrain, ocelotSunflower, gyro, dir*52, 6, 0.1, initialDistance));
				addCommand("inches-to-raise-lift-1", new DetectInches(robotContext, 100));
				addCommand("wait-inches-to-lift-1", new WaitCommand(getCommand("inches-to-raise-lift-1")));
				addCommand("raise-lift-1", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
				addCommand("inches-to-outtake-1", new DetectInches(robotContext, initialDistance - 10));
				addCommand("wait-inches-to-outtake-1", new WaitCommand(getCommand("inches-to-outtake-1")));
				addCommand("outtake-cube-1", new DriveIntakeTime(robotContext, -0.65, 1.5));
				addCommand("wait-turn-to-scale-1", new WaitCommand(getCommand("turn-to-scale-1")));
				addCommand("wait-approach-scale-1", new WaitCommand(getCommand("approach-scale-1")));
				addCommand("wait-raise-lift-1", new WaitCommand(getCommand("raise-lift-1")));
				
				
				addCommand("time-outtake-1", new TimeCommand(0.25));
				addCommand("wait-time-outtake", new WaitCommand(getCommand("time-outtake-1")));
				
				//-------------------2nd cube--------------------
				
				//drive to 2nd cube
				double angleToCube2 = dir*147;
				double distanceToCube2 = 60;
				addCommand("approach-cube-PW-2", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, angleToCube2));
				addCommand("wait-approach-cube-PW-2", new WaitCommand(getCommand("approach-cube-PW-2")));
				addCommand("approach-cube-2", new DriveAlongTrapezoidalCurve(driveTrain, ocelotSunflower, distanceToCube2, angleToCube2, 1, 0.8, 0.01)); 
				addCommand("time-turn-cube-2", new TimeCommand(0.05));
				addCommand("wait-time-turn-2", new WaitCommand(getCommand("time-turn-cube-2")));
				addCommand("ocelot-turn-to-cube-2", new TurnAndMaintainDirection(driveTrain, ocelotSunflower, gyro, angleToCube2, 7, 0.1, distanceToCube2));
				addCommand("turn-to-cube-2", new TurnToDirection(ocelotSunflower, gyro, angleToCube2, 7, 0.1, 3));
				addCommand("lower-lift-2", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
				addCommand("wait-outtake-cube-1", new WaitCommand(getCommand("outtake-cube-1")));
				addCommand("open-intake-2", new SetIntakeExtending(intakeStrat2, true));
				addCommand("start-intake-2", new SetIntakeDrive(intakeStrat2, 1.0));
				addCommand("wait-turn-to-cube-2", new WaitCommand(getCommand("turn-to-cube-2")));
				addCommand("wait-approach-cube-2", new WaitCommand(getCommand("approach-cube-2")));

				//intake 2nd cube
				addCommand("drive-to-cube-PW-2", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, angleToCube2));
				addCommand("wait-drive-to-cube-PW-2", new WaitCommand(getCommand("drive-to-cube-PW-2")));
				addCommand("drive-to-cube-2", new DriveUntilSeconds(ocelotSunflower, 0.6, angleToCube2, 0.9)); 
				addCommand("wait-drive-to-cube-2", new WaitCommand(getCommand("drive-to-cube-2")));
				addCommand("close-intake-2", new SetIntakeExtending(intakeStrat2, false));
				addCommand("intake-time-2", new TimeCommand(0.15));
				addCommand("wait-intake-time-2", new WaitCommand(getCommand("intake-time-2")));
				addCommand("wait-lower-lift-2", new WaitCommand(getCommand("lower-lift-2")));
//				addCommand("lift-height-intake-cube-2", new DetectLiftHeight(robotContext, 10)); //Potentially saves some time
//				addCommand("wait-lift-height-intake-cube-2", new WaitCommand(getCommand("lift-height-intake-cube-2")));

				
				//approach scale with 2nd cube
				double distanceToScale2 = 43;
				double angleToScale2 = -15;
				addCommand("approach-scale-PW-2", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, dir*angleToScale2));
				addCommand("wait-approach-scale-PW-2", new WaitCommand(getCommand("approach-scale-PW-2")));
				addCommand("approach-scale-2", new DriveUntilInches(driveTrain, ocelotSunflower, distanceToScale2, dir*angleToScale2, 0.9)); 
				addCommand("raise-lift-2", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleBack));
				addCommand("time-turn-scale-2", new TimeCommand(0.1));
				addCommand("wait-time-turn-2", new WaitCommand(getCommand("time-turn-scale-2")));
				addCommand("ocelot-turn-to-scale-2", new TurnAndMaintainDirection(driveTrain, ocelotSunflower, gyro, dir*-170, 6, 0.1, distanceToScale2));
				addCommand("turn-to-scale-2", new TurnToDirection(ocelotSunflower, gyro, dir*-170, 6, 0.1, 3));
				addCommand("time-intake-2", new TimeCommand(0.65));
				addCommand("wait-intake-2", new WaitCommand(getCommand("time-intake-2")));
				addCommand("stop-intake-2", new SetIntakeDrive(intakeStrat2, 0.0));
				addCommand("wait-turn-to-scale-2", new WaitCommand(getCommand("turn-to-scale-2")));
				addCommand("wait-approach-scale-2", new WaitCommand(getCommand("approach-scale-2")));
				addCommand("wait-raise-lift-2", new WaitCommand(getCommand("raise-lift-2")));
				
				//spit 2nd cube
				double angleToCube3 = dir*150;
				addCommand("outtake-cube-2", new SetIntakeDrive(intakeStrat2, -0.9));
				addCommand("approach-cube-PW-3", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, angleToCube3));
				addCommand("wait-approach-cube-PW-2", new WaitCommand(getCommand("approach-cube-PW-3")));
				addCommand("time-outtake-cube-2", new TimeCommand(0.4));
				addCommand("wait-time-outtake-cube-2", new WaitCommand(getCommand("time-outtake-cube-2")));
				
				//-------------------------3rd cube------------------
				
				//drive to 3nd cube
				double distanceToCube3 = 40;
				addCommand("lower-lift-3", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
				addCommand("approach-cube-3", new DriveAlongTrapezoidalCurve(driveTrain, ocelotSunflower, distanceToCube3, angleToCube3, 1, 0.6, 0.01)); 
				addCommand("time-turn-cube-3", new TimeCommand(0.05));
				addCommand("wait-time-turn-3", new WaitCommand(getCommand("time-turn-cube-3")));
				addCommand("ocelot-turn-to-cube-3", new TurnAndMaintainDirection(driveTrain, ocelotSunflower, gyro, angleToCube3, 10, 0.1, distanceToCube3));
				addCommand("turn-to-cube-3", new TurnToDirection(ocelotSunflower, gyro, angleToCube3, 10, 0.1, 3));
				addCommand("stop-outtake-cube-2", new SetIntakeDrive(intakeStrat2, 0.0));
				addCommand("open-intake-3", new SetIntakeExtending(intakeStrat2, true));
				addCommand("start-intake-3", new SetIntakeDrive(intakeStrat2, 1.0));
				addCommand("wait-turn-to-cube-3", new WaitCommand(getCommand("turn-to-cube-3")));
				addCommand("wait-approach-cube-3", new WaitCommand(getCommand("approach-cube-3")));
				addCommand("wait-lower-lift-3", new WaitCommand(getCommand("lower-lift-3")));
//				addCommand("lift-height-intake-cube-3", new DetectLiftHeight(robotContext, 10));
//				addCommand("wait-lift-height-intake-cube-3", new WaitCommand(getCommand("lift-height-intake-cube-3")));
				
				//intake 3nd cube
				double angleToScale3 = -35;
				addCommand("drive-to-cube-PW-3", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, angleToCube3));
				addCommand("wait-drive-to-cube-PW-2", new WaitCommand(getCommand("drive-to-cube-PW-3")));
				addCommand("drive-to-cube-3", new DriveUntilSeconds(ocelotSunflower, 0.8, angleToCube3, 0.6));
				addCommand("wait-drive-to-cube-3", new WaitCommand(getCommand("drive-to-cube-3")));
				addCommand("close-intake-3", new SetIntakeExtending(intakeStrat2, false));
				addCommand("approach-scale-PW-3", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, dir*angleToScale3));
				addCommand("wait-approach-cube-PW-2", new WaitCommand(getCommand("approach-scale-PW-3")));
				addCommand("intake-time-3", new TimeCommand(0.15));
				addCommand("wait-intake-time-3", new WaitCommand(getCommand("intake-time-3")));
			}
		}
		
	}
	
}
