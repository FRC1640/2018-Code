package main.org.usfirst.frc.team1640.robot.traversal.ocelot;

import main.org.usfirst.frc.team1640.constants.mechanical.DimensionConstants;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DriveStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.steer.SteerStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;
import main.org.usfirst.frc.team1640.utilities.Vector;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class OffsetOcelot implements OcelotStrategy {
	
	private IDriveTrain driveTrain;
	private DriveStrategy driveStrat;
	private SteerStrategy steerStrat;
	
	private double lateralDrive;
	private double longitudinalDrive;
	private double axialDrive;
	private ControlMode controlMode;
	
	private final Vector axisOfRotation;
	
	private final Vector flPosition;
	private final Vector frPosition;
	private final Vector blPosition;
	private final Vector brPosition;
	
	private final Vector fl;
	private final Vector fr;
	private final Vector bl;
	private final Vector br;
	private final Vector flMax;
	private final Vector frMax;
	private final Vector blMax;
	private final Vector brMax;
	
	public OffsetOcelot(IDriveTrain driveTrain, DriveStrategy driveStrat, SteerStrategy steerStrat, Vector axisOfRotation) {
		this.driveTrain = driveTrain;
		this.driveStrat = driveStrat;
		this.steerStrat = steerStrat;
		this.axisOfRotation = axisOfRotation;
		
		final double kW = DimensionConstants.ROBOT_WIDTH/2;
		final double kL = DimensionConstants.ROBOT_LENGTH/2;
		
		flPosition = new Vector(-kW, kL);
		frPosition = new Vector(kW, kL);
		blPosition = new Vector(-kW, -kL);
		brPosition = new Vector(kW, -kL);
		
		fl = new Vector(0, 0);
		fr = new Vector(0, 0);
		bl = new Vector(0, 0);
		br = new Vector(0, 0);
		
		flMax = new Vector(0, 0);
		frMax = new Vector(0, 0);
		blMax = new Vector(0, 0);
		brMax = new Vector(0, 0);
	}

	@Override
	public void setLateralDrive(double drive) {
		lateralDrive = drive;
	}

	@Override
	public void setControlMode(ControlMode control) {
		controlMode = control;
	}

	@Override
	public void init() {
		driveStrat.init();
	}

	@Override
	public void execute() {
		// calculate vectors representing the directions and proportional drives of the pivots
		updateTranslationVector(fl, flPosition, lateralDrive, longitudinalDrive, axialDrive);
		updateTranslationVector(fr, frPosition, lateralDrive, longitudinalDrive, axialDrive);
		updateTranslationVector(bl, blPosition, lateralDrive, longitudinalDrive, axialDrive);
		updateTranslationVector(br, brPosition, lateralDrive, longitudinalDrive, axialDrive);
		
		// scale the controls to the maximum possible setting while maintaining a constant ratio
		double m = Math.max(MathUtilities.magnitude(lateralDrive, longitudinalDrive), Math.abs(axialDrive));
		double scaledLateral = 0, scaledLongitudinal = 0, scaledAxial = 0;
		if (m != 0) {
			scaledLateral = lateralDrive/m;
			scaledLongitudinal = longitudinalDrive/m;
			scaledAxial = axialDrive/m;
		}
		
		// calculate new vectors using the scaled controls
		updateTranslationVector(flMax, flPosition, scaledLateral, scaledLongitudinal, scaledAxial);
		updateTranslationVector(frMax, frPosition, scaledLateral, scaledLongitudinal, scaledAxial);
		updateTranslationVector(blMax, blPosition, scaledLateral, scaledLongitudinal, scaledAxial);
		updateTranslationVector(brMax, brPosition, scaledLateral, scaledLongitudinal, scaledAxial);
		
		// determine which vector is biggest, then use it to normalize other vectors
		double maxMag = MathUtilities.maximum(flMax.magnitude(), frMax.magnitude(), blMax.magnitude(), brMax.magnitude());
		if (maxMag != 0) {
			fl.multiply(1/maxMag);
			fr.multiply(1/maxMag);
			bl.multiply(1/maxMag);
			br.multiply(1/maxMag);
		}
		else {
			fl.set(0, 0);
			fr.set(0, 0);
			bl.set(0, 0);
			br.set(0, 0);
		}
		
		// update the pivots
		driveStrat.setFLDrive(fl.magnitude());
		driveStrat.setFRDrive(fr.magnitude());
		driveStrat.setBLDrive(bl.magnitude());
		driveStrat.setBRDrive(br.magnitude());
		
		if (!fl.isZero()) {
			steerStrat.setFLDirection(fl.angle());
		}
		else {
			steerStrat.setFLDirection(MathUtilities.angle(lateralDrive, longitudinalDrive));
		}
		if (!fr.isZero()) {
			steerStrat.setFRDirection(fr.angle());
		}
		else {
			steerStrat.setFRDirection(MathUtilities.angle(lateralDrive, longitudinalDrive));
		}
		if (!bl.isZero()) {
			steerStrat.setBLDirection(bl.angle());
		}
		else {
			steerStrat.setBLDirection(MathUtilities.angle(lateralDrive, longitudinalDrive));
		}
		if (!br.isZero()) {
			steerStrat.setBRDirection(br.angle());
		}
		else {
			steerStrat.setBRDirection(MathUtilities.angle(lateralDrive, longitudinalDrive));
		}
		
		driveStrat.setControlMode(controlMode);
		
		driveStrat.execute();
		steerStrat.execute();
	}

	@Override
	public void end() {
		driveStrat.end();
	}
	
	public Vector getAxisOfRotation() {
		return axisOfRotation;
	}

	@Override
	public void setLongitudinalDrive(double drive) {
		longitudinalDrive = drive;
	}

	@Override
	public void setAxialDrive(double drive) {
		axialDrive = drive;
	}
	
	private void updateTranslationVector(Vector translationVector, Vector pivotPosition, double lateral, double longitudinal, double axial) {
		
		double kR = MathUtilities.distanceBetween(pivotPosition, axisOfRotation);
		
		if (kR != 0) {
			translationVector.setX(lateral + axial*(pivotPosition.getY()-axisOfRotation.getY())/kR);
			translationVector.setY(longitudinal + axial*(axisOfRotation.getX()-pivotPosition.getX())/kR);
		}
		else {
			translationVector.setX(0);
			translationVector.setY(0);
		}
	}

}
