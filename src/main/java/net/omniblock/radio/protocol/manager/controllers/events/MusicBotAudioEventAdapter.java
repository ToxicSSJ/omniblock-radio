package net.omniblock.radio.protocol.manager.controllers.events;

import java.util.Collections;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.omniblock.radio.protocol.manager.console.Console;
import net.omniblock.radio.protocol.manager.controllers.MusicBotController;

public class MusicBotAudioEventAdapter extends AudioEventAdapter {

    private final AudioPlayer player;
    private final MusicBotController controller;
    
    protected boolean started = false;
    
    public MusicBotAudioEventAdapter(AudioPlayer player, MusicBotController controller) {
    	
        this.player = player;
        this.controller = controller;
        
    }

    public void add(AudioTrack track) {

        if (player.getPlayingTrack() == null) {
            player.playTrack(track);
        }
        
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
    	
    	if (!started) {

			started = true;

			Console.WRITTER.printInfo("Se ha iniciado el bot " + controller.botNames().get(controller.type) + "!");
			return;

		}
    	
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        
    	if ((controller.getSongPos() + 1) >= controller.getSongList().size()) {

			controller.setSongPos(0);
			Collections.shuffle(controller.getSongList());

		} else { controller.setSongPos(controller.getSongPos() + 1); }
    	
		controller.playNextSong();
		return;
    	
    }

	public MusicBotController getController() {
		return controller;
	}
    
}
