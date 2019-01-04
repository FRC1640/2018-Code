package main.org.usfirst.frc.team1640.robot.tests;

import main.org.usfirst.frc.team1640.robot.auton.commands.PrintCommand;
import main.org.usfirst.frc.team1640.robot.auton.scripts.AutonScript;
import main.org.usfirst.frc.team1640.robot.auton.scripts.ExampleAutonScript;
import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.utilities.Strategy;

public class AddingExternallyToAutonScriptTest implements Strategy {
	private AutonScript script;
	
	public AddingExternallyToAutonScriptTest(IRobotContext robotContext) {
		script = new AutonScript();
	}

	@Override
	public void init() {
		script.addCommand("test1", new PrintCommand("Test 1"));
		script.addCommand("test2", new PrintCommand("Test 2"));
	}

	@Override
	public void execute() {
		script.execute();
	}

	@Override
	public void end() {
		script = null;
	}
	
	
	
}
