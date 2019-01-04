package main.org.usfirst.frc.team1640.placer.lift.motion;

import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

public class LiftMotionMagic extends LiftMotion {
	private final int CANTimeout = 20;
	private ElapsedTimer timer;
	private double prevSetpoint;
	private boolean printed;
	

	@Override
	public void execute() {
		liftMotor.set(ControlMode.MotionMagic, inchesToCounts(setpoint));
//		if(!printed){
//			System.out.println("MO: " + liftMotor.getMotorOutputPercent());
//			System.out.println("V: " + liftMotor.getSelectedSensorVelocity(0)); 
//			System.out.println("E: " + liftMotor.getClosedLoopError(0));
//		}
		if(setpoint != prevSetpoint){
			timer.restart();
			printed = false;
			
			if(inchesToCounts(setpoint) > liftMotor.getSelectedSensorPosition(0)){ //going up
				liftMotor.selectProfileSlot(0, 0);
				liftMotor.configMotionCruiseVelocity(6000, CANTimeout); 
				liftMotor.configMotionAcceleration(7500, CANTimeout);
			}
			else{
				liftMotor.selectProfileSlot(1, 0);
				liftMotor.configMotionCruiseVelocity(6000, CANTimeout); 
				liftMotor.configMotionAcceleration(8500, CANTimeout);
			}
		}
		else if(isDone() && !printed){
			System.out.println("LIFT TIME: " + timer.getElapsedMilliseconds());
			printed = true;
		}
		prevSetpoint = setpoint;
	}

	@Override
	public void init() {
		liftMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, CANTimeout);
		liftMotor.configClosedloopRamp(0, CANTimeout);
		liftMotor.setNeutralMode(NeutralMode.Brake);
		liftMotor.setSensorPhase(false);
		liftMotor.setInverted(false);

		liftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, CANTimeout);
		liftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, CANTimeout);

		liftMotor.configContinuousCurrentLimit(30, CANTimeout);
		liftMotor.configPeakCurrentLimit(55, CANTimeout);
		liftMotor.configPeakCurrentDuration(100, 0);
		liftMotor.enableCurrentLimit(true);
		
		liftMotor.configPeakOutputForward(+12.0f, CANTimeout);
		liftMotor.configPeakOutputReverse(-12.0f, CANTimeout);
		liftMotor.configNominalOutputForward(+0.0f, CANTimeout);
		liftMotor.configNominalOutputReverse(-0.0f, CANTimeout);

		liftMotor.configMotionCruiseVelocity(5500, CANTimeout); 
		liftMotor.configMotionAcceleration(6500, CANTimeout);
		
		//up
		liftMotor.selectProfileSlot(0, 0);
		liftMotor.config_kF(0, 0.428, CANTimeout);
		liftMotor.config_kP(0, 0.2, CANTimeout);
		liftMotor.config_kI(0, 0, CANTimeout);
		liftMotor.config_kD(0, 0, CANTimeout);

		//down
		liftMotor.config_kF(1, 0.428, CANTimeout);
		liftMotor.config_kP(1, 0.2, CANTimeout);
		liftMotor.config_kI(1, 0, CANTimeout);
		liftMotor.config_kD(1, 0, CANTimeout);
 
		liftMotor.setSelectedSensorPosition(0, 0, CANTimeout);
		
		timer = new ElapsedTimer();
	}

}
