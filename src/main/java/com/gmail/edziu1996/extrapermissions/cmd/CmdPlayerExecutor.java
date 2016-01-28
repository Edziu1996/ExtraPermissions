package com.gmail.edziu1996.extrapermissions.cmd;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.config.ConfigLang;
import com.gmail.edziu1996.extrapermissions.config.ConfigPlayers;
import com.gmail.edziu1996.extrapermissions.manager.RanksManager;

public class CmdPlayerExecutor implements CommandExecutor
{
	RanksManager rm = new RanksManager();
	Game game = ExtraPermissions.getPlugin().getGame();
	ConfigPlayers player = ExtraPermissions.getPlugin().playersConf;
	ConfigLang lang = ExtraPermissions.getPlugin().langConf;
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String name = args.<String>getOne("name").get();
		String option = args.<String>getOne("option").get();
		String value = args.<String>getOne("value").get();
		
		Player p = game.getServer().getPlayer(name).orElse(null);
		
		if (p != null)
		{
			String sid = p.getUniqueId().toString();
			if (option.equalsIgnoreCase("prefix"))
			{
				player.get().getNode(sid, option).setValue(value);
				player.save();
				player.loadByPlayer(p);
				
				String out1 = lang.newPrefixPlayer1.replace("%name%", name).replace("%prefix%", value);
				String out2 = lang.newPrefixPlayer2.replace("%prefix%", value);
				
				src.sendMessage(Text.of(out1));
				p.sendMessage(Text.of(out2));
			}
			
			if (option.equalsIgnoreCase("suffix"))
			{
				player.get().getNode(sid, option).setValue(value);
				player.save();
				player.loadByPlayer(p);
				
				String out1 = lang.newSuffixPlayer1.replace("%name%", name).replace("%suffix%", value);
				String out2 = lang.newSuffixPlayer2.replace("%suffix%", value);
				
				src.sendMessage(Text.of(out1));
				p.sendMessage(Text.of(out2));
			}
			
			if (option.equalsIgnoreCase("rank"))
			{
				if (src instanceof Player)
				{
					if (src.hasPermission("experm.experm.group." + value))
					{
						if (args.hasAny("timeUnit") && args.hasAny("time"))
						{
							int time = args.<Integer>getOne("time").get();
							RanksManager.TimeUnit unit = RanksManager.TimeUnit.converFromString(args.<String>getOne("timeUnit").get());
							
							rm.setPlayerTimeRank(p, value, time, unit);
							
							String out1 = lang.newTimeRankPlayer1.replace("%name%", name).replace("%rank%", value).replace("%time%", time +"").replace("%timeUnit%", unit+"");
							String out2 = lang.newTimeRankPlayer2.replace("%rank%", value).replace("%time%", time +"").replace("%timeUnit%", unit+"");
							
							src.sendMessage(Text.of(out1));
							p.sendMessage(Text.of(out2));
						}
						else
						{
							rm.setPlayerRank(p, value);
							
							String out1 = lang.newRankPlayer1.replace("%name%", name).replace("%rank%", value);
							String out2 = lang.newRankPlayer2.replace("%rank%", value);
							
							src.sendMessage(Text.of(out1));
							p.sendMessage(Text.of(out2));
						}
					}
					else
					{
						String out = lang.newRankPlayerPerm;
						src.sendMessage(Text.of(out));
					}
				}
				else
				{
					if (args.hasAny("timeUnit") && args.hasAny("time"))
					{
						int time = args.<Integer>getOne("time").get();
						RanksManager.TimeUnit unit = RanksManager.TimeUnit.converFromString(args.<String>getOne("timeUnit").get());
						
						rm.setPlayerTimeRank(p, value, time, unit);
						
						String out1 = lang.newTimeRankPlayer1.replace("%name%", name).replace("%rank%", value).replace("%time%", time +"").replace("%timeUnit%", unit+"");
						String out2 = lang.newTimeRankPlayer2.replace("%rank%", value).replace("%time%", time +"").replace("%timeUnit%", unit+"");
						
						src.sendMessage(Text.of(out1));
						p.sendMessage(Text.of(out2));
					}
					else
					{
						rm.setPlayerRank(p, value);
						
						String out1 = lang.newRankPlayer1.replace("%name%", name).replace("%rank%", value);
						String out2 = lang.newRankPlayer2.replace("%rank%", value);
						
						src.sendMessage(Text.of(out1));
						p.sendMessage(Text.of(out2));
					}
				}
				
			}
			
			if (option.equalsIgnoreCase("remove"))
			{
				
				player.get().getNode(sid).removeChild(value);
				player.save();
				player.loadByPlayer(p);
				rm.playerLoadRank(p);
				
				String out = lang.removeValPlayer.replace("%name%", name).replace("%value%", value);
				
				src.sendMessage(Text.of(out));
			}
		}
		else
		{
			src.sendMessage(Text.of(lang.mustPlayerOnline));
		}
		return CommandResult.success();
	}

}
