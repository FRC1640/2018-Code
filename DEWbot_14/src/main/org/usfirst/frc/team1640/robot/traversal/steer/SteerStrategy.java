package main.org.usfirst.frc.team1640.robot.traversal.steer;

import main.org.usfirst.frc.team1640.robot.traversal.TraversalStrategy;

public interface SteerStrategy extends TraversalStrategy {
	public void setFLDirection(double direction);
	public void setFRDirection(double direction);
	public void setBLDirection(double direction);
	public void setBRDirection(double direction);
}
