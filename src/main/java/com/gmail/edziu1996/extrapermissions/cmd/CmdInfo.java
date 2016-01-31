package com.gmail.edziu1996.extrapermissions.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

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
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.config.ConfigLang;
import com.gmail.edziu1996.extrapermissions.config.ConfigPlayers;
import com.gmail.edziu1996.extrapermissions.config.ConfigRanks;
import com.gmail.edziu1996.nameapi.NameAPI;

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
		
		if (opt.equalsIgnoreCase("group") && args.hasAny("name"))
		{
			String name = args.<String>getOne("name").get();
			if (ranks.ranksMap.containsKey(name))
			{
				PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
				pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Rank: " + name)).build());
				
				List<Text> list = new ArrayList<Text>();
				
				for (Entry<Object, ? extends CommentedConfigurationNode> e : ranks.ranksMap.get(name).entrySet())
				{
					if (e.getKey().toString().equalsIgnoreCase("permissions"))
					{
						list.add(Text.of(TextColors.AQUA, "Permissions: "));
						list.addAll(calculateRank(name));
					}
					else if (e.getKey().toString().equalsIgnoreCase("prefix") || e.getKey().toString().equalsIgnoreCase("suffix"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, e.getKey().toString() + ": ", TextColors.BLUE , e.getValue().getValue().toString()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(e.getValue().getValue().toString()), ")"))
								.build());
					}
					else
					{
						list.add(Text.of(TextColors.AQUA, e.getKey().toString() + ": ", TextColors.BLUE, e.getValue().getValue().toString()));
					}
				}
				
				pages.contents(list);
				pages.sendTo(src);
			
			}
			else
			{
				src.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(lang.rankExist)));
			}
				
		}
		else if (opt.equalsIgnoreCase("group") && !args.hasAny("name"))
		{
			PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Ranks")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (String e : ranks.ranksMap.keySet())
			{
				list.add(Text.of(TextColors.AQUA, "- " + e));
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		
		if (opt.equalsIgnoreCase("player") && args.hasAny("name"))
		{
			String name = args.<String>getOne("name").get();
			Player p = game.getServer().getPlayer(name).orElse(null);
			
			PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player: " + name)).build());
			
			List<Text> list = new ArrayList<Text>();
			
			String sid = null;
			
			if (p != null)
			{
				sid = NameAPI.getPlugin().getUUID(p).toString();
			}
			else
			{
				sid = NameAPI.getPlugin().getPlayerUUIDFromName(name, game).toString();
			}
			
			if (players.playersMap.containsKey(sid))
			{
				for (Entry<Object, ? extends CommentedConfigurationNode> e : players.playersMap.get(sid).entrySet())
				{
					if (e.getKey().toString().equalsIgnoreCase("prefix") || e.getKey().toString().equalsIgnoreCase("suffix"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, e.getKey().toString() + ": ", TextColors.BLUE , e.getValue().getValue().toString()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(e.getValue().getValue().toString()), ")"))
								.build());
					}
					else
					{
						list.add(Text.of(TextColors.AQUA, e.getKey().toString() + ": ", TextColors.BLUE , e.getValue().getValue().toString()));
					}
				}
			}
			
			if (p != null)
			{
				if (!p.getSubjectData().getAllPermissions().isEmpty())
				{
					list.add(Text.of("Permissions: "));
					
					for (Entry<String, Boolean> e : p.getSubjectData().getAllPermissions().get(SubjectData.GLOBAL_CONTEXT).entrySet())
					{
						String val = e.getValue().toString();
						Text tx = Text.of(val);
						
						if (val.equalsIgnoreCase("true"))
						{
							tx = Text.of(TextColors.DARK_GREEN, val);
						}
						
						if (val.equalsIgnoreCase("false"))
						{
							tx = Text.of(TextColors.RED, val);
						}
						
						list.add(Text.builder().append(
								Text.of(TextColors.AQUA, "- " + e.getKey().toString() + ": "))
								.append(tx)
								.build());
					}
				}
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else if (opt.equalsIgnoreCase("player") && !args.hasAny("name"))
		{
			PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Players")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (String e : players.playersMap.keySet())
			{
				UUID id = UUID.fromString(e);
				String name = NameAPI.getPlugin().getPlayerNameFormUUID(id, game);
				
				if (name != null)
				{
					list.add(Text.of(TextColors.AQUA, "- " + name));
				}
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		
		return CommandResult.success();
	}
	
	private List<Text> calculateRank(String rank)
	{
		List<Text> temp = new ArrayList<Text>();
		
		if (ranks.ranksMap.containsKey(rank))
		{
			if (ranks.ranksMap.get(rank).containsKey("inheritance"))
			{
				if (ranks.ranksMap.get(rank).get("inheritance").getString() != null)
				{
					temp.addAll(calculateRank(ranks.ranksMap.get(rank).get("inheritance").getString()));
				}
			}
			
			if (ranks.ranksMap.get(rank).containsKey("permissions"))
			{
				if (ranks.ranksMap.get(rank).get("permissions").hasMapChildren())
				{
					temp.add(Text.of(TextColors.AQUA, "+ inheritance " + rank + ": "));
					for (Entry<Object, ? extends CommentedConfigurationNode> e : ranks.ranksMap.get(rank).get("permissions").getChildrenMap().entrySet())
					{
						String val = e.getValue().getValue().toString();
						Text tx = Text.of(val);
						
						if (val.equalsIgnoreCase("true"))
						{
							tx = Text.of(TextColors.DARK_GREEN, val);
						}
						
						if (val.equalsIgnoreCase("false"))
						{
							tx = Text.of(TextColors.RED, val);
						}
						
						temp.add(Text.builder().append(
								Text.of(TextColors.AQUA, "- " + e.getKey().toString() + ": "))
								.append(tx)
								.build());
					}
				}
			}
		}
		
		return temp;
	}

}
