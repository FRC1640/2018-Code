package main.org.usfirst.frc.team1640.robot.auton.commands.placer;

import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;

public class PlacerPresetCommand implements AutonCommand{
	private boolean isDone;
	private PlacerPreset preset;
	private Placer placer;
	private boolean init;
	
	public PlacerPresetCommand(IRobotContext robotContext, PlacerPreset preset){
		placer = robotContext.getPlacer();
		this.preset = preset;
	}
	
	
	@Override
	public void execute(){
		if(!init){
			init = true;
			placer.setPreset(preset);
		}
		if (!isDone) {
//			System.out.println("Placer Preset Command is Running");
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
