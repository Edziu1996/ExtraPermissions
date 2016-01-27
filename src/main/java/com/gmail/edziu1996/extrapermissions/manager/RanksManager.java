package com.gmail.edziu1996.extrapermissions.manager;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.config.ConfigPlayers;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class RanksManager
{
	Map<String, Map<Object, ? extends CommentedConfigurationNode>> ranksMap = ExtraPermissions.getPlugin().ranksConf.ranksMap;
	Map<String, Map<Object, ? extends CommentedConfigurationNode>> playersMap = ExtraPermissions.getPlugin().playersConf.playersMap;
	ConfigPlayers playerConf = ExtraPermissions.getPlugin().playersConf;
	
	public void playerLoadRank(Player pl)
	{
		UUID pid = pl.getUniqueId();
		String id = pid.toString();
		
		
		if (playersMap.containsKey(id) && playersMap.get(id).containsKey("rank"))
		{
			checkLastRank(pl);
			String rank = playersMap.get(id).get("rank").getString();
			calculateRank(pl, rank);
		}
		else if (playersMap.containsKey(id) && playersMap.get(id).isEmpty())
		{
			playerConf.get().removeChild(id);
			playerConf.save();
			
			String rank = getDefaultRank();
			calculateRank(pl, rank);
		}
		else
		{
			String rank = getDefaultRank();
			calculateRank(pl, rank);
		}
	}
	
	public void realodRanks(Game game)
	{
		for (Player p : game.getServer().getOnlinePlayers())
		{
			playerLoadRank(p);
		}
	}
	
	public String getDefaultRank()
	{
		String rank = "default";
		
		for (Entry<String, Map<Object, ? extends CommentedConfigurationNode>> e : ranksMap.entrySet())
		{
			for (Entry<Object, ? extends CommentedConfigurationNode> ee : e.getValue().entrySet())
			{
				if (ee.getKey().equals("default"))
				{
					if (ee.getValue().getBoolean())
					{
						rank = e.getKey().toString();
					}
				}
			}
		}
		
		return rank;
	}

	private void calculateRank(Player p, String rank)
	{
		if (ranksMap.containsKey(rank))
		{
			if (ranksMap.get(rank).containsKey("inheritance"))
			{
				if (ranksMap.get(rank).get("inheritance").getString() != null)
				{
					calculateRank(p, ranksMap.get(rank).get("inheritance").getString());
				}
			}
			
			if (ranksMap.get(rank).containsKey("permissions"))
			{
				if (ranksMap.get(rank).get("permissions").hasMapChildren())
				{
					for (Entry<Object, ? extends CommentedConfigurationNode> e : ranksMap.get(rank).get("permissions").getChildrenMap().entrySet())
					{
						p.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, e.getKey().toString(), Tristate.fromBoolean(e.getValue().getBoolean()));
					}
				}
			}
		}
	}
	
	private void checkLastRank(Player p)
	{
		UUID id = p.getUniqueId();
		String sid = id.toString();
		
		if (playersMap.containsKey(sid))
		{
			if (playersMap.get(sid).containsKey("rankTimed") && playersMap.get(sid).containsKey("rankTime"))
			{
				if (playersMap.get(sid).get("rankTimed").getBoolean())
				{
					if (playersMap.get(sid).get("rankTime") != null)
					{
						long time = playersMap.get(sid).get("rankTime").getLong();
						long curTime = GregorianCalendar.getInstance().getTime().getTime();
						
						ExtraPermissions.getPlugin().getLogger().info(" 1 || " + time);
						ExtraPermissions.getPlugin().getLogger().info(" 2 || " + curTime);
						
						if (playersMap.get(sid).containsKey("lastRank"))
						{
							String lastRank = playersMap.get(sid).get("lastRank").getString();
							
							if (time < curTime)
							{
//								playersMap.get(sid).remove("lastRank");
//								playersMap.get(sid).remove("rankTime");
//								playersMap.get(sid).remove("rankTimed");
								
								playerConf.get().getNode(sid).removeChild("lastRank");
								playerConf.get().getNode(sid).removeChild("rankTime");
								playerConf.get().getNode(sid).removeChild("rankTimed");
								
								playerConf.get().getNode(sid, "rank").setValue(lastRank);
								
//								playersMap.get(sid).get("rank").setValue(lastRank);
							}
						}
						else
						{
							if (time < curTime)
							{
//								playersMap.get(sid).remove("rankTime");
//								playersMap.get(sid).remove("rankTimed");
//								playersMap.get(sid).remove("rank");
								
								playerConf.get().getNode(sid).removeChild("lastRank");
								playerConf.get().getNode(sid).removeChild("rankTime");
								playerConf.get().getNode(sid).removeChild("rankTimed");
								
								if (!playerConf.get().getNode(sid).hasMapChildren())
								{
									playerConf.get().removeChild(sid);
								}
							}
						}
						
						//saveInFile(p);
						
						playerConf.save();
						playerConf.loadByPlayer(p);
					}
				}
			}
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
	
	public void setPlayerTimeRank(Player p, String rank, int time, TimeUnit unit)
	{
		int timeUnit = unit.get();
		String sid = p.getUniqueId().toString();
		
		if (playersMap.containsKey(sid))
		{
			if (playersMap.get(sid).containsKey("rank"))
			{
				String lastRank = playersMap.get(sid).get("rank").getString();
				
				GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
				cal.add(timeUnit, time);
				
				playerConf.get().getNode(sid, "rank").setValue(rank);
				playerConf.get().getNode(sid, "lastRank").setValue(lastRank);
				playerConf.get().getNode(sid, "rankTimed").setValue(true);
				playerConf.get().getNode(sid, "rankTime").setValue(cal.getTime().getTime());
				
			}
			else
			{
				GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
				cal.add(timeUnit, time);
				
				playerConf.get().getNode(sid, "rank").setValue(rank);
				playerConf.get().getNode(sid, "rankTimed").setValue(true);
				playerConf.get().getNode(sid, "rankTime").setValue(cal.getTime().getTime());
				
			}
		}
		else
		{
			GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
			cal.add(timeUnit, time);
			
			playerConf.get().getNode(sid, "rank").setValue(rank);
			playerConf.get().getNode(sid, "rankTimed").setValue(true);
			playerConf.get().getNode(sid, "rankTime").setValue(cal.getTime().getTime());
			
		}
		
		playerConf.save();
		playerConf.loadByPlayer(p);
		playerLoadRank(p);
	}
	
	public void setPlayerRank(Player p, String rank)
	{
		playerConf.get().getNode(p.getUniqueId().toString(), "rank").setValue(rank);
		
		String sid = p.getUniqueId().toString();
		if (playersMap.containsKey(sid))
		{
			if (playersMap.get(sid).containsKey("lastRank"))
			{
				playerConf.get().getNode(sid).removeChild("lastRank");
			}
			
			if (playersMap.get(sid).containsKey("rankTimed"))
			{
				playerConf.get().getNode(sid).removeChild("rankTimed");
			}
			
			if (playersMap.get(sid).containsKey("rankTime"))
			{
				playerConf.get().getNode(sid).removeChild("rankTime");
			}
		}
		
		playerConf.save();
		playerConf.loadByPlayer(p);
		
		playerLoadRank(p);
	}
	
	public String getPlayerRank(Player p)
	{
		String rank = getDefaultRank();
		
		if (playersMap.containsKey(p.getUniqueId().toString()))
		{
			if (playersMap.get(p.getUniqueId().toString()).containsKey("rank"))
			{
				rank = playersMap.get(p.getUniqueId().toString()).get("rank").getString();
			}
		}
		
		return rank;
	}

	public void removePlayerRank(Player p, String rank)
	{
		String sid = p.getUniqueId().toString();
		
		if (playersMap.containsKey(sid))
		{
			if (playersMap.get(sid).containsKey("lastRank"))
			{
				if (playersMap.get(sid).get("rank").getString().equals(rank))
				{
//					playersMap.get(sid).get("rank").setValue(playersMap.get(sid).get("lastRank").getString());
//					playersMap.get(sid).remove("lastRank");
//					playersMap.get(sid).remove("rankTime");
//					playersMap.get(sid).remove("rankTimed");
					
					playerConf.get().getNode(sid, "rank").setValue(playersMap.get(sid).get("lastRank").getString());
					playerConf.get().getNode(sid).removeChild("lastRank");
					playerConf.get().getNode(sid).removeChild("rankTime");
					playerConf.get().getNode(sid).removeChild("rankTimed");
				}
			}
			else if (playersMap.get(sid).containsKey("rank"))
			{
				if (playersMap.get(sid).get("rank").getString().equals(rank))
				{
					playerConf.get().getNode(sid).removeChild("rank");
				}
			}
		}
		
		playerConf.save();
		playerConf.loadByPlayer(p);
		
		playerLoadRank(p);
	}
}
