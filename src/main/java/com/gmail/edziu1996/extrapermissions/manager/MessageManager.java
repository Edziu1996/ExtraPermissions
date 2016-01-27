package com.gmail.edziu1996.extrapermissions.manager;

import java.util.Map;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class MessageManager
{
	static RanksManager rm = new RanksManager();
	static Map<String, Map<Object, ? extends CommentedConfigurationNode>> playerMap = ExtraPermissions.getPlugin().playersConf.playersMap;
	static Map<String, Map<Object, ? extends CommentedConfigurationNode>> ranksMap = ExtraPermissions.getPlugin().ranksConf.ranksMap;
	
	
	public static Text transformChatMessage(CommandSource src, String input)
	{
		String output = "[Console] %message%";
		
		if (!(src instanceof ConsoleSource))
		{
			Player pl = (Player) src;
			
			// %player%
			String rank = rm.getPlayerRank(pl);
			
			String playerName = pl.getName();
			String playerID = pl.getUniqueId().toString();
			
			String formula = ExtraPermissions.getPlugin().config.formule;
			
			if (formula != null)
			{
				if (formula.length() > 0)
				{
					output = formula;
				}
			}
			
			String world = pl.getWorld().getProperties().getWorldName().replace("DIM-1", "Nether").replace("DIM1", "The End");
			
			
//			String rank_prefix = ranksMap.get(rank).get("prefix").getString();
//			String rank_suffix = ranksMap.get(rank).get("suffix").getString();
			String rank_prefix = getVal(ranksMap, rank, "prefix");
			String rank_suffix = getVal(ranksMap, rank, "suffix");
			
//			String player_prefix = playerMap.get(playerID).get("prefix").getString();
//			String player_suffix = playerMap.get(playerID).get("suffix").getString();
			String player_prefix = getVal(playerMap, playerID, "prefix");
			String player_suffix = getVal(playerMap, playerID, "suffix");
			
			output = convert(output, "%player%", playerName);
			output = convert(output, "%world%", world);
			output = convert(output, "%rank_prefix%", rank_prefix);
			output = convert(output, "%rank_suffix%", rank_suffix);
			output = convert(output, "%player_prefix%", player_prefix);
			output = convert(output, "%player_suffix%", player_suffix);
			
		}
		
		output = convert(output, "%message%", input);
		
		Builder message = Text.builder();
		Text text = Text.builder()
				.append(TextSerializers.FORMATTING_CODE.deserialize(output))
				.onHover(TextActions.showText(Text.of(src.getName())))
				.build();
		
		return message.append(Text.of(text)).build();
	}

	private static String getVal(Map<String, Map<Object, ? extends CommentedConfigurationNode>> map, String rank, String key)
	{
		if (map != null)
		{
			if (map.containsKey(rank))
			{
				if (map.get(rank).containsKey(key))
				{
					return map.get(rank).get(key).getString();
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	private static String convert(String input, String name, String repl)
	{
		String temp = input;
		
		if (repl != null)
		{
			if (repl.length() > 0)
			{
				temp = temp.replace(name, repl);
			}
			else
			{
				temp = temp.replace(name, "");
			}
			
		}
		else
		{
			temp = temp.replace(name, "");
		}
		
		return temp;
	}

}
