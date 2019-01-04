package main.org.usfirst.frc.team1640.utilities;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDSourceDouble implements PIDSource{
	private double value;
	private PIDSourceType pidSource = PIDSourceType.kDisplacement;
	
	public void setValue(double value){
		this.value = value;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		this.pidSource = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return pidSource;
	}

	@Override
	public double pidGet() {
		return value;
	}

}