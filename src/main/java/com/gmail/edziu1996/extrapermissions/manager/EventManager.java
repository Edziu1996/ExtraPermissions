package com.gmail.edziu1996.extrapermissions.manager;


import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;

public class EventManager
{
	private RanksManager rm = new RanksManager();
	Game game = ExtraPermissions.getPlugin().getGame();
	
	@Listener
	public void onJoin(ClientConnectionEvent.Join event)
	{
		Player pl = event.getTargetEntity();
		
		rm.playerLoadRank(pl);
	}
	
	@Listener(order=Order.PRE)
	public void onMessageChat(MessageChannelEvent.Chat event)
	{
		if(event.getMessage().isEmpty()) { return; }
		
		MessageChannel channel = MessageChannel.TO_ALL;
		
		if (event.getCause().first(Player.class).isPresent())
		{
			String text = event.getMessage().toPlain();
			
			Text msg = MessageManager.transformChatMessage(event, text.substring(text.indexOf(" ") + 1));
			
			event.setChannel(channel);
			event.setMessage(msg);
		}
	}
	
	@Listener
	public void onBlockBreak(ChangeBlockEvent event)
	{
		if (event.getCause().containsType(CommandSource.class))
		{
			CommandSource source = event.getCause().first(CommandSource.class).get();
			
			if (source != null)
			{
				if (source instanceof Player)
				{
					Player p = (Player) source;
					
					if (!p.hasPermission("ExtraPerm.build"))
					{
						p.sendMessage(Text.of("You do not have permission to build"));
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
