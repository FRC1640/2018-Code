package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels.PointWheelsFieldCentric;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilSeconds;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeDrive;
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

public class CloseCrossSwitch extends AutonScript {

	public CloseCrossSwitch(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.02, 0.0, 0.0001, 0.9);
		ShortestAngleSunflower ocelotSunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.01, 0.00004, 0.0005, 0.9);
		ShortestAngleSunflower ocelotSunflowerRough = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0, 0.0005, 0.9);
		ShortestAngleSunflower largeTurn = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.011, 0, 0, 0.9);

		SteerStrategy defaultSteer = new DefaultSteer(driveTrain);
		SunflowerStrategy sunflowerVision = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.038, 0, 0.001, 1.0);
		
		GyroCorrectedOcelot gyroCorrectedOcelot = new GyroCorrectedOcelot(driveTrain, gyro, ocelot);
		CurrentStopIntakeTogether intakeStrat = new CurrentStopIntakeTogether(new DefaultIntakeTogether(new DefaultIntakeSeparately(robotContext.getIntake()), 0.5, 1.0));
		
		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(robotContext.getIntake());
		IntakeTogetherStrategy intakeStrat2 = new DefaultIntakeTogether(intakeSeparately, 0.5, 1.0);
//		GyroCorrectedOcelot gyroCorrectedOcelot = new GyroCorrectedOcelot(driveTrain, gyro, ocelot);

		
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
		
		int swSide = 0;
		
		switch(config.getSwPos()) {
		case Left: swSide = -1; break;
		case Right: swSide = 1; break;
		default: System.out.println("Start2Sc2Cube2Sc: Invalid scale position: " + config.getSwPos().name());
		}
		
		if (dir != 0) {
			
			addCommand("offset-gyro", new OffsetGyro(gyro, dir*90));
			
			if (dir != scSide && dir != swSide) { // on same side as scale
				
				//approach scale, lift placer, outtake cube
				double initialDistance = 10;
				addCommand("leave-wall-PW-1", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, 0));
				addCommand("wait-leave-wall-PW-1", new WaitCommand(getCommand("leave-wall-PW-1")));
				addCommand("leave-wall-1", new DriveAlongTrapezoidalCurve(driveTrain, ocelotSunflower, initialDistance, 0, 1, 0.7, 0.01)); 
				addCommand("wait-leave-wall-1", new WaitCommand(getCommand("leave-wall-1")));
				
//				addCommand("delay", new TimeCommand(3));
//				addCommand("wait-delay", new WaitCommand(getCommand("delay")));

				double forwardDrive = 210;
				addCommand("forward-drive-PW-1", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, 90));
				addCommand("wait-forward-drive-PW-1", new WaitCommand(getCommand("forward-drive-PW-1")));
				addCommand("forward-drive-1", new DriveAlongTrapezoidalCurve(driveTrain, ocelotSunflower, forwardDrive, 90, 1, 0.7, 0.01));
				addCommand("ocelot-turn", new TurnAndMaintainDirection(driveTrain, ocelotSunflower, gyro, 90, 3, 0.1, forwardDrive));
				addCommand("wait-forward-drive-1", new WaitCommand(getCommand("forward-drive-1")));
				
				double approachSwitch = 115;
				addCommand("approach-switch-PW-1", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, 0));
				addCommand("wait-approach-switch-PW-1", new WaitCommand(getCommand("approach-switch-PW-1")));
				addCommand("approach-switch-1", new DriveAlongTrapezoidalCurve(driveTrain, ocelotSunflower, approachSwitch, 0, 1, 0.7, 0.01));
				addCommand("lift-to-switch", new PlacerPresetCommand(robotContext, PlacerPreset.Switch));
				addCommand("wait-approach-switch-1", new WaitCommand(getCommand("approach-switch-1")));
				
				addCommand("turn-to-switch-1", new TurnToDirection(largeTurn, gyro, -90, 10, 0.1, 4));
				addCommand("wait-turn-to-switch-1", new WaitCommand(getCommand("turn-to-switch-1")));
				addCommand("wait-lift-to-switch", new WaitCommand(getCommand("lift-to-switch")));
				
				addCommand("hit-switch-PW-1", new PointWheelsFieldCentric(driveTrain, gyro, defaultSteer, -90));
				addCommand("wait-hit-switch-PW-1", new WaitCommand(getCommand("hit-switch-PW-1")));
				addCommand("hit-switch-1", new DriveUntilSeconds( ocelotSunflower, 1, -90, 0.5));
				addCommand("wait-hit-switch-1", new WaitCommand(getCommand("hit-switch-1")));
				
				addCommand("drop-cube", new SetIntakeDrive(intakeStrat2, -0.6));
			}
		}
		
	}
	
}
