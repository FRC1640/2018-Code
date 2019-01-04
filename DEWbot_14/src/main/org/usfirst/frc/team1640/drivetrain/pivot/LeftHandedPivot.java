package main.org.usfirst.frc.team1640.drivetrain.pivot;

import main.org.usfirst.frc.team1640.utilities.MathUtilities;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;

public class LeftHandedPivot implements IPivot {
	//private com.ctre.CANTalon drive, steer;
	private final int kCANTimeout = 20;
	private WPI_TalonSRX driveMotor, steerMotor;

	private AnalogInput resolver;
	public PIDController anglePID;
	private boolean enabled;
	private double minVoltage, dVoltage;
	private double offset;
	private boolean flipDrive;
	private double targetAngle;
	private int encOffset;
	private double drive, setpoint;
	private String name;

	private ControlMode controlMode;
	private ControlMode enabledControlMode;
	
	private int kPIDIndex = 0;
	private double kSensorUnitsPerRotation = 4096; //TODO make sure this is right then use it
	private int kMaxSetpoint = 4000;
	private double kP = 0.15, kI = 0.0, kD = 0.2, kF = 0.0365;
	
	public LeftHandedPivot(int driveChannel, int steerChannel, int resolverChannel, double minVoltage, double maxVoltage, double offset, String name) {
		// setup all motors and sensors for the pivots
		driveMotor = new WPI_TalonSRX(driveChannel);
		steerMotor = new WPI_TalonSRX(steerChannel);
		resolver = new AnalogInput(resolverChannel) {	
			@Override
			public double pidGet(){
				double dAngle = getTargetAngle() - getAngle();
				flipDrive = MathUtilities.inRange(Math.abs(dAngle), 90, 270);
				return (flipDrive) ? Math.sin(Math.toRadians(dAngle)) : -Math.sin(Math.toRadians(dAngle));
			}
		};
		
		this.minVoltage = minVoltage;
		this.dVoltage = maxVoltage - minVoltage;
		this.offset = offset;
		this.name = name;
		
		controlMode = ControlMode.PercentOutput;
		enabledControlMode = controlMode;
		
		anglePID = new PIDController(1, 0, 0.01, 0, resolver, steerMotor, 0.02);//0.9, 0.0001 //0.95, 0.0075
		anglePID.setOutputRange(-1, 1);
		anglePID.setInputRange(-1, 1);
		anglePID.enable();
		anglePID.setSetpoint(0.0);
		
		flipDrive = false;
		targetAngle = 0.0;
		
		driveMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, kCANTimeout);
		driveMotor.configClosedloopRamp(0, kCANTimeout);
		driveMotor.setNeutralMode(NeutralMode.Coast);
		
		enabled = true;
	}
	
	public void setTargetAngle(double angle) {
		targetAngle = angle%360;
	}

	// TODO consider reworking how we change control modes in case we decide to add more
	// TODO implement a method for driving using inches per second
	public void setDrive(double drive) {
		drive = MathUtilities.constrain(drive, -1.0, 1.0);
		if (enabled){
			this.drive = drive;
			double flippedDrive = (flipDrive ? -drive : drive);
			// TODO implement encoder error detection
			// TODO instead of setting peak to 0, disable the velocity control so any i in the PID doesn't build up.
			if(drive == 0.0) {
				driveMotor.configPeakOutputForward(+0.0f, kCANTimeout);
				driveMotor.configPeakOutputReverse(-0.0f, kCANTimeout);
			}
			else{
				driveMotor.configPeakOutputForward(+12.0f, kCANTimeout);
				driveMotor.configPeakOutputReverse(-12.0f, kCANTimeout);
			}
			
			setpoint = flippedDrive;
			if (controlMode == ControlMode.Velocity) {
				setpoint *= kMaxSetpoint;
			}
			driveMotor.set(controlMode, setpoint);
		}
		else {
			driveMotor.set(0.0);
		}
	}
	
	public void enable() {
		if (!enabled){
			enabled = true;
			anglePID.enable();
			controlMode = enabledControlMode;
		}
	}
	
	public void disable() {
		if (enabled){
			enabled = false;
			anglePID.disable();
			anglePID.reset();
			controlMode = ControlMode.Disabled;
		}
	}
	
	public void setControlMode(ControlMode controlMode) {
		if (controlMode != enabledControlMode) {
			switch (controlMode) {
			case Velocity:
				enabledControlMode = ControlMode.Velocity;
				driveMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, kCANTimeout);
				driveMotor.getSensorCollection().setQuadraturePosition(0, kCANTimeout);
				driveMotor.configPeakOutputForward(+12.0f, kCANTimeout);
				driveMotor.configPeakOutputReverse(-12.0f, kCANTimeout);
				driveMotor.configNominalOutputForward(+0.0f, kCANTimeout);
				driveMotor.configNominalOutputReverse(-0.0f, kCANTimeout);
				driveMotor.config_kP(kPIDIndex, kP, kCANTimeout);
				driveMotor.config_kI(kPIDIndex, kI, kCANTimeout);
				driveMotor.config_kD(kPIDIndex, kD, kCANTimeout);
				driveMotor.config_kF(kPIDIndex, kF, kCANTimeout);
				break;
			default:
				enabledControlMode = ControlMode.PercentOutput;
				break;
			}
		}
	}
	
	public double getAngle() {
		return ((360.0 * (resolver.getVoltage() - minVoltage) / dVoltage) + 360.0 - offset) % 360;
	}
	
	public double getVoltage() {
		return resolver.getVoltage();
	}

	public double getVelocity() {
		return driveMotor.getSelectedSensorVelocity(kPIDIndex); // TODO multiply by (600/kSensorUnitsPerRotation) to convert to RPM;
	}
	
	public int getPosition() {
		return driveMotor.getSelectedSensorPosition(0) - encOffset;
	}
	
	public void resetPosition() {
		encOffset = getPosition();
	}
	
	// TODO make sure the conversion works and name those constants
	public double getInches() {
		return ((getPosition()*2*Math.PI*2*(12.0/32))/64.0);
	}
	
	public double getFeet() {
		return getInches()/12.0;
	}
	
	public ControlMode getControlMode(){
		return controlMode;
	}
	
	public double getSetpoint() {
		return (controlMode == ControlMode.Velocity) ? setpoint : drive;
	}
	
	public double getTargetAngle() {
		return targetAngle;
	}
	
	public double getDrive() {
		return drive;
	}
	
	public boolean getFlipDrive() {
		return flipDrive;
	}
}

