package com.gmail.edziu1996.extrapermissions.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import com.gmail.edziu1996.extrapermissions.api.ConfigManager;
import com.gmail.edziu1996.extrapermissions.api.EPPlayer;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class ConfigPlayers extends ConfigManager
{
	public Map<String,Map<Object, ? extends CommentedConfigurationNode>> playersMap = new HashMap<String, Map<Object,? extends CommentedConfigurationNode>>();

	public ConfigPlayers(String name)
	{
		super(name);
	}
	
	public void clearMap()
	{
		playersMap.clear();
	}
	
	@Override
	public void init()
	{
		this.clearMap();
		
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

	public void loadByPlayer(UUID id)
	{
		String sid = id.toString();
		
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
	
	public void loadByPlayer(EPPlayer p)
	{
		loadByPlayer(p.getPlayer());
	}
	
	public void loadByPlayer(Player p)
	{
		loadByPlayer(p.getUniqueId());
	}
	
	
}
