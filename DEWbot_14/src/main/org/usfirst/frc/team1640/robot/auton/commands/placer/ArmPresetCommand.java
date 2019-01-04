package main.org.usfirst.frc.team1640.robot.auton.commands.placer;

import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.placer.arm.Arm.ArmPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;

public class ArmPresetCommand implements AutonCommand {
	private boolean isDone;
	private ArmPreset preset;
	private Placer placer;
	private boolean init;
	
	public ArmPresetCommand(IRobotContext robotContext, ArmPreset preset){
		placer = robotContext.getPlacer();
		this.preset = preset;
	}
	
	
	@Override
	public void execute(){
		if(!init){
			init = true;
			placer.setArmManual(preset);
		}
		if (!isDone) {
			placer.execute();
			isDone = placer.atSetpoint();
		}
	}
	
	@Override
	public boolean isRunning(){
		return !isDone;
	}
	
	@Override
	public boolean isInitialized(){
		return true;
	}
	
	@Override
	public void reset(){
		init = false;
		isDone = false;
	}
}
