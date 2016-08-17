package com.gmail.edziu1996.extrapermissions.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.api.Game;

import com.gmail.edziu1996.extrapermissions.core.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigRanks;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class EPGroup
{
	ConfigRanks ranks = ExtraPermissions.getPlugin().ranksConf;
	Map<String, Map<Object, ? extends CommentedConfigurationNode>> ranksMap = ranks.ranksMap;
	Game game = ExtraPermissions.getPlugin().getGame();
	
	private final String groupName;
	//private EPGroup inheritance = null;
	private String inheritance = null;
	private Map<String, String> prefix = new HashMap<>();
	private Map<String, String> suffix = new HashMap<>();
	private Map<String, Map<String, Boolean>> permissions = new HashMap<>();
	
	public EPGroup(String name)
	{
		this.groupName = name;
		
		init();
		
		Manager.addGroup(this);
	}
	
	public void reload()
	{
		inheritance = null;
		prefix.clear();
		suffix.clear();
		permissions.clear();
		
		init();
	}
	
	private void init()
	{
		if (ranksMap.get(groupName).containsKey("inheritance"))
		{
			String inher = ranksMap.get(groupName).get("inheritance").getString();
			
			if (ranksMap.containsKey(inher))
			{
				if (!Manager.hasGroup(inher))
				{
					inheritance = new EPGroup(inher).getName();
				}
				else
				{
					inheritance = Manager.getGroup(inher).getName();
				}
			}
		}
		
		if (ranks.ranksMap.get(groupName).containsKey("suffix"))
		{
			suffix.put("default", ranks.ranksMap.get(groupName).get("suffix").getString());
		}
		
		if (ranks.ranksMap.get(groupName).containsKey("prefix"))
		{
			prefix.put("default", ranks.ranksMap.get(groupName).get("prefix").getString());
		}
		
		if (ranks.ranksMap.get(groupName).containsKey("world"))
		{
			if (ranks.ranksMap.get(groupName).get("world").hasMapChildren())
			{
				for (Entry<Object, ? extends CommentedConfigurationNode> e : ranks.ranksMap.get(groupName).get("world").getChildrenMap().entrySet())
				{
					String world = e.getKey().toString();
					
					if (e.getValue().hasMapChildren())
					{
						if (e.getValue().getChildrenMap().containsKey("suffix"))
						{
							suffix.put(world, e.getValue().getChildrenMap().get("suffix").getString());
						}
						
						if (e.getValue().getChildrenMap().containsKey("prefix"))
						{
							prefix.put(world, e.getValue().getChildrenMap().get("prefix").getString());
						}
					}
				}
			}
		}
		
		permissions = calculatePermissions(this);
	}
	
	private Map<String, Map<String, Boolean>> calculatePermissions(EPGroup epgroup)
	{
		Map<String, Map<String, Boolean>> map = new HashMap<>();
		
		String rank = epgroup.getName();
		
		if (ranksMap.containsKey(rank))
		{
			Map<Object, ? extends CommentedConfigurationNode> group = ranksMap.get(rank);
			
			if (epgroup.hasInheritance())
			{
				map = calculatePermissions(Manager.getGroup(epgroup.getInheritanceName()));
			}
			
			if (group.containsKey("permissions"))
			{
				if (group.get("permissions").hasMapChildren())
				{
					Map<Object, ? extends CommentedConfigurationNode> perm = group.get("permissions").getChildrenMap();
					
					for (Entry<Object, ? extends CommentedConfigurationNode> e : perm.entrySet())
					{
						Map<String, Boolean> temp = new HashMap<>();
						
						if (map.containsKey("default"))
						{
							temp = map.get("default");
							map.remove("default");
						}
						
						if (temp.containsKey(e.getKey().toString()))
						{
							temp.replace(e.getKey().toString(), e.getValue().getBoolean());
						}
						else
						{
							temp.put(e.getKey().toString(), e.getValue().getBoolean());
						}
						
						map.put("default", temp);
					}
				}
			}
			
			if (group.containsKey("worlds"))
			{
				if (group.get("worlds").hasMapChildren())
				{
					for (Entry<Object, ? extends CommentedConfigurationNode> s : group.get("worlds").getChildrenMap().entrySet())
					{
						String name = s.getKey().toString();
						
						if (s.getValue().hasMapChildren())
						{
							Map<Object, ? extends CommentedConfigurationNode> world = s.getValue().getChildrenMap();
							
							if (world.containsKey("permissions"))
							{
								Map<String, Boolean> temp = new HashMap<>();
								
								if (map.containsKey(name))
								{
									temp = map.get(name);
									map.remove(name);
								}
								
								for (Entry<Object, ? extends CommentedConfigurationNode> p : world.get("permissions").getChildrenMap().entrySet())
								{
									if (temp.containsKey(p.getKey().toString()))
									{
										temp.replace(p.getKey().toString(), p.getValue().getBoolean());
									}
									else
									{
										temp.put(p.getKey().toString(), p.getValue().getBoolean());
									}
								}
								
								map.put(name, temp);
							}
						}
					}
				}
			}
		}
		
		return map;
	}

	public Map<String, Boolean> getPermissions()
	{
		Map<String, Boolean> map = new HashMap<>();
		
		if (permissions.containsKey("default"))
		{
			map = permissions.get("default");
		}
		
		return map;
	}
	
	public Map<String, Map<String, Boolean>> getAllPermissions()
	{
		Map<String, Map<String, Boolean>> map = permissions;
		return map;
	}
	
	public Map<String, Boolean> getPermissionsWithWorld(String name)
	{
		Map<String, Boolean> map = getPermissions();
		
		if (permissions.containsKey(name))
		{
			for (Entry<String, Boolean> m : permissions.get(name).entrySet())
			{
				if (map.containsKey(m.getKey()))
				{
					map.replace(m.getKey(), m.getValue());
				}
				else
				{
					map.put(m.getKey(), m.getValue());
				}
			}
		}
		
		return map;
	}

	public boolean hasInheritance()
	{
		return inheritance != null ? true : false;
	}
	
	public String getInheritanceName()
	{
		return hasInheritance() ? inheritance : null;
	}
	
	public EPGroup getInheritance()
	{
		return hasInheritance() ? Manager.getGroup(getInheritanceName()) : null;
	}
	
	public boolean hasPrefix()
	{
		return prefix.containsKey("default");
	}
	
	public String getPrefix()
	{
		return hasPrefix() ? prefix.get("default") : null;
	}
	
	public Map<String, String> getPrefixes()
	{
		return prefix;
	}
	
	public boolean hasPrefixWithWorld(String worldName)
	{
		return prefix.containsKey(worldName);
	}
	
	public String getPrefixWithWorld(String worldName)
	{
		return hasPrefixWithWorld(worldName) ? prefix.get(worldName) : getPrefix();
	}
	
	public boolean hasSuffix()
	{
		return suffix.containsKey("default");
	}
	
	public String getSuffix()
	{
		return hasSuffix() ? suffix.get("default") : null;
	}
	
	public Map<String, String> getSuffixes()
	{
		return suffix;
	}
	
	public boolean hasSuffixWithWorld(String worldName)
	{
		return suffix.containsKey(worldName);
	}
	
	public String getSuffixWithWorld(String worldName)
	{
		return hasSuffixWithWorld(worldName) ? suffix.get(worldName) : getSuffix();
	}
	
	public boolean hasName()
	{
		return groupName != null ? true : false;
	}
	
	public String getName()
	{
		return hasName() ? groupName : null;
	}

	public boolean hasPermissions()
	{
		return !permissions.isEmpty();
	}
	
	public boolean hasPermissionsWithWorld(String world)
	{
		return !permissions.get(world).isEmpty();
	}
}
