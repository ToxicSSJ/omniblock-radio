package net.omniblock.radio.protocol.manager.console.writter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.omniblock.radio.protocol.manager.console.color.ConsoleColorUtil;

public class ConsoleWritter {

	protected Boolean write = true;
	
	public ConsoleWritter() { }
	
	public void printLines(String...lines) {

		for(String text : lines){
			
			text = ConsoleColorUtil.translateColors(text, '&');
			logText(text, true);
			continue;
			
		}
		
		return;
		
	}
	
	public void printLine(String text) {

		text = ConsoleColorUtil.translateColors(text, '&');
		logText(text, true);
		return;
		
	}
	
	public void printLine(String text, boolean color) {

		text = color ? ConsoleColorUtil.translateColors(text, '&') : text;
		logText(text, true);
		return;
		
	}
	
	public void printLine(String text, boolean color, boolean formatted) {

		text = color ? ConsoleColorUtil.translateColors(text, '&') : text;
		logText(text, formatted);
		return;
		
	}
	
	public void printInfo(String text) {
		
		String prefix = ConsoleColorUtil.translateColors("&bINFO&r", '&');
		logText(prefix + " " + ConsoleColorUtil.translateColors(text, '&'), true);
		return;
		
	}
	
	public void printError(String text) {
		
		String prefix = ConsoleColorUtil.translateColors("&cERROR&r", '&');
		logText(prefix + " " + ConsoleColorUtil.translateColors(text, '&'), true);
		return;
		
	}
	
	public void printWarning(String text) {
		
		String prefix = ConsoleColorUtil.translateColors("&eADVERT&r", '&');
		logText(prefix + " " + ConsoleColorUtil.translateColors(text, '&'), true);
		return;
		
	}
	
	protected void logText(String text, boolean timeformat) {
		
		if(timeformat) {
			String time = getTimeFormat();
			System.out.println(time + " " + text);
			return;
		}
		
		System.out.println(text);
		
	}
	
	private String getTimeFormat() {
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		
		return "[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "]";
		
	}
	
}
