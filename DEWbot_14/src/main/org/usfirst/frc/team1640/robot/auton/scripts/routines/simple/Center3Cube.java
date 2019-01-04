package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.simple;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeoutCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.curve.DriveAlongTrapezoidalCurve;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilInches;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeDrive;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeExtending;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.DeltaCubeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
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

public class Center3Cube extends AutonScript {
	
	public Center3Cube(IRobotContext robotContext, FieldConfig config) {
		
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
		
			addCommand("center-switch-to-switch", new CenterSwitch2Switch(robotContext, config));
			addCommand("wait-center-switch-to-switch", new WaitCommand(getCommand("center-switch-to-switch")));
			
			if (swSide == -1) { // left side
				addCommand("drive-away2", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 46, -148, 1.0, 0.8, 0.01));
				addCommand("wait-drive-away2", new WaitCommand(getCommand("drive-away2")));
				
				addCommand("turn-to-stack2", new TurnToDirection(sunflower, gyro, -43, 7, 0.1, 3)); //angle -53
				addCommand("lower-lift2", new DeltaCubeCommand(robotContext, 1, PlacerPreset.Floor));
				addCommand("wait-turn-to-stack2", new WaitCommand(getCommand("turn-to-stack2")));
				addCommand("wait-lower-lift2", new WaitCommand(getCommand("lower-lift2")));
				
				addCommand("intake2", new SetIntakeDrive(intakeStrat, 1.0));
				addCommand("open-intake2", new SetIntakeExtending(intakeStrat, true));
				addCommand("drive-to-cubes2", new TimeoutCommand(new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 24, -48, 1.0, 0.8, 0.01), 2));
				addCommand("wait-drive-to-cubes2", new WaitCommand(getCommand("drive-to-cubes2")));
				addCommand("close-intake2", new SetIntakeExtending(intakeStrat, false));
				addCommand("time-for-intake2", new TimeCommand(0.15));
				addCommand("wait-time-for-intake2", new WaitCommand(getCommand("time-for-intake2")));
				
				//leave cubes
				addCommand("leave-cubes2", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 10, 127, 1.0, 0.9, 0.01));
				addCommand("wait-leave-cubes2", new WaitCommand(getCommand("leave-cubes2")));
				addCommand("stop-intake2", new SetIntakeDrive(intakeStrat, 0));
				
				//ocelot to switch
				addCommand("turn-to-switch2", new TurnToDirection(sunflower, gyro, 0, 6, 0.1, 5));
				addCommand("drive-to-switch2", new DriveUntilInches(driveTrain, sunflower, 25, 30, 0.7)); //angle 28
				addCommand("raise-lift2", new PlacerPresetCommand(robotContext, PlacerPreset.Switch));
				addCommand("wait-drive-to-switch2", new WaitCommand(getCommand("drive-to-switch2")));
				addCommand("wait-turn-to-switch2", new WaitCommand(getCommand("turn-to-switch2")));
//				addCommand("drop-cube2", new OpenIntakeTime(robotContext, 0.5));
//				addCommand("wait-drop-cube2", new WaitCommand(getCommand("drop-cube2")));
			}
			else { // right side
				addCommand("drive-away", new DriveStraightAndTimeout(robotContext,50, 150, 1.0, 5));
				addCommand("wait-drive-away", new WaitCommand(getCommand("drive-away")));
				
				addCommand("turn-to-stack", new TurnToDirection(sunflower, gyro, 45, 10, 0.1, 3)); //angle 53
				addCommand("lower-lift", new DeltaCubeCommand(robotContext, 1, PlacerPreset.Floor));
				addCommand("wait-turn-to-stack", new WaitCommand(getCommand("turn-to-stack")));
				addCommand("wait-lower-lift", new WaitCommand(getCommand("lower-lift")));
				
				addCommand("intake2", new SetIntakeDrive(intakeStrat, 1.0));
				addCommand("open-intake2", new SetIntakeExtending(intakeStrat, true));
				addCommand("drive-to-cubes2", new TimeoutCommand(new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 24, 45, 1.0, 0.8, 0.01), 2));
				addCommand("wait-drive-to-cubes2", new WaitCommand(getCommand("drive-to-cubes2")));
				addCommand("close-intake2", new SetIntakeExtending(intakeStrat, false));
				addCommand("time-for-intake2", new TimeCommand(0.15));
				addCommand("wait-time-for-intake2", new WaitCommand(getCommand("time-for-intake2")));
				
				addCommand("leave-cubes2", new DriveAlongTrapezoidalCurve(driveTrain, sunflower, 10, -127, 1.0, 0.9, 0.01));
				addCommand("wait-leave-cubes2", new WaitCommand(getCommand("leave-cubes2")));
				addCommand("stop-intake2", new SetIntakeDrive(intakeStrat, 0));
				
				//ocelot to switch
				addCommand("turn-to-switch2", new TurnToDirection(sunflower, gyro, 0, 6, 0.1, 5));
				addCommand("drive-to-switch2", new DriveUntilInches(driveTrain, sunflower, 25, -30, 0.7)); //angle 28
				addCommand("raise-lift2", new PlacerPresetCommand(robotContext, PlacerPreset.Switch));
				addCommand("wait-drive-to-switch2", new WaitCommand(getCommand("drive-to-switch2")));
				addCommand("wait-turn-to-switch2", new WaitCommand(getCommand("turn-to-switch2")));

			}
		}
	}

}
