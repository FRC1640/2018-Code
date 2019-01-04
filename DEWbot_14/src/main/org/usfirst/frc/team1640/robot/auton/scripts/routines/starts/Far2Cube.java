package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.OpenIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeDrive;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeExtending;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.commands.vision.TurnToDirectionVisionRight;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraightAndTimeout;
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

public class Far2Cube extends AutonScript {

	public Far2Cube(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.02, 0.0, 0.0001, 0.9);
		ShortestAngleSunflower sunflower2CubeTurn = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.021, 0.0, 0.001, 0.9);
		SunflowerStrategy sunflowerVision = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.05, 0, 0.001, 1.0);
		
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
				addCommand("approach-alley", new DriveStraight(robotContext, 220, 0, 1.0)); //218
				addCommand("wait-approach-alley", new WaitCommand(getCommand("approach-alley")));
//				addCommand("delay", new TimeCommand(2));
//				addCommand("wait-delay", new WaitCommand(getCommand("delay")));
				
				addCommand("through-alley", new DriveStraight(robotContext, 190, dir*90, 1.0)); //184 //160;
				addCommand("lift-to-scale", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
				addCommand("wait-through-alley", new WaitCommand(getCommand("through-alley")));
				
				
				addCommand("turn-to-scale", new TurnToDirection(sunflower2CubeTurn, gyro, -dir*20, 4, 0.2, 1.5));
				addCommand("wait-turn-to-scale", new WaitCommand(getCommand("turn-to-scale")));
				
				addCommand("drive-to-scale", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 34, 0, 1.0, 0.6, 0.01)); //48 //62;
				addCommand("wait-drive-to-scale", new WaitCommand(getCommand("drive-to-scale")));
				addCommand("wait-lift-to-scale", new WaitCommand(getCommand("lift-to-scale")));
				addCommand("outtake", new DriveIntakeTime(robotContext, -0.6, 1.5));
				addCommand("wait-outtake", new WaitCommand(getCommand("outtake")));
				
				
				//back away
				addCommand("drive-away", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 20, 180, 1.0));
				addCommand("wait-drive-away", new WaitCommand(getCommand("drive-away")));
				
				
				//turn and begin intaking
				addCommand("turn-to-cube", new TurnToDirection(sunflower2CubeTurn, gyro, -dir*175, 6, 0.25, 3));
				addCommand("lower-placer", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
				addCommand("open-intake", new SetIntakeExtending(intakeStrat2, true));
				addCommand("start-intake", new SetIntakeDrive(intakeStrat2, 1.0));
				addCommand("wait-turn-to-cube", new WaitCommand(getCommand("turn-to-cube")));
				
				//vision turn
//				addCommand("turn-vision", new TurnToDirectionVisionRight(robotContext.getVisionServer(), sunflowerVision, gyro, 6.0, 0.1, 1));
//				addCommand("wait-turn-vision", new WaitCommand(getCommand("turn-vision")));
				addCommand("wait-lift-to-floor", new WaitCommand(getCommand("lower-placer")));

				
				//go towards cube and close intake
				addCommand("drive-to-cube", new DriveStraightAndTimeout(robotContext, 43, -dir*155, 1.0, 1.15)); //LOWERED TIMEOUT
				addCommand("wait-drive-to-cube", new WaitCommand(getCommand("drive-to-cube")));
				addCommand("close-intake", new SetIntakeExtending(intakeStrat2, false));
				
				addCommand("drive-to-cube", new DriveStraightAndTimeout(robotContext, 30, dir*10, 1.0, 1.15)); //LOWERED TIMEOUT
				addCommand("wait-drive-to-cube", new WaitCommand(getCommand("drive-to-cube")));
				
			}
		}
		
	}
	
}
