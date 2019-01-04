package main.org.usfirst.frc.team1640.robot.auton.scripts.routines.cubes;

import main.org.usfirst.frc.team1640.robot.auton.commands.PrintCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.drive.DriveStraight;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;

public class NCube2Sc extends AutonScript {
	
	public NCube2Sc(IRobotContext robotContext, FieldConfig config, int n) {
		
		int dir = 0;
		
		switch(config.getStartPos()) {
		case Left: dir = 1; break;
		case Right: dir = -1; break;
		default: System.out.println("Invalid starting location");
		}
		
		addCommand("lift-to-transport", new PrintCommand("Lift to Transport"));
		addCommand("wait-lift-to-transport", new WaitCommand(getCommand("lift-to-transport")));
		switch(config.getScPos()) {
		case Left: addCommand("exit-alley", new DriveStraight(robotContext, 14.25+(n-1)*28.01, dir*90, 1.0)); break;
		case Right: addCommand("exit-alley", new DriveStraight(robotContext, 14.25+(6-n)*28.01, dir*90, 1.0)); break;
		}
		addCommand("wait-exit-alley", getCommand("exit-alley"));
		addCommand("lift-to-scale-back", new PrintCommand("Lift to Scale Back"));
		addCommand("drive-to-scale", new DriveStraight(robotContext, 51.75, 0, 1.0));
		addCommand("wait-drive-to-scale", new WaitCommand(getCommand("drive-to-scale")));
		addCommand("outtake", new PrintCommand("Outtake for half a second"));
	}
}
