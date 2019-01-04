package main.org.usfirst.frc.team1640.utilities.curves;

public class ReverseAtEndCurve extends Curve {
	
	private double x1;
	private double x2;
	private double maxY;
	private double reverseY;
	private double xStartReverse;
	
	public ReverseAtEndCurve(double x1, double x2, double maxY, double reverseY, double xStartReverse) {
		this.x1 = x1;
		this.x2 = x2;
		this.maxY = maxY;
		this.reverseY = reverseY;
		this.xStartReverse = xStartReverse;
	}
	
	@Override
	public double getY(double x) {
		if (x1 <= x && x <= xStartReverse) {
			return maxY;
		}
		else if (xStartReverse < x && x <= x2) {
			return reverseY;
		}
		else {
			return 0;
		}
	}
}
