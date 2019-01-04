package main.org.usfirst.frc.team1640.constants.mechanical;

public class DimensionConstants {
	
	public static final double ROBOT_LENGTH = 27.25; //Inches
	public static final double ROBOT_WIDTH = 21; //Inches
	public static final double BUMPER_WIDTH = 3; //Inches
	public static final double ROBOT_DIAGONAL = Math.sqrt(Math.pow(ROBOT_WIDTH, 2) + Math.pow(ROBOT_LENGTH, 2));
	public static final double ROBOT_WIDTH_TO_DIAGONAL_RATIO = ROBOT_WIDTH / ROBOT_DIAGONAL;
	public static final double ROBOT_LENGTH_TO_DIAGONAL_RATIO = ROBOT_LENGTH / ROBOT_DIAGONAL;

}
