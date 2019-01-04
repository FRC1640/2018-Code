package main.org.usfirst.frc.team1640.placer.lift;

import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;

public class TestLift extends Lift {
	
	@Override
	public void setPreset(PlacerPreset liftPreset) {
		//Set lift to preset
	}
	
	@Override
	public void setManualSpeed(double manualValue) {
		//Set manual value -1,1
	}
	
	@Override
	public double getHeight() {
		//Return current lift height
		return 0;
	}
	
	@Override
	public double getSetpoint(){
		return 0;
	}
	
	@Override
	public void execute(){
		
	}
}
