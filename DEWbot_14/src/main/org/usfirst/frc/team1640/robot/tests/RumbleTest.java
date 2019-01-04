package main.org.usfirst.frc.team1640.robot.tests;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.controllers.rumblers.AlternatingRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.BurstRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.DefaultRumbler;
import main.org.usfirst.frc.team1640.controllers.rumblers.PulsingRumbler;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class RumbleTest implements Strategy {
	private IController controller;
	private BurstRumbler rumbler;
	private DefaultRumbler rumbler2;
	private AlternatingRumbler rumbler3;
	private PulsingRumbler rumbler4;
	
	public RumbleTest(IController controller) {
		this.controller = controller;
		rumbler = controller.createBurstRumbler();
		rumbler2 = controller.createDefaultRumbler();
		rumbler3 = controller.createAlternatingRumbler();
		rumbler4 = controller.createPulsingRumbler();
	}

	@Override
	public void init() {
		
	}

	@Override
	public void execute() {
		controller.setDeadband(0.2);
		
		if (controller.getAButtonPressed()) {
			rumbler.burstLeftRumble(1.0, 2);
		}
		if (controller.getAButtonReleased()) {
			rumbler.cancelLeftRumble();
		}
		
		if (controller.getBButtonReleased()) {
			rumbler.burstRightRumble(0.5, 1);
		}
		
		if (controller.getXButtonPressed()) {
			rumbler3.rumble(1.0, 1, -1);
		}
		if (controller.getXButtonReleased()) {
			rumbler3.cancel();
		}
		
		if (controller.getYButtonPressed()) {
			rumbler4.pulseRight(3);
		}
		if (controller.getYButtonReleased()) {
			rumbler4.cancelRight();
		}
		
		rumbler2.setLeftRumble(controller.getLeftTrigger()*1.0);
		rumbler2.setRightRumble(controller.getRightTrigger()*1.0);
		
//		controller.setRumble(controller.getLeftY());
//		if (controller.getPOVWestPressed()) {
//			controller.burstLeftRumble(1.0);
//		}
//		if (controller.getPOVEastPressed()) {
//			controller.burstRightRumble(1.0);
//		}
//		if (controller.getPOVNorthPressed()) {
//			controller.burstRumble(1.0);
//		}
	}

	@Override
	public void end() {
		
	}

}
