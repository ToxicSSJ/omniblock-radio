package net.omniblock.radio.protocol.manager.data;

import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.omniblock.radio.protocol.category.RadioType;

public interface RadioController {

	public void start(RadioType type);
	
	public JDA getClient();
	
	public AudioSendHandler getAudioSendHandler();
	
	public AudioEventAdapter getAudioEventAdapter();
	
}
