package main.org.usfirst.frc.team1640.robot.auton.scripts;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.TimeCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.DriveIntakeTime;
import main.org.usfirst.frc.team1640.robot.auton.commands.intake.SetIntakeExtending;
import main.org.usfirst.frc.team1640.robot.auton.commands.vision.DriveUntilInchesVision;
import main.org.usfirst.frc.team1640.robot.auton.commands.vision.StrafeUntilAlignedVision;
import main.org.usfirst.frc.team1640.robot.auton.commands.vision.WaitForVisionCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DefaultDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.vision.VisionServer;

public class StrafeToCubeScript extends AutonScript {
	public StrafeToCubeScript(IRobotContext robotContext) {
		VisionServer visionServer = robotContext.getVisionServer();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		IGyro gyro = robotContext.getSensorSet().getGyro();
		DefaultDrive driveStrat = new DefaultDrive(driveTrain);
		DefaultSteer steerStrat = new DefaultSteer(driveTrain);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, steerStrat, driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		SunflowerStrategy sunflowerStrat = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.015, 0, 0.001, 1.0);

		addCommand("find-vision-strafe", new WaitForVisionCommand(visionServer, 5));
		addCommand("wait-vision-strafe", new WaitCommand(getCommand("find-vision-strafe")));
		addCommand("time-refresh-strafe", new TimeCommand(0.05));
		addCommand("Wait-refresh-strafe", new WaitCommand(getCommand("time-refresh-strafe")));
		addCommand("vision-drive-strafe", new StrafeUntilAlignedVision(visionServer, sunflowerStrat, driveTrain, gyro, 0.5));
		addCommand("wait-drive-strafe", new WaitCommand(getCommand("vision-drive-strafe")));
		addCommand("vision-drive-forward", new DriveUntilInchesVision(visionServer, sunflowerStrat, driveTrain, gyro, 0.5));
		//addCommand("intake", new SetIntakeExtending(robotContext, true));
		addCommand("wait-drive-forward", new WaitCommand(getCommand("vision-drive-forward")));
		//addCommand("start-intake-vision", new DriveIntakeTime(robotContext, -1.0, 1.0));
		//addCommand("close-intake-vision", new SetIntakeExtending(robotContext, false));
		//addCommand("wait-intake", new WaitCommand(getCommand("start-intake-vision")));
	}
}
