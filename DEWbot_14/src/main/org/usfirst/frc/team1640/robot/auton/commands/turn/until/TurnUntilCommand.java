package main.org.usfirst.frc.team1640.robot.auton.commands.turn.until;

import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;
import main.org.usfirst.frc.team1640.robot.traversal.swerve.axial.AxialStrategy;
import main.org.usfirst.frc.team1640.utilities.MathUtilities;

public abstract class TurnUntilCommand implements AutonCommand {
	private boolean initialized = false;
	private boolean done = false;
	private double drive;
	
	private AxialStrategy axialStrat;
	
	public TurnUntilCommand(AxialStrategy axialStrat, double drive) {
		
		this.axialStrat = axialStrat;
		
		// make sure drive is valid value
		this.drive = MathUtilities.constrain(drive, -1.0, 1.0);
	}

	@Override
	public void execute() {
		if(!initialized){
			initialized = true;
			axialStrat.init();
			initEndCondition();
		}
		if(!done){
			axialStrat.setAxialDrive(drive);
			axialStrat.execute();
		}
		if(isEndConditionMet()){
			done = true;
			axialStrat.setAxialDrive(0.0);
			axialStrat.execute();
			axialStrat.end();
		}
	}

	@Override
	public boolean isRunning() {
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void reset() {
		initialized = false;
		done = false;
		initEndCondition();
	}
	
	public abstract void initEndCondition();
	
	public abstract boolean isEndConditionMet();

}
