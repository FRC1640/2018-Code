package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.worlds;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeDrive;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnAndMaintainDirection;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.worlds.subscripts.DoubleCloseScaleAndCube;
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

public class ThreeCubeScale extends AutonScript {

	public ThreeCubeScale(IRobotContext robotContext, FieldConfig config) {
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
		default: System.out.println("Ocelot2Cube: Invalid starting location: " + config.getStartPos().name());
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
				addCommand("double-close-and-cube", new DoubleCloseScaleAndCube(robotContext, config));
				addCommand("wait-double-close-and-cube", new WaitCommand(getCommand("double-close-and-cube")));

				//approach scale with 3rd cube
				double distanceToScale3 = 67;
				double angleToScale3 = -35;
				addCommand("approach-scale-3", new DriveAlongTrapezoidalCurve(driveTrain, ocelotSunflowerRough, distanceToScale3, dir*angleToScale3, 1, 0.7, 0.01)); 
				addCommand("raise-lift-3", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
				addCommand("time-turn-scale-3", new TimeCommand(0.1));
				addCommand("wait-time-turn-3", new WaitCommand(getCommand("time-turn-scale-3")));
				addCommand("ocelot-turn-to-scale-3", new TurnAndMaintainDirection(driveTrain, ocelotSunflowerRough, gyro, dir*40, 6, 0.1, distanceToScale3));
				addCommand("turn-to-scale-3", new TurnToDirection(ocelotSunflowerRough, gyro, dir*40, 6, 0.1, 3));
				addCommand("time-intake-3", new TimeCommand(0.6));
				addCommand("wait-intake-3", new WaitCommand(getCommand("time-intake-3")));
				addCommand("stop-intake-3", new SetIntakeDrive(intakeStrat2, 0.0));
				addCommand("wait-turn-to-scale-3", new WaitCommand(getCommand("turn-to-scale-3")));
				addCommand("wait-approach-scale-3", new WaitCommand(getCommand("approach-scale-3")));
				addCommand("wait-raise-lift-3", new WaitCommand(getCommand("raise-lift-3")));
				
				//spit 3rd cube
				addCommand("outtake-cube-3", new SetIntakeDrive(intakeStrat2, -0.8));
			}
		}
		
	}
	
}
