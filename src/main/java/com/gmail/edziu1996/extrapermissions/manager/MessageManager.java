package com.gmail.edziu1996.extrapermissions.manager;

import java.util.Map;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent.Chat;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.nameapi.NameAPI;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class MessageManager
{
	static RanksManager rm = new RanksManager();
	static Map<String, Map<Object, ? extends CommentedConfigurationNode>> playerMap = ExtraPermissions.getPlugin().playersConf.playersMap;
	static Map<String, Map<Object, ? extends CommentedConfigurationNode>> ranksMap = ExtraPermissions.getPlugin().ranksConf.ranksMap;
	
	
	public static Text transformChatMessage(Chat event, String input)
	{
		String output = "[Console] %message%";
		
		if (event.getCause().first(Player.class).isPresent())
		{
			Player pl = event.getCause().first(Player.class).get();
			
			String rank = rm.getPlayerRank(pl);
			
			String playerName = pl.getName();
			
			if (NameAPI.getPlugin().hasDisplayName(pl))
			{
				playerName = NameAPI.getPlugin().getDisplayName(pl);
			}
			
//			DisplayNameData data = pl.getOrCreate(DisplayNameData.class).get();
//			data.displayName().set(Text.of("Tesad"));
//			data.customNameVisible().set(true);
//			pl.offer(data);
//			
//			pl.sendMessage(Text.of(pl.get(DisplayNameData.class).get().customNameVisible().get()));
//			pl.sendMessage(Text.of(pl.get(DisplayNameData.class).get().displayName().get().toPlain()));
			
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
