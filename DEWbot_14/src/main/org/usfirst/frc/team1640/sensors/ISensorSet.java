package main.org.usfirst.frc.team1640.sensors;

import main.org.usfirst.frc.team1640.sensors.gyroscope.IGyro;
import edu.wpi.first.wpilibj.AnalogInput;

public interface ISensorSet {
	public IGyro getGyro();
	public AnalogInput getArmSensor();
}
