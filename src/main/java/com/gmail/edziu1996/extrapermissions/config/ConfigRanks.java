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
		get().getNode("admin", "permissions", "extraperm.build").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.use").setValue(true);
		
		//Admin Group
		get().getNode("admin", "permissions", "extraperm.experm.group.admin.prefix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.admin.suffix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.admin.permission").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.admin.inheritance").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.admin.remove.prefix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.admin.remove.suffix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.admin.remove.permission").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.admin.remove.inheritance").setValue(true);
		
		//Default Group
		get().getNode("admin", "permissions", "extraperm.experm.group.default.prefix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.default.suffix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.default.permission").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.default.inheritance").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.default.remove.prefix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.default.remove.suffix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.default.remove.permission").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.group.default.remove.inheritance").setValue(true);
		
		get().getNode("admin", "permissions", "extraperm.experm.player.use").setValue(true);
		
		//Admin Player
		get().getNode("admin", "permissions", "extraperm.experm.player.admin.prefix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.admin.suffix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.admin.rank").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.admin.remove.prefix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.admin.remove.suffix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.admin.remove.rank").setValue(true);
		
		//Default Player
		get().getNode("admin", "permissions", "extraperm.experm.player.default.prefix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.default.suffix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.default.rank").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.default.remove.prefix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.default.remove.suffix").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.player.default.remove.rank").setValue(true);
		
		get().getNode("admin", "permissions", "extraperm.experm.info.use").setValue(true);
		get().getNode("admin", "permissions", "extraperm.experm.use").setValue(true);
	}
	
	@Override
	public void init()
	{
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
