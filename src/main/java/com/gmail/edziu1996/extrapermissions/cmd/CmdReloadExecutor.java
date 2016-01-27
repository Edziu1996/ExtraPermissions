package com.gmail.edziu1996.extrapermissions.cmd;

import static org.spongepowered.api.text.Text.of;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.gmail.edziu1996.extrapermissions.ConfigLang;
import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.config.ConfigNormal;
import com.gmail.edziu1996.extrapermissions.config.ConfigPlayers;
import com.gmail.edziu1996.extrapermissions.config.ConfigRanks;
import com.gmail.edziu1996.extrapermissions.manager.RanksManager;

public class CmdReloadExecutor implements CommandExecutor
{
	ConfigPlayers pla = ExtraPermissions.getPlugin().playersConf;
	ConfigRanks ran = ExtraPermissions.getPlugin().ranksConf;
	ConfigNormal conf = ExtraPermissions.getPlugin().config;
	ConfigLang lang = ExtraPermissions.getPlugin().langConf;
	RanksManager rank = new RanksManager();
	
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		src.sendMessage(of(lang.startReload));
		pla.setup();
		ran.setup();
		conf.setup();
		rank.realodRanks(ExtraPermissions.getPlugin().getGame());
		src.sendMessage(of(lang.endReload));
		
		return CommandResult.success();
	}

}
