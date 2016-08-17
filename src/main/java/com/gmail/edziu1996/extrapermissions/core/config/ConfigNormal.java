package com.gmail.edziu1996.extrapermissions.core.config;

import com.gmail.edziu1996.extrapermissions.api.ConfigManager;

public class ConfigNormal extends ConfigManager
{
	public String defaultRank = "user";
	public String formule = "<%player%> ";
	public String lang = "en_US";

	public ConfigNormal(String name)
	{
		super(name);
	}
	
	@Override
	public void populate()
	{
		get().getNode("chat", "formula")
		.setValue("%world%%rank_prefix%%player_prefix%%player%%player_suffix%%rank_suffix%: ")
		.setComment("%world% - world name; %rank_prefix%,%rank_suffix% - rank prefix and suffix; %player_prefix%,%player_suffix% - player suffix and prefix; %player% - player name");
		get().getNode("lang").setValue("en_US");
		get().getNode("defaultRank").setValue("user");
	}
	
	@Override
	public void init()
	{
		formule = check(get().getNode("chat", "formula").getString(), formule);
		lang = check(get().getNode("lang").getString(), lang);
		defaultRank = check(get().getNode("defaultRank").getString(), defaultRank);
	}
	
	private String check(String s1, String defaults)
	{
		if (s1 != null)
		{
			if (s1.length() > 0)
			{
				return s1;
			}
			else
			{
				return defaults;
			}
		}
		else
		{
			return defaults;
		}
	}

}
