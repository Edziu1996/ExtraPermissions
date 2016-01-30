package com.gmail.edziu1996.extrapermissions.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.api.entity.living.player.Player;

import com.gmail.edziu1996.extrapermissions.manager.ConfigManager;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class ConfigPlayers extends ConfigManager
{
	public Map<String,Map<Object, ? extends CommentedConfigurationNode>> playersMap = new HashMap<String, Map<Object,? extends CommentedConfigurationNode>>();

	public ConfigPlayers(String name)
	{
		super(name);
	}
	
	@Override
	public void init()
	{
		for (Entry<Object, ? extends CommentedConfigurationNode> e : get().getChildrenMap().entrySet())
		{
			if (!e.getValue().getChildrenMap().isEmpty())
			{
				playersMap.put(e.getKey().toString(), e.getValue().getChildrenMap());
			}
			else
			{
				get().removeChild(e.getKey());
			}
		}
	}

	public void loadByPlayer(Player p)
	{
		String sid = p.getUniqueId().toString();
		
		if (get().hasMapChildren())
		{
			if (get().getChildrenMap().containsKey(sid))
			{
				playersMap.remove(sid);
				playersMap.put(sid, get().getNode(sid).getChildrenMap());
			}
			else
			{
				playersMap.remove(sid);
			}
		}
	}
}
