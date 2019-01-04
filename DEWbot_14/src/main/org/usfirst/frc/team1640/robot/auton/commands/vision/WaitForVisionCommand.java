package main.org.usfirst.frc.team1640.robot.auton.commands.vision;

import java.util.ArrayList;

import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.vision.VisionServer;
import main.org.usfirst.frc.team1640.vision.messages.VisionUpdate;

public class WaitForVisionCommand implements AutonCommand {

	private VisionServer visionServer;
	private volatile boolean hasFoundTargets;
	private int minimumTargetsSeen;
	
	public WaitForVisionCommand(VisionServer visionServer, int minTargets) {
		hasFoundTargets = false;
		minimumTargetsSeen = minTargets;
		this.visionServer = visionServer;
	}
	
	@Override
	public void execute() {
		try {
			ArrayList<VisionUpdate> updates = visionServer.getCurrentVisionUpdates();
			
			if(updates == null || updates.size() == 0)
				return;
			
			if(updates.size() > minimumTargetsSeen && !Double.isNaN(visionServer.getBestGuessAngle())) {
				System.out.println("Vision Wait Done");
				hasFoundTargets = true;
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean isRunning() {
		return !hasFoundTargets;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void reset() {
		System.out.println("Reset Vision Wait");
		hasFoundTargets = false;
	}
}
