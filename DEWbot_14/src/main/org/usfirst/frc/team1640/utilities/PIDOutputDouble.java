package main.org.usfirst.frc.team1640.utilities;

import edu.wpi.first.wpilibj.PIDOutput;

public class PIDOutputDouble implements PIDOutput{
	private double value;
	
	@Override
	public void pidWrite(double output) {
		this.value = output;
	}
	
	public double getValue(){
		return value;
	}

}