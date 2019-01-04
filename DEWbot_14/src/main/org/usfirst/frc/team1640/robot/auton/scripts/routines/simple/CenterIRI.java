package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.simple;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.placer.arm.Arm.ArmPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.OpenIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeDrive;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeExtending;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.ArmPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.DeltaCubeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.DelayedTurnAndMaintainDirection;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnAndMaintainDirection;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
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
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class CenterIRI extends AutonScript {
	
	public CenterIRI(IRobotContext robotContext, FieldConfig config) {
		
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0.0, 0.0001, 0.9);
		
		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(robotContext.getIntake());
		IntakeTogetherStrategy intakeStrat = new DefaultIntakeTogether(intakeSeparately, 0.4, 1.0);
		
		int swSide = 0;
		switch(config.getSwPos()) {
		case Left: swSide = -1; break;
		case Right: swSide = 1; break;
		default: System.out.println("CenterSwitch2Switch2: Invalid switch position: " + config.getSwPos().name());
		}
		
		if (swSide != 0) {
			addCommand("center-switch", new CenterSwitch(robotContext, config));
			addCommand("wait-center-switch", new WaitCommand(getCommand("center-switch")));
			if (swSide == -1) { // left side
				//back away
//				addCommand("lift-to-shoot", new PlacerPresetCommand(robotContext, PlacerPreset.Switch));
//				addCommand("arm-to-shoot", new ArmPresetCommand(robotContext, ArmPreset.MIDDLE));
//				addCommand("drive-to-shoot", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 52, 3, 0.7, 1, 0.01));
//				addCommand("turn-to-shoot", new DelayedTurnAndMaintainDirection(driveTrain, sunflower, gyro, 40, 3, 0.1, 40, 0, 2));
//				addCommand("wait-drive-to-shoot", new WaitCommand(getCommand("drive-to-shoot")));
//				addCommand("wait-turn-to-shoot", new WaitCommand(getCommand("turn-to-shoot")));
//				addCommand("wait-lift-to-shoot", new WaitCommand(getCommand("lift-to-shoot")));
//				addCommand("wait-arm-to-shoot", new WaitCommand(getCommand("arm-to-shoot")));
//				
//				addCommand("shoot-cube", new DriveIntakeTime(robotContext, -500, 5));
//				addCommand("wait-shoot", new WaitCommand(getCommand("shoot-cube")));
				
				//back away
				addCommand("drive-away", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 80, -140, 1.0, 1, 0.01));
				addCommand("maintain-angle", new TurnAndMaintainDirection(driveTrain, sunflower, gyro, 0, 3, 0.1, 70));
				addCommand("wait-drive-away", new WaitCommand(getCommand("drive-away")));
				
				//turn to stack
//				addCommand("turn-to-stack", new TurnToDirection(sunflower, gyro, -44, 7, 0.1, 3)); //angle -53
				addCommand("lower-lift", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
//				addCommand("wait-turn-to-stack", new WaitCommand(getCommand("turn-to-stack")));
				addCommand("wait-lower-lift", new WaitCommand(getCommand("lower-lift")));
				
				//pick up 2nd cube
				addCommand("intake", new SetIntakeDrive(intakeStrat, 1.0));
				addCommand("open-intake", new SetIntakeExtending(intakeStrat, true));
				addCommand("drive-to-cubes", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 30, 0, 1.0, 0.8, 0.01));
				addCommand("wait-drive-to-cubes", new WaitCommand(getCommand("drive-to-cubes")));
				addCommand("close-intake", new SetIntakeExtending(intakeStrat, false));
				addCommand("time-for-intake", new TimeCommand(0.15));
				addCommand("wait-time-for-intake", new WaitCommand(getCommand("time-for-intake")));
				
				addCommand("drive-away-2", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 5, 180, 1.0, 0.8, 0.01));
				addCommand("wait-drive-to-cubes", new WaitCommand(getCommand("drive-away-2")));
				addCommand("intake", new SetIntakeDrive(intakeStrat, 0.0));

				addCommand("switch-lift", new PlacerPresetCommand(robotContext, PlacerPreset.Switch));
				addCommand("delta-cube", new DeltaCubeCommand(robotContext, 1));
				addCommand("arm", new ArmPresetCommand(robotContext, ArmPreset.MIDDLE));
				
				addCommand("wait-arm", new WaitCommand(getCommand("arm")));
				addCommand("wait-delta-cube", new WaitCommand(getCommand("delta-cube")));
				
				addCommand("drive-to-shoot", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 7, 3, 0.7, 1, 0.01));
				addCommand("turn-to-shoot", new DelayedTurnAndMaintainDirection(driveTrain, sunflower, gyro, 40, 3, 0.1, 40, 0, 2));
				addCommand("wait-drive-to-shoot", new WaitCommand(getCommand("drive-to-shoot")));
				addCommand("wait-turn-to-shoot", new WaitCommand(getCommand("turn-to-shoot")));

				addCommand("shoot", new DriveIntakeTime(robotContext, -5, 1));
				

				
			}
			else { // right side
				addCommand("drive-away", new DriveStraightAndTimeout(robotContext, 43, 155, 1.0, 5));
				addCommand("wait-drive-away", new WaitCommand(getCommand("drive-away")));
				
				addCommand("turn-to-stack", new TurnToDirection(sunflower, gyro, 50, 10, 0.1, 3)); //angle 53
				addCommand("lower-lift", new PlacerPresetCommand(robotContext, PlacerPreset.Floor));
				addCommand("wait-turn-to-stack", new WaitCommand(getCommand("turn-to-stack")));
				addCommand("wait-lower-lift", new WaitCommand(getCommand("lower-lift")));
				
				addCommand("intake", new SetIntakeDrive(intakeStrat, 1.0));
				addCommand("open-intake", new SetIntakeExtending(intakeStrat, true));
				addCommand("drive-to-cubes", new DriveStraightAndTimeout(robotContext, 20, 53, 1.0, 2));
				addCommand("wait-drive-to-cubes", new WaitCommand(getCommand("drive-to-cubes")));
				addCommand("close-intake", new SetIntakeExtending(intakeStrat, false));
				addCommand("time-for-intake", new TimeCommand(0.25));
				addCommand("wait-time-for-intake", new WaitCommand(getCommand("time-for-intake")));
				
				addCommand("leave-cubes", new DriveStraightAndTimeout(robotContext, 20, -127, 1.0, 2));
				addCommand("wait-leave-cubes", new WaitCommand(getCommand("leave-cubes")));
				addCommand("stop-intake", new SetIntakeDrive(intakeStrat, 0));
				
				addCommand("turn-to-switch", new TurnToDirection(sunflower, gyro, 0, 6, 0.1, 5));
				addCommand("drive-to-switch", new DriveUntilInches(driveTrain, sunflower, 60, -23, 0.5));
				addCommand("raise-lift", new PlacerPresetCommand(robotContext, PlacerPreset.Switch));
				addCommand("wait-drive-to-switch", new WaitCommand(getCommand("drive-to-switch")));
				addCommand("wait-turn-to-switch", new WaitCommand(getCommand("turn-to-switch")));
				addCommand("drop-cube", new OpenIntakeTime(robotContext, 0.5));
				addCommand("wait-drop-cube", new WaitCommand(getCommand("drop-cube")));
			}
		}
	}

}
