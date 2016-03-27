package com.gmail.edziu1996.extrapermissions.cmd;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.config.ConfigLang;
import com.gmail.edziu1996.extrapermissions.config.ConfigRanks;
import com.gmail.edziu1996.extrapermissions.manager.RanksManager;

public class CmdGroupExecutor implements CommandExecutor
{
	ConfigRanks ranks = ExtraPermissions.getPlugin().ranksConf;
	RanksManager rm = new RanksManager();
	ConfigLang lang = ExtraPermissions.getPlugin().langConf;
	Game game = ExtraPermissions.getPlugin().getGame();
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String name = args.<String>getOne("name").get();
		String option = args.<String>getOne("option").get();
		String value = args.<String>getOne("value").get();
		
		if (option.equalsIgnoreCase("prefix"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".prefix";
				String permDef = null;
				
				if (name.equalsIgnoreCase(rm.getDefaultRank()))
				{
					permDef = "extraperm.experm.group.default.prefix";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
				{
					ranks.get().getNode(name, option).setValue(value);
					ranks.save();
					ranks.loadByRank(name);
					
					String out = lang.newPrefixGroup.replace("%group%", name).replace("%prefix%", value);
					
					src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
				}
			}
			else
			{
				ranks.get().getNode(name, option).setValue(value);
				ranks.save();
				ranks.loadByRank(name);
				
				String out = lang.newPrefixGroup.replace("%group%", name).replace("%prefix%", value);
				
				src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
			}
		}

		if (option.equalsIgnoreCase("suffix"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".suffix";
				String permDef = null;
				
				if (name.equalsIgnoreCase(rm.getDefaultRank()))
				{
					permDef = "extraperm.experm.group.default.suffix";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
				{
					ranks.get().getNode(name, option).setValue(value);
					ranks.save();
					ranks.loadByRank(name);
					
					String out = lang.newSuffixGroup.replace("%group%", name).replace("%suffix%", value);
					
					src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
				}
			}
			else
			{
				ranks.get().getNode(name, option).setValue(value);
				ranks.save();
				ranks.loadByRank(name);
				
				String out = lang.newSuffixGroup.replace("%group%", name).replace("%suffix%", value);
				
				src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
			}
		}
		
		if (option.equalsIgnoreCase("permission") || option.equalsIgnoreCase("perm"))
		{
			if (args.hasAny("vale_perm"))
			{
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.group." + name + ".permission";
					String permDef = null;
					
					if (name.equalsIgnoreCase(rm.getDefaultRank()))
					{
						permDef = "extraperm.experm.group.default.permission";
					}
					
					if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
					{
						boolean bool = args.<Boolean>getOne("vale_perm").get();
						
						ranks.get().getNode(name, "permissions", value).setValue(bool);
						ranks.save();
						ranks.loadByRank(name);
						
						String out = lang.newPermGroup.replace("%group%", name).replace("%perm%", value).replace("%value%", bool + "");
						
						src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
					}
				}
				else
				{
					boolean bool = args.<Boolean>getOne("vale_perm").get();
					
					ranks.get().getNode(name, "permissions", value).setValue(bool);
					ranks.save();
					ranks.loadByRank(name);
					
					String out = lang.newPermGroup.replace("%group%", name).replace("%perm%", value).replace("%value%", bool + "");
					
					src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
				}
			}
			else
			{
				src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize("Usage: /experm <rank_name> permission <permission> <true|false>")));
			}
		}
		
		if (option.equalsIgnoreCase("inheritance"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".inheritance";
				String permDef = null;
				
				if (name.equalsIgnoreCase(rm.getDefaultRank()))
				{
					permDef = "extraperm.experm.group.default.inheritance";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
				{
					ranks.get().getNode(name, option).setValue(value);
					ranks.save();
					ranks.loadByRank(name);
					
					String out = lang.newInheGroup.replace("%group%", name).replace("%inhe%", value);
					
					src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
				}
			}
			else
			{
				ranks.get().getNode(name, option).setValue(value);
				ranks.save();
				ranks.loadByRank(name);
				
				String out = lang.newInheGroup.replace("%group%", name).replace("%inhe%", value);
				
				src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
			}
		}
		
		if (option.equalsIgnoreCase("remove"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".remove." + value;
				String permDef = null;
				
				if (name.equalsIgnoreCase(rm.getDefaultRank()))
				{
					permDef = "extraperm.experm.group.default.remove." + value;
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
				{
					ranks.get().getNode(name).removeChild(value);
					ranks.save();
					ranks.loadByRank(name);
					
					String out = lang.removeValGroup.replace("%group%", name).replace("%value%", value);
					
					src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
				}
			}
			else
			{
				ranks.get().getNode(name).removeChild(value);
				ranks.save();
				ranks.loadByRank(name);
				
				String out = lang.removeValGroup.replace("%group%", name).replace("%value%", value);
				
				src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(out)));
			}
		}
		
		return CommandResult.success();
	}

}
