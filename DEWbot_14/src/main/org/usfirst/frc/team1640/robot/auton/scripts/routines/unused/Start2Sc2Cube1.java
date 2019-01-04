package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.unused;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.PrintCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.turn.TurnToDirection;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.GetCube;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.Start2Sc;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class Start2Sc2Cube1 extends AutonScript {
	
	public Start2Sc2Cube1(IRobotContext robotContext, FieldConfig config) {
		IGyro gyro = robotContext.getSensorSet().getGyro();
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		ManualShiftingDrive driveStrat = new ManualShiftingDrive(driveTrain);
		driveStrat.setTransmission(0);
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), driveStrat);
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		ShortestAngleSunflower sunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.01, 0.0, 0.0001, 0.9);
		
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = -1; break;
		case Right: dir = 1; break;
		default: System.out.println("Invalid starting location");
		}
		
		addCommand("Start2Scale", new Start2Sc(robotContext, config));
		addCommand("wait-Start2Scale", new WaitCommand(getCommand("Start2Scale")));
		addCommand("drive-to-alley", new DriveStraight(robotContext, 40.75, 180, 1.0));
		addCommand("wait-drive-to-alley", new WaitCommand(getCommand("drive-to-alley")));
		addCommand("lift-to-transport", new PrintCommand("Lift to Transport Height"));
		addCommand("turn-to-cubes", new TurnToDirection(sunflower, gyro, 180, 2, 0.25, 1));
		addCommand("wait-turn-to-cubes", new WaitCommand(getCommand("turn-to-cubes")));
		addCommand("enter-alley", new DriveStraight(robotContext, 30, -dir*90, 1.0));
		addCommand("wait-enter-alley", new WaitCommand(getCommand("enter-alley")));
//		addCommand("GetCube", new GetCube(robotContext));
	}
}
