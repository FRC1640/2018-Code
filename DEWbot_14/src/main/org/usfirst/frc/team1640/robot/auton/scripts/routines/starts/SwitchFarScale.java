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
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class SwitchFarScale extends AutonScript {

	public SwitchFarScale(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.0225, 0.0, 0.001, 0.9);
		SunflowerStrategy sunflowerVision = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.05, 0, 0.001, 1.0);
		ShortestAngleSunflower ocelotSunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.01, 0.00003, 0.0005, 0.9);
		ShortestAngleSunflower ocelotSunflowerTurn = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0.000031, 0.0005, 0.9);

		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(robotContext.getIntake());
		IntakeTogetherStrategy intakeStrat2 = new DefaultIntakeTogether(intakeSeparately, 0.5, 1.0);
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = -1; break;
		case Right: dir = 1; break;
		default: System.out.println("SwitchFarScale: Invalid starting location: " + config.getStartPos().name());
		}
		
		int scSide = 0;
		int swSide = 0;
		
		switch(config.getScPos()) {
		case Left: scSide = -1; break;
		case Right: scSide = 1; break;
		default: System.out.println("SwitchFarScale: Invalid scale position: " + config.getScPos().name());
		}
		switch(config.getSwPos()) {
		case Left: swSide = -1; break;
		case Right: swSide = 1; break;
		default: System.out.println("SwitchFarScale: Invalid switch position: " + config.getSwPos().name());
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
				addCommand("double-side-switch", new DoubleSideSwitch(robotContext, config));
				addCommand("wait-double-side-switch", new WaitCommand(getCommand("double-side-switch")));
				
				addCommand("drive-back-2", new DriveUntilInches(driveTrain, ocelotSunflowerTurn, 3, 0, 0.6)); 
				addCommand("time-turn-1", new TimeCommand(0.05));
				addCommand("wait-time-turn-1", new WaitCommand(getCommand("time-turn-1")));
				addCommand("turn-to-alley-1", new TurnAndMaintainDirection(driveTrain, ocelotSunflowerTurn, gyro, 90, 6, 0.1, 20));
				addCommand("wait-drive-back-2", new WaitCommand(getCommand("drive-back-2")));
				addCommand("wait-turn-to-alley-1", new WaitCommand(getCommand("turn-to-alley-1")));
				
				addCommand("placer-to-transport", new PlacerPresetCommand(robotContext, PlacerPreset.CubeTravel));
				addCommand("drive-alley", new DriveUntilInches(driveTrain, ocelotSunflower, 80, dir*90, 1.0)); 
				addCommand("wait-drive-alley", new WaitCommand(getCommand("drive-alley")));
				addCommand("wait-lift-to-transport", new WaitCommand(getCommand("placer-to-transport")));
				
				addCommand("stop-intake", new SetIntakeDrive(intakeStrat2, 0));

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
