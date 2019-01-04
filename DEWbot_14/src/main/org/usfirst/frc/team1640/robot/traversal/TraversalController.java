package main.org.usfirst.frc.team1640.robot.traversal;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.controllers.rumblers.AlternatingRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.DefaultRumbler;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ControllerShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.drive.ManualShiftingDrive;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.AdaptiveCOMOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.DefaultOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.FieldCentricOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.GyroCorrectedOcelot;
import main.org.usfirst.frc.team1640.robot.traversal.ocelot.OcelotStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.sunflower.ShortestAngleSunflower;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class TraversalController implements Strategy {
	private IController driverController;
	private IGyro gyro;
	private IDriveTrain driveTrain;
	private Placer placer;
	
	private AlternatingRumbler centricRumbler;
	private DefaultRumbler snapRumbler;
	
	private final ControllerShiftingDrive autoDrive;
	private final ManualShiftingDrive manualDrive;
	private final DefaultSteer steer;
	private final FieldCentricOcelot fieldCentric;
	private final DefaultOcelot defaultOcelot;
	private final AdaptiveCOMOcelot comOcelot;
	private final ShortestAngleSunflower snapSunflower;
	private final GyroCorrectedOcelot gyroCorrectedOcelot;
	
	private OcelotStrategy myOcelotStrat;
	private TraversalStrategy myStrat;
	
	private boolean snap;
	private double direction;
	private double driveScaling;
	
	private boolean prevLeftBumper;
	
	public TraversalController(IRobotContext robotContext) {
		driverController = robotContext.getDriverController();
		gyro = robotContext.getSensorSet().getGyro();
		driveTrain = robotContext.getDriveTrain();
		placer = robotContext.getPlacer();
		
		centricRumbler = driverController.createAlternatingRumbler();
		snapRumbler = driverController.createDefaultRumbler();
		
		// Elemental strategies (used to compose the main ones)
		autoDrive = new ControllerShiftingDrive(driveTrain);
		manualDrive = new ManualShiftingDrive(driveTrain);
		manualDrive.setTransmission(1);
		steer = new DefaultSteer(driveTrain);
		defaultOcelot = new DefaultOcelot(driveTrain, steer, autoDrive);
		comOcelot = new AdaptiveCOMOcelot(defaultOcelot, placer);
		fieldCentric = new FieldCentricOcelot(driveTrain, gyro, comOcelot);
		snapSunflower = new ShortestAngleSunflower(driveTrain, gyro, fieldCentric, 0.01, 0.0, 0.0001, 0.9);
		gyroCorrectedOcelot = new GyroCorrectedOcelot(driveTrain, gyro, fieldCentric);
		
		// Main Drive Strategies (default values)
		myOcelotStrat = fieldCentric;
		myStrat = gyroCorrectedOcelot;
		
		snap = false;
		direction = 0;
		driveScaling = 1.0;
	}

	@Override
	public void init() {
		myStrat.init();
	}

	@Override
	public void execute() {
		driverController.setDeadband(0.3);
		double x1 = driverController.getLeftX();
		double y1 = driverController.getLeftY();
		double x2 = driverController.getRightX();
		double y2 = driverController.getRightY();
		
		boolean leftBumper = driverController.getLeftBumper();
		// Normal Driving
		gyroCorrectedOcelot.setLateralDrive(driveScaling*x1);
		gyroCorrectedOcelot.setLongitudinalDrive(driveScaling*y1);
		gyroCorrectedOcelot.setAxialDrive(driveScaling*x2);
		
		// Snap Driving
		snapSunflower.setLateralDrive(driveScaling*x1);
		snapSunflower.setLongitudinalDrive(driveScaling*y1);
		snap = MathUtilities.magnitude(x2, y2) > 0.5;
		if (snap) {
			direction = snapDirection(MathUtilities.angle(x2, y2));
			snapSunflower.setDirection(direction);
		}
		
		// Switch fieldcentric on and off
		if (driverController.getBackButtonReleased()) {
			System.out.println("switching fc");
			if (myOcelotStrat == fieldCentric) {
				myOcelotStrat = comOcelot;
				centricRumbler.rumble(0.25, 0.5, -1);
			}
			else {
				myOcelotStrat = fieldCentric;
				centricRumbler.cancel();
			}
			snapSunflower.setOcelotStrategy(myOcelotStrat);
			gyroCorrectedOcelot.setOcelotStrategy(myOcelotStrat);
		}
		
		if(driverController.getLeftBumperPressed()){
			myOcelotStrat = comOcelot;
			centricRumbler.rumble(0.25, 0.5, -1);
			snapSunflower.setOcelotStrategy(myOcelotStrat);
			gyroCorrectedOcelot.setOcelotStrategy(myOcelotStrat);
			System.out.println("robot centric");
		}
		else if(!leftBumper && prevLeftBumper){
			myOcelotStrat = fieldCentric;
			centricRumbler.cancel();
			snapSunflower.setOcelotStrategy(myOcelotStrat);
			gyroCorrectedOcelot.setOcelotStrategy(myOcelotStrat);
			System.out.println("FC");
		}
		
		// Choose the drive strategy
		if (driverController.getAButtonReleased()) {
			defaultOcelot.setDriveStrategy(manualDrive);
			driveScaling = 1.0;
		}
		if (driverController.getBButtonReleased()) {
			defaultOcelot.setDriveStrategy(manualDrive);
			driveScaling = 0.5;
		}
		if (driverController.getXButtonReleased()) {
			defaultOcelot.setDriveStrategy(autoDrive);
			driveScaling = 1.0;
		}
		
		// Choose the overall traversal strategy
//		if (driverController.getLeftBumperPressed()) {
//			snapSunflower.acceptNewDirections(true);
//			myStrat.end();
//			myStrat = snapSunflower;
//			myStrat.init();
//		}
//		if (driverController.getLeftBumper()) {
//			if (!snapSunflower.atDirection()) {
//				snapRumbler.setLeftRumble(0.75);
//				snapRumbler.setRightRumble(0.75);
//			}
//			else {
//				snapRumbler.setLeftRumble(0);
//				snapRumbler.setRightRumble(0);
//			}
//		}
//		else {
//			snapRumbler.setLeftRumble(0);
//			snapRumbler.setRightRumble(0);
//		}
//		if (driverController.getLeftBumperReleased()){
//			snapSunflower.acceptNewDirections(false);
//			myStrat.end();
//			myStrat = gyroCorrectedOcelot;
//			myStrat.init();
//		}
				
		myStrat.execute();
		prevLeftBumper = leftBumper;
	}

	@Override
	public void end() {
		myStrat.end();
	}
	
	private double snapDirection(double direction) {
		if (!MathUtilities.inRange(direction, 45, 325)) {
			return 0;
		}
		else if (MathUtilities.inRange(direction, 45, 135)) {
			return 90;
		}
		else if (MathUtilities.inRange(direction, 135, 225)) {
			return 180;
		}
		else {
			return 270;
		}
	}

}
