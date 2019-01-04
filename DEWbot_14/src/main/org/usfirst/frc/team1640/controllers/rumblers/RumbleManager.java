package main.org.usfirst.frc.team1640.controllers.rumblers;

import java.util.ArrayList;

import main.org.usfirst.frc.team1640.utilities.MathUtilities;

public class RumbleManager {
	private ArrayList<Rumbler> rumblers;
	private boolean paused;
	
	public RumbleManager() {
		rumblers = new ArrayList<Rumbler>();
		paused = false;
	}
	
	public void update() {
		for (Rumbler rumble : rumblers) {
			rumble.update();
		}
	}
	
	public DefaultRumbler createDefaultRumbler() {
		DefaultRumbler rumbler = new DefaultRumbler();
		rumblers.add(rumbler);
		return rumbler;
	}
	
	public BurstRumbler createBurstRumbler() {
		BurstRumbler rumbler = new BurstRumbler();
		rumblers.add(rumbler);
		return rumbler;
	}
	
	public AlternatingRumbler createAlternatingRumbler() {
		AlternatingRumbler rumbler = new AlternatingRumbler();
		rumblers.add(rumbler);
		return rumbler;
	}
	
	public PulsingRumbler createPulsingRumbler() {
		PulsingRumbler rumbler = new PulsingRumbler();
		rumblers.add(rumbler);
		return rumbler;
	}
	
	public double getLeftRumble() {
		double leftRumble = 0;
		int contributors = 0;
		
		for (Rumbler rumbler : rumblers) {
			if (rumbler != null) {
				if (rumbler.getLeftRumble() > 0) {
					contributors++;
				}
			}
		}
		
		for (Rumbler rumbler : rumblers) {
			if (rumbler != null && contributors > 0) {
				leftRumble += rumbler.getLeftRumble()/contributors;
			}
		}
		
		if (!paused) {
			return MathUtilities.constrain(leftRumble, 0, 1);
		}
		else {
			return 0;
		}
	}
	
	public double getRightRumble() {
		double rightRumble = 0;
		int contributors = 0;
		
		for (Rumbler rumbler : rumblers) {
			if (rumbler != null) {
				if (rumbler.getRightRumble() > 0) {
					contributors++;
				}
			}
		}
		
		for (Rumbler rumbler : rumblers) {
			if (rumbler != null && contributors > 0) {
				rightRumble += rumbler.getRightRumble()/contributors;
			}
		}
		if (!paused) {
			return MathUtilities.constrain(rightRumble, 0, 1);
		}
		else {
			return 0;
		}
	}
	
	public void pauseRumble() {
		paused = true;
	}
	
	public void resumeRumble() {
		paused = false;
	}
	
	public void cancelRumble() {
		for (Rumbler rumbler: rumblers) {
			if (rumbler != null) {
				rumbler.cancel();
			}
		}
	}

}
