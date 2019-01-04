package main.org.usfirst.frc.team1640.utilities.timers;

public class ElapsedTimer {
	private long startTime; // nanoseconds
	
	public void start() {
		startTime = System.nanoTime();
	}
	
	public void restart() {
		start();
	}
	
	public long getElapsedNanoseconds() {
		return System.nanoTime() - startTime;
	}
	
	public double getElapsedMilliseconds() {
		return (System.nanoTime() - startTime)/1000000.0;
	}
	
	public double getElapsedSeconds() {
		return (System.nanoTime() - startTime)/1000000000.0;
	}
}
