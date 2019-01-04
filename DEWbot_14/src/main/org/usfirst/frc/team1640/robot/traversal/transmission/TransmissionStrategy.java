package main.org.usfirst.frc.team1640.robot.traversal.transmission;

import main.org.usfirst.frc.team1640.robot.traversal.TraversalStrategy;


public interface TransmissionStrategy extends TraversalStrategy {
	public void setFLTransmission(double transmission);
	public void setFRTransmission(double transmission);
	public void setBLTransmission(double transmission);
	public void setBRTransmission(double transmission);
	public void setTransmission(double transmission);
}
