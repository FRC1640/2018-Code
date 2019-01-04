package main.org.usfirst.frc.team1640.robot.auton;

import java.util.HashMap;

import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;

public class ScriptController {
	private enum SwitchNumber {Switch0, Switch1, Switch2, Switch3, Switch4};
	private enum ScaleNumber {Scale0, Scale1, Scale2, Scale3, Scale4};
	private enum FieldSetting {SameSide, OppositeSide, SwitchOnly, ScaleOnly};
	private enum StartPosition {Left, Right, Center};
	private enum AutonType {SimpleSwitch, Cross, Complex, PowerUp, Custom1, Custom2};
	
	private AutonType autonType;
	private FieldSetting fieldSetting;
	private ComplexScriptInput inputs;
	
	private AutonScript script;
	
	private IRobotContext robotContext;
	
	public ScriptController(IRobotContext robotContext){
		this.robotContext = robotContext;
	}
	
	public void execute(){
		if(autonChanged() || script == null){
			switch(autonType){
				case Cross:{
					script = null; //new CrossScript(robotContext);
					break;
				}
				case SimpleSwitch: {
					script = null; //new SimpleSwitch(robotContext);
					break;
				}
				case Complex: {
					script = getComplexScript(inputs, fieldSetting, robotContext);
					break;
				}
				case PowerUp: {
					break;
				}
				case Custom1: {
					break;
				}
				case Custom2: {
					break;
				}
				default:{
					break;
				}
			}
		}
		script.execute();
	}
	
	private boolean autonChanged(){
		//read from network tables if auton selection changed
		StartPosition startPos = StartPosition.Left;
		fieldSetting = FieldSetting.SameSide;
		inputs = new ComplexScriptInput(startPos);
		inputs.setNumbers(SwitchNumber.Switch0, ScaleNumber.Scale0, fieldSetting);
		autonType = AutonType.Complex;
		return false;
	}
	
	private AutonScript getComplexScript(ComplexScriptInput input, FieldSetting fieldSetting, IRobotContext robotContext){
		SwitchNumber switchNum = input.getSwitchNumber(fieldSetting);
		ScaleNumber scaleNum = input.getScaleNumber(fieldSetting);
		StartPosition startPos = input.getStartPos();
		AutonScript complexScript = null;
		switch(fieldSetting){
			case SameSide: {
				complexScript = getSameSideScript(switchNum, scaleNum, startPos);
				break;
			}
			case OppositeSide: {
				complexScript = getOppSideScript(switchNum, scaleNum, startPos);
				break;
			}
			case SwitchOnly: {
				complexScript = getSwitchOnlyScript(switchNum, scaleNum, startPos);
				break;
			}
			case ScaleOnly: {
				complexScript = getScaleOnlyScript(switchNum, scaleNum, startPos);
				break;
			}
		}
		return complexScript;
	}
	
	private AutonScript getSameSideScript(SwitchNumber switchNum, ScaleNumber scaleNum, StartPosition startPos){
		int switchNumber = switchNum.ordinal();
		int scaleNumber = scaleNum.ordinal();
		int blockCount = 1;
		boolean atSwitch;
		AutonScript script = null;
		if(switchNumber != 0){
			//add placeSw
			System.out.println("PlaceSW");
			switchNumber--;
			atSwitch = true;
		}else if(scaleNumber != 0){
			//add placeSc
			System.out.println("PlaceSc");
			scaleNumber--;
			atSwitch = false;
		}else{
			System.out.println("No script");
			return null;
		}
		for(;scaleNumber > 0; scaleNumber--){
			if(atSwitch){
				System.out.println("Switch to " + blockCount);
				atSwitch = false;
			}else{
				System.out.println("Scale to " + blockCount);
			}
			System.out.println(blockCount + " to scale");
			blockCount++;
		}
		for(;switchNumber > 0; switchNumber--){
			if(atSwitch){
				System.out.println("Switch to " + blockCount);
			}else{
				System.out.println("Scale to " + blockCount);
				atSwitch = true;
			}
			System.out.println(blockCount + " to switch");
			blockCount++;
		}
		return script;
	}
	
	private AutonScript getOppSideScript(SwitchNumber switchNum, ScaleNumber scaleNum, StartPosition startPos){
		int switchNumber = switchNum.ordinal();
		int scaleNumber = scaleNum.ordinal();
		int blockCount = 6;
		boolean atSwitch;
		AutonScript script = null;
		if(scaleNumber != 0){
			System.out.println("PlaceFarSc");
			scaleNumber--;
			atSwitch = false;
		}else if(switchNumber != 0){
			System.out.println("PlaceFarSw");
			switchNumber--;
			atSwitch = true;
		}else{
			System.out.println("No script");
			return null;
		}
		for(;scaleNumber > 0; scaleNumber--){
			System.out.println("Far scale to " + blockCount);
			System.out.println(blockCount + " to far scale");
			blockCount--;
		}
		for(;switchNumber > 0; switchNumber--){
			if(atSwitch){
				System.out.println("Far Switch to " + blockCount);
			}else{
				System.out.println("Far Scale to " + blockCount);
				atSwitch = true;
			}
			System.out.println(blockCount + " to far switch");
			blockCount--;
		}
		return script;
	
	}
	
	private AutonScript getSwitchOnlyScript(SwitchNumber switchNum, ScaleNumber scaleNum, StartPosition startPos){
		int switchNumber = switchNum.ordinal();
		int scaleNumber = scaleNum.ordinal();
		int blockCount = 1;
		boolean atSwitch;
		AutonScript script = null;
		if(switchNumber != 0){
			System.out.println("PlaceSW");
			switchNumber--;
			atSwitch = true;
		}else if(scaleNumber != 0){
			System.out.println("Place Far Sc");
			scaleNumber--;
			atSwitch = false;
		}else{
			System.out.println("No script");
			return null;
		}
		for(;switchNumber > 0; switchNumber--){
			System.out.println("Switch to " + blockCount);
			System.out.println(blockCount + " to switch");
			blockCount++;
		}
		blockCount = 6;
		for(;scaleNumber > 0; scaleNumber--){
			if(atSwitch){
				System.out.println("Switch to " + blockCount);
				atSwitch = false;
			}else{
				System.out.println("Far Scale to " + blockCount);
			}
			System.out.println(blockCount + " to far scale");
			blockCount--;
		}
		return script;
	}
	
	private AutonScript getScaleOnlyScript(SwitchNumber switchNum, ScaleNumber scaleNum, StartPosition startPos){
		int switchNumber = switchNum.ordinal();
		int scaleNumber = scaleNum.ordinal();
		int blockCount = 1;
		boolean atSwitch;
		AutonScript script = null;
		if(scaleNumber != 0){
			System.out.println("Place Sc");
			scaleNumber--;
			atSwitch = false;
		}else if(switchNumber != 0){
			System.out.println("Place Far Sw");
			switchNumber--;
			atSwitch = true;
		}else{
			System.out.println("No script");
			return null;
		}
		for(;scaleNumber > 0; scaleNumber--){
			System.out.println("Scale to " + blockCount);
			System.out.println(blockCount + " to scale");
			blockCount++;
		}
		blockCount = 6;
		for(;switchNumber > 0; switchNumber--){
			if(atSwitch){
				System.out.println("Far Switch to " + blockCount);
			}else{
				System.out.println("Scale to " + blockCount);
				atSwitch = true;
			}
			System.out.println(blockCount + " to far switch");
			blockCount--;
		}
		return script;
	}
	
	private class ComplexScriptInput{
		private HashMap<FieldSetting, SwitchNumber> switchNums;
		private HashMap<FieldSetting, ScaleNumber> scaleNums;
		private StartPosition startPos;
		
		public ComplexScriptInput(StartPosition startPos){
			this.startPos = startPos;
		}
		
		public void setNumbers(SwitchNumber switchNum, ScaleNumber scaleNum, FieldSetting fieldSetting){
			switchNums.put(fieldSetting, switchNum);
			scaleNums.put(fieldSetting, scaleNum);
		}
		
		public SwitchNumber getSwitchNumber(FieldSetting fieldSetting){
			return switchNums.get(fieldSetting);
		}
		
		public ScaleNumber getScaleNumber(FieldSetting fieldSetting){
			return scaleNums.get(fieldSetting);
		}
		
		public StartPosition getStartPos(){
			return startPos;
		}
	}
}
