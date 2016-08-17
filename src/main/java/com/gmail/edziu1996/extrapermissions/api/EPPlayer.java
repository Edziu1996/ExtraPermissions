package com.gmail.edziu1996.extrapermissions.api;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;

import com.gmail.edziu1996.extrapermissions.core.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigPlayers;
import com.gmail.edziu1996.nameapi.NameAPI;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class EPPlayer
{
	private ConfigPlayers players = ExtraPermissions.getPlugin().playersConf;
	private static Game game = ExtraPermissions.getPlugin().getGame();
	
	private Player player;
	private final UUID id;
	private final String name;
	private String group;
	private String lastGroup = null;
	private boolean rankTimed = false;
	private long rankTime = 0;
	private Map<String, String> prefix = new HashMap<>();
	private Map<String, String> suffix = new HashMap<>();
	private Map<String, Map<String, Boolean>> permissions = new HashMap<>();
	
	public EPPlayer(String name)
	{
		this(NameAPI.getPlugin().getPlayerUUIDFromName(name, game));
	}
	
	public EPPlayer(UUID id)
	{
		this.id = id;
		
		name = NameAPI.getPlugin().getPlayerNameFormUUID(id, game);
		
		init();
		
		Manager.replacePlayer(this);
	}
	
	public EPPlayer(Player p)
	{
		player = p;
		id = player.getUniqueId();
		name = player.getName();
		
		init();
		
		Manager.replacePlayer(this);
	}
	
	public boolean isOnline()
	{
		boolean temp = false;
		
		if (game.getServer().getOnlinePlayers().contains(this.getPlayer()))
		{
			temp = true;
		}
		
		return temp;
	}
	
	public void reload()
	{
		group = null;
		lastGroup = null;
		rankTimed = false;
		rankTime = 0;
		prefix.clear();
		suffix.clear();
		permissions.clear();
		
		init();
	}
	
	private void init()
	{
		String sid = id.toString();
		
		Map<Object, ? extends CommentedConfigurationNode> play = players.playersMap.get(sid);
		
		checkLastRank();
		
		if (play.containsKey("lastRank"))
		{
			lastGroup = play.get("lastRank").getString();
		}
		
		if (play.containsKey("rankTime"))
		{
			rankTime = play.get("rankTime").getLong();
		}
		
		if (play.containsKey("rankTimed"))
		{
			rankTimed = play.get("rankTimed").getBoolean();
		}
		
		if (play.containsKey("rank"))
		{
			group = play.get("rank").getString();
		}
		else
		{
			group = Manager.getDefaultGroup().getName();
		}
		
		if (play.containsKey("suffix"))
		{
			suffix.put("default", play.get("suffix").getString());
		}
		
		if (play.containsKey("prefix"))
		{
			prefix.put("default", play.get("prefix").getString());
		}
		
		if (play.containsKey("worlds"))
		{
			if (play.get("worlds").hasMapChildren())
			{
				for (Entry<Object, ? extends CommentedConfigurationNode> s : play.get("worlds").getChildrenMap().entrySet())
				{
					String world = s.getKey().toString();
					
					if (s.getValue().hasMapChildren())
					{
						if (s.getValue().getChildrenMap().containsKey("suffix"))
						{
							suffix.put(world, s.getValue().getChildrenMap().get("suffix").getString());
						}
						
						if (s.getValue().getChildrenMap().containsKey("prefix"))
						{
							suffix.put(world, s.getValue().getChildrenMap().get("prefix").getString());
						}
					}
				}
			}
		}
		
		permissions = calculatePermissions();
	}
	
	private Map<String, Map<String, Boolean>> calculatePermissions()
	{
		Map<String, Map<String, Boolean>> map = new HashMap<>();
		
		if (Manager.hasGroup(group))
		{
			map = Manager.getGroup(group).getAllPermissions();
		}
		
		String sid = this.getUUIDString();
		
		Map<Object, ? extends CommentedConfigurationNode> play = players.playersMap.get(sid);
		
		if (play.containsKey("permissions"))
		{
			if (play.get("permissions").hasMapChildren())
			{
				Map<String, Boolean> temp = new HashMap<>();
				
				if (map.containsKey("default"))
				{
					temp = map.get("default");
					map.remove("default");
				}
				
				for (Entry<Object, ? extends CommentedConfigurationNode> s : play.get("permissions").getChildrenMap().entrySet())
				{
					if (temp.containsKey(s.getKey().toString()))
					{
						temp.replace(s.getKey().toString(), s.getValue().getBoolean());
					}
					else
					{
						temp.put(s.getKey().toString(), s.getValue().getBoolean());
					}
				}
				
				map.put("default", temp);
			}
		}
		
		if (play.containsKey("worlds"))
		{
			if (play.get("worlds").hasMapChildren())
			{
				for (Entry<Object, ? extends CommentedConfigurationNode> s : play.get("worlds").getChildrenMap().entrySet())
				{
					String world = s.getKey().toString();
					
					if (s.getValue().hasMapChildren())
					{
						if (s.getValue().getChildrenMap().containsKey("permissions"))
						{
							CommentedConfigurationNode perm = s.getValue().getChildrenMap().get("permissions");
							
							if (perm.hasMapChildren())
							{
								Map<String, Boolean> temp = new HashMap<>();
								
								if (map.containsKey(world))
								{
									temp = map.get(world);
									map.remove(world);
								}
								
								for (Entry<Object, ? extends CommentedConfigurationNode> p : perm.getChildrenMap().entrySet())
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
								
								map.put(world, temp);
							}
						}
					}
				}
			}
		}
		
		return map;
	}

	private void checkLastRank()
	{
		if (hasRankTime() && rankTimed())
		{
			long time = getRankTime();
			long curTime = GregorianCalendar.getInstance().getTime().getTime();
			
			String sid = this.getUUID().toString();
			
			if (hasLastGroup())
			{
				String lastRank = players.playersMap.get(sid).get("lastRank").getString();
				
				players.get().getNode(sid, "rank").setValue(lastRank);
			}
			
			if (time < curTime)
			{
				players.get().getNode(sid).removeChild("lastRank");
				players.get().getNode(sid).removeChild("rankTime");
				players.get().getNode(sid).removeChild("rankTimed");
				
				if (!players.get().getNode(sid).hasMapChildren())
				{
					players.get().removeChild(sid);
				}
			}
			
			if (!players.get().getNode(sid).hasMapChildren())
			{
				players.get().removeChild(sid);
			}
			
			players.save();
			players.loadByPlayer(id);
			
		}
	}

	public Player getPlayer()
	{
		return player;
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
	
	public Map<String, Boolean> getPermissionsWithWorld(String worldName)
	{
		Map<String, Boolean> map = getPermissions();
		
		if (permissions.containsKey(worldName))
		{
			for (Entry<String, Boolean> perm : permissions.get(worldName).entrySet())
			{
				if (map.containsKey(perm.getKey()))
				{
					map.replace(perm.getKey(), perm.getValue());
				}
				else
				{
					map.put(perm.getKey(), perm.getValue());
				}
			}
		}
		
		
		return map;
	}
	
	public String getGroupName()
	{
		String rank = this.getGroup().getName();
		
		return rank;
	}
	
	public EPGroup getGroup()
	{
		return Manager.getGroup(group);
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

	public boolean hasLastGroup()
	{
		return lastGroup != null ? true : false;
	}
	
	public String getLastGroup()
	{
		return hasLastGroup() ? lastGroup : null;
	}
	
	public boolean hasRankTime()
	{
		return leftTime() >= 0;
	}
	
	public long getRankTime()
	{
		return hasRankTime() ? rankTime : 0;
	}
	
	public long leftTime()
	{
		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
		long time = cal.getTime().getTime();
		
		long leftTime = rankTime-time;
		return leftTime;
	}
	
	public boolean rankTimed()
	{
		return rankTimed;
	}

	public UUID getUUID()
	{
		return id;
	}

	public String getUUIDString()
	{
		return id.toString();
	}
	
	public boolean hasPermissions()
	{
		return !permissions.isEmpty();
	}
	
	public boolean hasPermissionsWithWorld(String world)
	{
		return !permissions.get(world).isEmpty();
	}
	
	public String getName()
	{
		return name;
	}
}
