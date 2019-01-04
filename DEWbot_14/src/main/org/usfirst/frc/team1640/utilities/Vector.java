package main.org.usfirst.frc.team1640.utilities;

public class Vector {
	// Right hand rule is used and 0 degrees is the positive y direction.
	
	private double x;
	private double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double angle() {
		return MathUtilities.angle(x, y);
	}
	
	public double magnitude() {
		return MathUtilities.magnitude(x, y);
	}
	
	public void multiply(double scalar) {
		x *= scalar;
		y *= scalar;
	}
	
	public boolean isZero() {
		return x == 0 && y == 0;
	}
	
	@Override
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
}
