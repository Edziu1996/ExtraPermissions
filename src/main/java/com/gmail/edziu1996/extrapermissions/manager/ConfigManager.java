package com.gmail.edziu1996.extrapermissions.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigManager
{
	private Path configFile;
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	private CommentedConfigurationNode configNode;
	
	public ConfigManager(String name)
	{
		this(name, ".conf");
	}
	
	public ConfigManager(String name, String suffix)
	{
		configFile = Paths.get(ExtraPermissions.getPlugin().getConfigDir() + "/" + name + suffix);
		configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();
	}
	
	public ConfigManager(String folder, String name, String suffix)
	{
		configFile = Paths.get(ExtraPermissions.getPlugin().getConfigDir() + "/" + folder + "/" + name + suffix);
		configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();
	}
	
	public void setup()
	{
		if (!Files.exists(configFile))
		{
			try
			{
				Files.createFile(configFile);
				load();
				populate();
				save();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			load();
		}
	}
	
	public void load()
	{
		try
		{
			configNode = configLoader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		init();
	}
	
	public void init() {}

	public void save()
	{
		try
		{
			configLoader.save(configNode);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void populate() {}
	
	public CommentedConfigurationNode get()
	{
		return configNode;
	}
}
