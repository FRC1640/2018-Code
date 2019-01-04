package main.org.usfirst.frc.team1640.robot.traversal.ocelot;

import main.org.usfirst.frc.team1640.constants.mechanical.DimensionConstants;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.drivetrain.pivot.IPivot;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DriveStrategy;
import main.org.usfirst.frc.team1640.robot.traversal.steer.SteerStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class DefaultOcelot implements OcelotStrategy {
	private SteerStrategy steerStrat;
	private DriveStrategy driveStrat;
	private IPivot flPivot, frPivot, blPivot, brPivot;
	private double lateralDrive, longitudinalDrive, axialDrive;
	
	public DefaultOcelot(IDriveTrain driveTrain, SteerStrategy steerStrat, DriveStrategy driveStrat) {
		this.steerStrat = steerStrat;
		this.driveStrat = driveStrat;
		
		flPivot = driveTrain.getFLPivot();
		frPivot = driveTrain.getFRPivot();
		blPivot = driveTrain.getBLPivot();
		brPivot = driveTrain.getBRPivot();
		
		lateralDrive = 0.0;
		longitudinalDrive = 0.0;
		axialDrive = 0.0;
	};
	
	public void init() {
		steerStrat.init();
		driveStrat.init();
	}
	
	public void end() {
		steerStrat.end();
		driveStrat.end();
	}

	// executes the swerve drive functionality
	public void execute() {
		double xPos = lateralDrive + axialDrive * DimensionConstants.ROBOT_WIDTH_TO_DIAGONAL_RATIO;
		double xNeg = lateralDrive - axialDrive * DimensionConstants.ROBOT_WIDTH_TO_DIAGONAL_RATIO;
		double yPos = longitudinalDrive + axialDrive * DimensionConstants.ROBOT_LENGTH_TO_DIAGONAL_RATIO;
		double yNeg = longitudinalDrive - axialDrive * DimensionConstants.ROBOT_LENGTH_TO_DIAGONAL_RATIO;
		
		double fld = MathUtilities.magnitude(xPos, yPos);
		double frd = MathUtilities.magnitude(xPos, yNeg);
		double bld = MathUtilities.magnitude(xNeg, yPos);
		double brd = MathUtilities.magnitude(xNeg, yNeg);
		
		double m = Math.max(MathUtilities.magnitude(lateralDrive, longitudinalDrive), Math.abs(axialDrive));
		double sx1, sy1, sx2;
		if (m != 0) {
			sx1 = lateralDrive/m;
			sy1 = longitudinalDrive/m;
			sx2 = axialDrive/m;
		}
		else {
			sx1 = 0;
			sy1 = 0;
			sx2 = 0;
		}
		
		double maxMag = MathUtilities.magnitude(Math.abs(sx1) + DimensionConstants.ROBOT_WIDTH_TO_DIAGONAL_RATIO*Math.abs(sx2),
				Math.abs(sy1) + DimensionConstants.ROBOT_LENGTH_TO_DIAGONAL_RATIO*Math.abs(sx2));
		
		if (maxMag != 0) {
			fld /= maxMag;
			frd /= maxMag;
			bld /= maxMag;
			brd /= maxMag;
		}
		else {
			fld = 0;
			frd = 0;
			bld = 0;
			brd = 0;
		}
		
		double fls = MathUtilities.angle(xPos, yPos);
		double frs = MathUtilities.angle(xPos, yNeg);
		double bls = MathUtilities.angle(xNeg, yPos);
		double brs = MathUtilities.angle(xNeg, yNeg);
		
		if((lateralDrive == 0 && longitudinalDrive == 0 && axialDrive == 0)){
			fls = flPivot.getTargetAngle();
			frs = frPivot.getTargetAngle();
			bls = blPivot.getTargetAngle();
			brs = brPivot.getTargetAngle();
		}
		
		steerStrat.setFLDirection(fls);
		steerStrat.setFRDirection(frs);
		steerStrat.setBLDirection(bls);
		steerStrat.setBRDirection(brs);

		driveStrat.setFLDrive(fld);
		driveStrat.setFRDrive(frd);
		driveStrat.setBLDrive(bld);
		driveStrat.setBRDrive(brd);
		
		steerStrat.execute();
		driveStrat.execute();
	}

	@Override
	public void setLateralDrive(double drive) {
		lateralDrive = drive;
	}

	@Override
	public void setLongitudinalDrive(double drive) {
		longitudinalDrive = drive;
	}

	@Override
	public void setAxialDrive(double drive) {
		axialDrive = drive;
	}

	@Override
	public void setControlMode(ControlMode control) {
		driveStrat.setControlMode(control);
	}
	
	public void setDriveStrategy(DriveStrategy driveStrat) {
		this.driveStrat.end();
		this.driveStrat = driveStrat;
		this.driveStrat.init();
	}

}