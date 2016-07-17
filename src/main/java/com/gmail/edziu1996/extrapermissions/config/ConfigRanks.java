package com.gmail.edziu1996.extrapermissions.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.gmail.edziu1996.extrapermissions.manager.ConfigManager;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class ConfigRanks extends ConfigManager
{
	public Map<String, Map<Object, ? extends CommentedConfigurationNode>> ranksMap = new HashMap<String, Map<Object, ? extends CommentedConfigurationNode>>();
	
	public ConfigRanks(String name)
	{
		super(name);
	}
	
	@Override
	public void populate()
	{
		get().getNode("user", "prefix").setValue("[User]");
		get().getNode("user", "permissions", "extraperm.build").setValue(false);
		get().getNode("user", "default").setValue(true);
		
		get().getNode("admin", "prefix").setValue("[Admin]");
		get().getNode("admin", "inheritance").setValue("user");
		get().getNode("admin", "permissions", "extraperm").setValue(true);
	}
	
	public void clearMap()
	{
		ranksMap.clear();
	}
	
	@Override
	public void init()
	{
		this.clearMap();
		
		for (Entry<Object, ? extends CommentedConfigurationNode> e : get().getChildrenMap().entrySet())
		{
			if (!e.getValue().getChildrenMap().isEmpty())
			{
				ranksMap.put(e.getKey().toString(), e.getValue().getChildrenMap());
			}
			else
			{
				get().removeChild(e.getKey());
			}
		}
	}

	public void loadByRank(String name)
	{
		if (get().hasMapChildren())
		{
			if (get().getChildrenMap().containsKey(name))
			{
				ranksMap.remove(name);
				ranksMap.put(name, get().getNode(name).getChildrenMap());
			}
			else
			{
				ranksMap.remove(name);
			}
		}
	}
}
