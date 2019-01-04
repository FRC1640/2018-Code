package main.org.usfirst.frc.team1640.robot.context;

import main.org.usfirst.frc.team1640.compressor.AirCompressor;
import main.org.usfirst.frc.team1640.controllers.Controller;
import main.org.usfirst.frc.team1640.drivetrain.IDriveTrain;
import main.org.usfirst.frc.team1640.intake.Intake;
import main.org.usfirst.frc.team1640.placer.Placer;
import main.org.usfirst.frc.team1640.robot.driverstation.DStation;
import main.org.usfirst.frc.team1640.sensors.ISensorSet;
import main.org.usfirst.frc.team1640.vision.VisionServer;
//import main.org.usfirst.frc.team1640.vision.VisionServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public interface IRobotContext {
	public abstract IDriveTrain getDriveTrain();
	
	public abstract DStation getDStation();
	
	public abstract Controller getDriverController();
	
	public abstract Controller getOperatorController();
	
	public abstract ISensorSet getSensorSet();
	
	public abstract PowerDistributionPanel getPDP();
	
	public abstract Intake getIntake();

	public abstract VisionServer getVisionServer();
	
	public abstract AirCompressor getAirCompressor();
	
	public abstract Placer getPlacer();


}
