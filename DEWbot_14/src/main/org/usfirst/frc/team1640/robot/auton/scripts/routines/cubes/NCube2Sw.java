package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.cubes;

import main.org.usfirst.frc.team1640.robot.auton.commands.PrintCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;

public class NCube2Sw extends AutonScript {
	
	public NCube2Sw(IRobotContext robotContext, FieldConfig config, int n) {
		
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = 1; break;
		case Right: dir = -1; break;
		default: System.out.println("Invalid starting location");
		}
		
		addCommand("lift-to-transport", new PrintCommand("Lift to Transport"));
		addCommand("wait-lift-to-transport", new WaitCommand(getCommand("lift-to-transport")));
		switch(config.getSwPos()) {
		case Left: addCommand("exit-alley", new DriveStraight(robotContext, -4.5+(n-1)*28.01, dir*90, 1.0)); break;
		case Right: addCommand("exit-alley", new DriveStraight(robotContext, -4.5+(6-n)*28.01, dir*90, 1.0)); break;
		}
		addCommand("wait-exit-alley", getCommand("exit-alley"));
		addCommand("lift-to-switch-height", new PrintCommand("Lift to Switch Height"));
		addCommand("drive-to-switch", new DriveStraight(robotContext, 10, 180, 0.3));
		addCommand("wait-drive-to-switch", new WaitCommand(getCommand("drive-to-switch")));
		addCommand("outtake", new PrintCommand("Outtake for half a second"));
	}
}
