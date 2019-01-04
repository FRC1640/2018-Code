package main.org.usfirst.frc.team1640.placer.arm;

import java.util.HashMap;

import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.placer.arm.motion.ArmDecel;
import main.org.usfirst.frc.team1640.placer.arm.motion.ArmMotion;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;

public class Arm {
	public enum ArmPreset {NONE, LOW, MIDDLE, HIGH, BACK};
	private ArmPreset manualPreset = ArmPreset.NONE;
	private ArmMotion armMotion;
	private PlacerPreset preset = PlacerPreset.None;
	private PlacerPreset prevPreset = preset;
	private HashMap<ArmPreset, Double> armAngles = new HashMap<ArmPreset, Double>();
	private HashMap<PlacerPreset, ArmPreset> presetAngles = new HashMap<PlacerPreset, ArmPreset>();
	private boolean isStarted;
	
	public Arm(ISensorSet sensorSet) {
		
		//-7,53,79,143.6
		//-4.6,46,72.6,133.65
		
		armAngles.put(ArmPreset.NONE, 0.0);
		armAngles.put(ArmPreset.LOW, -4.6);
		armAngles.put(ArmPreset.MIDDLE, 46.0);
		armAngles.put(ArmPreset.HIGH, 72.6);
		armAngles.put(ArmPreset.BACK, 133.00);
		
		presetAngles.put(PlacerPreset.Floor, ArmPreset.LOW);
		presetAngles.put(PlacerPreset.Switch, ArmPreset.LOW);
		presetAngles.put(PlacerPreset.ScaleMin, ArmPreset.LOW);
		presetAngles.put(PlacerPreset.ScaleMid, ArmPreset.LOW);  //high
		presetAngles.put(PlacerPreset.ScaleMax, ArmPreset.MIDDLE);
		presetAngles.put(PlacerPreset.ScaleBack, ArmPreset.BACK);
		presetAngles.put(PlacerPreset.None, ArmPreset.NONE);
		presetAngles.put(PlacerPreset.CubeTravel, ArmPreset.MIDDLE);
		presetAngles.put(PlacerPreset.ScaleShoot, ArmPreset.MIDDLE);
		presetAngles.put(PlacerPreset.Start, ArmPreset.HIGH);
		armMotion = new ArmDecel(sensorSet);
		
	}
	
	public void setPreset(PlacerPreset preset) {
		//Set arm to preset position
		this.preset = preset;
		manualPreset = ArmPreset.NONE;
		if(preset != prevPreset){
			isStarted = false;
		}
		prevPreset = preset;
	}
	
	public void setManual(ArmPreset manualPreset) {
		//Set manual control, input -1, 1
		this.manualPreset = manualPreset;
		preset = PlacerPreset.None;
	}
	
	public double getAngle() {
		//Returns current angle of arm
		return armMotion.getAngle();
	}
	
	public double getSetpoint(){
		//Select correct angle set point
		double setPoint;
		if (preset == PlacerPreset.None)
			setPoint = getPresetSetpoint(manualPreset);
		
		else 
			setPoint = getPresetSetpoint(preset);		
		return setPoint;
	}

	public void execute() {
		//Set arm to move to correct set point
		armMotion.setArmAngle(getSetpoint());
		armMotion.execute();
		isStarted = true;
	}
	
	public boolean isDone(){
		return armMotion.isDone() && isStarted;
	}

	public double getPresetSetpoint(ArmPreset armPreset){
		return armAngles.get(armPreset);
	}
	
	public double getPresetSetpoint(PlacerPreset placerPreset){
		return armAngles.get(presetAngles.get(placerPreset));
	}
	
	public ArmPreset getArmPreset(){
		return preset == PlacerPreset.None ? manualPreset : presetAngles.get(preset);
	}

}
