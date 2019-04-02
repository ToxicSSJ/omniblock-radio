package net.omniblock.radio.protocol.manager.controllers.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.omniblock.radio.protocol.manager.controllers.MusicBotController;

public class MusicBotAudioSendHandler implements AudioSendHandler {

	private final AudioPlayer audioPlayer;
	private final MusicBotController controller;
	
	private AudioFrame lastFrame;

	public MusicBotAudioSendHandler(AudioPlayer audioPlayer, MusicBotController controller) {
		
	    this.audioPlayer = audioPlayer;
	    this.controller = controller;
	    
	}

	@Override
	public boolean canProvide() {
		lastFrame = audioPlayer.provide();
		return lastFrame != null;
	}

	@Override
	public byte[] provide20MsAudio() {
		return lastFrame.getData();
	}

	@Override
	public boolean isOpus() {
		return true;
	}

	public MusicBotController getController() {
		return controller;
	}

}
