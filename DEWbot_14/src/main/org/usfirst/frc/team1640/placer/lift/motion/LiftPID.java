package main.org.usfirst.frc.team1640.placer.lift.motion;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class LiftPID extends LiftMotion {
	private int CANTimeout = 20;
	private double p = 0, i = 0, d = 0;
	
	public LiftPID(){
	}
	
	public void execute(){
		liftMotor.set(ControlMode.Position, setpoint);
	}

	@Override
	public void init() {
		liftMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, CANTimeout);
		liftMotor.configClosedloopRamp(0, CANTimeout);
		liftMotor.setNeutralMode(NeutralMode.Brake);
		
		liftMotor.config_kP(0, p, CANTimeout);
		liftMotor.config_kI(0, i, CANTimeout);
		liftMotor.config_kD(0, d, CANTimeout);	
	}

}
