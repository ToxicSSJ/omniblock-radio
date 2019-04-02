package net.omniblock.radio.protocol.manager.console.cmds;

import net.omniblock.radio.protocol.manager.console.CommandCatcher.Command;

public class StopCommand implements Command {

	@Override
	public boolean execute(String command, String[] args) {
		
		if(command.equalsIgnoreCase("stop")) {
			
		}
		return false;
		
	}

}
