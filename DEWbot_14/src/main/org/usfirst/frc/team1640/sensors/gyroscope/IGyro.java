package main.org.usfirst.frc.team1640.sensors.gyroscope;

//import com.kauailabs.sf2.orientation.OrientationHistory;

public interface IGyro {
	
	public double getPitch();
	
	public double getRoll();
	
	public double getYaw();
	
	public void resetYaw();
	
	public void setYawOffset(double offset);
	
	public boolean hasRecentlyOffset();
	
	//public OrientationHistory getOrientationHistory();
}
