package main.org.usfirst.frc.team1640.robot.placer;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.placer.Placer.PlacerPreset;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.placer.ToScaleMaxCommand;
import main.org.usfirst.frc.team1640.robot.auton.scripts.placer.ToScaleMidCommand;
import main.org.usfirst.frc.team1640.robot.auton.scripts.placer.ToScaleMinCommand;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class PlacerController {
	
	private Placer placer;
	private IController opController;
	private double prevLeftY, prevRightY;
	private boolean prevN, prevNE, prevNW, prevS, prevSW, prevSE;
	
	public PlacerController(IRobotContext robotContext) {
		this.opController = robotContext.getOperatorController();
		placer = robotContext.getPlacer();
	}
	
	public void init(){
		System.out.println("Placer controller init");
	}
	
	public void execute() {
		
		opController.setDeadband(0.3);
		double leftY = opController.getLeftY();
		double rightY = opController.getRightY();
		boolean north = opController.getPOVNorth();
		boolean NE = opController.getPOVNorthEast();
		boolean NW = opController.getPOVNorthWest();
		boolean south = opController.getPOVSouth();
		boolean SW = opController.getPOVSouthWest();
		boolean SE = opController.getPOVSouthEast();
		
		if (opController.getAButtonReleased()) {
			System.out.println("Placer to floor");
			placer.setPreset(PlacerPreset.Floor);
		}
		else if (opController.getBButtonReleased()) {
			System.out.println("Placer to switch");
			placer.setPreset(PlacerPreset.Switch);
		}
		else if (opController.getXButtonReleased()) {
			System.out.println("Placer to transport");
			placer.setPreset(PlacerPreset.CubeTravel);
		}

		else if (opController.getYButtonReleased()) {
			System.out.println("Placer to Scale Mid");
			placer.setPreset(PlacerPreset.ScaleMid);
		}
		else if (opController.getLeftBumperReleased()) {
			System.out.println("Placer to climb");
			placer.setPreset(PlacerPreset.ScaleShoot);
		}

		else if (opController.getLeftTriggerExitedRange()) {
			System.out.println("Placer to Scale Min");
			placer.setPreset(PlacerPreset.ScaleMin);
		}
		else if (opController.getRightBumper() && opController.getRightTriggerExitedRange()) {
			System.out.println("Placer to Scale back");
			placer.setPreset(PlacerPreset.ScaleBack);
		}

		else if (opController.getRightTriggerExitedRange()) {
			System.out.println("Placer to Scale max");
			placer.setPreset(PlacerPreset.ScaleMax);
		}
		else if(getButtonFallingEdge(north || NW || NE, prevN || prevNE || prevNW)) {
			System.out.println("Placer up 1 cube");
			placer.setDeltaCube(1);
		}
		else if(getButtonFallingEdge(south || SW || SE, prevS || prevSE || prevSW)){
			System.out.println("Placer down 1 cube");
			placer.setDeltaCube(-1);
			
		}
		else {
			if(leftY != 0 || getAxisFallingEdge(leftY, prevLeftY)){
				placer.setLiftManual(leftY);
			}
			if(getAxisEnteredRange(rightY, prevRightY, 0.75)){
				boolean up = rightY > 0;
				placer.setArmManual(up);
			}
		}
		if (opController.getBackButtonPressed()) {
			System.out.println("Placer controller resetting encoder");
			placer.resetLiftEncoder();
		}
		prevLeftY = leftY;
		prevN = north;
		prevNW = NW;
		prevNE = NE;
		prevS = south;
		prevSW = SW;
		prevSE = SE;
		prevRightY = rightY;
		
		placer.execute();
	}
	
	private boolean getAxisFallingEdge(double current, double prev) {
		return current == 0 && prev !=0;
	}
	private boolean getButtonFallingEdge(boolean current, boolean prev) {
		return current && !prev;
	}
	private boolean getAxisEnteredRange(double current, double prev, double minVal){
		return Math.abs(current) > minVal && Math.abs(prev) < minVal;
	}
	
	public double getLiftHeight() {
		return placer.getLiftHeight();
	}

}

