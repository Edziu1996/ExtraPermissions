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
import com.gmail.edziu1996.extrapermissions.config.ConfigPlayers;
import com.gmail.edziu1996.extrapermissions.manager.RanksManager;

public class CmdPlayerExecutor implements CommandExecutor
{
	RanksManager rm = new RanksManager();
	Game game = ExtraPermissions.getPlugin().getGame();
	ConfigPlayers player = ExtraPermissions.getPlugin().playersConf;
	
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
				src.sendMessage(Text.of(name + " have"));
				player.get().getNode(sid, option).setValue(value);
				player.save();
				player.loadByPlayer(p);
				
				src.sendMessage(Text.of(p.getName() + " has a new prefix: " + value));
				p.sendMessage(Text.of("You hava a new prefix: " + value));
			}
			
			if (option.equalsIgnoreCase("suffix"))
			{
				player.get().getNode(sid, option).setValue(value);
				player.save();
				player.loadByPlayer(p);
				
				src.sendMessage(Text.of(p.getName() + " has a new suffix: " + value));
				p.sendMessage(Text.of("You hava a new suffix: " + value));
			}
			
			if (option.equalsIgnoreCase("rank"))
			{
				if (args.hasAny("timeUnit") && args.hasAny("time"))
				{
					int time = args.<Integer>getOne("time").get();
					RanksManager.TimeUnit unit = RanksManager.TimeUnit.converFromString(args.<String>getOne("timeUnit").get());
					
					rm.setPlayerTimeRank(p, value, time, unit);
					
					src.sendMessage(Text.of(p.getName() + " is already " + value + " for " + time + " " + unit));
					p.sendMessage(Text.of("You are already " + value + " for " + time + " " + unit));
				}
				else
				{
					rm.setPlayerRank(p, value);
					src.sendMessage(Text.of(p.getName() + " is already " + value));
					p.sendMessage(Text.of("You are already " + value));
				}
			}
			
			if (option.equalsIgnoreCase("remove"))
			{
				
				player.get().getNode(sid).removeChild(value);
				player.save();
				player.loadByPlayer(p);
				rm.playerLoadRank(p);
				
				src.sendMessage(Text.of("Rank " + name + " has remove a " + value));
			}
		}
		else
		{
			src.sendMessage(Text.of("Player must be online!"));
		}
		return CommandResult.success();
	}

}
