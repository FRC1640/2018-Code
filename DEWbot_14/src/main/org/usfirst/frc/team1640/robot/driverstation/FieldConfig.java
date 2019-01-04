package main.org.usfirst.frc.team1640.robot.driverstation;

public class FieldConfig {
	public static enum StartPos {
		None, Left, Center, Right;
	}
	
	public static enum SwPos {
		None, Left, Right;
	}
	
	public static enum ScPos {
		None, Left, Right;
	}
	
	private StartPos startPos;
	private SwPos swPos;
	private ScPos scPos;
	
	public FieldConfig() {
		this.startPos = StartPos.None;
		this.swPos = SwPos.None;
		this.scPos = ScPos.None;
	}
	
	public void setStartPos(StartPos startPos) {
		this.startPos = startPos;
	}
	
	public StartPos getStartPos() {
		return startPos;
	}
	
	public void setSwPos(SwPos swPos) {
		this.swPos = swPos;
	}
	
	public SwPos getSwPos() {
		return swPos;
	}
	
	public void setScPos(ScPos scPos) {
		this.scPos = scPos;
	}
	
	public ScPos getScPos() {
		return scPos;
	}

}
