package main.org.usfirst.frc.team1640.placer.arm;

import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;

public class TestArm extends Arm {
	
	public TestArm(ISensorSet sensorSet) {
		super(sensorSet);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setPreset(PlacerPreset preset) {
		
	}
	
	@Override
	public void setManual(ArmPreset manualPresetValue) {
		
	}
	
	@Override
	public double getAngle() {
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
