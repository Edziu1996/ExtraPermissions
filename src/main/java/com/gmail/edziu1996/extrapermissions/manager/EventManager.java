package com.gmail.edziu1996.extrapermissions.manager;


import java.util.Map.Entry;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

public class EventManager
{
	private RanksManager rm = new RanksManager();
	
	@Listener
	public void onJoin(ClientConnectionEvent.Join event)
	{
		Player pl = event.getTargetEntity();
		//pl.getDisplayNameData().displayName().set(Text.of("Test"));
		
		rm.playerLoadRank(pl);
		
		for (Entry<String, Boolean> e : pl.getSubjectData().getAllPermissions().get(SubjectData.GLOBAL_CONTEXT).entrySet())
		{
			pl.sendMessage(Text.of(e.getKey() + ": " + e.getValue()));
		}
	}
	
	@Listener(order=Order.FIRST)
	public void onMessageChat(MessageChannelEvent.Chat event)
	{
		if(!event.getMessage().isPresent()) { return; }
		
		MessageChannel channel = MessageChannel.TO_ALL;
		
		
		
		String msgPlain = event.getMessage().get().toPlain();
		Text msg = MessageManager.transformChatMessage(event, msgPlain.substring(msgPlain.indexOf(" ") + 1));

		event.setChannel(channel);
		event.setMessage(msg);
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
