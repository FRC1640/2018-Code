package main.org.usfirst.frc.team1640.sensors.gyroscope;

//import com.kauailabs.sf2.orientation.OrientationHistory;

public class TestGyro implements IGyro {

	@Override
	public double getYaw() {
		
		return 0;
	}

	@Override
	public void resetYaw() {
		
	}
	
	@Override
	public void setYawOffset(double offset) {
		
	}

	@Override
	public double getPitch() {

		return 0;
	}

	@Override
	public double getRoll() {
		
		return 0;
	}

	@Override
	public boolean hasRecentlyOffset() {
		return false;
	}

	/*
	@Override
	public OrientationHistory getOrientationHistory() {
		return null;
	}
	*/
	
}
