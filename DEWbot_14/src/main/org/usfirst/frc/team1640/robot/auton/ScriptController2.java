package main.org.usfirst.frc.team1640.robot.auton;

import main.org.usfirst.frc.team1640.drivetrain.cvtpivot.ICVTPivot;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.simple.Center3Cube;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.simple.CenterIRI;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.simple.CenterSwitch;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.simple.CenterSwitch2Switch;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.simple.CrossTheLine;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.CloseCrossSwitch;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.DoubleSideSwitch;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.Far2CubeV2;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.OcelotThreeCube;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.Start2Sc;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.Start2Sc2Cube2Sc;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.Start2Sw;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.Start2Sw2Cube12Sc;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.SwitchFarScale;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.SwitchSwitchCross;
import main.org.usfirst.frc.team1640.robot.auton.scripts.routines.starts.TripleSideSwitch;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig;
import main.org.usfirst.frc.team1640.robot.driverstation.FieldConfig.StartPos;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class ScriptController2 implements Strategy {
	private IRobotContext robotContext;
	
	private AutonScript script;
	
	private boolean ready;
	
	public ScriptController2(IRobotContext robotContext) {
		this.robotContext = robotContext;
		
		script = null;
		ready = false;
	}
	
	@Override
	public void init() {
	}

	@Override
	public void execute() {
		if (!ready) {
			AutonInstruction inst = robotContext.getDStation().getAutonInstruction();
			FieldConfig config = robotContext.getDStation().getFieldConfiguration();
			
			switch(inst.getSimple()) {
			case "CrossTheLine": script = new CrossTheLine(robotContext, config); break;
			case "1Cube": script = new CenterSwitch(robotContext, config); break;
			case "2Cube": script = new CenterSwitch2Switch(robotContext, config); break;
			case "2CubeAndDrive": script = null; break;
			case "3Cube": script = new Center3Cube(robotContext, config); break;
			case "ComplexScript": script = chooseComplexScript(inst, config); break;
			case "Custom1": script = new CenterIRI(robotContext, config); break;
			case "Custom2": script = null; break;
			case "Custom3": script = null; break;
			default: System.out.println("Invalid Simple Selection: " + inst.getSimple()); script = new CenterSwitch2Switch(robotContext, config); break;
			}
						
			if (script != null) {
				System.out.println("Selected Script: " + script.getClass().getSimpleName());
				ready = true;
			}
			
			ICVTPivot pivot;
			if (config.getStartPos() == StartPos.Left){
				pivot = robotContext.getDriveTrain().getFRPivot();
			}
			else {
				pivot = robotContext.getDriveTrain().getFLPivot();
			}
			
			robotContext.getDriveTrain().setFavoritePivot(pivot);
			
		}
		else {
			if (script != null) {
				try {
					script.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Null Script");
			}
		}
	}

	@Override
	public void end() {
	}
	
	private AutonScript chooseComplexScript(AutonInstruction inst, FieldConfig config) {
		
		String startPos = config.getStartPos().name();
		String swPos = config.getSwPos().name();
		String scPos = config.getScPos().name();
		
		if (startPos.equals("Center")) {
			System.out.println("Can't run ComplexScript from Center");
		}
		if (startPos.equals("")) {
			System.out.println("No Starting Position Specified");
			return null;
		}
		if (swPos.equals("")) {
			System.out.println("No Switch Position Specified");
			return null;
		}
		if (scPos.equals("")) {
			System.out.println("No Scale Position Specified");
		}
		
		if (startPos.equals(swPos) && startPos.equals(scPos)) {
			
			switch(inst.getComplexBoth()) {
			case "CrossTheLine": return new CrossTheLine(robotContext, config);
			case "Scale": return new Start2Sc(robotContext, config);
			case "Switch": return new Start2Sw(robotContext, config);
			case "2CubeSwitch": return new DoubleSideSwitch(robotContext, config);
			case "2CubeSwitchAndDrive": return new SwitchSwitchCross(robotContext, config);
			case "3CubeSwitch": return new TripleSideSwitch(robotContext, config);
			case "2CubeScale": return new Start2Sc2Cube2Sc(robotContext, config);
			case "3CubeScale": return new OcelotThreeCube(robotContext, config);
			case "SwitchAndScale": return new Start2Sw2Cube12Sc(robotContext, config);
			case "Custom1": return null;
			case "Custom2": return null;
			case "Custom3": return null;
			default: return new CrossTheLine(robotContext, config);
			}
		}
		else if (startPos.equals(swPos) && !startPos.equals(scPos)) {
			
			switch(inst.getComplexSwitchOnly()) {
			case "CrossTheLine": return new CrossTheLine(robotContext, config);
			case "Scale": return new Start2Sc(robotContext, config);
			case "2CubeScale": return new Far2CubeV2(robotContext, config);
			case "Switch": return new Start2Sw(robotContext, config);
			case "SwitchFarScale": return new SwitchFarScale(robotContext, config);
			case "2CubeSwitch": return new DoubleSideSwitch(robotContext, config);
			case "2CubeSwitchAndDrive": return new SwitchSwitchCross(robotContext, config);
			case "3CubeSwitch": return new TripleSideSwitch(robotContext, config);
			case "SwitchAndScale": return null;
			case "Custom1": return null;
			case "Custom2": return null;
			case "Custom3": return null;
			default: return new CrossTheLine(robotContext, config);
			}
		}
		else if (!startPos.equals(swPos) && startPos.equals(scPos)) {
			switch(inst.getComplexScaleOnly()) {
			case "CrossTheLine": return new CrossTheLine(robotContext, config);
			case "Scale": return new Start2Sc(robotContext, config);
			case "2CubeScale": return new Start2Sc2Cube2Sc(robotContext, config);
			case "3CubeScale": return new OcelotThreeCube(robotContext, config);
			case "CloseCrossSwitch": return new CloseCrossSwitch(robotContext, config);
			case "SwitchAndScale": return null;
			case "Custom1": return null;
			case "Custom2": return null;
			case "Custom3": return null;
			default: return new CrossTheLine(robotContext, config);
			}
		}
		else if (!startPos.equals(swPos) && !startPos.equals(scPos)) {
			switch(inst.getComplexNeither()) {
			case "CrossTheLine": return new CrossTheLine(robotContext, config);
			case "Scale": return new Start2Sc(robotContext, config);
			case "2CubeScale": return new Far2CubeV2(robotContext, config);
			case "CloseCrossSwitch": return new CloseCrossSwitch(robotContext, config);
			case "SwitchAndScale": return null;
			case "Custom1": return null;
			case "Custom2": return null;
			case "Custom3": return null;
			default: return new CrossTheLine(robotContext, config);
			}
		}
		else {
			System.out.println("No valid auton combination");
			return null;
		}
	}
	
	public void reset() {
		ready = false;
	}

}
