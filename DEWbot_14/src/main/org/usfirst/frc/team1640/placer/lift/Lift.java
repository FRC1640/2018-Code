package main.org.usfirst.frc.team1640.placer.lift;

import java.util.HashMap;

import main.org.usfirst.frc.team1640.constants.mechanical.PlacerConstants;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.placer.lift.motion.LiftMotion;
import main.org.usfirst.frc.team1640.placer.lift.motion.LiftMotionMagic;
import main.org.usfirst.frc.team1640.placer.lift.motion.LiftPercentOutput;
import main.org.usfirst.frc.team1640.placer.lift.motion.TestLiftMotion;

public class Lift {
	
	private LiftMotion liftMotion;
	private PlacerPreset liftPreset = PlacerPreset.None;
	private HashMap<PlacerPreset, Double> liftHeight = new HashMap<PlacerPreset, Double>();
//	private final double MANUAL_SPEED = 0.2;
	private int deltaCube;
	private PlacerPreset prevPreset = liftPreset;
	private double currentSetpoint;
	private boolean isStarted;
	private double manualSpeed;
	
	private final LiftMotion MANUAL_MOTION = new LiftPercentOutput();
	private final LiftMotion TEST_MOTION = new TestLiftMotion();
	private final LiftMotion NORMAL_MOTION = new LiftMotionMagic();
	
	private int prevDeltaCube;
	private boolean manualControl, prevManualControl;
	
	public Lift() {
		double scaleMax = 60;
		double floor = 0.5;
		liftPreset = PlacerPreset.None;
		liftHeight.put(PlacerPreset.Floor, floor);
		liftHeight.put(PlacerPreset.Switch, 25.0);
		liftHeight.put(PlacerPreset.ScaleMin, 59.0); //before worlds: 56
		liftHeight.put(PlacerPreset.ScaleMax, scaleMax);
		liftHeight.put(PlacerPreset.ScaleMid, 67.0); //before worlds: 67
		liftHeight.put(PlacerPreset.None, 0.0);
		liftHeight.put(PlacerPreset.ScaleBack, 57.5);
		liftHeight.put(PlacerPreset.CubeTravel, floor);
		liftHeight.put(PlacerPreset.ScaleShoot, 40.0);
		liftHeight.put(PlacerPreset.Start, 0.0);
		liftMotion = NORMAL_MOTION;
		liftMotion.init();
	}
	
	public void setPreset(PlacerPreset liftPreset) {
		//Set lift to preset
		this.liftPreset = liftPreset;
		currentSetpoint = liftHeight.get(liftPreset);
		if(liftPreset != prevPreset){
			isStarted = false;
		}
		prevPreset = liftPreset;
		manualSpeed = 0;
		
		manualControl = false;
	}
	
	public void setManualSpeed(double manualSpeed){
		liftPreset = PlacerPreset.None;
		this.manualSpeed = manualSpeed;
		
		if(manualSpeed != 0){
			manualControl = true;
		}
	}
	
	public void setDeltaCube(int deltaCube) {
		prevDeltaCube = this.deltaCube;
		this.deltaCube = deltaCube;
//		liftMotion = NORMAL_MOTION;
		
		manualControl = manualControl && prevDeltaCube == deltaCube;
		if(!manualControl){
			manualSpeed = 0;
		}		
//		System.out.println("mc: " + manualControl + " dc: " + deltaCube + " pc: " + prevDeltaCube);
	}
	
	public double getDeltaCubeSetpoint(int deltaCube){
		double dcSetpoint = currentSetpoint + PlacerConstants.CUBE_HEIGHT * deltaCube;
		dcSetpoint = Math.min(dcSetpoint, PlacerConstants.MAX_HEIGHT - 0.5);
		dcSetpoint = Math.max(dcSetpoint, 0);
		return dcSetpoint;
	}
	
	public double getSetpoint(){
		return getDeltaCubeSetpoint(deltaCube);
	}
	
	public void execute(){
		if(manualControl){ //manual control
			liftMotion = MANUAL_MOTION;
			currentSetpoint = getHeight();
//			System.out.println("setting cs: " + currentSetpoint);
			liftMotion.setLiftHeight(manualSpeed);
		}
		else{
			if(prevManualControl){
				liftMotion.setLiftHeight(0);
				liftMotion = NORMAL_MOTION;
			}
			if (getSetpoint() >= PlacerConstants.FLOOR_BUFFER && getSetpoint() <= PlacerConstants.MAX_HEIGHT) {
				liftMotion.setLiftHeight(getSetpoint());
				isStarted = true;
			}
		}
		liftMotion.execute();
		
		prevManualControl = manualControl;
	}
	
	public void reset() {
		liftMotion.reset();
	}
	
	public boolean isDone(){
		return liftMotion.isDone() && isStarted;
	}
	
	public double getHeight() {
		//Return current lift height
		return liftMotion.getHeight();
	}
}
