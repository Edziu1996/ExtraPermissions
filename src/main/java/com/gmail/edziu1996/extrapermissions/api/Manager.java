package com.gmail.edziu1996.extrapermissions.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;

import com.gmail.edziu1996.extrapermissions.core.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigPlayers;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigRanks;
import com.gmail.edziu1996.extrapermissions.core.manager.RanksManager;
import com.gmail.edziu1996.nameapi.NameAPI;

public class Manager
{
	private static Game game = ExtraPermissions.getPlugin().getGame();
	private static String defaultGroup = ExtraPermissions.getPlugin().config.defaultRank;
	private static RanksManager rm = new RanksManager();
	private static ConfigRanks ranksConf = ExtraPermissions.getPlugin().ranksConf;
	private static ConfigPlayers playersConf = ExtraPermissions.getPlugin().playersConf;
	private static Map<String, EPGroup> groups = new HashMap<>();
	private static Map<String, EPPlayer> players = new HashMap<>();
	
	public static void init()
	{
		initGroup();
		initPlayers();
	}
	
	public static void initGroup()
	{
		for (String rank : ranksConf.ranksMap.keySet())
		{
			new EPGroup(rank);
		}
	}
	
	public static void initPlayers()
	{
		
		for (String player : playersConf.playersMap.keySet())
		{
			new EPPlayer(UUID.fromString(player));
		}
	}
	
	public static void load()
	{
		for (String rank : ranksConf.ranksMap.keySet())
		{
			if (!hasGroup(rank))
			{
				new EPGroup(rank);
			}
		}
		
		for (String player : playersConf.playersMap.keySet())
		{
			if (!hasPlayer(UUID.fromString(player)))
			{
				new EPPlayer(UUID.fromString(player));
			}
		}
	}
	
	public static void addGroup(EPGroup group)
	{
		String name = group.getName();
		
		if (!groups.containsKey(name))
		{
			groups.put(name, group);
		}
	}
	
	public static void replaceGroup(EPGroup group)
	{
		String name = group.getName();
		
		if (groups.containsKey(name))
		{
			groups.replace(name, group);
		}
		else
		{
			groups.put(name, group);
		}
	}
	
	public static void removeGroup(String name)
	{
		if (hasGroup(name))
		{
			groups.remove(name);
		}
	}
	
	public static void clearGroups()
	{
		groups.clear();
	}
	
 	public static boolean hasGroup(String name)
	{
 		if (name == null)
 		{
 			return false;
 		}
 		
		return groups.containsKey(name);
	}
	
	public static EPGroup getGroup(String name)
	{
		return hasGroup(name) ? groups.get(name) : null;
	}

	public static EPGroup getDefaultGroup()
	{
		return hasGroup(defaultGroup) ? groups.get(defaultGroup) : null;
	}
	
	public static String getDefaultGroupName()
	{
		return getDefaultGroup() != null ? getDefaultGroup().getName() : null;
	}
		
	public static List<String> getGroupsName()
	{
		List<String> list = new ArrayList<String>();
		
		for (String s : groups.keySet())
		{
			list.add(s);
		}
		
		return list;
	}
	
	public static List<EPGroup> getGroups()
	{
		List<EPGroup> list = new ArrayList<>();
		
		for (EPGroup e : groups.values())
		{
			list.add(e);
		}
		
		return list;
	}

	public static void addPlayer(EPPlayer player)
	{
		String sid = player.getUUID().toString();
		
		if (!players.containsKey(sid))
		{
			players.put(sid, player);
		}
	}
	
	public static void replacePlayer(EPPlayer player)
	{
		String sid = player.getUUID().toString();
		
		if (players.containsKey(sid))
		{
			players.replace(sid, player);
		}
		else
		{
			players.put(sid, player);
		}
	}
	
	public static EPPlayer getPlayer(UUID id)
	{
		String sid = id.toString();
		
		if (hasPlayer(id))
		{
			return players.get(sid);
		}
		else
		{
			return null;
		}
	}
	
	public static EPPlayer getPlayer(String name)
	{
		String sid = NameAPI.getPlugin().getPlayerUUIDFromName(name, game).toString();
		
		if (hasPlayer(name))
		{
			return players.get(sid);
		}
		else
		{
			return null;
		}
	}
	
	public static void removePlayer(UUID id)
	{
		String sid = id.toString();
		
		if (hasPlayer(id))
		{
			players.remove(sid);
		}
	}
	
	public static void removePlayer(String name)
	{
		String sid = NameAPI.getPlugin().getPlayerUUIDFromName(name, game).toString();
		
		if (hasPlayer(name))
		{
			players.remove(sid);
		}
	}
	
	public static void removePlayer(Player p)
	{
		String sid = p.getUniqueId().toString();
		
		if (hasPlayer(sid))
		{
			players.remove(sid);
		}
	}
	
	public static void removePlayer(EPPlayer p)
	{
		String sid = p.getUUID().toString();
		
		if (hasPlayer(p))
		{
			players.remove(sid);
		}
	}
	
	public static void clearPlayers()
	{
		players.clear();
	}
	
	public static boolean hasPlayer(UUID uuid)
	{
		if (uuid == null)
		{
			return false;
		}
		
		return players.containsKey(uuid.toString());
	}
	
	public static boolean hasPlayer(String name)
	{
		String sid = null;
		
		try
		{
			sid = NameAPI.getPlugin().getPlayerUUIDFromName(name, game).toString();
		}
		catch (Exception e)
		{}
		
		if (sid == null)
		{
			return false;
		}
		
		return players.containsKey(sid);
	}
	
	public static boolean hasPlayer(EPPlayer p)
	{
		String sid = p.getUUID().toString();
		
		if (sid == null)
		{
			return false;
		}
		
		return players.containsKey(sid);
	}
	
	public static void clearAll()
	{
		clearGroups();
		clearPlayers();
	}

	public static void reload()
	{
		for (EPGroup group : groups.values())
		{
			group.reload();
		}
		
		for (EPPlayer p : players.values())
		{
			p.reload();
		}
	}
	
	public static void reload(EPPlayer player)
	{
		players.get(player.getUUID().toString()).reload();
	}
	
	public static void reload(EPGroup group)
	{
		groups.get(group.getName()).reload();
	}

	public static List<EPPlayer> getPlayers()
	{
		List<EPPlayer> list = new ArrayList<>();
		
		for (EPPlayer e : players.values())
		{
			list.add(e);
		}
		
		return list;
	}
	
	public static List<EPPlayer> getPlayersByRank(String name)
	{
		List<EPPlayer> temp = new ArrayList<>();
		
		if (hasGroup(name))
		{
			for (EPPlayer player : getPlayers())
			{
				if (player.getGroupName().equals(name))
				{
					temp.add(player);
				}
			}
		}
		else
		{
			for (EPPlayer player : getPlayers())
			{
				if (player.getGroupName().equals(getDefaultGroupName()))
				{
					temp.add(player);
				}
			}
		}
		
		return temp;
	}

	public static void reloadPlayersByRank(String name)
	{
		for (EPPlayer player : getPlayersByRank(name))
		{
			player.reload();
			
			if (player.isOnline())
			{
				rm.playerLoadRank(player);
			}
		}
	}
	
	
}
