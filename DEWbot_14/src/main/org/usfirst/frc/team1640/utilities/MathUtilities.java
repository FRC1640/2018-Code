package main.org.usfirst.frc.team1640.utilities;

public final class MathUtilities {
	private MathUtilities() {}
	
	/**
	 * calculate magnitude of vector <x, y>.
	 */
	public static double magnitude(double x, double y) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	/**
	 * calculate angle vector <x, y> makes counter-clockwise with y axis
	 */
	public static double angle(double x, double y) {
		return (-Math.toDegrees(Math.atan2(-x, -y))+180)%360;
	}
	
	/**
	 * calculate the x component of a vector given angle and magnitude
	 */
	public static double xFromPolar(double angle, double magnitude) {
		return (magnitude*dcos((angle+90)%360));
	}
	
	/**
	 * calculate the y component of a vector given angle and magnitude
	 */
	public static double yFromPolar(double angle, double magnitude) {
		return (magnitude*dsin((angle+90))%360);
	}
	
	/**
	 * calculate the shortest angle between two angles
	 */
	public static double shortestAngleBetween(double startingAngle, double endingAngle){ //find shortest angle between two angles
		double counterClockwiseAngle = endingAngle % 360 - startingAngle % 360;
		double clockwiseAngle = counterClockwiseAngle < 0 ? (360 + counterClockwiseAngle) : (counterClockwiseAngle - 360); //ensure that the 359-0 gap is counted for
		return (Math.abs(counterClockwiseAngle) >= 180 ? clockwiseAngle : counterClockwiseAngle); //choose shortest angle
	}
	
	public static boolean inRange (double value, double lowerBound, double upperBound) {
		return (value >= lowerBound && value <= upperBound) ||
				(value >= upperBound && value <= lowerBound);
	}
	
	public static double minimum(double... values) {
		double min = values[0];
		for (double value : values) {
			if (value < min) min = value;
		}
		return min;
	}
	
	public static double minimumByAbsoluteValue(double... values) {
		double min = values[0];
		for (double value : values) {
			if (Math.abs(value) < Math.abs(min)) min = value;
		}
		return min;
	}
	
	public static double maximum(double... values) {
		double max = values[0];
		for (double value : values) {
			if (value > max) max = value;
		}
		return max;
	}
	
	public static double maximumByAbsoluteValue(double... values) {
		double max = values[0];
		for (double value: values) {
			if (Math.abs(value) > Math.abs(max)) max = value;
		}
		return max;
	}
	
	public static double constrain(double value, double lowerBound, double upperBound) {
		if (value > upperBound) {
			value = upperBound;
		}
		else if (value < lowerBound) {
			value = lowerBound;
		}
		return value;
	}
	
	public static double distanceBetween(double x1, double y1, double x2, double y2) {
		return magnitude(x2-x1, y2-y1);
	}
	
	public static double distanceBetween(Vector v1, Vector v2) {
		return distanceBetween(v1.getX(), v1.getY(), v2.getX(), v2.getY());
	}
	
	public static double dsin(double angleInDegrees) {
		return Math.sin(Math.toRadians(angleInDegrees));
	}
	
	public static double dcos(double angleInDegrees) {
		return Math.cos(Math.toRadians(angleInDegrees));
	}
	
	public static double dtan(double angleInDegrees) {
		return Math.tan(Math.toRadians(angleInDegrees));
	}
	
	public static double darcsin(double ratio) {
		return Math.toDegrees(Math.asin(ratio));
	}
	
	public static double darccos(double ratio) {
		return Math.toDegrees(Math.acos(ratio));
	}
	
	public static double darctan(double ratio) {
		return Math.toDegrees(Math.atan(ratio));
	}
	
	public static double darctan2(double y, double x) {
		return Math.toDegrees(Math.atan2(y, x));
	}
}
