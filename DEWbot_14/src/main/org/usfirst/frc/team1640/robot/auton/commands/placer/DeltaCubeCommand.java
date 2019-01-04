package main.org.usfirst.frc.team1640.robot.auton.commands.placer;

import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;

public class DeltaCubeCommand implements AutonCommand{
	private boolean isDone;
	private int deltaCube;
	private Placer placer;
	private boolean init;
	private PlacerPreset preset;
	
	public DeltaCubeCommand(IRobotContext robotContext, int deltaCube){
		placer = robotContext.getPlacer();
		this.deltaCube = deltaCube;
	}
	
	public DeltaCubeCommand(IRobotContext robotContext, int deltaCube, PlacerPreset preset) {
		this(robotContext, deltaCube);
		this.preset = preset;
	}
	
	
	@Override
	public void execute(){
		if(!init){
			init = true;
			if(preset != null) {
				placer.setPreset(preset);
			}
			placer.setDeltaCube(deltaCube);
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
