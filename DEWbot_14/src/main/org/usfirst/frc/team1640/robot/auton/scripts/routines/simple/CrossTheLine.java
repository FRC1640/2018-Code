package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.simple;

import main.org.usfirst.frc.team1640.constants.auton.AutonConstants;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.auton.commands.drive.until.DriveUntilSeconds;
import main.org.usfirst.frc.team1640.robot.auton.commands.gyro.OffsetGyro;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DefaultDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.GyroCorrectedOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;

public class CrossTheLine extends AutonScript {
	
	public CrossTheLine(IRobotContext robotContext, FieldConfig config) {
		IDriveTrain driveTrain = robotContext.getDriveTrain();
		IGyro gyro = robotContext.getSensorSet().getGyro();
		DefaultOcelot ocelot = new DefaultOcelot(driveTrain, new DefaultSteer(driveTrain), new DefaultDrive(driveTrain));
		FieldCentricOcelot fieldCentric = new FieldCentricOcelot(driveTrain, gyro, ocelot);
		GyroCorrectedOcelot myStrat = new GyroCorrectedOcelot(driveTrain, gyro, fieldCentric);
		
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = -1; break;
		case Right: dir = 1; break;
		case Center: dir = -1; break;
		default: dir = AutonConstants.DEFAULT_START_POS; System.out.println("Start2Sc: Invalid starting location: " + config.getStartPos().name());
		}
		
		if (dir != 0) {
			addCommand("offset-gyro", new OffsetGyro(gyro, dir*90));
			addCommand("drive-forward", new DriveUntilSeconds(myStrat, 3, 0, 0.5));
		}
	}

}
