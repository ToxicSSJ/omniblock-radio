package net.omniblock.radio.protocol.manager.console.reader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import net.omniblock.radio.protocol.manager.console.CommandCatcher;
import net.omniblock.radio.protocol.manager.console.Console;

public class ConsoleReader implements Runnable {

	protected CommandCatcher catcher = new CommandCatcher();
	protected Boolean read = true;
	
	@Override
	public void run() {
		
		while(read) {
			
			try (InputStreamReader instream = new InputStreamReader(System.in);
			        BufferedReader buffer = new BufferedReader(instream)) {
				
				String writed;
				String[] args = new String[0];
				
			    while ((writed = buffer.readLine()) != null) {
			        
			    	if (writed.isEmpty()) {
			            break;
			        }
			    	
			    	if(writed.contains(" ")) {
	                	
	                	String[] cacheargs = Arrays.copyOfRange(writed.split(" "), 1, writed.split(" ").length);
	                	args = cacheargs;
	                	
	                	writed = writed.split(" ")[0];
	                	writed = writed.replaceAll(" ", "");
	                	
	                }
	                
	                if(!CommandCatcher.catchCommand(writed, args)) {
	                	Console.WRITTER.printError(CommandCatcher.NOT_RECOGNIZED_COMMAND.replaceFirst("%s", writed));
	                }
	                
	                run();
	            	return;
			    	
			    }
			    
			} catch (Exception e) {
				
			    e.printStackTrace();
			    
			}
			
            
        }
		
	}

	public boolean isRead() {
		return read;
	}

}
