package com.gmail.edziu1996.extrapermissions.cmd;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.config.ConfigLang;
import com.gmail.edziu1996.extrapermissions.config.ConfigRanks;

public class CmdGroupExecutor implements CommandExecutor
{
	ConfigRanks ranks = ExtraPermissions.getPlugin().ranksConf;
	ConfigLang lang = ExtraPermissions.getPlugin().langConf;
	
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
			
			String out = lang.newPrefixGroup.replace("%group%", name).replace("%prefix%", value);
			
			src.sendMessage(Text.of(out));
		}

		if (option.equalsIgnoreCase("suffix"))
		{
			ranks.get().getNode(name, option).setValue(value);
			ranks.save();
			ranks.loadByRank(name);
			
			String out = lang.newSuffixGroup.replace("%group%", name).replace("%suffix%", value);
			
			src.sendMessage(Text.of(out));
		}
		
		if (option.equalsIgnoreCase("permission"))
		{
			if (args.hasAny("vale_perm"))
			{
				boolean bool = args.<Boolean>getOne("vale_perm").get();
				ranks.get().getNode(name, option, value).setValue(bool);
				ranks.save();
				
				String out = lang.newPermGroup.replace("%group%", name).replace("%perm%", value).replace("value", bool + "");
				
				src.sendMessage(Text.of(out));
			}
			else
			{
				src.sendMessage(Text.of("Usage: /experm <rank_name> permission <permission> <true|false>"));
			}
		}
		
		if (option.equalsIgnoreCase("inheritance"))
		{
			ranks.get().getNode(name, option).setValue(value);
			ranks.save();
			ranks.loadByRank(name);
			
			String out = lang.newInheGroup.replace("%group%", name).replace("%inhe%", value);
			
			src.sendMessage(Text.of(out));
		}
		
		if (option.equalsIgnoreCase("remove"))
		{
			
			ranks.get().getNode(name).removeChild(value);
			ranks.save();
			ranks.loadByRank(name);
			
			String out = lang.removeValGroup.replace("%group%", name).replace("%value%", value);
			
			src.sendMessage(Text.of(out));
		}
		
		return CommandResult.success();
	}

}
