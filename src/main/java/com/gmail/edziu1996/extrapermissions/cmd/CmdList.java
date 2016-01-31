package com.gmail.edziu1996.extrapermissions.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.config.ConfigPlayers;
import com.gmail.edziu1996.extrapermissions.config.ConfigRanks;
import com.gmail.edziu1996.extrapermissions.manager.RanksManager;
import com.gmail.edziu1996.nameapi.NameAPI;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class CmdList implements CommandExecutor
{
	ConfigRanks ranks = ExtraPermissions.getPlugin().ranksConf;
	ConfigPlayers players = ExtraPermissions.getPlugin().playersConf;
	RanksManager rm = new RanksManager();
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String opt = args.<String>getOne("option").get();
		
		if (opt.equalsIgnoreCase("groups"))
		{
			PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Ranks: ")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (String e : ranks.ranksMap.keySet())
			{
				list.add(Text.of(TextColors.AQUA, "- " + e));
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		
		if (opt.equalsIgnoreCase("players") && !args.hasAny("name"))
		{
			PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player: rank")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (Entry<String, Map<Object, ? extends CommentedConfigurationNode>> e : players.playersMap.entrySet())
			{
				if (e.getValue().containsKey("rank"))
				{
					UUID uuid = UUID.fromString(e.getKey());
					String playerName = NameAPI.getPlugin().getPlayerNameFormUUID(uuid, ExtraPermissions.getPlugin().getGame());
					list.add(Text.of(TextColors.AQUA, "- " + playerName + ": ", TextColors.BLUE, e.getValue().get("rank").getString()));
				}
				else
				{
					UUID uuid = UUID.fromString(e.getKey());
					String playerName = NameAPI.getPlugin().getPlayerNameFormUUID(uuid, ExtraPermissions.getPlugin().getGame());
					list.add(Text.of(TextColors.AQUA, "- " + playerName + ": ", TextColors.BLUE, rm.getDefaultRank()));
				}
				
			}
			for (Player p : ExtraPermissions.getPlugin().getGame().getServer().getOnlinePlayers())
			{
				if (!list.contains(Text.of("- " + p.getName())))
				{
					if (players.playersMap.containsKey(p.getUniqueId().toString()))
					{
						if (!players.playersMap.get(p.getUniqueId().toString()).containsKey("rank"))
						{
							list.add(Text.of(TextColors.AQUA, "- " + p.getName()));
						}
					}
					else
					{
						list.add(Text.of(TextColors.AQUA, "- " + p.getName()));
					}
				}
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else if (opt.equalsIgnoreCase("players") && args.hasAny("name"))
		{
			String name = args.<String>getOne("name").get();
			PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Players from "+name+": ")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (Entry<String, Map<Object, ? extends CommentedConfigurationNode>> e : players.playersMap.entrySet())
			{
				if (e.getValue().containsKey("rank"))
				{
					if (e.getValue().get("rank").getString().equalsIgnoreCase(name))
					{
						UUID uuid = UUID.fromString(e.getKey());
						String playerName = NameAPI.getPlugin().getPlayerNameFormUUID(uuid, ExtraPermissions.getPlugin().getGame());
						list.add(Text.of(TextColors.AQUA, "- " + playerName));
					}
				}
				
			}
			
			if (name.equalsIgnoreCase(rm.getDefaultRank()))
			{
				for (Player p : ExtraPermissions.getPlugin().getGame().getServer().getOnlinePlayers())
				{
					if (!list.contains(Text.of("- " + p.getName())))
					{
						if (players.playersMap.containsKey(p.getUniqueId().toString()))
						{
							if (!players.playersMap.get(p.getUniqueId().toString()).containsKey("rank"))
							{
								list.add(Text.of(TextColors.AQUA, "- " + p.getName()));
							}
						}
						else
						{
							list.add(Text.of(TextColors.AQUA, "- " + p.getName()));
						}
					}
				}
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		
		return CommandResult.success();
	}

}
