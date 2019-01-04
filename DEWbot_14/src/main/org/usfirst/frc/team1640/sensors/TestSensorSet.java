package main.org.usfirst.frc.team1640.sensors;

import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import main.org.usfirst.frc.team1640.sensors.gyroscope.TestGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SerialPort;

public class TestSensorSet implements ISensorSet {
	private IGyro gyro;
	
	public TestSensorSet() {
		gyro = new TestGyro();
	}
	
	public IGyro getGyro() {
		return gyro;
	}

	@Override
	public AnalogInput getArmSensor() {
		return null;
	}
}
