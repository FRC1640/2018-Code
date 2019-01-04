package main.org.usfirst.frc.team1640.robot.context;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import main.org.usfirst.frc.team1640.compressor.AirCompressor;
import main.org.usfirst.frc.team1640.compressor.TestAirCompressor;
import main.org.usfirst.frc.team1640.controllers.Controller;
import main.org.usfirst.frc.team1640.drivetrain.DriveTrain;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.intake.TestIntake;
import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.placer.TestPlacer;
import main.org.usfirst.frc.team1640.robot.driverstation.DStation;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;
import main.org.usfirst.frc.team1640.sensors.SensorSet;
import main.org.usfirst.frc.team1640.vision.VisionServer;

public class RobotContext implements IRobotContext {
	private IDriveTrain driveTrain;
	private DStation driverStation;
	private Controller driverController, operatorController;
	private ISensorSet sensorSet;
	private PowerDistributionPanel pdp;
	private Placer placer;
	private VisionServer visionServer;
	private Intake intake;
	private AirCompressor compressor;
	
	public RobotContext() {
		driveTrain = new DriveTrain();
		
		int kDriverControllerPort = 0;
		int kOperatorControllerPort = 1;
		driverController = new Controller(kDriverControllerPort);
		operatorController = new Controller(kOperatorControllerPort);
		
		
		sensorSet = new SensorSet();
		
		placer = new Placer(sensorSet);
		
		visionServer = new VisionServer(this);
		
		intake = new Intake();
		
		compressor = new AirCompressor();
		
		driverStation = new DStation();
		
//		int kPDP_ID = 0;
//		pdp = new PowerDistributionPanel(kPDP_ID);
	}
	
	public IDriveTrain getDriveTrain() {
		return driveTrain;
	}
	
	public DStation getDStation() {
		return driverStation;
	}
	
	public Controller getDriverController() {
		return driverController;
	}
	
	public Controller getOperatorController() {
		return operatorController;
	}
	
	public ISensorSet getSensorSet() {
		return sensorSet;
	}
	
	public PowerDistributionPanel getPDP() {
		return pdp;
	}
	
	public Placer getPlacer() {
		return placer;
	}
	
	public Intake getIntake() {
		return intake;
  }
  
	public VisionServer getVisionServer() {
		return visionServer;
	}
	
	public AirCompressor getAirCompressor() {
		return compressor;
	}
}
