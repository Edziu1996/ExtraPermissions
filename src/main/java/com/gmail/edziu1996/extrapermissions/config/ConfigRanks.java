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
		get().getNode("user", "permissions", "ExtraPerm.build").setValue(false);
		
		get().getNode("admin", "prefix").setValue("[Admin]");
		get().getNode("admin", "inheritance").setValue("user");
		get().getNode("admin", "permissions", "ExtraPerm.build").setValue(true);
		get().getNode("admin", "permissions", "ExtraPerm.experm.group").setValue(true);
		get().getNode("admin", "permissions", "ExtraPerm.experm.reload").setValue(true);
		get().getNode("admin", "permissions", "ExtraPerm.experm.uuid").setValue(true);
		get().getNode("admin", "permissions", "ExtraPerm.experm.player").setValue(true);
		get().getNode("admin", "permissions", "ExtraPerm.experm").setValue(true);
	}
	
	@Override
	public void init()
	{
		for (Entry<Object, ? extends CommentedConfigurationNode> e : get().getChildrenMap().entrySet())
		{
			ranksMap.put(e.getKey().toString(), e.getValue().getChildrenMap());
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