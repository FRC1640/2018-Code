package main.org.usfirst.frc.team1640.controllers;

import main.org.usfirst.frc.team1640.controllers.rumblers.AlternatingRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.BurstRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.DefaultRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.PulsingRumbler;


public interface IController {
	
	public void update();
	
	public void setAxisToFullThreshold(double threshold);
	
	public void setAxisInRangeBounds(double lowerBound, double upperBound);
	
	public void setDeadband(double deadBand);
	
	public DefaultRumbler createDefaultRumbler();
	
	public BurstRumbler createBurstRumbler();
	
	public AlternatingRumbler createAlternatingRumbler();
	
	public PulsingRumbler createPulsingRumbler();
	
	public void cancelRumble();
	
	public void pauseRumble();
	
	public void resumeRumble();

	public double getLeftX();
	
	public double getRightX();

	public double getLeftY();
	
	public double getRightY();

	public double getLeftTrigger();
	
	public double getRightTrigger();
	
	public boolean getLeftXInRange();
	
	public boolean getRightXInRange();
	
	public boolean getLeftYInRange();
	
	public boolean getRightYInRange();
	
	public boolean getLeftTriggerInRange();
	
	public boolean getRightTriggerInRange();
	
	public boolean getLeftXEnteredRange();
	
	public boolean getRightXEnteredRange();
	
	public boolean getLeftYEnteredRange();
	
	public boolean getRightYEnteredRange();
	
	public boolean getLeftTriggerEnteredRange();
	
	public boolean getRightTriggerEnteredRange();
	
	public boolean getLeftXExitedRange();
	
	public boolean getRightXExitedRange();
	
	public boolean getLeftYExitedRange();
	
	public boolean getRightYExitedRange();
	
	public boolean getLeftTriggerExitedRange();
	
	public boolean getRightTriggerExitedRange();

	public boolean getLeftBumper();
	
	public boolean getRightBumper();

	public boolean getLeftBumperPressed();
	
	public boolean getRightBumperPressed();
	
	public boolean getLeftBumperReleased();
	
	public boolean getRightBumperReleased();
	
	public boolean getLeftStickButton();
	
	public boolean getRightStickButton();
	
	public boolean getLeftStickButtonPressed();
	
	public boolean getRightStickButtonPressed();
	
	public boolean getLeftStickButtonReleased();
	
	public boolean getRightStickButtonReleased();
	
	public boolean getAButton();

	public boolean getAButtonPressed() ;

	public boolean getAButtonReleased();
	
	public boolean getBButton();

	public boolean getBButtonPressed();
	
	public boolean getBButtonReleased();

	public boolean getXButton();

	public boolean getXButtonPressed();

	public boolean getXButtonReleased();

	public boolean getYButton();

	public boolean getYButtonPressed();

	public boolean getYButtonReleased();

	public boolean getBackButton();
	
	public boolean getBackButtonPressed();

	public boolean getBackButtonReleased();

	public boolean getStartButton();

	public boolean getStartButtonPressed();

	public boolean getStartButtonReleased();
	
	public int getPOV();
	
	public boolean getPOVEast();
	
	public boolean getPOVNorthEast();
	
	public boolean getPOVNorth();
	
	public boolean getPOVNorthWest();
	
	public boolean getPOVWest();
	
	public boolean getPOVSouthWest();
	
	public boolean getPOVSouth();
	
	public boolean getPOVSouthEast();
	
	public boolean getPOVEastPressed();
	
	public boolean getPOVNorthEastPressed();
	
	public boolean getPOVNorthPressed();
	
	public boolean getPOVNorthWestPressed();
	
	public boolean getPOVWestPressed();
	
	public boolean getPOVSouthWestPressed();
	
	public boolean getPOVSouthPressed();
	
	public boolean getPOVSouthEastPressed();
	
	public boolean getPOVEastReleased();
	
	public boolean getPOVNorthEastReleased();
	
	public boolean getPOVNorthReleased();
	
	public boolean getPOVNorthWestReleased();
	
	public boolean getPOVWestReleased();
	
	public boolean getPOVSouthWestReleased();
	
	public boolean getPOVSouthReleased() ;
	
	public boolean getPOVSouthEastReleased();
}
