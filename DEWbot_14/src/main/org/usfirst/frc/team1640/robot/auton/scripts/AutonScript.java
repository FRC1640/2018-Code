package main.org.usfirst.frc.team1640.robot.auton.scripts;

import java.util.ArrayList;

import main.org.usfirst.frc.team1640.robot.auton.commands.AutonCommand;

public class AutonScript implements AutonCommand {
	
	// class to cluster an AutonCommand with its name
	private class NamedAutonCommand implements AutonCommand {
		String name;
		AutonCommand command;
		
		public NamedAutonCommand(String name, AutonCommand command) {
			this.name = name;
			this.command = command;
		}
		
		public String getName() {
			return name;
		}

		@Override
		public void execute() {
			command.execute();
		}

		@Override
		public boolean isRunning() {
			return command.isRunning();
		}

		@Override
		public boolean isInitialized() {
			return command.isInitialized();
		}

		@Override
		public void reset() {
			command.reset();
		}
	}
	
	private int position = 0;
	private ArrayList<NamedAutonCommand> script = new ArrayList<NamedAutonCommand>();
	private ArrayList<NamedAutonCommand> executing = new ArrayList<NamedAutonCommand>();

	@Override
	public final void execute() {
		if(position <= script.size()-1 && script.get(position).isInitialized()) { //if the previous command is initialized, move on to the next one
			NamedAutonCommand a = script.get(position);
			executing.add(a); //add new command to be executed
			position++;
			System.out.println("added " + position + " to executing (" + a.getName() + ")");
		}
		for(int i = 0; i < executing.size(); ){ //execute all commands
			NamedAutonCommand command = executing.get(i);
			command.execute();
			if (!command.isRunning()) { // remove any commands that are no longer running
				executing.remove(i);
			}
			else {
				i++;
			}
		}
	}

	@Override
	public final boolean isRunning() { 
		return !(position == script.size() - 1 && executing.isEmpty()); //script is finished when all commands have been initialized and executed
	}

	@Override
	public final boolean isInitialized() {
		return isRunning();
	}
	
	public final void reset(){
		executing.clear(); //clear previous executing script
		position = 0; //reset position
		for(AutonCommand ac : script) { //reset each command in the script
			ac.reset();
		}
	}
	
	public final void addCommand(String name, AutonCommand command) {
		NamedAutonCommand namedCommand = new NamedAutonCommand(name, command);
		script.add(namedCommand);
	}
	
	public final AutonCommand getCommand(String name) {
		AutonCommand myCommand = null;
		for (NamedAutonCommand command : script) {
			if (command.getName().equals(name)) myCommand = command;
		}
		return myCommand;
	}
	
}
