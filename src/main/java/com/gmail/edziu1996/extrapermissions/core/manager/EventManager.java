package com.gmail.edziu1996.extrapermissions.core.manager;

import java.util.UUID;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import com.gmail.edziu1996.extrapermissions.api.EPPlayer;
import com.gmail.edziu1996.extrapermissions.api.Manager;
import com.gmail.edziu1996.extrapermissions.core.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigPlayers;

public class EventManager
{
	private RanksManager rm = new RanksManager();
	Game game = ExtraPermissions.getPlugin().getGame();
	private ConfigPlayers playersConf = ExtraPermissions.getPlugin().playersConf;
	
	@Listener
	public void onJoin(ClientConnectionEvent.Join event)
	{
		Player pl = event.getTargetEntity();
		
		rm.playerLoadRank(new EPPlayer(pl), pl.getWorld().getName());
	}
	
	@Listener
	public void onDisconned(ClientConnectionEvent.Disconnect event)
	{
		Player pl = event.getTargetEntity();
		
		if (Manager.hasPlayer(pl.getUniqueId()))
		{
			Manager.removePlayer(pl);
			
			String sid = pl.getUniqueId().toString();
			
			if (playersConf.playersMap.containsKey(sid))
			{
				new EPPlayer(UUID.fromString(sid));
			}
		}
	}
	
	@Listener
	public void onTeleport(DisplaceEntityEvent.Teleport event)
	{
		Entity e = event.getTargetEntity();
		
		if (event.getFromTransform().getExtent().getName() != event.getToTransform().getExtent().getName())
		{
			if (e instanceof Player)
			{
				Player p = (Player) e;
				
				if (p != null)
				{
					rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()), event.getToTransform().getExtent().getName());
				}
			}
		}
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
