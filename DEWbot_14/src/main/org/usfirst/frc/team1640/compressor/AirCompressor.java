package main.org.usfirst.frc.team1640.compressor;

import edu.wpi.first.wpilibj.Compressor;

public class AirCompressor {
	private Compressor compressor;
	
	public AirCompressor() {
		compressor = new Compressor();
	}
	
	public void start() {
		compressor.start();
	}
	
	public void stop() {
		compressor.stop();
	}

}
