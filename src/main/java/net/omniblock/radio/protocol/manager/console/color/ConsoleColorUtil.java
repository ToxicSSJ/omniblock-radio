package net.omniblock.radio.protocol.manager.console.color;

import java.util.Map;

/**
 * 
 * Esta clase se encargará de añadir metodos utiles para la
 * proyección de colores por consola.
 * 
 * @author zlToxicNetherlz
 *
 */
public class ConsoleColorUtil {

	/**
	 * 
	 * Traduce los colores de un mensaje a tipo ANSI escape code para
	 * ser imprimidos y visualizados desde la consola, se es requerido
	 * el caracter del color principal para generar la traducción.
	 * <br> </br>
	 * Referencia: {@link https://en.wikipedia.org/wiki/ANSI_escape_code}
	 * 
	 * @param message El mensaje que será traducido.
	 * @param colorkey El caracter que se usara para remplazar el color.
	 * 
	 * @return El mensaje traducido.
	 */
	public static String translateColors(String message, char colorkey) {
		return translateColors(message, colorkey, '�');
	}
	
	/**
	 * 
	 * Traduce los colores de un mensaje a tipo ANSI escape code para
	 * ser imprimidos y visualizados desde la consola, se es requerido
	 * el caracter del color principal para generar la traducción.
	 * <br> </br>
	 * Referencia: {@link https://en.wikipedia.org/wiki/ANSI_escape_code}
	 * 
	 * @param message El mensaje que será traducido.
	 * @param colorkey El caracter que se usara para remplazar el color.
	 * @param backgroundcolorkey El caracter que se usara para remplzar el color de fondo.
	 * 
	 * @return El mensaje traducido.
	 */
	public static String translateColors(String message, char colorkey, char backgroundcolorkey) {
		
		if(backgroundcolorkey != '�') {
			message = message.replaceAll(String.valueOf(backgroundcolorkey), String.valueOf(ConsoleColor.DEFAULT_BACKGROUNDCOLOR_CHAR));
		}
		
		message = message.replaceAll(String.valueOf(colorkey), String.valueOf(ConsoleColor.DEFAULT_COLOR_CHAR));
		
		for(Map.Entry<String, String> COLOR_MAP : ConsoleColor.COLOR_REFERENCE_MAP.entrySet()) {
			
			message =  message.contains(COLOR_MAP.getKey()) ? message.replaceAll(COLOR_MAP.getKey(), COLOR_MAP.getValue()) : message;
			
		}
		
		
		return message;
		
	}
	
	
}
