package main.org.usfirst.frc.team1640.placer;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;
import main.org.usfirst.frc.team1640.placer.arm.Arm;
import main.org.usfirst.frc.team1640.placer.arm.Arm.ArmPreset;
import main.org.usfirst.frc.team1640.placer.lift.Lift;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class Placer {
	public enum PlacerPreset {Floor, Switch, ScaleMin, ScaleMid, ScaleMax, None, ScaleBack, CubeTravel, ScaleShoot, Start};
	private PlacerPreset preset = PlacerPreset.Start;
	private PlacerPreset prevPreset = PlacerPreset.None;
	private PlacerPreset prevState = PlacerPreset.None;
	private ArmPreset manualArmPreset = ArmPreset.NONE;

	private Lift lift;
	private Arm arm;
	private static ArmPreset[] armVals;

	private double manualLiftValue;
	private int deltaCube;	
	
	private boolean setpointInit;
	private ElapsedTimer backTimer;
	
	public Placer(ISensorSet sensorSet){
		lift = new Lift();
		arm = new Arm(sensorSet);
		lift.setPreset(preset);
		arm.setPreset(preset);
		
		manualArmPreset = ArmPreset.NONE;
		armVals = ArmPreset.values();
		
		backTimer = new ElapsedTimer();
	}
	
	public void setPreset(PlacerPreset preset) {
		this.preset = preset;
		manualLiftValue = 0;
		manualArmPreset = ArmPreset.NONE;
		deltaCube = 0;
		System.out.println("arm preset: " + preset);
	}
	
	public void setArmManual(ArmPreset armPreset) {
		manualArmPreset = armPreset;
		preset = PlacerPreset.None;
	}
	
	public void setArmManual(boolean up){
		int delta = up ? 1 : -1;
		ArmPreset currentPreset = arm.getArmPreset(); 
		if(currentPreset == ArmPreset.NONE){
			System.out.println("Arm preset is none.");
			return;
		}
		if(!(!up && currentPreset == ArmPreset.LOW)
			&& !(up && currentPreset == ArmPreset.HIGH)){
			manualArmPreset = armVals[(currentPreset.ordinal() + delta) % armVals.length];
			System.out.println("Manual preset from joystick: " + manualArmPreset);
			preset = PlacerPreset.None;
		}
	}
	
	public void setLiftManual(double manualValue) {
		manualLiftValue = manualValue;
		deltaCube = 0;
		preset = PlacerPreset.None;
	}
	
	public void setDeltaCube(int delta) {
		deltaCube += delta;
	}
	
	public void execute(){
		if(preset != prevPreset){
			prevState = prevPreset;
			setpointInit = false;
		}
		
		switch(preset){
			case None:{
				manualControl();
				break;
			}
			case ScaleBack:{
				scaleBack();
				break;
			}
			case ScaleMin: case ScaleMid:{
				liftThenArm();
				break;
			}
			default: { 
				if(prevState == PlacerPreset.ScaleMin || prevState == PlacerPreset.ScaleMid){
					armThenLift();
				}
				else{
					sameTime();
				}
				break;
			}
		}
		
		if (isLiftSetPointValid(lift.getDeltaCubeSetpoint(deltaCube))) {
			lift.setDeltaCube(deltaCube);
		}
		else{
			if(lift.getDeltaCubeSetpoint(deltaCube) < PlacerConstants.FLOOR_BUFFER && preset != PlacerPreset.None){
				deltaCube += 1;
			}
			else{
				deltaCube -= 1;
			}
		}
		arm.execute();
		lift.execute();
		prevPreset = preset;
	}
	
	private void manualControl(){
		lift.setManualSpeed(manualLiftValue);
		
		if (isArmManualValid(manualArmPreset) && manualArmPreset != ArmPreset.NONE){
			arm.setManual(manualArmPreset);
		}
	}
	
	private void scaleBack(){
		if(!setpointInit){
			setpointInit = true;
			backTimer.restart();
		}
		lift.setPreset(preset);
		if (isArmBack() || lift.getHeight() >= PlacerConstants.MIN_BACK_HEIGHT) {
			arm.setPreset(preset);
		}
		else if(isArmFront() && backTimer.getElapsedMilliseconds() > 500){
			arm.setManual(ArmPreset.HIGH);
		}
	}

	private void armThenLift(){
		arm.setPreset(preset);
		if(isArmFront() && arm.isDone()){
			lift.setPreset(preset);
		}
	}
	
	private void liftThenArm(){
		if(!isArmFront()){
			arm.setPreset(preset);
		}
		else{
			lift.setPreset(preset);
			if(lift.isDone()){
				arm.setPreset(preset);
			}
		}
	}
	
	private void sameTime(){
		if(isArmFront()){
			lift.setPreset(preset);
		}
		arm.setPreset(preset);
	}
	
	private boolean isArmFront() {
		return arm.getAngle() < 80;
	}
	
	private boolean isArmBack() {
		return arm.getAngle() > 110;
	}
	
	private boolean isArmManualValid(ArmPreset manualArmPreset) {
		double manualSetPoint = arm.getPresetSetpoint(manualArmPreset);
		if (manualSetPoint <= PlacerConstants.ARM_LOWER_LIMIT || manualSetPoint >= PlacerConstants.ARM_UPPER_LIMIT) {
			return false;
		}
		
		if (manualSetPoint >= PlacerConstants.ARM_FRONT_UPPER_LIMIT && lift.getHeight() >= PlacerConstants.MIN_BACK_HEIGHT) {
			return false;
		}
		
		return true;
	}
	
	private  boolean isLiftSetPointValid(double setPoint) {
		if (setPoint < PlacerConstants.FLOOR_BUFFER || setPoint > PlacerConstants.MAX_HEIGHT) {
			return false;
		}
		
		if (isArmBack() && setPoint <= PlacerConstants.MIN_BACK_HEIGHT) {
			return false;
		}
		return true;
	}
	
	public boolean atSetpoint(){
		return lift.isDone() && arm.isDone();
	}
	
	public void resetLiftEncoder() {
		lift.reset();
	}
	
	public double getLiftHeight(){
		return lift.getHeight();
	}	
	
	public double getArmAngle(){
		return arm.getAngle();
	}
}
