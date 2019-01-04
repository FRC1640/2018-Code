package main.org.usfirst.frc.team1640.robot.tests;

import main.org.usfirst.frc.team1640.controllers.IController;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.traversal.drive.DefaultDrive;
import main.org.usfirst.frc.team1640.robot.traversal.steer.DefaultSteer;
import main.org.usfirst.frc.team1640.robot.traversal.transmission.DefaultTransmission;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class PivotUnitTest implements Strategy {
	private IDriveTrain driveTrain;
	private IController controller;
	
	private DefaultDrive driveStrat;
	private DefaultSteer steerStrat;
	private DefaultTransmission transmissionStrat;
	
	private enum Pivot {FL, FR, BL, BR};
	private Pivot pivot = Pivot.FL;
	
	public PivotUnitTest(IRobotContext robotContext) {
		driveTrain = robotContext.getDriveTrain();
		controller = robotContext.getDriverController();
		
		driveStrat = new DefaultDrive(driveTrain);
		steerStrat = new DefaultSteer(driveTrain);
		transmissionStrat = new DefaultTransmission(driveTrain);
	}

	@Override
	public void init() {
		driveStrat.init();
		steerStrat.init();
		transmissionStrat.init();
	}

	@Override
	public void execute() {
		
		if (controller.getAButtonPressed()) {
			switch(pivot) {
			case FL: pivot = Pivot.FR; break;
			case FR: pivot = Pivot.BL; break;
			case BL: pivot = Pivot.BR; break;
			case BR: pivot = Pivot.FL; break;
			}
		}
		
		controller.setDeadband(0.2);
		double drive = controller.getLeftY();
		double steer = controller.getLeftTrigger()*360;
		double transmission = controller.getRightX();
		
		System.out.println("Pivot: " + pivot.toString() + ", Drive: " + drive + ", Steer: " + steer + ", Transmission: " + transmission);

		switch(pivot) {
		case FL:
			driveStrat.setFLDrive(drive);
			steerStrat.setFLDirection(steer);
			transmissionStrat.setFLTransmission(transmission);
			break;
		case FR:
			driveStrat.setFRDrive(drive);
			steerStrat.setFRDirection(steer);
			transmissionStrat.setFRTransmission(transmission);
			break;
		case BL:
			driveStrat.setBLDrive(drive);
			steerStrat.setBLDirection(steer);
			transmissionStrat.setBLTransmission(transmission);
			break;
		case BR:
			driveStrat.setBRDrive(drive);
			steerStrat.setBRDirection(steer);
			transmissionStrat.setBRTransmission(transmission);
			break;
		}
		
		driveStrat.execute();
		steerStrat.execute();
		transmissionStrat.execute();
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
