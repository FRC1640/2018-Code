package main.org.usfirst.frc.team1640.utilities.curves;

public class SCurve extends Curve {
	
	private double x1;
	private double x2;
	private double x3;
	private double x4;
	private double minY;
	private double maxY;
	
	public SCurve(double x1, double x2, double x3, double x4, double minY, double maxY) {
		makeCurve(x1, x2, x3, x4, minY, maxY);
	}
	
	public SCurve(double distance, double minY, double maxY, double percentX1, double percentX2) {
		makeCurve(0, distance*percentX1, distance*percentX2, distance, minY, maxY);
	}
	
	private void makeCurve(double x1, double x2, double x3, double x4, double minY, double maxY) {
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.x4 = x4;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	@Override
	public double getY(double x) {
		if (x1 <= x && x < (x1+x2)/2) {
			return quadratic(x, 1, x2-x1, x1, minY);
		}
		else if ((x1+x2)/2 <= x && x < x2) {
			return quadratic(x, -1, x2-x1, x2, maxY);
		}
		else if (x2 <= x && x < x3) {
			return maxY;
		}
		else if (x3 <= x && x < (x3+x4)/2) {
			return quadratic(x, -1, x4-x3, x3, maxY);
		}
		else if ((x3+x4)/2 <= x && x < x4) {
			return quadratic(x, 1, x4-x3, x4, minY);
		}
		else {
			return minY;
		}
	}
	
	private double quadratic(double x, double direction, double period, double xShift, double yShift) {
		return direction*2*(maxY-minY)/Math.pow(period, 2)*Math.pow(x-xShift, 2)+ yShift;
	}
}
