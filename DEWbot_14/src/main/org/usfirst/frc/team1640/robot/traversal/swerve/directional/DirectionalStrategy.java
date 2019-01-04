package main.org.usfirst.frc.team1640.robot.traversal.swerve.directional;

import main.org.usfirst.frc.team1640.robot.traversal.TraversalStrategy;

public interface DirectionalStrategy extends TraversalStrategy {
	public void setDirection(double direction);
	public void acceptNewDirections(boolean maintainDirection);
}
