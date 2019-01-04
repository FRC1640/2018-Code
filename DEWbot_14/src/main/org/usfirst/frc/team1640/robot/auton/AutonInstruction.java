package main.org.usfirst.frc.team1640.robot.auton;


public class AutonInstruction {
	private String simple;
	private String complexBoth;
	private String complexSwitchOnly;
	private String complexScaleOnly;
	private String complexNeither;
	
	public AutonInstruction() {
		simple = "";
		complexBoth = "";
		complexSwitchOnly = "";
		complexScaleOnly = "";
		complexNeither = "";
	}
	
	public void setSimple(String simple) {
		this.simple = simple;
	}
	
	public String getSimple() {
		return simple;
	}
	
	public void setComplexBoth(String complexBoth) {
		this.complexBoth = complexBoth;
	}
	
	public String getComplexBoth() {
		return complexBoth;
	}
	
	public void setComplexSwitchOnly(String complexSwitchOnly) {
		this.complexSwitchOnly = complexSwitchOnly;
	}
	
	public String getComplexSwitchOnly() {
		return complexSwitchOnly;
	}
	
	public void setComplexScaleOnly(String complexScaleOnly) {
		this.complexScaleOnly = complexScaleOnly;
	}
	
	public String getComplexScaleOnly() {
		return complexScaleOnly;
	}
	
	public void setComplexNeither(String complexNeither) {
		this.complexNeither = complexNeither;
	}
	
	public String getComplexNeither() {
		return complexNeither;
	}
}
