package com.gmail.edziu1996.extrapermissions.core.manager;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import com.gmail.edziu1996.extrapermissions.api.EPPlayer;
import com.gmail.edziu1996.extrapermissions.api.Manager;
import com.gmail.edziu1996.extrapermissions.core.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigPlayers;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class RanksManager
{
	Map<String, Map<Object, ? extends CommentedConfigurationNode>> ranksMap = ExtraPermissions.getPlugin().ranksConf.ranksMap;
	Map<String, Map<Object, ? extends CommentedConfigurationNode>> playersMap = ExtraPermissions.getPlugin().playersConf.playersMap;
	ConfigPlayers playerConf = ExtraPermissions.getPlugin().playersConf;
	Game game = ExtraPermissions.getPlugin().getGame();
	
	public void playerLoadRank(EPPlayer pl)
	{
		playerLoadRank(pl, pl.getPlayer().getWorld().getName());
	}
	
	public void playerLoadRank(EPPlayer pl, String worldName)
	{
		pl.getPlayer().getSubjectData().clearPermissions();
		
		calculateRank(pl, worldName);
	}
	
	public void realodRanks(Game game)
	{
		for (Player p : game.getServer().getOnlinePlayers())
		{
			playerLoadRank(Manager.getPlayer(p.getUniqueId()));
		}
	}

	@SuppressWarnings("unused")
	private void calculateRank(EPPlayer pl)
	{
		calculateRank(pl, pl.getPlayer().getWorld().getName());
	}
	
	private void calculateRank(EPPlayer pl, String worldName)
	{
		Map<String, Boolean> map = pl.getPermissionsWithWorld(worldName);
		
		for (Entry<String, Boolean> p : map.entrySet())
		{
			Tristate tri = Tristate.fromBoolean(p.getValue());
			String name = p.getKey();
			
			pl.getPlayer().getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, name, tri);
		}
	}
	
	public static enum TimeUnit
	{
		MILLISECOND(GregorianCalendar.MILLISECOND),
		SECOND(GregorianCalendar.SECOND),
		MINUTE(GregorianCalendar.MINUTE),
		HOUR(GregorianCalendar.HOUR_OF_DAY),
		DAY(GregorianCalendar.DAY_OF_MONTH),
		MONTH(GregorianCalendar.MONTH),
		YEAR(GregorianCalendar.YEAR);
		
		private int number;
		
		private TimeUnit(int i)
		{
			number = i;
		}

		public int get()
		{
			return number;
		}
		
		public static TimeUnit converFromString(String s)
		{
			if (s.equalsIgnoreCase("ms"))
			{
				return MILLISECOND;
			}
			
			if (s.equalsIgnoreCase("s"))
			{
				return SECOND;
			}
			
			if (s.equalsIgnoreCase("m"))
			{
				return MINUTE;
			}
			
			if (s.equalsIgnoreCase("h"))
			{
				return HOUR;
			}
			
			if (s.equalsIgnoreCase("d"))
			{
				return DAY;
			}
			
			if (s.equalsIgnoreCase("mo"))
			{
				return MONTH;
			}
			
			if (s.equalsIgnoreCase("y"))
			{
				return YEAR;
			}
			
			return null;
		}
	}
	
	public void setPlayerTimeRank(EPPlayer p, String rank, int time, TimeUnit unit)
	{
		int timeUnit = unit.get();
		String sid = p.getUUID().toString();
		
		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
		cal.add(timeUnit, time);
		
		if (playerConf.get().hasMapChildren())
		{
			if (playerConf.get().getChildrenMap().containsKey(sid))
			{
				if (!p.getGroupName().equals(Manager.getDefaultGroupName()))
				{
					String lastRank = p.getGroupName();
					playerConf.get().getNode(sid, "lastRank").setValue(lastRank);
				}
			}
		}
		
		playerConf.get().getNode(sid, "rank").setValue(rank);
		playerConf.get().getNode(sid, "rankTimed").setValue(true);
		playerConf.get().getNode(sid, "rankTime").setValue(cal.getTime().getTime());
		
		
		playerConf.save();
		playerConf.loadByPlayer(p);
		
		Manager.reload(p);
		
		playerLoadRank(p);
	}
	
	public void setPlayerRank(EPPlayer p, String rank)
	{
		playerConf.get().getNode(p.getUUID().toString(), "rank").setValue(rank);
		
		String sid = p.getUUID().toString();
		if (playerConf.get().getChildrenMap().containsKey(sid))
		{
			if (playerConf.get().getNode(sid).getChildrenMap().containsKey("lastRank"))
			{
				playerConf.get().getNode(sid).removeChild("lastRank");
			}
			
			if (playerConf.get().getNode(sid).getChildrenMap().containsKey("rankTimed"))
			{
				playerConf.get().getNode(sid).removeChild("rankTimed");
			}
			
			if (playerConf.get().getNode(sid).getChildrenMap().containsKey("rankTime"))
			{
				playerConf.get().getNode(sid).removeChild("rankTime");
			}
		}
		
		playerConf.save();
		playerConf.loadByPlayer(p);
		
		Manager.reload(p);
		
		playerLoadRank(p);
	}

	public void removePlayerRank(EPPlayer p, String rank)
	{
		String sid = p.getUUID().toString();
		
		if (playerConf.get().hasMapChildren())
		{
			if (playerConf.get().getChildrenMap().containsKey(sid))
			{
				if (playerConf.get().getChildrenMap().get(sid).hasMapChildren())
				{
					if (playerConf.get().getChildrenMap().get(sid).getChildrenMap().containsKey("lastRank"))
					{
						if (p.getGroupName().equals(rank))
						{
							playerConf.get().getNode(sid, "rank").setValue(playersMap.get(sid).get("lastRank").getString());
							playerConf.get().getNode(sid).removeChild("lastRank");
							playerConf.get().getNode(sid).removeChild("rankTime");
							playerConf.get().getNode(sid).removeChild("rankTimed");
						}
					}
					else if (playerConf.get().getChildrenMap().get(sid).getChildrenMap().containsKey("rank"))
					{
						playerConf.get().getNode(sid).removeChild("rank");
					}
				}
				
				if (!playerConf.get().getChildrenMap().get(sid).hasMapChildren())
				{
					playerConf.get().removeChild(sid);
				}
			}
		}
		
		
		playerConf.save();
		playerConf.loadByPlayer(p);
		
		Manager.reload(p);
		
		playerLoadRank(p);
	}
}
