package com.gmail.edziu1996.extrapermissions;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import com.gmail.edziu1996.extrapermissions.config.ConfigLang;
import com.gmail.edziu1996.extrapermissions.config.ConfigNormal;
import com.gmail.edziu1996.extrapermissions.config.ConfigPlayers;
import com.gmail.edziu1996.extrapermissions.config.ConfigRanks;
import com.gmail.edziu1996.extrapermissions.manager.CommandsManager;
import com.gmail.edziu1996.extrapermissions.manager.EventManager;
import com.google.inject.Inject;

@Plugin(id=ExtraPermissions.PluginInfo.ID,name=ExtraPermissions.PluginInfo.NAME,version=ExtraPermissions.PluginInfo.VERSION,dependencies=ExtraPermissions.PluginInfo.DEPENDENCIES)
public class ExtraPermissions
{
	@Inject
	private Logger logger;
	
	private static ExtraPermissions plugin;
	
	@Inject
	@ConfigDir(sharedRoot=false)
	private Path configDir;
	
	Game game = Sponge.getGame();
	
	public ConfigRanks ranksConf;
	public ConfigPlayers playersConf;
	public ConfigNormal config;
	public ConfigLang langConf;
	
	@Listener
	public void preInit(GamePreInitializationEvent event)
	{
		plugin = this;
		
		ranksConf = new ConfigRanks("ranks");
		ranksConf.setup();
		
		playersConf = new ConfigPlayers("players");
		playersConf.setup();
		
		config = new ConfigNormal("config");
		config.setup();
		
		langConf = new ConfigLang("lang", config.lang, ".lang");
		langConf.setup();
	}
	
	@Listener
	public void init(GameInitializationEvent event)
	{
		game.getEventManager().registerListeners(this, new EventManager());
		CommandsManager.load(game);
	}
	
	@Listener
	public void postInit(GamePostInitializationEvent event) {}
	
	@Listener
	public void onServerStart(GameStartedServerEvent event)
	{
		logger.info("This plugin has started!");
	}
	
	@Listener
	public void onServerStop(GameStoppedServerEvent event)
	{
		logger.info("This plugin has stopped!");
	}
	
	public static ExtraPermissions getPlugin()
	{
		return plugin;
	}
	
	public Path getConfigDir()
	{
		return configDir;
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public class PluginInfo
	{
		public static final String ID = "ExtraPerm";
		public static final String NAME = "ExtraPermissions";
		public static final String VERSION = "0.3.0";
		public static final String DEPENDENCIES = "required-after:NameAPI@[0.2.0,)";
	}
}
