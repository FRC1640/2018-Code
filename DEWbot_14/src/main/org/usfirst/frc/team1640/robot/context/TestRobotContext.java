package main.org.usfirst.frc.team1640.robot.context;

import main.org.usfirst.frc.team1640.compressor.AirCompressor;
import main.org.usfirst.frc.team1640.compressor.TestAirCompressor;
import main.org.usfirst.frc.team1640.controllers.Controller;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.drivetrain.TestDriveTrain;
import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.robot.driverstation.DStation;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;
import main.org.usfirst.frc.team1640.sensors.TestSensorSet;
import main.org.usfirst.frc.team1640.vision.VisionServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class TestRobotContext implements IRobotContext {
	private IDriveTrain driveTrain;
	private DStation driverStation;
	private Controller driverController;
	private Controller operatorController;
	private ISensorSet sensorSet;
	private AirCompressor compressor;

	public TestRobotContext() {
		driveTrain = new TestDriveTrain();
		driverStation = new DStation();
		driverController = new Controller(0);
		operatorController = new Controller(1);
		sensorSet = new TestSensorSet();
		driverStation = new DStation();
		compressor = new TestAirCompressor();
	}
	
	@Override
	public IDriveTrain getDriveTrain() {
		
		return driveTrain;
	}

	@Override
	public DStation getDStation() {
		
		return driverStation;
	}

	@Override
	public Controller getDriverController() {
		
		return driverController;
	}

	@Override
	public Controller getOperatorController() {
		
		return operatorController;
	}

	@Override
	public ISensorSet getSensorSet() {
		
		return sensorSet;
	}

	@Override
	public PowerDistributionPanel getPDP() {
		
		return null;
	}
	
	@Override
	public Intake getIntake() {
		return null;
	}
	
	public AirCompressor getAirCompressor() {
		return compressor;
	}
	
	@Override
	public Placer getPlacer(){
		return null;
	}

	@Override
	public VisionServer getVisionServer() {
		// TODO Auto-generated method stub
		return null;
	}

}

/*
package main.org.usfirst.frc.team1640.robot.context;

import main.org.usfirst.frc.team1640.compressor.AirCompressor;
import main.org.usfirst.frc.team1640.controllers.Controller;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.drivetrain.TestDriveTrain;
import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.robot.driverstation.DStation;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;
import main.org.usfirst.frc.team1640.sensors.TestSensorSet;
import main.org.usfirst.frc.team1640.vision.VisionServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class TestRobotContext implements IRobotContext {
	private IDriveTrain driveTrain;
	private DStation driverStation;
	private Controller driverController;
	private Controller operatorController;
	private ISensorSet sensorSet;

	public TestRobotContext() {
		driveTrain = new TestDriveTrain();
		driverStation = new DStation();
		driverController = new Controller(0);
		operatorController = new Controller(1);
		sensorSet = new TestSensorSet();
	}
	
	@Override
	public IDriveTrain getDriveTrain() {
		
		return driveTrain;
	}

	@Override
	public DStation getDStation() {
		
		return null;
	}

	@Override
	public Controller getDriverController() {
		
		return driverController;
	}

	@Override
	public Controller getOperatorController() {
		
		return operatorController;
	}

	@Override
	public ISensorSet getSensorSet() {
		
		return sensorSet;
	}

	@Override
	public PowerDistributionPanel getPDP() {
		
		return null;
	}
	
	@Override
	public Intake getIntake() {
		return null;
	}
	
	public AirCompressor getAirCompressor() {
		return null;
	}
	
	@Override
	public Placer getPlacer(){
		return null;
	}

	@Override
	public VisionServer getVisionServer() {
		// TODO Auto-generated method stub
		return null;
	}

}
>>>>>>> refs/remotes/origin/master
*/
