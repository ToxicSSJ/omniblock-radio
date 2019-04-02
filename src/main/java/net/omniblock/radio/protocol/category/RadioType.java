package net.omniblock.radio.protocol.category;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.omniblock.radio.config.TokenConfig;

import net.omniblock.radio.protocol.manager.controllers.MusicBotController;
import net.omniblock.radio.protocol.manager.data.RadioController;

public enum RadioType {

	ELECTRO_BOT(TokenConfig.ELECTRO_TOKEN, MusicBotController.class, "electro"),
	POP_BOT(TokenConfig.POP_TOKEN, MusicBotController.class, "pop"),
	ROCK_BOT(TokenConfig.ROCK_TOKEN, MusicBotController.class, "rock"),
	VARIADA_BOT(TokenConfig.VARIADA_TOKEN, MusicBotController.class, "variada"),
	HALLOWEEN_BOT(TokenConfig.HALLOWEEN_TOKEN, MusicBotController.class, "halloween")

	;
	
	private String token;
	private Class<? extends RadioController> controller;
	
	private String[] args;
	
	RadioType(String token, Class<? extends RadioController> controller, String...args){
		
		this.token = token;
		this.controller = controller;
		
		this.args = args;
		
	}

	public void startBot() {
		
		try {
			
			Constructor<?> ctor = controller.getConstructor();
			RadioController rcontrol = (RadioController) ctor.newInstance();
			
			rcontrol.start(this);
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Class<? extends RadioController> getController() {
		return controller;
	}

	public void setController(Class<? extends RadioController> controller) {
		this.controller = controller;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}
	
}
