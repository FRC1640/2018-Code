package main.org.usfirst.frc.team1640.robot.auton.scripts.routines;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.PrintCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeExtending;
import main.org.usfirst.frc.team1640.robot.auton.commands.vision.StrafeUntilAlignedVision;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.intake.separate.DefaultIntakeSeparately;
import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.robot.intake.together.DefaultIntakeTogether;
import main.org.usfirst.frc.team1640.robot.intake.together.IntakeTogetherStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DefaultDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.vision.VisionServer;

public class GetCube extends AutonScript {

	public GetCube(IRobotContext robotContext) {
		VisionServer visionServer = robotContext.getVisionServer();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		IGyro gyro = robotContext.getSensorSet().getGyro();
		DefaultDrive driveStrat = new DefaultDrive(driveTrain);
		DefaultSteer steerStrat = new DefaultSteer(driveTrain);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, steerStrat, driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		SunflowerStrategy sunflowerStrat = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0, 0.001, 1.0);
		
		IntakeSeparatelyStrategy intakeSeparately = new DefaultIntakeSeparately(robotContext.getIntake());
		IntakeTogetherStrategy intakeStrat = new DefaultIntakeTogether(intakeSeparately, 0.5, 1.0);
		
		//addCommand("VisionStrafe", new PrintCommand("Strafe with vision"));
		//addCommand("wait-VisionStrafe", new PrintCommand("VisionStrafe"));
		addCommand("VisionStrafe", new StrafeUntilAlignedVision(visionServer, sunflowerStrat, driveTrain, gyro, 0.5));
		addCommand("wait-VisionStrafe", new WaitCommand(getCommand("VisionStrafe")));
		
		addCommand("open-intake", new PrintCommand("Open Intake"));
		addCommand("intake", new SetIntakeExtending(intakeStrat, true));
		addCommand("approach-cube", new DriveStraight(robotContext, 11, 180, 0.4));
		addCommand("wait-approach-cube", new WaitCommand(getCommand("approach-cube")));
		addCommand("close-intake", new SetIntakeExtending(intakeStrat, false));
		addCommand("intake", new DriveIntakeTime(robotContext, -1.0, 1.0));
		addCommand("time-intaking-cube", new TimeCommand(0.25));
		addCommand("wait-time-intaking-cube", new WaitCommand(getCommand("time-intaking-cube")));
	}
	
}
