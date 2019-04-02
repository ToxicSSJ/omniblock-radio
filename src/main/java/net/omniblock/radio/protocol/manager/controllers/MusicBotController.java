package net.omniblock.radio.protocol.manager.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.managers.AudioManager;
import net.omniblock.radio.config.ClientConfig;
import net.omniblock.radio.protocol.category.PlayResult;
import net.omniblock.radio.protocol.category.RadioType;
import net.omniblock.radio.protocol.manager.console.Console;
import net.omniblock.radio.protocol.manager.controllers.events.MusicBotAudioEventAdapter;
import net.omniblock.radio.protocol.manager.controllers.handler.MusicBotAudioSendHandler;
import net.omniblock.radio.protocol.manager.data.RadioController;
import net.omniblock.radio.protocol.manager.utils.ColorUtils;

public class MusicBotController implements RadioController, EventListener {

	public RadioType type;
	public JDA client;

	protected Guild guild;
	protected TextChannel textChannel;
	protected VoiceChannel voiceChannel;

	protected Message sendedMessage;
	protected EmbedBuilder offlineBuilder;

	protected File configFile, songsFile;
	protected JSONObject jsonConfigObject;

	protected List<String> songsList;
	protected Integer songPos;

	protected AudioSendHandler audioSendHandler;
	protected AudioEventAdapter audioEventAdapter;

	protected AudioPlayerManager playerManager;
	protected AudioPlayer audioPlayer;

	public void start(RadioType type) {

		if (validTypes().contains(type)) {

			this.type = type;
			
			this.configFile = configFiles().get(type);
			this.songsFile = playListsFiles().get(type);
			
			this.configDefaults();
			this.songsDefaults();

			try {

				this.client = new JDABuilder(AccountType.BOT).setStatus(OnlineStatus.DO_NOT_DISTURB)
						.setGame(Game.playing("http://www.omniblock.net/"))
						.setToken(type.getToken())
						.buildAsync();

			} catch (LoginException | IllegalArgumentException e) {
				e.printStackTrace();
			}

			this.client.addEventListener(this);

			playerManager = new DefaultAudioPlayerManager();
			AudioSourceManagers.registerRemoteSources(playerManager);

			audioPlayer = playerManager.createPlayer();
			audioPlayer.addListener(getAudioEventAdapter());

			audioSendHandler = new MusicBotAudioSendHandler(audioPlayer, this);
			
		}

	}

	public void setMsgID(String id) {

		this.jsonConfigObject.put("msgid", id);
		saveConfig();
		return;

	}

	public String getMsgID() {

		return (String) this.jsonConfigObject.get("msgid");

	}

	public void songsDefaults() {

		if (!this.songsFile.exists()) {

			try {

				FileUtils.openOutputStream(this.songsFile);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		songsList = readFileSongs();
		return;

	}

	public void configDefaults() {
		
		if (!this.configFile.exists()) {
			
			this.jsonConfigObject = new JSONObject();
			this.jsonConfigObject.put("msgid", "none");

			saveConfig();
			return;

		}

		loadConfig();
		return;

	}

	private List<String> readFileSongs() {

		try {
			return FileUtils.readLines(this.songsFile, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<String>();

	}

	private void saveConfig() {

		try {

			FileUtils.openOutputStream(this.configFile);

			try (FileWriter file = new FileWriter(this.configFile.getPath())) {
				file.write(jsonConfigObject.toString());
			}

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	private void loadConfig() {

		try {

			InputStream stream = FileUtils.openInputStream(this.configFile);
			String jsonTxt = IOUtils.toString(stream, "UTF-8");

			this.jsonConfigObject = new JSONObject(jsonTxt);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void startStream() {

		if (songsList.size() <= 0) {

			Console.WRITTER.printError("No se pueden reproducir canciones porque no se han añadido en la lista "
					+ playListsFiles().get(type).getName() + "!");
			return;

		}

		if (voiceChannel != null) {

			if (!getPlayer().isConnected())
				getPlayer().openAudioConnection(voiceChannel);

			getPlayer().setSendingHandler(audioSendHandler);

			Collections.shuffle(songsList);

			songPos = 0;
			playNextSong();
			return;

		} else {

			Console.WRITTER.printError("El canal de voz fue eliminado!");
			return;

		}

	}

	public void playNextSong() {
		
		String url = songsList.get(songPos);
		PlayResult result = playFromYouTube(url);

		if (result == PlayResult.CANNOT_PLAYED) {

			songsList.remove(url);
			playNextSong();
			return;

		}

	}

	private PlayResult playFromYouTube(final String url) {
		
		if(voiceChannel != null) {
			
			try {
				
				playerManager.loadItem(url, new AudioLoadResultHandler() {
					
					  @Override
					  public void trackLoaded(AudioTrack track) {
						  
						  sendedMessage.editMessage(
								  new EmbedBuilder().setColor(ColorUtils.getRandomColor())
									.setAuthor(botNames().get(type), null, botImageUrls().get(type))
									.setDescription("✅ **__Estado:__** Transmisión online! \n \n"
											+ "Radio oficial de Omniblock Network para el genero músical "
											+ musicalGenres().get(type).toLowerCase()
											+ ", la reproducción automatica predefinida está activada en este bot! \n"
											+ "\n**💿 Actualmente Reproduciendo:** \n \n"
											+ "**Titulo:** + " + track.getInfo().title + " \n" + "**Canal:** " + track.getInfo().author + " \n" + "**Link:** " + url + " \n")
									.setFooter(
											"🎶 Esta transmisión proviene de videos de youtube, Omniblock Network solo hace uso del contenido reproducible.",
											null)
									.setTimestamp(ZonedDateTime.now()).build()
								  ).complete();
						  
						  ((MusicBotAudioEventAdapter) audioEventAdapter).add(track);
						  
					  }

					  @Override
					  public void loadFailed(FriendlyException throwable) {
						  
						  if ((getSongPos() + 1) >= getSongList().size()) {

								setSongPos(0);
								Collections.shuffle(getSongList());

						  } else { setSongPos(getSongPos() + 1); }
					    	
						  playNextSong();
						  return;
						  
					  }

					  @Override
					  public void noMatches() { 
						  
						  if ((getSongPos() + 1) >= getSongList().size()) {

								setSongPos(0);
								Collections.shuffle(getSongList());

						  } else { setSongPos(getSongPos() + 1); }
					    	
						  playNextSong();
						  return;
						  
					  }
					  
					  @Override
					  public void playlistLoaded(AudioPlaylist playlist) {
						  
						  if ((getSongPos() + 1) >= getSongList().size()) {

								setSongPos(0);
								Collections.shuffle(getSongList());

						  } else { setSongPos(getSongPos() + 1); }
					    	
						  playNextSong();
						  return;
						  
					  }
					  
			    });
				
			    if(!getPlayer().isConnected())
			    	getPlayer().openAudioConnection(voiceChannel);
			    
			    return PlayResult.PLAYED;
			    
			} catch (Exception e) {
				
				e.printStackTrace();
			    Console.WRITTER.printWarning("No se pudo añadir el audio!");
			    
			}
			
		} else { 
			
			Console.WRITTER.printError("El canal fue eliminado o aún el bot no se ha conectado en él!");
			return PlayResult.NOT_JOINED;
			
		}
        
        return PlayResult.CANNOT_PLAYED;
    }

	@Override
	public void onEvent(Event event) {

		if (event instanceof ReadyEvent) {

			textChannel = client.getTextChannelById(Long.parseLong(textChannel()));
			voiceChannel = client.getVoiceChannelById(Long.parseLong(voiceChannels().get(type)));
			guild = client.getGuildById(Long.parseLong(ClientConfig.VALID[0]));

			offlineBuilder = new EmbedBuilder().setColor(ColorUtils.hex2Rgb("#ff0026"))
					.setAuthor(botNames().get(type), null, botImageUrls().get(type))
					.setDescription("✖ **__Estado:__** Inicializando la transmisión... \n \n"
							+ "Radio oficial de Omniblock Network para el genero músical "
							+ musicalGenres().get(type).toLowerCase()
							+ ", la reproducción automatica predefinida está activada en este bot! \n")
					.addField("Actualmente Reproduciendo: \n",
							"**Titulo:** Omniblock Network \n" + "**Canal:** omniblockyoutube \n"
									+ "**Link:** http://www.omniblock.net/ \n",
							true)
					.setFooter(
							"🎶 Las transmisiones pueden tardar cierto tiempo en ser iniciadas, todo depende del estado del servicio.",
							null)
					.setTimestamp(ZonedDateTime.now());

			if (textChannel == null && voiceChannel == null)
				throw new RuntimeException(
						"El bot " + botNames().get(type) + " no se encuentra en el servidor registrado!");

			if (getMsgID().equalsIgnoreCase("none")) {

				Console.WRITTER.printWarning("No se ha encontrado la ID del mensaje, se creará uno para su remplazo.");

				sendedMessage = textChannel.sendMessage(offlineBuilder.build()).complete();
				setMsgID(sendedMessage.getId());

			}

			if (sendedMessage == null) {

				sendedMessage = textChannel.getMessageById(Long.parseLong(getMsgID())).complete();

				if (sendedMessage == null) {

					sendedMessage = textChannel.sendMessage(offlineBuilder.build()).complete();
					setMsgID(sendedMessage.getId());

				}

				setMsgID(sendedMessage.getId());

			}

			startStream();
			return;

		}

	}

	@SuppressWarnings("serial")
	public Map<RadioType, String> voiceChannels() {

		return new HashMap<RadioType, String>() {
			{
				put(RadioType.ELECTRO_BOT, "363887411127910401");
				put(RadioType.POP_BOT, "363887368727560192");
				put(RadioType.ROCK_BOT, "363887452311519234");
				put(RadioType.VARIADA_BOT, "363897347241148418");
				put(RadioType.HALLOWEEN_BOT, "496190325614837770");

			}
		};

	}

	@SuppressWarnings("serial")
	public Map<RadioType, String> botNames() {

		return new HashMap<RadioType, String>() {
			{
				put(RadioType.ELECTRO_BOT, "Omniblock Radio Electronica");
				put(RadioType.POP_BOT, "Omniblock Radio Pop");
				put(RadioType.ROCK_BOT, "Omniblock Radio Rock");
				put(RadioType.VARIADA_BOT, "Omniblock Radio Variada");
				put(RadioType.HALLOWEEN_BOT, "Omniblock Radio Halloween");

			}
		};

	}

	@SuppressWarnings("serial")
	public Map<RadioType, String> musicalGenres() {

		return new HashMap<RadioType, String>() {
			{
				put(RadioType.ELECTRO_BOT, "Electronica");
				put(RadioType.POP_BOT, "Pop");
				put(RadioType.ROCK_BOT, "Rock");
				put(RadioType.VARIADA_BOT, "Variada");
				put(RadioType.VARIADA_BOT, "con temática de Halloween");

			}
		};

	}

	@SuppressWarnings("serial")
	public Map<RadioType, String> botImageUrls() {

		return new HashMap<RadioType, String>() {
			{
				put(RadioType.ELECTRO_BOT, "https://cdn.pbrd.co/images/GNk0bUc.png");
				put(RadioType.POP_BOT, "https://cdn.pbrd.co/images/GN8MTzZ.png");
				put(RadioType.ROCK_BOT, "https://cdn.pbrd.co/images/GNk1mri.png");
				put(RadioType.VARIADA_BOT, "https://cdn.pbrd.co/images/GNk1xoy.png");
				put(RadioType.HALLOWEEN_BOT, "https://image.spreadshirtmedia.net/image-server/v1/mp/designs/15178108,width=178,height=178/8-bit-pixel-jack-o-lantern-pumpkin.png");

			}
		};

	}

	@SuppressWarnings("serial")
	public Map<RadioType, File> playListsFiles() {

		return new HashMap<RadioType, File>() {
			{
				put(RadioType.ELECTRO_BOT, new File("config/songs/electro.txt"));
				put(RadioType.POP_BOT, new File("config/songs/pop.txt"));
				put(RadioType.ROCK_BOT, new File("config/songs/rock.txt"));
				put(RadioType.VARIADA_BOT, new File("config/songs/variada.txt"));
				put(RadioType.HALLOWEEN_BOT, new File("config/songs/halloween.txt"));

			}
		};

	}

	@SuppressWarnings("serial")
	public Map<RadioType, File> configFiles() {

		return new HashMap<RadioType, File>() {
			{
				put(RadioType.ELECTRO_BOT, new File("config/electro.json"));
				put(RadioType.POP_BOT, new File("config/pop.json"));
				put(RadioType.ROCK_BOT, new File("config/rock.json"));
				put(RadioType.VARIADA_BOT, new File("config/variada.json"));
				put(RadioType.HALLOWEEN_BOT, new File("config/halloween.json"));

			}
		};

	}
	
	public List<RadioType> validTypes() {

		return Arrays.asList(RadioType.ELECTRO_BOT, RadioType.POP_BOT, RadioType.ROCK_BOT, RadioType.VARIADA_BOT, RadioType.HALLOWEEN_BOT);

	}

	public String textChannel() {

		return "363887635610992641";

	}

	public AudioManager getPlayer() {
		return guild.getAudioManager();
	}

	public JDA getClient() {
		return client;
	}

	@Override
	public AudioEventAdapter getAudioEventAdapter() {

		if (audioEventAdapter == null)
			audioEventAdapter = new MusicBotAudioEventAdapter(audioPlayer, this);

		return audioEventAdapter;

	}

	@Override
	public AudioSendHandler getAudioSendHandler() {

		if (audioSendHandler == null)
			audioSendHandler = new MusicBotAudioSendHandler(audioPlayer, this);

		return audioSendHandler;

	}

	public List<String> getSongList(){
		return songsList;
	}
	
	public Integer getSongPos(){
		return songPos;
	}
	
	public void setSongPos(int newSongPos) {
		songPos = newSongPos;
		return;
	}
	
}
