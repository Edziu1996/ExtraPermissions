package com.gmail.edziu1996.extrapermissions.cmd;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.config.ConfigRanks;

public class CmdGroupExecutor implements CommandExecutor
{
	ConfigRanks ranks = ExtraPermissions.getPlugin().ranksConf;
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String name = args.<String>getOne("name").get();
		String option = args.<String>getOne("option").get();
		String value = args.<String>getOne("value").get();
		
		if (option.equalsIgnoreCase("prefix"))
		{
			ranks.get().getNode(name, option).setValue(value);
			ranks.save();
			ranks.loadByRank(name);
			
			src.sendMessage(Text.of("Rank " + name + " has a new prefix " + value));
		}

		if (option.equalsIgnoreCase("suffix"))
		{
			ranks.get().getNode(name, option).setValue(value);
			ranks.save();
			ranks.loadByRank(name);
			
			src.sendMessage(Text.of("Rank " + name + " has a new suffix " + value));
		}
		
		if (option.equalsIgnoreCase("permission"))
		{
			if (args.hasAny("vale_perm"))
			{
				boolean bool = args.<Boolean>getOne("vale_perm").get();
				ranks.get().getNode(name, option, value).setValue(bool);
				ranks.save();
				src.sendMessage(Text.of("Rank " + name + " has a new permission " + value + "(" + bool + ")"));
			}
			else
			{
				src.sendMessage(Text.of("Usage: /experm <rank name> permission <permission> <true|false>"));
			}
		}
		
		if (option.equalsIgnoreCase("inheritance"))
		{
			ranks.get().getNode(name, option).setValue(value);
			ranks.save();
			ranks.loadByRank(name);
			
			src.sendMessage(Text.of("Rank " + name + " has a new inheritance " + value));
		}
		
		if (option.equalsIgnoreCase("remove"))
		{
			
			ranks.get().getNode(name).removeChild(value);
			ranks.save();
			ranks.loadByRank(name);
			
			src.sendMessage(Text.of("Rank " + name + " has remove a " + value));
		}
		
		return CommandResult.success();
	}

}
