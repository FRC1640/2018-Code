package main.org.usfirst.frc.team1640.robot.auton.scripts;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.PrintCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.SetManualShift;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.pointwheels.PointWheels;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.GyroCorrectedOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.OcelotStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.steer.SteerStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.SunflowerStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.WheelsMaintainDirectionSunflower;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;


public class ExampleAutonScript extends AutonScript {
	
	public ExampleAutonScript(IRobotContext robotContext) {
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		IGyro gyro = robotContext.getSensorSet().getGyro();
		
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		SteerStrategy steerStrat = new DefaultSteer(driveTrain);
		OcelotStrategy ocelot = new DefaultOcelot(driveTrain, steerStrat, driveStrat);
		SunflowerStrategy sunflowerStrat = new WheelsMaintainDirectionSunflower(new ShortestAngleSunflower(driveTrain, gyro, ocelot, 0.007, 0, 0.001, 1.0), gyro);
		OcelotStrategy ocelotStrat = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		OcelotStrategy myStrat = new GyroCorrectedOcelot(driveTrain, gyro, ocelotStrat);
		
		addCommand("print1", new PrintCommand("Starting..."));
		addCommand("point-wheels", new PointWheels(driveTrain, steerStrat, 0));
		addCommand("wait-point-wheels", new WaitCommand(getCommand("point-wheels")));
		addCommand("print3", new PrintCommand("Finished pointing wheels"));
		addCommand("manual-shift", new SetManualShift(driveStrat, -1.0));
		addCommand("drive1", new TurnToDirection(sunflowerStrat, gyro, -90, 5, 0.1, 1));
		addCommand("wait-drive1", new WaitCommand(getCommand("drive1")));
		addCommand("print2", new PrintCommand("Finished!"));
	}
}