package net.omniblock.radio.protocol.manager.controllers.handler;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.omniblock.radio.protocol.manager.utils.MessageUtils;

public class DiscordCommandHandler {

	protected static List<DiscordCommand> registeredCommands = new ArrayList<DiscordCommand>();
	
	{
		
		
		
	}
	
	public static void executeCommand(Message message, String[] command) {
		
		for(DiscordCommand dc : registeredCommands) {
			
			if(dc.execute(message, command))
				return;
			
		}
		
		Message cache = message.getChannel().sendMessage(
				new EmbedBuilder()
					.setColor(Color.RED)
					.setTitle("💢 ¡Ese comando no existe!")
					.setDescription(
							message.getAuthor().getAsMention() + " El comando que has colocado no está " + 
							"registrado por el sistema, rectifica la sintaxis. \n" +
							"**Uso General:** `::<comando> <parametros>`")
					.setFooter("💣 Este mensaje se autodestruirá en 30 segundos!", null)
					.build()).complete();
		
		MessageUtils.deleteMessageAfter(cache, TimeUnit.SECONDS, 30);
		return;
		
	}
	
	public void addCommand(DiscordCommand command) {
		
		registeredCommands.add(command);
		return;
		
	}
	
	public interface DiscordCommand {
		
		public boolean execute(Message message, String[] command);
		
	}
	
}
