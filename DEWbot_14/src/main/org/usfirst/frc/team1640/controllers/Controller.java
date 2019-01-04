package main.org.usfirst.frc.team1640.controllers;

import main.org.usfirst.frc.team1640.controllers.rumblers.AlternatingRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.BurstRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.DefaultRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.PulsingRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.RumbleManager;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

public class Controller implements IController {
	
	private XboxController xboxController;
	
	private double axisToFullThreshold = 1.0;
	private double axisInRangeLowerBound = 0.0;
	private double axisInRangeUpperBound = 0.0;
	private double deadBand = 0.0;

	private boolean axisEnteredRangeLeftX, axisExitedRangeLeftX;
	private boolean axisEnteredRangeRightX, axisExitedRangeRightX;
	private boolean axisEnteredRangeLeftY, axisExitedRangeLeftY;
	private boolean axisEnteredRangeRightY, axisExitedRangeRightY;
	private boolean axisEnteredRangeLeftTrigger, axisExitedRangeLeftTrigger;
	private boolean axisEnteredRangeRightTrigger, axisExitedRangeRightTrigger;
	
	private boolean povEastPressed, povEastReleased;
	private boolean povNorthEastPressed, povNorthEastReleased;
	private boolean povNorthPressed, povNorthReleased;
	private boolean povNorthWestPressed, povNorthWestReleased;
	private boolean povWestPressed, povWestReleased;
	private boolean povSouthWestPressed, povSouthWestReleased;
	private boolean povSouthPressed, povSouthReleased;
	private boolean povSouthEastPressed, povSouthEastReleased;
	
	private boolean leftBumperPressed, leftBumperReleased;
	private boolean rightBumperPressed, rightBumperReleased;
	private boolean leftStickButtonPressed, leftStickButtonReleased;
	private boolean rightStickButtonPressed, rightStickButtonReleased;
	private boolean aButtonPressed, aButtonReleased;
	private boolean bButtonPressed, bButtonReleased;
	private boolean xButtonPressed, xButtonReleased;
	private boolean yButtonPressed, yButtonReleased;
	private boolean backButtonPressed, backButtonReleased;
	private boolean startButtonPressed, startButtonReleased;
	
	private boolean prevAxisInRangeLeftX = false;
	private boolean prevAxisInRangeRightX = false;
	private boolean prevAxisInRangeLeftY = false;
	private boolean prevAxisInRangeRightY = false;
	private boolean prevAxisInRangeLeftTrigger = false;
	private boolean prevAxisInRangeRightTrigger = false;
	
	private boolean prevPOVEast = false;
	private boolean prevPOVNorthEast = false;
	private boolean prevPOVNorth = false;
	private boolean prevPOVNorthWest = false;
	private boolean prevPOVWest = false;
	private boolean prevPOVSouthWest = false;
	private boolean prevPOVSouth = false;
	private boolean prevPOVSouthEast = false;
	
	private boolean prevLeftBumper = false;
	private boolean prevRightBumper = false;
	private boolean prevLeftStickButton = false;
	private boolean prevRightStickButton = false;
	private boolean prevAButton = false;
	private boolean prevBButton = false;
	private boolean prevXButton = false;
	private boolean prevYButton = false;
	private boolean prevBackButton = false;
	private boolean prevStartButton = false;
	
	private RumbleManager rumbleManager;
	
	// TODO this needs to be tested
	public Controller(int port) {
		xboxController = new XboxController(port);
		
		rumbleManager = new RumbleManager();
	}
	
	public void update() {
		
		boolean axisInRangeLeftX = getLeftXInRange();
		boolean axisInRangeRightX = getRightXInRange();
		boolean axisInRangeLeftY = getLeftYInRange();
		boolean axisInRangeRightY = getRightYInRange();
		boolean axisInRangeLeftTrigger = getLeftTriggerInRange();
		boolean axisInRangeRightTrigger = getRightTriggerInRange();
		
		boolean povEast = getPOVEast();
		boolean povNorthEast = getPOVNorthEast();
		boolean povNorth = getPOVNorth();
		boolean povNorthWest = getPOVNorthWest();
		boolean povWest = getPOVWest();
		boolean povSouthWest = getPOVSouthWest();
		boolean povSouth = getPOVSouth();
		boolean povSouthEast = getPOVSouthEast();
		
		boolean leftBumper = getLeftBumper();
		boolean rightBumper = getRightBumper();
		boolean leftStickButton = getLeftStickButton();
		boolean rightStickButton = getRightStickButton();
		boolean aButton = getAButton();
		boolean bButton = getBButton();
		boolean xButton = getXButton();
		boolean yButton = getYButton();
		boolean backButton = getBackButton();
		boolean startButton = getStartButton();
		
		// rising edge
		axisEnteredRangeLeftX = (!prevAxisInRangeLeftX && axisInRangeLeftX);
		axisEnteredRangeRightX = (!prevAxisInRangeRightX && axisInRangeRightX);
		axisEnteredRangeLeftY = (!prevAxisInRangeLeftY && axisInRangeLeftY);
		axisEnteredRangeRightY = (!prevAxisInRangeRightY && axisInRangeRightY);
		axisEnteredRangeLeftTrigger = (!prevAxisInRangeLeftTrigger && axisInRangeLeftTrigger);
		axisEnteredRangeRightTrigger = (!prevAxisInRangeRightTrigger && axisInRangeRightTrigger);
		
		povEastPressed = (!prevPOVEast && povEast);
		povNorthEastPressed = (!prevPOVNorthEast && povNorthEast);
		povNorthPressed = (!prevPOVNorth && povNorth);
		povNorthWestPressed = (!prevPOVNorthWest && povNorthWest);
		povWestPressed = (!prevPOVWest && povWest);
		povSouthWestPressed = (!prevPOVSouthWest && povSouthWest);
		povSouthPressed = (!prevPOVSouth && povSouth);
		povSouthEastPressed = (!prevPOVSouthEast && povSouthEast);
		
		leftBumperPressed = (!prevLeftBumper && leftBumper);
		rightBumperPressed = (!prevRightBumper && rightBumper);
		leftStickButtonPressed = (!prevLeftStickButton && leftStickButton);
		rightStickButtonPressed = (!prevRightStickButton && rightStickButton);
		aButtonPressed = (!prevAButton && aButton);
		bButtonPressed = (!prevBButton && bButton);
		xButtonPressed = (!prevXButton && xButton);
		yButtonPressed = (!prevYButton && yButton);
		backButtonPressed = (!prevBackButton && backButton);
		startButtonPressed = (!prevStartButton && startButton);
		
		// falling edge
		axisExitedRangeLeftX = (prevAxisInRangeLeftX && !axisInRangeLeftX);
		axisExitedRangeRightX = (prevAxisInRangeRightX && !axisInRangeRightX);
		axisExitedRangeLeftY = (prevAxisInRangeLeftY && !axisInRangeLeftY);
		axisExitedRangeRightY = (prevAxisInRangeRightY && !axisInRangeRightY);
		axisExitedRangeLeftTrigger = (prevAxisInRangeLeftTrigger && !axisInRangeLeftTrigger);
		axisExitedRangeRightTrigger = (prevAxisInRangeRightTrigger && !axisInRangeRightTrigger);
		
		povEastReleased = (prevPOVEast && !povEast);
		povNorthEastReleased = (prevPOVNorthEast && !povNorthEast);
		povNorthReleased = (prevPOVNorth && !povNorth);
		povNorthWestReleased = (prevPOVNorthWest && !povNorthWest);
		povWestReleased = (prevPOVWest && !povWest);
		povSouthWestReleased = (prevPOVSouthWest && !povSouthWest);
		povSouthReleased = (prevPOVSouth && !povSouth);
		povSouthEastReleased = (prevPOVSouthEast && !povSouthEast);
		
		leftBumperReleased = (!prevLeftBumper && leftBumper);
		rightBumperReleased = (!prevRightBumper && rightBumper);
		leftStickButtonReleased = (!prevLeftStickButton && leftStickButton);
		rightStickButtonReleased = (!prevRightStickButton && rightStickButton);
		aButtonReleased = (prevAButton && !aButton);
		bButtonReleased = (prevBButton && !bButton);
		xButtonReleased = (prevXButton && !xButton);
		yButtonReleased = (prevYButton && !yButton);
		backButtonReleased = (prevBackButton && !backButton);
		startButtonReleased = (prevStartButton && !startButton);
		
		prevAxisInRangeLeftX = axisInRangeLeftX;
		prevAxisInRangeRightX = axisInRangeRightX;
		prevAxisInRangeLeftY = axisInRangeLeftY;
		prevAxisInRangeRightY = axisInRangeRightY;
		prevAxisInRangeLeftTrigger = axisInRangeLeftTrigger;
		prevAxisInRangeRightTrigger = axisInRangeRightTrigger;
		
		prevPOVEast = povEast;
		prevPOVNorthEast = povNorthEast;
		prevPOVNorth = povNorth;
		prevPOVNorthWest = povNorthWest;
		prevPOVWest = povWest;
		prevPOVSouthWest = povSouthWest;
		prevPOVSouth = povSouth;
		prevPOVSouthEast = povSouthEast;
		
		prevLeftBumper = leftBumper;
		prevRightBumper = rightBumper;
		prevLeftStickButton = leftStickButton;
		prevRightStickButton = rightStickButton;
		prevAButton = aButton;
		prevBButton = bButton;
		prevXButton = xButton;
		prevYButton = yButton;
		prevBackButton = backButton;
		prevStartButton = startButton;
		
		rumbleManager.update();
		
		xboxController.setRumble(RumbleType.kLeftRumble, rumbleManager.getLeftRumble());
		xboxController.setRumble(RumbleType.kRightRumble, rumbleManager.getRightRumble());
	}
	
	private double calcAxisToFull(double axis) {
		if (Math.abs(axis) > axisToFullThreshold) {
			return Math.signum(axis); // if axis passes threshold, return -1.0 or 1.0
		}
		else {
			return axis;
		}
	}
	
	public void setAxisToFullThreshold(double threshold) {
		axisToFullThreshold = threshold;
	}
	
	private boolean calcAxisInRange(double axis) {
		return MathUtilities.inRange(axis, axisInRangeLowerBound, axisInRangeUpperBound);
	}
	
	public void setAxisInRangeBounds(double lowerBound, double upperBound) {
		axisInRangeLowerBound = lowerBound;
		axisInRangeUpperBound = upperBound;
	}
	
	private double applyDeadband1D(double value) {
		double absValue = Math.abs(value);
		if (absValue < deadBand) {
			return 0.0;
		}
		else {
			return Math.signum(value)*(absValue-deadBand)/(1-deadBand);
		}
	}
	
	private double[] applyDeadband2D(double value1, double value2) {
		double magnitude = MathUtilities.magnitude(value1, value2);
		if (magnitude < deadBand) {
			return new double[] {0.0, 0.0};
		}
		else {
			double newMagnitude = applyDeadband1D(MathUtilities.magnitude(value1, value2));
			double r = newMagnitude/magnitude;
			return new double[] {r*value1, r*value2};
		}
	}
	
	public void setDeadband(double deadBand) {
		this.deadBand = deadBand;
	}
	
	public DefaultRumbler createDefaultRumbler() {
		return rumbleManager.createDefaultRumbler();
	}
	
	public BurstRumbler createBurstRumbler() {
		return rumbleManager.createBurstRumbler();
	}
	
	public AlternatingRumbler createAlternatingRumbler() {
		return rumbleManager.createAlternatingRumbler();
	}
	
	public PulsingRumbler createPulsingRumbler() {
		return rumbleManager.createPulsingRumbler();
	}
	
	public void cancelRumble() {
		rumbleManager.cancelRumble();
	}
	
	public void pauseRumble() {
		rumbleManager.pauseRumble();
	}
	
	public void resumeRumble() {
		rumbleManager.resumeRumble();
	}

	public double getLeftX() {
		double x = xboxController.getX(Hand.kLeft);
		double y = xboxController.getY(Hand.kLeft);
		return calcAxisToFull(applyDeadband2D(x, y)[0]);
	}
	
	public double getRightX() {
		double x = xboxController.getX(Hand.kRight);
		double y = xboxController.getY(Hand.kRight);
		return calcAxisToFull(applyDeadband2D(x, y)[0]);
	}

	public double getLeftY() {
		double x = xboxController.getX(Hand.kLeft);
		double y = -xboxController.getY(Hand.kLeft);
		return calcAxisToFull(applyDeadband2D(x, y)[1]);
	}
	
	public double getRightY() {
		double x = xboxController.getX(Hand.kRight);
		double y = -xboxController.getY(Hand.kRight);
		return calcAxisToFull(applyDeadband2D(x, y)[1]);
	}

	public double getLeftTrigger() {
		double axis = xboxController.getTriggerAxis(Hand.kLeft);
		return calcAxisToFull(applyDeadband1D(axis));
	}
	
	public double getRightTrigger() {
		double axis = xboxController.getTriggerAxis(Hand.kRight);
		return calcAxisToFull(applyDeadband1D(axis));
	}
	
	public boolean getLeftXInRange() {
		return calcAxisInRange(getLeftX());
	}
	
	public boolean getRightXInRange() {
		return calcAxisInRange(getRightX());
	}
	
	public boolean getLeftYInRange() {
		return calcAxisInRange(getLeftY());
	}
	
	public boolean getRightYInRange() {
		return calcAxisInRange(getRightY());
	}
	
	public boolean getLeftTriggerInRange() {
		return calcAxisInRange(getLeftTrigger());
	}
	
	public boolean getRightTriggerInRange() {
		return calcAxisInRange(getRightTrigger());
	}
	
	public boolean getLeftXEnteredRange() {
		return axisEnteredRangeLeftX;
	}
	
	public boolean getRightXEnteredRange() {
		return axisEnteredRangeRightX;
	}
	
	public boolean getLeftYEnteredRange() {
		return axisEnteredRangeLeftY;
	}
	
	public boolean getRightYEnteredRange() {
		return axisEnteredRangeRightY;
	}
	
	public boolean getLeftTriggerEnteredRange() {
		return axisEnteredRangeLeftTrigger;
	}
	
	public boolean getRightTriggerEnteredRange() {
		return axisEnteredRangeRightTrigger;
	}
	
	public boolean getLeftXExitedRange() {
		return axisExitedRangeLeftX;
	}
	
	public boolean getRightXExitedRange() {
		return axisExitedRangeRightX;
	}
	
	public boolean getLeftYExitedRange() {
		return axisExitedRangeLeftY;
	}
	
	public boolean getRightYExitedRange() {
		return axisExitedRangeRightY;
	}
	
	public boolean getLeftTriggerExitedRange() {
		return axisExitedRangeLeftTrigger;
	}
	
	public boolean getRightTriggerExitedRange() {
		return axisExitedRangeRightTrigger;
	}

	public boolean getLeftBumper() {
		return xboxController.getBumper(Hand.kLeft);
	}
	
	public boolean getRightBumper() {
		return xboxController.getBumper(Hand.kRight);
	}

	public boolean getLeftBumperPressed() {
		return leftBumperPressed;
	}
	
	public boolean getRightBumperPressed() {
		return rightBumperPressed;
	}
	
	public boolean getLeftBumperReleased() {
		return leftBumperReleased;
	}
	
	public boolean getRightBumperReleased() {
		return rightBumperReleased;
	}
	
	public boolean getLeftStickButton() {
		return xboxController.getStickButton(Hand.kLeft);
	}
	
	public boolean getRightStickButton() {
		return xboxController.getStickButton(Hand.kRight);
	}
	
	public boolean getLeftStickButtonPressed() {
		return leftStickButtonPressed;
	}
	
	public boolean getRightStickButtonPressed() {
		return rightStickButtonPressed;
	}
	
	public boolean getLeftStickButtonReleased() {
		return leftStickButtonReleased;
	}
	
	public boolean getRightStickButtonReleased() {
		return rightStickButtonReleased;
	}
	
	public boolean getAButton() {
		return xboxController.getAButton();
	}

	public boolean getAButtonPressed() {
		return aButtonPressed;
	}

	public boolean getAButtonReleased() {
		return aButtonReleased;
	}
	
	public boolean getBButton() {
		return xboxController.getBButton();
	}

	public boolean getBButtonPressed() {
		return bButtonPressed;
	}
	
	public boolean getBButtonReleased() {
		return bButtonReleased;
	}

	public boolean getXButton() {
		return xboxController.getXButton();
	}

	public boolean getXButtonPressed() {
		return xButtonPressed;
	}

	public boolean getXButtonReleased() {
		return xButtonReleased;
	}

	public boolean getYButton() {
		return xboxController.getYButton();
	}

	public boolean getYButtonPressed() {
		return yButtonPressed;
	}

	public boolean getYButtonReleased() {
		return yButtonReleased;
	}

	public boolean getBackButton() {
		return xboxController.getBackButton();
	}

	public boolean getBackButtonPressed() {
		return backButtonPressed;
	}

	public boolean getBackButtonReleased() {
		return backButtonReleased;
	}

	public boolean getStartButton() {
		return xboxController.getStartButton();
	}

	public boolean getStartButtonPressed() {
		return startButtonPressed;
	}

	public boolean getStartButtonReleased() {
		return startButtonReleased;
	}
	
	public int getPOV() {
		return xboxController.getPOV(0);
	}
	
	public boolean getPOVEast() {
		return getPOV() == 90;
	}
	
	public boolean getPOVNorthEast() {
		return getPOV() == 45;
	}
	
	public boolean getPOVNorth() {
		return getPOV() == 0;
	}
	
	public boolean getPOVNorthWest() {
		return getPOV() == 315;
	}
	
	public boolean getPOVWest() {
		return getPOV() == 270;
	}
	
	public boolean getPOVSouthWest() {
		return getPOV() == 225;
	}
	
	public boolean getPOVSouth() {
		return getPOV() == 180;
	}
	
	public boolean getPOVSouthEast() {
		return getPOV() == 135;
	}
	
	public boolean getPOVEastPressed() {
		return povEastPressed;
	}
	
	public boolean getPOVNorthEastPressed() {
		return povNorthEastPressed;
	}
	
	public boolean getPOVNorthPressed() {
		return povNorthPressed;
	}
	
	public boolean getPOVNorthWestPressed() {
		return povNorthWestPressed;
	}
	
	public boolean getPOVWestPressed() {
		return povWestPressed;
	}
	
	public boolean getPOVSouthWestPressed() {
		return povSouthWestPressed;
	}
	
	public boolean getPOVSouthPressed() {
		return povSouthPressed;
	}
	
	public boolean getPOVSouthEastPressed() {
		return povSouthEastPressed;
	}
	
	public boolean getPOVEastReleased() {
		return povEastReleased;
	}
	
	public boolean getPOVNorthEastReleased() {
		return povNorthEastReleased;
	}
	
	public boolean getPOVNorthReleased() {
		return povNorthReleased;
	}
	
	public boolean getPOVNorthWestReleased() {
		return povNorthWestReleased;
	}
	
	public boolean getPOVWestReleased() {
		return povWestReleased;
	}
	
	public boolean getPOVSouthWestReleased() {
		return povSouthWestReleased;
	}
	
	public boolean getPOVSouthReleased() {
		return povSouthReleased;
	}
	
	public boolean getPOVSouthEastReleased() {
		return povSouthEastReleased;
	}
}
