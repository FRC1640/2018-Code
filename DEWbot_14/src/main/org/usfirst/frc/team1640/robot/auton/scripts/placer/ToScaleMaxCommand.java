package main.org.usfirst.frc.team1640.robot.auton.scripts.placer;

import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.placer.arm.Arm.ArmPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.WaitCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.ArmPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.commands.placer.PlacerPresetCommand;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;

public class ToScaleMaxCommand extends AutonScript {

	public ToScaleMaxCommand(IRobotContext robotContext) {
		addCommand("lift", new PlacerPresetCommand(robotContext, PlacerPreset.ScaleMax));
		addCommand("wait-lift", new WaitCommand(getCommand("lift")));
		
		addCommand("arm", new ArmPresetCommand(robotContext, ArmPreset.MIDDLE));
		addCommand("wait-arm", new WaitCommand(getCommand("arm")));
	}
	
}
