package main.org.usfirst.frc.team1640.sensors;

import main.org.usfirst.frc.team1640.sensors.gyroscope.Gyro;
import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;

public class SensorSet implements ISensorSet {
	private IGyro gyro;
	private AnalogInput armSensor;

	
	public SensorSet() {
		gyro = new Gyro(SPI.Port.kMXP);
		armSensor = new AnalogInput(4);
	}
	
	public IGyro getGyro() {
		return gyro;
	}
	
	public AnalogInput getArmSensor() {
		return armSensor;
	}
}
