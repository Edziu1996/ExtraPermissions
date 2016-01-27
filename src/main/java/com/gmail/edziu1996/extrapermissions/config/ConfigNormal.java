package com.gmail.edziu1996.extrapermissions.config;

import com.gmail.edziu1996.extrapermissions.manager.ConfigManager;

public class ConfigNormal extends ConfigManager
{
	public String formule = "<%player%> %message%";
	public String lang = "en_US";

	public ConfigNormal(String name)
	{
		super(name);
	}
	
	@Override
	public void populate()
	{
		get().getNode("chat", "formula")
		.setValue("%world%%rank_prefix%%player_prefix%%player%%player_suffix%%rank_suffix%: %message%")
		.setComment("%world% - world name; %rank_prefix%,%rank_suffix% - rank prefix and suffix; %player_prefix%,%player_suffix% - player suffix and prefix; %player% - player name; %message% - message");
		get().getNode("lang").setValue("en_US");
	}
	
	@Override
	public void init()
	{
		String temp = get().getNode("chat", "formula").getString();
		
		if (temp != null)
		{
			if (temp.length() > 0)
			{
				formule = temp;
			}
		}
		
		String temp2 = get().getNode("lang").getString();
		
		if (temp2 != null)
		{
			if (temp2.length() > 0)
			{
				lang = temp2;
			}
		}
	}

}
