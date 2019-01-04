package main.org.usfirst.frc.team1640.robot.traversal.transmission;

import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.drivetrain.cvtpivot.ICVTPivot;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class DefaultTransmission implements TransmissionStrategy {
	private ICVTPivot flPivot, frPivot, blPivot, brPivot;
	private double flTransmission, frTransmission, blTransmission, brTransmission;
	
	public DefaultTransmission(IDriveTrain driveTrain) {
		flPivot = driveTrain.getFLPivot();
		frPivot = driveTrain.getFRPivot();
		blPivot = driveTrain.getBLPivot();
		brPivot = driveTrain.getBRPivot();
	}

	@Override
	public void init() {
	}

	@Override
	public void execute() {
		flPivot.setTransmission(flTransmission);
		frPivot.setTransmission(frTransmission);
		blPivot.setTransmission(blTransmission);
		brPivot.setTransmission(brTransmission);
	}

	@Override
	public void end() {
		
	}

	@Override
	public void setFLTransmission(double transmission) {
		flTransmission = transmission;
	}

	@Override
	public void setFRTransmission(double transmission) {
		frTransmission = transmission;
	}

	@Override
	public void setBLTransmission(double transmission) {
		blTransmission = transmission;
	}

	@Override
	public void setBRTransmission(double transmission) {
		brTransmission = transmission;
	}
	
	@Override
	public void setTransmission(double transmission) {
		flTransmission = transmission;
		frTransmission = transmission;
		blTransmission = transmission;
		brTransmission = transmission;
	}
}
