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
	public void populate()
	{
		get().getNode("hea6c72c-06d2-4cv6-a45d-bae23947b6f1", "rank").setValue("user");
		get().getNode("hea6c72c-06d2-4cv6-a45d-bae23947b6f1", "suffix").setValue(" : ");
		get().getNode("hea6c72c-06d2-4cv6-a45d-bae23947b6f1", "prefix").setValue(" [User] ");
	}
	
	@Override
	public void init()
	{
		for (Entry<Object, ? extends CommentedConfigurationNode> e : get().getChildrenMap().entrySet())
		{
			playersMap.put(e.getKey().toString(), e.getValue().getChildrenMap());
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
