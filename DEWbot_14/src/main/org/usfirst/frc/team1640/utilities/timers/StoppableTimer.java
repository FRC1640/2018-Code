package main.org.usfirst.frc.team1640.utilities.timers;

public class StoppableTimer {
	private ElapsedTimer timer = new ElapsedTimer();
	
	private boolean isStopped = true;
	private long stoppedTime = 0;
	
	public void start() {
		timer.start();
		isStopped = false;
	}
	
	public void stop() {
		if (!isStopped) {
			stoppedTime = stoppedTime + timer.getElapsedNanoseconds();
			isStopped = true;
		}
	}
	
	public void reset() {
		stoppedTime = 0;
		timer.restart();
		isStopped = true;
	}
	
	public long getNanoseconds() {
		if (isStopped) {
			return stoppedTime;
		}
		else {
			return stoppedTime + timer.getElapsedNanoseconds();
		}
	}
	
	public double geMilliseconds() {
		double t = stoppedTime/1000000.0;
		if (isStopped) {
			return t;
		}
		else {
			return t + timer.getElapsedMilliseconds();
		}
	}
	
	public double getSeconds() {
		double t = stoppedTime/1000000000.0;
		if (isStopped) {
			return t;
		}
		else {
			return t + timer.getElapsedSeconds();
		}
	}
}
