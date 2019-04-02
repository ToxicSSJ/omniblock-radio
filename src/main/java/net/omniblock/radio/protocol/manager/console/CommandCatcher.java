package net.omniblock.radio.protocol.manager.console;

import net.omniblock.radio.protocol.manager.console.cmds.StopCommand;

public class CommandCatcher {

	public static final String NOT_RECOGNIZED_COMMAND = "El comando '%s' no ha sido reconocido por el sistema!";
	
	public static final String NOT_ENOUGHT_ARGUMENTS = "No hay suficientes argumentos para el comando '%s' por favor rectifique la sintaxis!";
	public static final String NOT_RECOGNIZED_ARGUMENT = "No se ha reconocido el argumento '%s' en el comando '%s' por favor rectifique la sintaxis!";
	
	private static final Command[] COMMANDS = new Command[] {
			
			new StopCommand()
			
	};
	
	public CommandCatcher() { }
	
	public static boolean catchCommand(String command, String[] args) {
		
		for(Command cmd : COMMANDS) {
			
			if(cmd.execute(command, args)) 
				return true;
			
		}
		
		return false;
		
	}
	
	/**
	 * 
	 * Clase general encargada de tener instancias
	 * para el manejo de comandos del sistema.
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
	public interface Command {
		
		/**
		 * 
		 * Este metodo será ejecutado una vez un comando
		 * que aún no ha sido identificado pase por el
		 * lector como proceso general. Si este metodo
		 * devuelve true, el lector parará y no se seguirá
		 * tratando de identificar el manager de dicho
		 * comando. Caso contrario el lector seguirá con
		 * las demas instancias de comandos.
		 * 
		 * @param cmd El comando en su formato habitual.
		 * @param args Los argumentos extras del comando.
		 * @return <strong>true</strong> si el procesador
		 * del comando fue identificado.
		 */
		public boolean execute(String command, String[] args);
		
	}
	
}
