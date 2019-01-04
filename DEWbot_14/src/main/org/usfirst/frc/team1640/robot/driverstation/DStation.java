package main.org.usfirst.frc.team1640.robot.driverstation;

import main.org.usfirst.frc.team1640.robot.auton.AutonInstruction;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.ScPos;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.StartPos;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.SwPos;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DStation {
	private FieldConfig config;
	private AutonInstruction autonInstruction;
	
	public DStation() {
		config = new FieldConfig();
		autonInstruction = new AutonInstruction();
	}
	
	public FieldConfig getFieldConfiguration() {
		
		isFieldConfigReady();
		
		return config;
	}
	
	public boolean isFieldConfigReady() {
		
		String gsmMsg = DriverStation.getInstance().getGameSpecificMessage();
		parseGameSpecificMessage(gsmMsg);
		
		String startPosMsg = SmartDashboard.getString("Starting Position", "");
		parseStartPosMessage(startPosMsg);
		
		return config.getStartPos() != StartPos.None
				&& config.getSwPos() != SwPos.None
				&& config.getScPos() != ScPos.None;
	}
	
	public AutonInstruction getAutonInstruction() {
		
		isAutonStringReady();
		
		return autonInstruction;
	}
	
	public boolean isAutonStringReady() {
		
		autonInstruction.setSimple(SmartDashboard.getString("Simple Script", ""));
		autonInstruction.setComplexBoth(SmartDashboard.getString("Both", ""));
		autonInstruction.setComplexSwitchOnly(SmartDashboard.getString("Switch Only", ""));
		autonInstruction.setComplexScaleOnly(SmartDashboard.getString("Scale Only", ""));
		autonInstruction.setComplexNeither(SmartDashboard.getString("Neither", ""));
		
		return !(autonInstruction.getSimple().length() > 1)
				&& !(autonInstruction.getComplexBoth().length() > 1)
				&& !(autonInstruction.getComplexSwitchOnly().length() > 1)
				&& !(autonInstruction.getComplexScaleOnly().length() > 1)
				&& !(autonInstruction.getComplexNeither().length() > 1);
	}
	
	private void parseGameSpecificMessage(String msg) {
		if (msg.length() >= 2) {
			if (msg.charAt(0) == 'L') {
				config.setSwPos(SwPos.Left);
			}
			else if (msg.charAt(0) == 'R'){
				config.setSwPos(SwPos.Right);
			}
			else {
				System.out.println("Invalid Switch Data from FMS");
			}
			
			if (msg.charAt(1) == 'L') {
				config.setScPos(ScPos.Left);
			}
			else if (msg.charAt(1) == 'R'){
				config.setScPos(ScPos.Right);
			}
			else {
				System.out.println("Invalid Scale Data from FMS");
			}
		}
		else {
			System.out.println("Invalid Data from FMS");
			config.setSwPos(SwPos.None);
			config.setScPos(ScPos.None);
		}
	}
	
	private void parseStartPosMessage(String msg) {
			if (msg.equals("Left")) {
				config.setStartPos(StartPos.Left);
			}
			else if (msg.equals("Right")){
				config.setStartPos(StartPos.Right);
			}
			else if (msg.equals("Center")) {
				config.setStartPos(StartPos.Center);
			}
			else {
				config.setStartPos(StartPos.None);
			}
	}
	
}
