package main.org.usfirst.frc.team1640.sensors.gyroscope;

import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

import com.kauailabs.navx.frc.AHRS;
//import com.kauailabs.sf2.frc.navXSensor;
//import com.kauailabs.sf2.orientation.OrientationHistory;

import edu.wpi.first.wpilibj.SPI;

public class Gyro implements IGyro {
	private AHRS gyro;
	//private OrientationHistory orientationHistory;
	private double yawOffset;
	private boolean isConnected;
	private ElapsedTimer resetTimer;
	
	private final int kResetDelay = 100;
	
	public Gyro(SPI.Port port) {
		gyro = new AHRS(port);
		isConnected = false;
		new Thread(new Runnable() {

			@Override
			public void run() {
				connecting();
			}
			
		}).start();
		resetTimer = new ElapsedTimer();
	}
	
	// TODO make sure follows right hand rule
	public synchronized double getPitch() {
		if (isConnected) {
			return gyro.getPitch();
		}
		else {
			return 0;
		}
	}
	
	// TODO make sure follows right hand rule
	public synchronized double getRoll() {
		if (isConnected) {
			return gyro.getRoll();
		}
		else {
			return 0;
		}
	}
	
	public synchronized double getYaw() {
		if (isConnected) {
			return (-gyro.getYaw() + yawOffset)%360;
		}
		else {
			return 0;
		}
	}
	
	public synchronized void resetYaw() {
		System.out.println("Resetting yaw. Previous:" + getYaw());
		yawOffset = gyro.getYaw();
		resetTimer.restart(); // restart the reset delay timer
	}
	
	public synchronized void setYawOffset(double offset) {
		yawOffset = (gyro.getYaw() + offset)%360;
		resetTimer.restart();
	}
	
	private synchronized void connecting() {
		while (!gyro.isConnected()) {
			yawOffset = 0;
		}
		isConnected = true;
		resetYaw();
		
		/*
		navXSensor navX = new navXSensor(gyro, "Drivetrain Orientation");
		orientationHistory = new OrientationHistory(navX, gyro.getRequestedUpdateRate() * 10);
		*/
	}
	
	public synchronized boolean hasRecentlyOffset() {
		// if a reset recently occurred, return true
		if (resetTimer.getElapsedMilliseconds() < kResetDelay) {
			return true;
		}
		else {
			return false; // otherwise, return false
		}
	}
	
	/*
	public OrientationHistory getOrientationHistory() {
		return orientationHistory;
	}
	*/
	
}
