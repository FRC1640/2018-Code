package main.org.usfirst.frc.team1640.robot.tests;

import main.org.usfirst.frc.team1640.controllers.Controller;
import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class ControllerTestStrategy implements Strategy {
	private IController controller;
	
	public ControllerTestStrategy(IController driverController) {
		this.controller = driverController;
	}

	@Override
	public void init() {
	}

	@Override
	public void execute() {
		controller.setAxisInRangeBounds(0.75, 1.0);
		controller.setDeadband(0.2);
		
//		if (controller.getPOV() != -1) System.out.println(controller.getPOV());
		
//		if (controller.getAButton()) System.out.println("A Button");
//		if (controller.getBButton()) System.out.println("B Button");
//		if (controller.getXButton()) System.out.println("X Button");
//		if (controller.getYButton()) System.out.println("Y Button");
//		if (controller.getStartButton()) System.out.println("Start Button");
//		if (controller.getBackButton()) System.out.println("Back Button");
//		if (controller.getLeftBumper()) System.out.println("Left Bumper");
//		if (controller.getRightBumper()) System.out.println("Right Bumper");
//		if (controller.getLeftStickButton()) System.out.println("Left Stick Button");
//		if (controller.getRightStickButton()) System.out.println("Right Stick Button");
//		if (controller.getLeftXInRange()) System.out.println("Left X In Range");
//		if (controller.getRightXInRange()) System.out.println("Right X In Range");
//		if (controller.getLeftYInRange()) System.out.println("Left Y In Range");
//		if (controller.getRightYInRange()) System.out.println("Right Y In Range");
//		if (controller.getLeftTriggerInRange()) System.out.println("Left Trigger In Range");
//		if (controller.getRightTriggerInRange()) System.out.println("Right Trigger In Range");
//		if (controller.getPOVEast()) System.out.println("POV East");
//		if (controller.getPOVNorthEast()) System.out.println("POV North East");
//		if (controller.getPOVNorth()) System.out.println("POV North");
//		if (controller.getPOVNorthWest()) System.out.println("POV North West");
//		if (controller.getPOVWest()) System.out.println("POV West");
//		if (controller.getPOVSouthWest()) System.out.println("POV South West");
//		if (controller.getPOVSouth()) System.out.println("POV South");
//		if (controller.getPOVSouthEast()) System.out.println("POV South East");
		
		// Rising Edge
		if (controller.getAButtonPressed()) System.out.println("A Button Pressed");
		if (controller.getBButtonPressed()) System.out.println("B Button Pressed");
		if (controller.getXButtonPressed()) System.out.println("X Button Pressed");
		if (controller.getYButtonPressed()) System.out.println("Y Button Pressed");
		if (controller.getStartButtonPressed()) System.out.println("Start Button Pressed");
		if (controller.getBackButtonPressed()) System.out.println("Back Button Pressed");
		if (controller.getLeftBumperPressed()) System.out.println("Left Bumper Pressed");
		if (controller.getRightBumperPressed()) System.out.println("Right Bumper Pressed");
		if (controller.getLeftStickButtonPressed()) System.out.println("Left Stick Button Pressed");
		if (controller.getRightStickButtonPressed()) System.out.println("Right Stick Button Pressed");
		if (controller.getLeftXEnteredRange()) System.out.println("Left X Entered Range");
		if (controller.getRightXEnteredRange()) System.out.println("Right X Entered Range");
		if (controller.getLeftYEnteredRange()) System.out.println("Left Y Entered Range");
		if (controller.getRightYEnteredRange()) System.out.println("Right Y Entered Range");
		if (controller.getLeftTriggerEnteredRange()) System.out.println("Left Trigger Entered Range");
		if (controller.getRightTriggerEnteredRange()) System.out.println("Right Trigger Entered Range");
		if (controller.getPOVEastPressed()) System.out.println("POV East Pressed");
		if (controller.getPOVNorthEastPressed()) System.out.println("POV North East Pressed");
		if (controller.getPOVNorthPressed()) System.out.println("POV North Pressed");
		if (controller.getPOVNorthWestPressed()) System.out.println("POV North West Pressed");
		if (controller.getPOVWestPressed()) System.out.println("POV West Pressed");
		if (controller.getPOVSouthWestPressed()) System.out.println("POV South West Pressed");
		if (controller.getPOVSouthPressed()) System.out.println("POV South Pressed");
		if (controller.getPOVSouthEastPressed()) System.out.println("POV South East Pressed");
		
		// Falling Edge
		if (controller.getAButtonReleased()) System.out.println("A Button Released");
		if (controller.getBButtonReleased()) System.out.println("B Button Released");
		if (controller.getXButtonReleased()) System.out.println("X Button Released");
		if (controller.getYButtonReleased()) System.out.println("Y Button Released");
		if (controller.getStartButtonReleased()) System.out.println("Start Button Released");
		if (controller.getBackButtonReleased()) System.out.println("Back Button Released");
		if (controller.getLeftBumperReleased()) System.out.println("Left Bumper Released");
		if (controller.getRightBumperReleased()) System.out.println("Right Bumper Released");
		if (controller.getLeftStickButtonReleased()) System.out.println("Left Stick Button Released");
		if (controller.getRightStickButtonReleased()) System.out.println("Right Stick Button Released");
		if (controller.getLeftXExitedRange()) System.out.println("Left X Exited Range");
		if (controller.getRightXExitedRange()) System.out.println("Right X Exited Range");
		if (controller.getLeftYExitedRange()) System.out.println("Left Y Exited Range");
		if (controller.getRightYExitedRange()) System.out.println("Right Y Exited Range");
		if (controller.getLeftTriggerExitedRange()) System.out.println("Left Trigger Exited Range");
		if (controller.getRightTriggerExitedRange()) System.out.println("Right Trigger Exited Range");
		if (controller.getPOVEastReleased()) System.out.println("POV East Released");
		if (controller.getPOVNorthEastReleased()) System.out.println("POV North East Released");
		if (controller.getPOVNorthReleased()) System.out.println("POV North Released");
		if (controller.getPOVNorthWestReleased()) System.out.println("POV North West Released");
		if (controller.getPOVWestReleased()) System.out.println("POV West Released");
		if (controller.getPOVSouthWestReleased()) System.out.println("POV South West Released");
		if (controller.getPOVSouthReleased()) System.out.println("POV South Released");
		if (controller.getPOVSouthEastReleased()) System.out.println("POV South East Released");
		
	}

	@Override
	public void end() {
	}

}
