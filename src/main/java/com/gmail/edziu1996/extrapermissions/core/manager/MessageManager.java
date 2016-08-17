package com.gmail.edziu1996.extrapermissions.core.manager;

import java.util.Map;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent.Chat;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.edziu1996.extrapermissions.api.EPGroup;
import com.gmail.edziu1996.extrapermissions.api.EPPlayer;
import com.gmail.edziu1996.extrapermissions.api.Manager;
import com.gmail.edziu1996.extrapermissions.core.ExtraPermissions;
import com.gmail.edziu1996.nameapi.NameAPI;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class MessageManager
{
	static RanksManager rm = new RanksManager();
	static Map<String, Map<Object, ? extends CommentedConfigurationNode>> playerMap = ExtraPermissions.getPlugin().playersConf.playersMap;
	static Map<String, Map<Object, ? extends CommentedConfigurationNode>> ranksMap = ExtraPermissions.getPlugin().ranksConf.ranksMap;
	
	
	public static Text transformChatMessage(Chat event, String input)
	{
		String output = "[Console] ";
		
		String s = event.getRawMessage().toPlain();
		String s2 = event.getMessage().toPlain().substring(event.getMessage().toPlain().indexOf(" ") + 1);
		
		Text message = Text.of(input);
		
		if (s.length() != s2.length())
		{
			message = TextSerializers.FORMATTING_CODE.deserialize(input);
		}
		
		if (event.getCause().first(Player.class).isPresent())
		{
			Player pl = event.getCause().first(Player.class).get();
			
			EPPlayer player = Manager.getPlayer(pl.getUniqueId());
			EPGroup group = player.getGroup();
			String worldName = player.getPlayer().getWorld().getName();
			
			String playerName = pl.getName();
			
			if (NameAPI.getPlugin().isVisibleDisplayName(pl))
			{
				playerName = NameAPI.getPlugin().getDisplayName(pl);
			}
			
			String formula = ExtraPermissions.getPlugin().config.formule;
			
			if (formula != null)
			{
				if (formula.length() > 0)
				{
					output = formula;
				}
			}
			
			String world = pl.getWorld().getName().replace("DIM-1", "Nether").replace("DIM1", "The End");
			
			String rank_prefix = group.getPrefix();
			String rank_suffix = group.getSuffix();
			
			rank_prefix = group.getPrefixWithWorld(worldName);
			rank_suffix = group.getSuffixWithWorld(worldName);
			
			String player_prefix = player.getPrefix();
			String player_suffix = player.getSuffix();
			
			player_prefix = player.getPrefixWithWorld(worldName);
			player_suffix = player.getSuffixWithWorld(worldName);
			
			output = convert(output, "%player%", playerName);
			output = convert(output, "%world%", world);
			output = convert(output, "%rank_prefix%", rank_prefix);
			output = convert(output, "%rank_suffix%", rank_suffix);
			output = convert(output, "%player_prefix%", player_prefix);
			output = convert(output, "%player_suffix%", player_suffix);
			
		}
		
		Builder messageBuild = Text.builder();
		Text text = Text.builder()
				.append(
						TextSerializers.FORMATTING_CODE.deserialize(output),
						message
						)
				.build();
		
		return messageBuild.append(Text.of(text)).build();
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
