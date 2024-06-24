package com.loot;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.sound.sampled.*;
import javax.swing.*;

import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@PluginDescriptor(
	name = "Lens Loot"
)
public class LootPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private LootConfig config;
	private Clip clip = null;

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
	}
	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if(event.getMessage().equals("You find some treasure in the chest!")){
			if(client.getLocalPlayer().getWorldLocation().getRegionID()==12127)
			{
				try {
					clip = AudioSystem.getClip();
					InputStream stream = new BufferedInputStream(new FileInputStream(new File("src/main/resources/sound/loot.wav")));
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream);
					clip.open(audioInputStream);
					FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					volume.setValue(-15f);
					clip.loop(0);
					clip.addLineListener(new LineListener() {
						public void update(LineEvent myLineEvent) {
							if (myLineEvent.getType() == LineEvent.Type.STOP){
								clip.close();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Provides
	LootConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LootConfig.class);
	}
}