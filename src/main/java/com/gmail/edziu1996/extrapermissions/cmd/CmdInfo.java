package com.gmail.edziu1996.extrapermissions.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.config.ConfigLang;
import com.gmail.edziu1996.extrapermissions.config.ConfigPlayers;
import com.gmail.edziu1996.extrapermissions.config.ConfigRanks;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class CmdInfo implements CommandExecutor
{
	Game game = ExtraPermissions.getPlugin().getGame();
	ConfigLang lang = ExtraPermissions.getPlugin().langConf;
	ConfigRanks ranks = ExtraPermissions.getPlugin().ranksConf;
	ConfigPlayers players = ExtraPermissions.getPlugin().playersConf;
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String opt = args.<String>getOne("option").get();
		String name = args.<String>getOne("name").get();
		
		if (opt.equalsIgnoreCase("group"))
		{
			if (ranks.equals(name))
			{
				PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
				pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Rank: " + name)).build());
				
				List<Text> list = new ArrayList<Text>();
				
				for (Entry<Object, ? extends CommentedConfigurationNode> e : ranks.ranksMap.get(name).entrySet())
				{
					if (e.getKey().toString().equalsIgnoreCase("permissions"))
					{
						list.add(Text.of("Permissions: "));
						for (Entry<Object, ? extends CommentedConfigurationNode> ee : e.getValue().getChildrenMap().get(SubjectData.GLOBAL_CONTEXT).getChildrenMap().entrySet())
						{
							list.add(Text.of("- " + ee.getKey().toString() + ": " + ee.getValue().getValue().toString()));
						}
					}
					else
					{
						list.add(Text.of(e.getKey().toString() + ": " + e.getValue()));
					}
					
				}
				
				pages.contents(list);
				pages.sendTo(src);
			
			}
			else
			{
				src.sendMessage(Text.of(lang.rankExist));
			}
				
		}
		
		if (opt.equalsIgnoreCase("player"))
		{
			Player p = game.getServer().getPlayer(name).orElse(null);
			
			if (p != null)
			{
				PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
				pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player: " + name)).build());
				
				List<Text> list = new ArrayList<Text>();
				
				for (Entry<Object, ? extends CommentedConfigurationNode> e : ranks.ranksMap.get(name).entrySet())
				{
					list.add(Text.of(e.getKey().toString() + ": " + e.getValue()));
				}
				
				pages.contents(list);
				pages.sendTo(src);
			}
			else
			{
				src.sendMessage(Text.of(lang.mustPlayerOnline));
			}
		}
		
		return CommandResult.success();
	}

}
