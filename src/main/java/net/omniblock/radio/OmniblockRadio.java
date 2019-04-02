package net.omniblock.radio;

import net.omniblock.radio.protocol.category.RadioType;
import net.omniblock.radio.protocol.manager.console.Console;

public class OmniblockRadio {

	public static void main(String[] args) {
		
		Console.WRITTER.printInfo("Se ha inicializado el sistema general de Omniblock Radio!");
		
		RadioType.ELECTRO_BOT.startBot();
		RadioType.POP_BOT.startBot();
		RadioType.ROCK_BOT.startBot();
		RadioType.VARIADA_BOT.startBot();
		RadioType.HALLOWEEN_BOT.startBot();

	}
	
}
