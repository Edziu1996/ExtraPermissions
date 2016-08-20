package com.gmail.edziu1996.extrapermissions.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.api.Game;

import com.gmail.edziu1996.extrapermissions.api.ConfigManager;
import com.gmail.edziu1996.extrapermissions.core.ExtraPermissions;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class ConfigRanks extends ConfigManager
{
	public Map<String, Map<Object, ? extends CommentedConfigurationNode>> ranksMap = new HashMap<String, Map<Object, ? extends CommentedConfigurationNode>>();
	Game game = ExtraPermissions.getPlugin().getGame();
	
	public ConfigRanks(String name)
	{
		super(name);
	}
	
	@Override
	public void populate()
	{
		get().getNode("user", "prefix").setValue("[User]");
		get().getNode("user", "permissions", "extraperm.build").setValue(false);
		get().getNode("user", "worlds", "world", "permissions", "extraperm.build").setValue(true);
		get().getNode("user", "worlds", "DIM-1", "prefix").setValue("[User in Nether]");
		get().getNode("user", "worlds", "DIM1", "prefix").setValue("[User in The End]");
		
		get().getNode("admin", "prefix").setValue("[Admin]");
		get().getNode("admin", "inheritance").setValue("user");
		get().getNode("admin", "permissions", "extraperm").setValue(true);
		get().getNode("admin", "permissions", "extraperm.build").setValue(true);
		get().getNode("admin", "worlds", "DIM-1", "prefix").setValue("[Admin in Nether]");
		get().getNode("admin", "worlds", "DIM1", "prefix").setValue("[Admin in The End]");
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
