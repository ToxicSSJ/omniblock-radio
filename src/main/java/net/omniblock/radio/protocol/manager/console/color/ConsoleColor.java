package net.omniblock.radio.protocol.manager.console.color;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Esta clase contendr√° las variables constantes de los colores
 * que se usar√°n para proyecci√≥n de colores por consola.
 * 
 * @author zlToxicNetherlz
 *
 */
public class ConsoleColor {

	public static final Map<String, String> COLOR_REFERENCE_MAP = new HashMap<String, String>();
	
	public static final char DEFAULT_COLOR_CHAR = 'ß';
	public static final char DEFAULT_BACKGROUNDCOLOR_CHAR = '£';
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	
	static {
		
		COLOR_REFERENCE_MAP.put("ßr", ANSI_RESET);
		COLOR_REFERENCE_MAP.put("ß0", ANSI_BLACK);
		COLOR_REFERENCE_MAP.put("ßc", ANSI_RED);
		COLOR_REFERENCE_MAP.put("ßa", ANSI_GREEN);
		COLOR_REFERENCE_MAP.put("ße", ANSI_YELLOW);
		COLOR_REFERENCE_MAP.put("ß1", ANSI_BLUE);
		COLOR_REFERENCE_MAP.put("ßd", ANSI_PURPLE);
		COLOR_REFERENCE_MAP.put("ßb", ANSI_CYAN);
		COLOR_REFERENCE_MAP.put("ßf", ANSI_WHITE);
		
		COLOR_REFERENCE_MAP.put("£0", ANSI_BLACK_BACKGROUND);
		COLOR_REFERENCE_MAP.put("£c", ANSI_RED_BACKGROUND);
		COLOR_REFERENCE_MAP.put("£a", ANSI_GREEN_BACKGROUND);
		COLOR_REFERENCE_MAP.put("£e", ANSI_YELLOW_BACKGROUND);
		COLOR_REFERENCE_MAP.put("£1", ANSI_BLUE_BACKGROUND);
		COLOR_REFERENCE_MAP.put("£d", ANSI_PURPLE_BACKGROUND);
		COLOR_REFERENCE_MAP.put("£b", ANSI_CYAN_BACKGROUND);
		COLOR_REFERENCE_MAP.put("£f", ANSI_WHITE_BACKGROUND);
		
	}
	
}
