package com.gmail.edziu1996.extrapermissions.core.cmd;

import static org.spongepowered.api.text.Text.of;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.edziu1996.extrapermissions.api.EPGroup;
import com.gmail.edziu1996.extrapermissions.api.EPPlayer;
import com.gmail.edziu1996.extrapermissions.api.Manager;
import com.gmail.edziu1996.extrapermissions.core.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigLang;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigNormal;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigPlayers;
import com.gmail.edziu1996.extrapermissions.core.config.ConfigRanks;
import com.gmail.edziu1996.extrapermissions.core.manager.RanksManager;

public class CmdExtraPermExecutor implements CommandExecutor
{
	ConfigPlayers players = ExtraPermissions.getPlugin().playersConf;
	ConfigRanks ranks = ExtraPermissions.getPlugin().ranksConf;
	ConfigNormal conf = ExtraPermissions.getPlugin().config;
	ConfigLang lang = ExtraPermissions.getPlugin().langConf;
	RanksManager rm = new RanksManager();
	Game game = ExtraPermissions.getPlugin().getGame();
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		String option = args.<String>getOne("option").orElse(null);
		String world = args.<String>getOne("world").orElse(null);
		
		if (option != null)
		{
			if (src.hasPermission("ExtraPerm.experm.group.use") && (option.equalsIgnoreCase("group") || option.equalsIgnoreCase("g")))
			{
				if (world != null)
				{
					return cmdGroup(src, args, world);
				}
				else
				{
					return cmdGroup(src, args);
				}
			}
			else if (src.hasPermission("ExtraPerm.experm.player.use") && (option.equalsIgnoreCase("player") || option.equalsIgnoreCase("p")))
			{
				if (world != null)
				{
					return cmdPlayer(src, args, world);
				}
				else
				{
					return cmdPlayer(src, args);
				}
			}
			else if (src.hasPermission("ExtraPerm.experm.reload.use") && (option.equalsIgnoreCase("reload") || option.equalsIgnoreCase("rl")))
			{
				return cmdReload(src, args);
			}
			else if (src.hasPermission("ExtraPerm.experm.uuid.use") && (option.equalsIgnoreCase("uuid") || option.equalsIgnoreCase("id")))
			{
				return cmdUUID(src, args);
			}
			else if (src.hasPermission("ExtraPerm.experm.info.use") && (option.equalsIgnoreCase("info") || option.equalsIgnoreCase("i")))
			{
				if (world != null)
				{
					return cmdInfo(src, args, world);
				}
				else
				{
					return cmdInfo(src, args);
				}
			}
			else if (src.hasPermission("ExtraPerm.experm.list.use") && (option.equalsIgnoreCase("list") || option.equalsIgnoreCase("l")))
			{
				if (world != null)
				{
					return cmdList(src, args, world);
				}
				else
				{
					return cmdList(src, args);
				}
			}
			else
			{
				PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
				
				pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Extra Permissions Sub-Commands")).build());
				
				List<Text> list = new ArrayList<Text>();
				
				if (src.hasPermission("ExtraPerm.experm.group.use"))
				{
					list.add(Text.builder().append(of("/experm group <name> <prefix|suffix|permission|inheritance|remove> <value> [value] [-w <world_name>]")).onClick(TextActions.suggestCommand("/experm group")).build());
				}
				
				if (src.hasPermission("ExtraPerm.experm.reload.use"))
				{
					list.add(Text.builder().append(of("/experm reload")).onClick(TextActions.suggestCommand("/experm reload")).build());
				}
				
				if (src.hasPermission("ExtraPerm.experm.player.use"))
				{
					list.add(Text.builder().append(of("/experm player <name> <prefix|suffix|rank|remove> <value> [-t <ms|s|m|h|d|mo|y> <time>] [-w <world_name>]")).onClick(TextActions.suggestCommand("/experm player")).build());
				}
				
				if (src.hasPermission("ExtraPerm.experm.uuid.use"))
				{
					list.add(Text.builder().append(of("/experm uuid")).onClick(TextActions.suggestCommand("/experm uuid")).build());
				}
				
				if (src.hasPermission("ExtraPerm.experm.info.use"))
				{
					list.add(Text.builder().append(of("/experm info <player|group> [name]  [-w <world_name>]")).onClick(TextActions.suggestCommand("/experm info")).build());
				}
				
				if (src.hasPermission("ExtraPerm.experm.list.use"))
				{
					list.add(Text.builder().append(of("/experm list <players|groups> [name]  [-w <world_name>]")).onClick(TextActions.suggestCommand("/experm list")).build());
				}
				
				pages.contents(list);
				pages.sendTo(src);
			}
		}
		else
		{
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Extra Permissions Sub-Commands")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			if (src.hasPermission("ExtraPerm.experm.group.use"))
			{
				list.add(Text.builder().append(of("/experm group <name> <prefix|suffix|permission|inheritance> <value> [permission value]")).onClick(TextActions.suggestCommand("/experm group")).build());
			}
			
			if (src.hasPermission("ExtraPerm.experm.reload.use"))
			{
				list.add(Text.builder().append(of("/experm reload")).onClick(TextActions.suggestCommand("/experm reload")).build());
			}
			
			if (src.hasPermission("ExtraPerm.experm.player.use"))
			{
				list.add(Text.builder().append(of("/experm player <name> <prefix|suffix|rank> <value> [-t <ms|s|m|h|d|mo|y> <time>]")).onClick(TextActions.suggestCommand("/experm player")).build());
			}
			
			if (src.hasPermission("ExtraPerm.experm.uuid.use"))
			{
				list.add(Text.builder().append(of("/experm uuid")).onClick(TextActions.suggestCommand("/experm uuid")).build());
			}
			
			if (src.hasPermission("ExtraPerm.experm.info.use"))
			{
				list.add(Text.builder().append(of("/experm info <player|group> [name]")).onClick(TextActions.suggestCommand("/experm info")).build());
			}
			
			if (src.hasPermission("ExtraPerm.experm.list.use"))
			{
				list.add(Text.builder().append(of("/experm list <players|groups> [name]")).onClick(TextActions.suggestCommand("/experm list")).build());
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		
		return CommandResult.empty();
	}

	private CommandResult cmdPlayer(CommandSource src, CommandContext args)
	{
		if (!args.hasAny("option2") || !args.hasAny("option3") || !args.hasAny("value"))
		{
			sendMessage(src, lang.commandUsage + "/extraperm player <name> <prefix|suffix|rank|permission|remove> [name]");
			return CommandResult.empty();
		}
		
		String name = args.<String>getOne("option2").get();
		String option = args.<String>getOne("option3").get();
		String value = args.<String>getOne("value").get();
		
		Player p = game.getServer().getPlayer(name).orElse(null);
		
		if (p != null)
		{
			String sid = p.getUniqueId().toString();
			
			if (option.equalsIgnoreCase("permission") && args.hasAny("value2"))
			{
				boolean bool = Boolean.valueOf(args.<String>getOne("value2").get());
				
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.player.permission";
					
					if (src.hasPermission(perm))
					{
						players.get().getNode(sid, "permissions", value).setValue(bool);
						players.save();
						players.loadByPlayer(p);
						
						String out1 = lang.newPermPlayer1.replace("%name%", name).replace("%perm%", value).replace("%value%", bool + "");
						String out2 = lang.newPermPlayer2.replace("%perm%", value).replace("%value%", bool + "");
						
						Manager.getPlayer(p.getUniqueId()).reload();
						
						rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
						
						sendMessage(src, out1);
						sendMessage(p, out2);
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					players.get().getNode(sid, "permissions", value).setValue(bool);
					players.save();
					players.loadByPlayer(p);
					
					String out1 = lang.newPermPlayer1.replace("%name%", name).replace("%perm%", value).replace("%value%", bool + "");
					String out2 = lang.newPermPlayer2.replace("%perm%", value).replace("%value%", bool + "");

					Manager.getPlayer(p.getUniqueId()).reload();
					
					rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
					
					sendMessage(src, out1);
					sendMessage(p, out2);
				}
			}
			else if (option.equalsIgnoreCase("permission") && !args.hasAny("value2"))
			{
				sendMessage(src, lang.commandUsage + "/extraperm player <name> permission <name> <value>");
				return CommandResult.empty();
			}
			else if (option.equalsIgnoreCase("prefix"))
			{
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.player.prefix";
					
					if (src.hasPermission(perm))
					{
						players.get().getNode(sid, option).setValue(value);
						players.save();
						players.loadByPlayer(p);
						
						String out1 = lang.newPrefixPlayer1.replace("%name%", name).replace("%prefix%", value);
						String out2 = lang.newPrefixPlayer2.replace("%prefix%", value);
						
						Manager.getPlayer(p.getUniqueId()).reload();
						
						sendMessage(src, out1);
						sendMessage(p, out2);
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					players.get().getNode(sid, option).setValue(value);
					players.save();
					players.loadByPlayer(p);
					
					String out1 = lang.newPrefixPlayer1.replace("%name%", name).replace("%prefix%", value);
					String out2 = lang.newPrefixPlayer2.replace("%prefix%", value);
					
					Manager.getPlayer(p.getUniqueId()).reload();
					
					sendMessage(src, out1);
					sendMessage(p, out2);
				}
			}
			else if (option.equalsIgnoreCase("suffix"))
			{
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.player.suffix";
					
					if (src.hasPermission(perm))
					{
						players.get().getNode(sid, option).setValue(value);
						players.save();
						players.loadByPlayer(p);
						
						String out1 = lang.newSuffixPlayer1.replace("%name%", name).replace("%suffix%", value);
						String out2 = lang.newSuffixPlayer2.replace("%suffix%", value);
						
						Manager.getPlayer(p.getUniqueId()).reload();
						
						sendMessage(src, out1);
						sendMessage(p, out2);
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					players.get().getNode(sid, option).setValue(value);
					players.save();
					players.loadByPlayer(p);
					
					String out1 = lang.newSuffixPlayer1.replace("%name%", name).replace("%suffix%", value);
					String out2 = lang.newSuffixPlayer2.replace("%suffix%", value);
					
					Manager.getPlayer(p.getUniqueId()).reload();
					
					sendMessage(src, out1);
					sendMessage(p, out2);
				}
			}
			else if (option.equalsIgnoreCase("rank"))
			{
				if (src instanceof Player)
				{
					EPPlayer player = Manager.getPlayer(p.getUniqueId());
					
					String perm = "extraperm.experm.player.remove.rank." + player.getGroupName();
					String perm2 = "extraperm.experm.player.rank." + value;
					
					String perm2Def = null;
					
					if (value.equals(Manager.getDefaultGroupName()))
					{
						perm2Def = "extraperm.experm.player.rank.default";
					}
					
					String permDef = null;
					
					if (player.getGroupName().equals(Manager.getDefaultGroupName()))
					{
						permDef = "extraperm.experm.player.remove.rank.default";
					}
					
					if ((src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false)) && (src.hasPermission(perm2) || (perm2Def != null ? src.hasPermission(perm2Def) : false)))
					{
						setRank(args, player, src, name, value);
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm) + "and " + perm2;
						sendMessage(src, out);
					}
				}
				else
				{
					EPPlayer player = Manager.getPlayer(p.getUniqueId());
					setRank(args, player, src, name, value);
				}
				
			}
			else if (option.equalsIgnoreCase("remove"))
			{
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.player.remove." + value;
					String perm2 = null;
					
					if (value.equals("rank"))
					{
						EPPlayer player = Manager.getPlayer(p.getUniqueId());
						
						perm = "extraperm.experm.player.remove.rank." + player.getGroupName();
						
						if (player.getGroupName().equals(Manager.getDefaultGroupName()))
						{
							perm2 = "extraperm.experm.player.remove.rank.default";
						}
					}
					
					if (src.hasPermission(perm) || (perm2 != null ? src.hasPermission(perm2) : false))
					{
						if (args.hasAny("value2") && value.equals("permission"))
						{
							players.get().getNode(sid).removeChild(args.<String>getOne("value2").get());
							players.save();
							players.loadByPlayer(p);

							Manager.getPlayer(p.getUniqueId()).reload();
							
							rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
							
							String out = lang.removeValPlayer.replace("%name%", name).replace("%value%", args.<String>getOne("value2").get());
							
							sendMessage(src, out);
						}
						else
						{
							players.get().getNode(sid).removeChild(value);
							players.save();
							players.loadByPlayer(p);

							Manager.getPlayer(p.getUniqueId()).reload();
							
							rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
							
							String out = lang.removeValPlayer.replace("%name%", name).replace("%value%", value);
							
							sendMessage(src, out);
						}
						
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					if (args.hasAny("value2") && value.equals("permission"))
					{
						players.get().getNode(sid).removeChild(args.<String>getOne("value2").get());
						players.save();
						players.loadByPlayer(p);

						Manager.getPlayer(p.getUniqueId()).reload();
						
						rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
						
						String out = lang.removeValPlayer.replace("%name%", name).replace("%value%", args.<String>getOne("value2").get());
						
						sendMessage(src, out);
					}
					else
					{
						players.get().getNode(sid).removeChild(value);
						players.save();
						players.loadByPlayer(p);

						Manager.getPlayer(p.getUniqueId()).reload();
						
						rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
						
						String out = lang.removeValPlayer.replace("%name%", name).replace("%value%", value);
						
						sendMessage(src, out);
					}
				}
				
			}
			else
			{
				sendMessage(src, lang.commandUsage + "/extraperm player <name> <prefix|suffix|rank|permission|remove> [name]");
				return CommandResult.empty();
			}
		}
		else
		{
			sendMessage(src, lang.mustPlayerOnline);
			return CommandResult.empty();
		}
		return CommandResult.success();
	}
	

	private CommandResult cmdPlayer(CommandSource src, CommandContext args, String world)
	{
		if (!args.hasAny("option2") || !args.hasAny("option3") || !args.hasAny("value"))
		{
			sendMessage(src, lang.commandUsage + "/extraperm player <name> <prefix|suffix|rank|permission|remove> [name] -w <world>");
			return CommandResult.empty();
		}
		
		String name = args.<String>getOne("option2").get();
		String option = args.<String>getOne("option3").get();
		String value = args.<String>getOne("value").get();
		
		Player p = game.getServer().getPlayer(name).orElse(null);
		
		if (p != null)
		{
			String sid = p.getUniqueId().toString();
			
			if (option.equalsIgnoreCase("permission") && args.hasAny("value2"))
			{
				boolean bool = Boolean.valueOf(args.<String>getOne("value2").get());
				
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.player.permission." + world;
					
					if (src.hasPermission(perm))
					{
						players.get().getNode(sid, world, "permissions", value).setValue(bool);
						players.save();
						players.loadByPlayer(p);
						
						String out1 = lang.newPermWorldPlayer1.replace("%name%", name).replace("%perm%", value).replace("%value%", bool + "").replace("%world%", world);
						String out2 = lang.newPermWorldPlayer2.replace("%perm%", value).replace("%value%", bool + "").replace("%world%", world);
						
						Manager.getPlayer(p.getUniqueId()).reload();
						
						rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
						
						sendMessage(src, out1);
						sendMessage(p, out2);
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					players.get().getNode(sid, world, "permissions", value).setValue(bool);
					players.save();
					players.loadByPlayer(p);
					
					String out1 = lang.newPermWorldPlayer1.replace("%name%", name).replace("%perm%", value).replace("%value%", bool + "").replace("%world%", world);
					String out2 = lang.newPermWorldPlayer2.replace("%perm%", value).replace("%value%", bool + "").replace("%world%", world);

					Manager.getPlayer(p.getUniqueId()).reload();
					
					rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
					
					sendMessage(src, out1);
					sendMessage(p, out2);
				}
			}
			else if (option.equalsIgnoreCase("permission") && !args.hasAny("value2"))
			{
				sendMessage(src, lang.commandUsage + "/extraperm player <name> permission <name> <value> -w <world>");
				return CommandResult.empty();
			}
			else if (option.equalsIgnoreCase("prefix"))
			{
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.player.prefix." + world;
					
					if (src.hasPermission(perm))
					{
						players.get().getNode(sid, world, option).setValue(value);
						players.save();
						players.loadByPlayer(p);
						
						String out1 = lang.newPrefixWorldPlayer1.replace("%name%", name).replace("%prefix%", value).replace("%world%", world);
						String out2 = lang.newPrefixWorldPlayer2.replace("%prefix%", value).replace("%world%", world);
						
						Manager.getPlayer(p.getUniqueId()).reload();
						
						sendMessage(src, out1);
						sendMessage(p, out2);
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					players.get().getNode(sid, world, option).setValue(value);
					players.save();
					players.loadByPlayer(p);
					
					String out1 = lang.newPrefixWorldPlayer1.replace("%name%", name).replace("%prefix%", value).replace("%world%", world);
					String out2 = lang.newPrefixWorldPlayer2.replace("%prefix%", value).replace("%world%", world);
					
					Manager.getPlayer(p.getUniqueId()).reload();
					
					sendMessage(src, out1);
					sendMessage(p, out2);
				}
			}
			else if (option.equalsIgnoreCase("suffix"))
			{
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.player.suffix." + world;
					
					if (src.hasPermission(perm))
					{
						players.get().getNode(sid, world, option).setValue(value);
						players.save();
						players.loadByPlayer(p);
						
						String out1 = lang.newSuffixWorldPlayer1.replace("%name%", name).replace("%suffix%", value).replace("%world%", world);
						String out2 = lang.newSuffixWorldPlayer2.replace("%suffix%", value).replace("%world%", world);
						
						Manager.getPlayer(p.getUniqueId()).reload();
						
						sendMessage(src, out1);
						sendMessage(p, out2);
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					players.get().getNode(sid, world, option).setValue(value);
					players.save();
					players.loadByPlayer(p);
					
					String out1 = lang.newSuffixWorldPlayer1.replace("%name%", name).replace("%suffix%", value).replace("%world%", world);
					String out2 = lang.newSuffixWorldPlayer2.replace("%suffix%", value).replace("%world%", world);
					
					Manager.getPlayer(p.getUniqueId()).reload();
					
					sendMessage(src, out1);
					sendMessage(p, out2);
				}
			}
			else if (option.equalsIgnoreCase("remove"))
			{
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.player.remove." + value + "." + world;
					String perm2 = null;
					
					if (world.equals(game.getServer().getDefaultWorldName()))
					{
						perm2 = "extraperm.experm.player.remove." + value + ".dafault";
					}
					
					if (src.hasPermission(perm) || (perm2 != null ? src.hasPermission(perm2) : false))
					{
						
						if (args.hasAny("value2") && value.equals("permission"))
						{
							players.get().getNode(sid, world, "permission").removeChild(args.<String>getOne("value2").get());
							players.save();
							players.loadByPlayer(p);

							Manager.getPlayer(p.getUniqueId()).reload();
							
							rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
							
							String out = lang.removeValWorldPlayer.replace("%name%", name).replace("%value%", args.<String>getOne("value2").get()).replace("%world%", world);
							
							sendMessage(src, out);
						}
						else
						{
							players.get().getNode(sid, world).removeChild(value);
							players.save();
							players.loadByPlayer(p);

							Manager.getPlayer(p.getUniqueId()).reload();
							
							rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
							
							String out = lang.removeValWorldPlayer.replace("%name%", name).replace("%value%", value).replace("%world%", world);
							
							sendMessage(src, out);
						}
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					if (args.hasAny("value2") && value.equals("permission"))
					{
						players.get().getNode(sid, world, "permissions").removeChild(args.<String>getOne("value2").get());
						players.save();
						players.loadByPlayer(p);

						Manager.getPlayer(p.getUniqueId()).reload();
						
						rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
						
						String out = lang.removeValWorldPlayer.replace("%name%", name).replace("%value%", args.<String>getOne("value2").get()).replace("%world%", world);
						
						sendMessage(src, out);
					}
					else
					{
						players.get().getNode(sid, world).removeChild(value);
						players.save();
						players.loadByPlayer(p);

						Manager.getPlayer(p.getUniqueId()).reload();
						
						rm.playerLoadRank(Manager.getPlayer(p.getUniqueId()));
						
						String out = lang.removeValWorldPlayer.replace("%name%", name).replace("%value%", value).replace("%world%", world);
						
						sendMessage(src, out);
					}
				}
				
			}
			else
			{
				sendMessage(src, lang.commandUsage + "/extraperm player <name> <prefix|suffix|rank|permission|remove> [name] -w <world>");
				return CommandResult.empty();
			}
		}
		else
		{
			sendMessage(src, lang.mustPlayerOnline);
			return CommandResult.empty();
		}
		return CommandResult.success();
	}
	
	
	private CommandResult cmdList(CommandSource src, CommandContext args)
	{
		if (!args.hasAny("option2"))
		{
			sendMessage(src, lang.commandUsage + "/extraperm list <players|groups> [name]");
			return CommandResult.empty();
		}
		
		String opt = args.<String>getOne("option2").get();
		
		if (opt.equalsIgnoreCase("groups") && !args.hasAny("option3"))
		{
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Ranks: ")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (EPGroup group : Manager.getGroups())
			{
				list.add(Text.of(TextColors.AQUA, "- " + group.getName()));
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else if (opt.equalsIgnoreCase("groups") && args.hasAny("option3"))
		{
			String rankName = args.<String>getOne("option3").get();
			
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player from rank: " + rankName)).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (EPPlayer player : Manager.getPlayers())
			{
				if (player.getGroupName().equals(rankName))
				{
					if (game.getServer().getOnlinePlayers().contains(player.getPlayer()))
					{
						list.add(of(TextColors.GREEN, "- " + player.getName()));
					}
					else
					{
						list.add(of(TextColors.RED, "- " + player.getName()));
					}
				}
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else if (opt.equalsIgnoreCase("players"))
		{
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player: rank")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (EPPlayer player : Manager.getPlayers())
			{
				if (game.getServer().getOnlinePlayers().contains(player.getPlayer()))
				{
					list.add(Text.of(TextColors.GREEN, "- " + player.getName() + ": ", TextColors.BLUE, player.getGroupName()));
				}
				else
				{
					list.add(Text.of(TextColors.RED, "- " + player.getName() + ": ", TextColors.BLUE, player.getGroupName()));
				}
			}

			pages.contents(list);
			pages.sendTo(src);
		}
		else
		{
			sendMessage(src, lang.commandUsage + "/extraperm list <players|groups> [name]");
			return CommandResult.empty();
		}
		
		return CommandResult.success();
	}
	

	private CommandResult cmdList(CommandSource src, CommandContext args, String world)
	{
		if (!args.hasAny("option2"))
		{
			sendMessage(src, lang.commandUsage + "/extraperm list <players|groups> [name] -w <world>");
			return CommandResult.empty();
		}
		
		String opt = args.<String>getOne("option2").get();
		
		if (opt.equalsIgnoreCase("groups") && !args.hasAny("option3"))
		{
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Ranks: ")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (EPGroup group : Manager.getGroups())
			{
				list.add(Text.of(TextColors.AQUA, "- " + group.getName()));
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else if (opt.equalsIgnoreCase("groups") && args.hasAny("option3"))
		{
			String rankName = args.<String>getOne("option3").get();
			
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player from rank: " + rankName)).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (EPPlayer player : Manager.getPlayers())
			{
				if (player.getPlayer().getWorld().getName().equals(world))
				{
					if (player.getGroupName().equals(rankName))
					{
						if (game.getServer().getOnlinePlayers().contains(player.getPlayer()))
						{
							list.add(of(TextColors.GREEN, "- " + player.getName()));
						}
						else
						{
							list.add(of(TextColors.RED, "- " + player.getName()));
						}
					}
				}
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else if (opt.equalsIgnoreCase("players"))
		{
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player: rank")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (EPPlayer player : Manager.getPlayers())
			{
				if (player.getPlayer().getWorld().getName().equals(world))
				{
					if (game.getServer().getOnlinePlayers().contains(player.getPlayer()))
					{
						list.add(Text.of(TextColors.GREEN, "- " + player.getName() + ": ", TextColors.BLUE, player.getGroupName()));
					}
					else
					{
						list.add(Text.of(TextColors.RED, "- " + player.getName() + ": ", TextColors.BLUE, player.getGroupName()));
					}
				}
				
			}

			pages.contents(list);
			pages.sendTo(src);
		}
		else
		{
			sendMessage(src, lang.commandUsage + "/extraperm list <players|groups> [name] -w <world>");
			return CommandResult.empty();
		}
		
		return CommandResult.success();
	}
	
	
	private CommandResult cmdGroup(CommandSource src, CommandContext args)
	{
		if (!args.hasAny("option2") || !args.hasAny("option3") || !args.hasAny("value"))
		{
			sendMessage(src, lang.commandUsage + "/extraperm group <name> <prefix|suffix|permission|inheritance|remove> <value> [value]");
			return CommandResult.empty();
		}
		
		String name = args.<String>getOne("option2").get();
		String option = args.<String>getOne("option3").get();
		String value = args.<String>getOne("value").get();
		
		if (option.equalsIgnoreCase("prefix"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".prefix";
				String permDef = null;
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()))
				{
					permDef = "extraperm.experm.group.default.prefix";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
				{
					ranks.get().getNode(name, option).setValue(value);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.newPrefixGroup.replace("%group%", name).replace("%prefix%", value);
					
					sendMessage(src, out);
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					sendMessage(src, out);
				}
			}
			else
			{
				ranks.get().getNode(name, option).setValue(value);
				ranks.save();
				ranks.loadByRank(name);
				
				Manager.getGroup(name).reload();
				Manager.reloadPlayersByRank(name);
				
				String out = lang.newPrefixGroup.replace("%group%", name).replace("%prefix%", value);
				
				sendMessage(src, out);
			}
		}
		else if (option.equalsIgnoreCase("suffix"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".suffix";
				String permDef = null;
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()))
				{
					permDef = "extraperm.experm.group.default.suffix";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
				{
					ranks.get().getNode(name, option).setValue(value);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.newSuffixGroup.replace("%group%", name).replace("%suffix%", value);
					
					sendMessage(src, out);
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					sendMessage(src, out);
				}
			}
			else
			{
				ranks.get().getNode(name, option).setValue(value);
				ranks.save();
				ranks.loadByRank(name);
				
				Manager.getGroup(name).reload();
				Manager.reloadPlayersByRank(name);
				
				String out = lang.newSuffixGroup.replace("%group%", name).replace("%suffix%", value);
				
				sendMessage(src, out);
			}
		}
		else if (option.equalsIgnoreCase("permission") || option.equalsIgnoreCase("perm"))
		{
			if (args.hasAny("value2"))
			{
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.group." + name + ".permission";
					String permDef = null;
					
					if (name.equalsIgnoreCase(Manager.getDefaultGroupName()))
					{
						permDef = "extraperm.experm.group.default.permission";
					}
					
					if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
					{
						boolean bool = Boolean.valueOf(args.<String>getOne("value2").get());
						
						ranks.get().getNode(name, "permissions", value).setValue(bool);
						ranks.save();
						ranks.loadByRank(name);
						
						Manager.getGroup(name).reload();
						Manager.reloadPlayersByRank(name);
						
						String out = lang.newPermGroup.replace("%group%", name).replace("%perm%", value).replace("%value%", bool + "");
						
						sendMessage(src, out);
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					boolean bool = args.<Boolean>getOne("value2").get();
					
					ranks.get().getNode(name, "permissions", value).setValue(bool);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.newPermGroup.replace("%group%", name).replace("%perm%", value).replace("%value%", bool + "");
					
					sendMessage(src, out);
				}
			}
			else
			{
				sendMessage(src, lang.commandUsage + "/experm group <rank_name> permission <permission> <true|false>");
			}
		}
		else if (option.equalsIgnoreCase("inheritance"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".inheritance";
				String permDef = null;
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()))
				{
					permDef = "extraperm.experm.group.default.inheritance";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
				{
					ranks.get().getNode(name, option).setValue(value);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.newInheGroup.replace("%group%", name).replace("%inhe%", value);
					
					sendMessage(src, out);
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					sendMessage(src, out);
				}
			}
			else
			{
				ranks.get().getNode(name, option).setValue(value);
				ranks.save();
				ranks.loadByRank(name);
				
				Manager.getGroup(name).reload();
				Manager.reloadPlayersByRank(name);
				
				String out = lang.newInheGroup.replace("%group%", name).replace("%inhe%", value);
				
				sendMessage(src, out);
			}
		}
		else if (option.equalsIgnoreCase("remove"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".remove." + value;
				String permDef = null;
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()))
				{
					permDef = "extraperm.experm.group.default.remove." + value;
				}
				
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false))
				{
					if (args.hasAny("value2") && value.equals("permission"))
					{
						ranks.get().getNode(name, "permissions").removeChild(args.<String>getOne("value2").get());
						ranks.save();
						ranks.loadByRank(name);
						
						Manager.getGroup(name).reload();
						Manager.reloadPlayersByRank(name);
						
						String out = lang.removeValGroup.replace("%group%", name).replace("%value%", args.<String>getOne("value2").get());
						
						sendMessage(src, out);
					}
					else
					{
						ranks.get().getNode(name).removeChild(value);
						ranks.save();
						ranks.loadByRank(name);
						
						Manager.getGroup(name).reload();
						Manager.reloadPlayersByRank(name);
						
						String out = lang.removeValGroup.replace("%group%", name).replace("%value%", value);
						
						sendMessage(src, out);
					}
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					sendMessage(src, out);
				}
			}
			else
			{
				if (args.hasAny("value2") && value.equals("permission"))
				{
					ranks.get().getNode(name).removeChild(args.<String>getOne("value2").get());
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.removeValGroup.replace("%group%", name).replace("%value%", args.<String>getOne("value2").get());
					
					sendMessage(src, out);
				}
				else
				{
					ranks.get().getNode(name).removeChild(value);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.removeValGroup.replace("%group%", name).replace("%value%", value);
					
					sendMessage(src, out);
				}
				
			}
		}
		else
		{
			sendMessage(src, lang.commandUsage + "/extraperm group <name> <prefix|suffix|permission|inheritance> <value> [permission value]");
			return CommandResult.empty();
		}
		
		return CommandResult.success();
	}
	

	private CommandResult cmdGroup(CommandSource src, CommandContext args, String world)
	{
		if (!args.hasAny("option2") || !args.hasAny("option3") || !args.hasAny("value"))
		{
			sendMessage(src, lang.commandUsage + "/extraperm group <name> <prefix|suffix|permission|inheritance|remove> <value> [value] -w <world>");
			return CommandResult.empty();
		}
		
		String name = args.<String>getOne("option2").get();
		String option = args.<String>getOne("option3").get();
		String value = args.<String>getOne("value").get();
		
		if (option.equalsIgnoreCase("prefix"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".prefix." + world;
				String permDef = null;
				String perm2Def = null;
				String perm3Def = null;
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && !world.equals(game.getServer().getDefaultWorldName()))
				{
					permDef = "extraperm.experm.group.default.prefix." + world;
				}
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && world.equals(game.getServer().getDefaultWorldName()))
				{
					perm2Def = "extraperm.experm.group.default.prefix.default";
				}
				
				if (world.equals(game.getServer().getDefaultWorldName()) && !name.equalsIgnoreCase(Manager.getDefaultGroupName()))
				{
					perm3Def = "extraperm.experm.group." + name + ".prefix.default";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false) || (perm2Def != null ? src.hasPermission(perm2Def) : false) || (perm3Def != null ? src.hasPermission(perm3Def) : false))
				{
					ranks.get().getNode(name, world, option).setValue(value);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.newPrefixWorldGroup.replace("%group%", name).replace("%prefix%", value).replace("%world", world);
					
					sendMessage(src, out);
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					sendMessage(src, out);
				}
			}
			else
			{
				ranks.get().getNode(name, world, option).setValue(value);
				ranks.save();
				ranks.loadByRank(name);
				
				Manager.getGroup(name).reload();
				Manager.reloadPlayersByRank(name);
				
				String out = lang.newPrefixWorldGroup.replace("%group%", name).replace("%prefix%", value).replace("%world", world);
				
				sendMessage(src, out);
			}
		}
		else if (option.equalsIgnoreCase("suffix"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".suffix." + world;
				String permDef = null;
				String perm2Def = null;
				String perm3Def = null;
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && !world.equals(game.getServer().getDefaultWorldName()))
				{
					permDef = "extraperm.experm.group.default.suffix." + world;
				}
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && world.equals(game.getServer().getDefaultWorldName()))
				{
					perm2Def = "extraperm.experm.group.default.suffix.default";
				}
				
				if (world.equals(game.getServer().getDefaultWorldName()) && !name.equalsIgnoreCase(Manager.getDefaultGroupName()))
				{
					perm3Def = "extraperm.experm.group." + name + ".suffix.default";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false) || (perm2Def != null ? src.hasPermission(perm2Def) : false) || (perm3Def != null ? src.hasPermission(perm3Def) : false))
				{
					ranks.get().getNode(name, world, option).setValue(value);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.newSuffixWorldGroup.replace("%group%", name).replace("%suffix%", value).replace("%world", world);
					
					sendMessage(src, out);
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					sendMessage(src, out);
				}
			}
			else
			{
				ranks.get().getNode(name, world, option).setValue(value);
				ranks.save();
				ranks.loadByRank(name);
				
				Manager.getGroup(name).reload();
				Manager.reloadPlayersByRank(name);
				
				String out = lang.newSuffixWorldGroup.replace("%group%", name).replace("%suffix%", value).replace("%world", world);
				
				sendMessage(src, out);
			}
		}
		else if (option.equalsIgnoreCase("permission") || option.equalsIgnoreCase("perm"))
		{
			if (args.hasAny("value2"))
			{
				if (src instanceof Player)
				{
					String perm = "extraperm.experm.group." + name + ".permission." + world;
					String permDef = null;
					String perm2Def = null;
					String perm3Def = null;
					
					if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && !world.equals(game.getServer().getDefaultWorldName()))
					{
						permDef = "extraperm.experm.group.default.permission." + world;
					}
					
					if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && world.equals(game.getServer().getDefaultWorldName()))
					{
						perm2Def = "extraperm.experm.group.default.permission.default";
					}
					
					if (world.equals(game.getServer().getDefaultWorldName()) && !name.equalsIgnoreCase(Manager.getDefaultGroupName()))
					{
						perm3Def = "extraperm.experm.group." + name + ".permission.default";
					}
					
					if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false) || (perm2Def != null ? src.hasPermission(perm2Def) : false) || (perm3Def != null ? src.hasPermission(perm3Def) : false))
					{
						boolean bool = Boolean.valueOf(args.<String>getOne("value2").get());
						
						ranks.get().getNode(name, world, "permissions", value).setValue(bool);
						ranks.save();
						ranks.loadByRank(name);
						
						Manager.getGroup(name).reload();
						Manager.reloadPlayersByRank(name);
						
						String out = lang.newPermWorldGroup.replace("%group%", name).replace("%perm%", value).replace("%value%", bool + "").replace("%world", world);
						
						sendMessage(src, out);
					}
					else
					{
						String out = lang.dontHave.replace("%perm%", perm);
						sendMessage(src, out);
					}
				}
				else
				{
					boolean bool = args.<Boolean>getOne("value2").get();
					
					ranks.get().getNode(name, world, "permissions", value).setValue(bool);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.newPermWorldGroup.replace("%group%", name).replace("%perm%", value).replace("%value%", bool + "").replace("%world", world);
					
					sendMessage(src, out);
				}
			}
			else
			{
				sendMessage(src, lang.commandUsage + "/experm group <rank_name> permission <permission> <true|false>");
			}
		}
		else if (option.equalsIgnoreCase("inheritance"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".inheritance";
				String permDef = null;
				String perm2Def = null;
				String perm3Def = null;
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && !world.equals(game.getServer().getDefaultWorldName()))
				{
					permDef = "extraperm.experm.group.default.inheritance." + world;
				}
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && world.equals(game.getServer().getDefaultWorldName()))
				{
					perm2Def = "extraperm.experm.group.default.inheritance.default";
				}
				
				if (world.equals(game.getServer().getDefaultWorldName()) && !name.equalsIgnoreCase(Manager.getDefaultGroupName()))
				{
					perm3Def = "extraperm.experm.group." + name + ".inheritance.default";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false) || (perm2Def != null ? src.hasPermission(perm2Def) : false) || (perm3Def != null ? src.hasPermission(perm3Def) : false))
				{
					ranks.get().getNode(name, option).setValue(value);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.newInheGroup.replace("%group%", name).replace("%inhe%", value);
					
					sendMessage(src, out);
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					sendMessage(src, out);
				}
			}
			else
			{
				ranks.get().getNode(name, option).setValue(value);
				ranks.save();
				ranks.loadByRank(name);
				
				Manager.getGroup(name).reload();
				Manager.reloadPlayersByRank(name);
				
				String out = lang.newInheGroup.replace("%group%", name).replace("%inhe%", value);
				
				sendMessage(src, out);
			}
		}
		else if (option.equalsIgnoreCase("remove"))
		{
			if (src instanceof Player)
			{
				String perm = "extraperm.experm.group." + name + ".remove." + value + "." + world;
				String permDef = null;
				String perm2Def = null;
				String perm3Def = null;
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && !world.equals(game.getServer().getDefaultWorldName()))
				{
					permDef = "extraperm.experm.group.default.remove." + value + "." + world;
				}
				
				if (name.equalsIgnoreCase(Manager.getDefaultGroupName()) && world.equals(game.getServer().getDefaultWorldName()))
				{
					perm2Def = "extraperm.experm.group.default.remove." + value + ".default";
				}
				
				if (world.equals(game.getServer().getDefaultWorldName()) && !name.equalsIgnoreCase(Manager.getDefaultGroupName()))
				{
					perm3Def = "extraperm.experm.group." + name + ".remove." + value + ".default";
				}
				
				if (src.hasPermission(perm) || (permDef != null ? src.hasPermission(permDef) : false) || (perm2Def != null ? src.hasPermission(perm2Def) : false) || (perm3Def != null ? src.hasPermission(perm3Def) : false))
				{
					if (args.hasAny("value2") && value.equals("permission"))
					{
						ranks.get().getNode(name, world, "permissions").removeChild(args.<String>getOne("value2").get());
						ranks.save();
						ranks.loadByRank(name);
						
						Manager.getGroup(name).reload();
						Manager.reloadPlayersByRank(name);
						
						String out = lang.removeValWorldGroup.replace("%group%", name).replace("%value%", args.<String>getOne("value2").get()).replace("%world", world);
						
						sendMessage(src, out);
					}
					else
					{
						ranks.get().getNode(name, world).removeChild(value);
						ranks.save();
						ranks.loadByRank(name);
						
						Manager.getGroup(name).reload();
						Manager.reloadPlayersByRank(name);
						
						String out = lang.removeValWorldGroup.replace("%group%", name).replace("%value%", value).replace("%world", world);
						
						sendMessage(src, out);
					}
				}
				else
				{
					String out = lang.dontHave.replace("%perm%", perm);
					sendMessage(src, out);
				}
			}
			else
			{
				if (args.hasAny("value2") && value.equals("permission"))
				{
					ranks.get().getNode(name, world).removeChild(args.<String>getOne("value2").get());
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.removeValWorldGroup.replace("%group%", name).replace("%value%", args.<String>getOne("value2").get()).replace("%world", world);
					
					sendMessage(src, out);
				}
				else
				{
					ranks.get().getNode(name, world).removeChild(value);
					ranks.save();
					ranks.loadByRank(name);
					
					Manager.getGroup(name).reload();
					Manager.reloadPlayersByRank(name);
					
					String out = lang.removeValWorldGroup.replace("%group%", name).replace("%value%", value).replace("%world", world);
					
					sendMessage(src, out);
				}
			}
		}
		else
		{
			sendMessage(src, lang.commandUsage + "/extraperm group <name> <prefix|suffix|permission|inheritance> <value> [value] -w <world>");
			return CommandResult.empty();
		}
		
		return CommandResult.success();
	}
	

	private CommandResult cmdInfo(CommandSource src, CommandContext args)
	{
		if (!args.hasAny("option2"))
		{
			sendMessage(src, lang.commandUsage + "/extraperm info <player|group> [name]");
			return CommandResult.empty();
		}
		
		String opt = args.<String>getOne("option2").get();
		
		if (opt.equalsIgnoreCase("group") && args.hasAny("option3"))
		{
			String name = args.<String>getOne("option3").get();
			
			if (Manager.hasGroup(name))
			{
				EPGroup group = Manager.getGroup(name);
				
				PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
				pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Rank: " + name)).build());
				
				List<Text> list = new ArrayList<Text>();
				
				for (Entry<String, String> d : group.getPrefixes().entrySet())
				{
					if (d.getKey().equals("default"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Prefix: ", TextColors.BLUE , group.getPrefix()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(group.getPrefix()), ")"))
								.build());
					}
					
					if (!d.getKey().equals("default"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Prefix in " + d.getKey() + ": ", TextColors.BLUE , d.getValue()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(d.getValue()), ")"))
								.build());
					}
				}
				
				for (Entry<String, String> d : group.getSuffixes().entrySet())
				{
					if (d.getKey().equals("default"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Suffix: ", TextColors.BLUE , group.getPrefix()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(group.getPrefix()), ")"))
								.build());
					}
					
					if (!d.getKey().equals("default"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Suffix in " + d.getKey() + ": ", TextColors.BLUE , d.getValue()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(d.getValue()), ")"))
								.build());
					}
				}
					
				if (group.hasPermissions())
				{
					if (group.hasPermissionsWithWorld("default"))
					{
						list.add(Text.of(TextColors.AQUA, "Permissions: "));
						list.addAll(calculateRank(name));
					}
					
					for (Entry<String, Map<String, Boolean>> map : group.getAllPermissions().entrySet())
					{
						if (!map.getKey().equals("default"))
						{
							list.add(Text.of(TextColors.AQUA, "Permissions in " + map.getKey().toString() + ": "));
							list.addAll(calculateRank(name, map.getKey().toString()));
						}
					}
					
				}
				
				pages.contents(list);
				pages.sendTo(src);
			}
			else
			{
				sendMessage(src, lang.rankExist);
			}
				
				
		}
		else if (opt.equalsIgnoreCase("group") && !args.hasAny("option3"))
		{
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Ranks")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (String e : ranks.ranksMap.keySet())
			{
				list.add(Text.of(TextColors.AQUA, "- " + e));
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else if (opt.equalsIgnoreCase("player") && args.hasAny("option3"))
		{
			String name = args.<String>getOne("option3").get();
			
			if (Manager.hasPlayer(name))
			{
				EPPlayer player = Manager.getPlayer(name);
				
				PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
				pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player: " + name)).build());
				
				List<Text> list = new ArrayList<Text>();
				
				list.add(Text.builder()
						.append(of(TextColors.AQUA, "Rank: ", TextColors.BLUE, player.getGroupName()))
						.build());
				
				for (Entry<String, String> d : player.getPrefixes().entrySet())
				{
					if (d.getKey().equals("default"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Prefix: ", TextColors.BLUE , player.getPrefix()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(player.getPrefix()), ")"))
								.build());
					}
					
					if (!d.getKey().equals("default"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Prefix in " + d.getKey() + ": ", TextColors.BLUE , d.getValue()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(d.getValue()), ")"))
								.build());
					}
				}
				
				for (Entry<String, String> d : player.getSuffixes().entrySet())
				{
					if (d.getKey().equals("default"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Suffix: ", TextColors.BLUE , player.getPrefix()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(player.getPrefix()), ")"))
								.build());
					}
					
					if (!d.getKey().equals("default"))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Suffix in " + d.getKey() + ": ", TextColors.BLUE , d.getValue()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(d.getValue()), ")"))
								.build());
					}
				}
				
				for (Entry<String, Map<String, Boolean>> map : player.getAllPermissions().entrySet())
				{
					if (map.getKey().equals("default"))
					{
						list.add(Text.of(TextColors.AQUA, "Permissions: "));
						
						for (Entry<String, Boolean> pe : map.getValue().entrySet())
						{
							boolean val = pe.getValue();
							Text tx = Text.of(val);
							
							if (val)
							{
								tx = Text.of(TextColors.DARK_GREEN, val);
							}
							
							if (!val)
							{
								tx = Text.of(TextColors.RED, val);
							}
							
							list.add(Text.builder().append(
									Text.of(TextColors.AQUA, " - " + pe.getKey().toString() + ": "))
									.append(tx)
									.build());
						}
					}
					
					if (!map.getKey().equals("default"))
					{
						list.add(Text.of(TextColors.AQUA, "Permissions in " + map.getKey() + ": "));
						for (Entry<String, Boolean> pe : map.getValue().entrySet())
						{
							boolean val = pe.getValue();
							Text tx = Text.of(val);
							
							if (val)
							{
								tx = Text.of(TextColors.DARK_GREEN, val);
							}
							
							if (!val)
							{
								tx = Text.of(TextColors.RED, val);
							}
							
							list.add(Text.builder().append(
									Text.of(TextColors.AQUA, " - " + pe.getKey().toString() + ": "))
									.append(tx)
									.build());
						}
					}
				}
				
				pages.contents(list);
				pages.sendTo(src);
			}
			else
			{
				sendMessage(src, lang.playerExist);
			}
		}
		else if (opt.equalsIgnoreCase("player") && !args.hasAny("option3"))
		{
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Players")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (EPPlayer player : Manager.getPlayers())
			{
				list.add(Text.of(TextColors.AQUA, "- " + player.getName() + ": ", TextColors.BLUE, player.getGroupName()));
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else
		{
			sendMessage(src, lang.commandUsage + "/extraperm info <player|group> [name]");
			return CommandResult.empty();
		}
		
		return CommandResult.success();
	}
	
	
	private CommandResult cmdInfo(CommandSource src, CommandContext args, String world)
	{
		if (!args.hasAny("option2"))
		{
			sendMessage(src, lang.commandUsage + "/extraperm info <player|group> [name] -w <world>");
			return CommandResult.empty();
		}
		
		String opt = args.<String>getOne("option2").get();
		
		if (opt.equalsIgnoreCase("group") && args.hasAny("option3"))
		{
			String name = args.<String>getOne("option3").get();
			
			if (Manager.hasGroup(name))
			{
				EPGroup group = Manager.getGroup(name);
				
				PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
				pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Rank: " + name)).build());
				
				List<Text> list = new ArrayList<Text>();
				
				for (Entry<String, String> d : group.getPrefixes().entrySet())
				{
					if (d.getKey().equals(world))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Prefix in " + world + ": ", TextColors.BLUE , d.getValue()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(d.getValue()), ")"))
								.build());
					}
				}
				
				for (Entry<String, String> d : group.getSuffixes().entrySet())
				{
					if (!d.getKey().equals(world))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Suffix in " + world + ": ", TextColors.BLUE , d.getValue()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(d.getValue()), ")"))
								.build());
					}
				}
					
				if (group.hasPermissions())
				{
					for (Entry<String, Map<String, Boolean>> map : group.getAllPermissions().entrySet())
					{
						if (map.getKey().equals(world))
						{
							list.add(Text.of(TextColors.AQUA, "Permissions in " + world + ": "));
							list.addAll(calculateRank(name, map.getKey().toString()));
						}
					}
					
				}
				
				pages.contents(list);
				pages.sendTo(src);
			}
			else
			{
				sendMessage(src, lang.rankExist);
			}
				
				
		}
		else if (opt.equalsIgnoreCase("group") && !args.hasAny("option3"))
		{
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Ranks")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (String e : ranks.ranksMap.keySet())
			{
				list.add(Text.of(TextColors.AQUA, "- " + e));
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else if (opt.equalsIgnoreCase("player") && args.hasAny("option3"))
		{
			String name = args.<String>getOne("option3").get();
			
			if (Manager.hasPlayer(name))
			{
				EPPlayer player = Manager.getPlayer(name);
				
				PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
				pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player: " + name)).build());
				
				List<Text> list = new ArrayList<Text>();
				
				list.add(Text.builder()
						.append(of(TextColors.AQUA, "Rank: ", TextColors.BLUE, player.getGroupName()))
						.build());
				
				for (Entry<String, String> d : player.getPrefixes().entrySet())
				{
					if (d.getKey().equals(world))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Prefix in " + world + ": ", TextColors.BLUE , d.getValue()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(d.getValue()), ")"))
								.build());
					}
				}
				
				for (Entry<String, String> d : player.getSuffixes().entrySet())
				{
					if (d.getKey().equals(world))
					{
						list.add(Text.builder()
								.append(Text.of(TextColors.AQUA, "Suffix in " + world + ": ", TextColors.BLUE , d.getValue()))
								.append(Text.of(" (",TextSerializers.FORMATTING_CODE.deserialize(d.getValue()), ")"))
								.build());
					}
				}
				
				for (Entry<String, Map<String, Boolean>> map : player.getAllPermissions().entrySet())
				{
					if (map.getKey().equals(world))
					{
						list.add(Text.of(TextColors.AQUA, "Permissions in " + world + ": "));
						
						for (Entry<String, Boolean> pe : map.getValue().entrySet())
						{
							boolean val = pe.getValue();
							Text tx = Text.of(val);
							
							if (val)
							{
								tx = Text.of(TextColors.DARK_GREEN, val);
							}
							
							if (!val)
							{
								tx = Text.of(TextColors.RED, val);
							}
							
							list.add(Text.builder().append(
									Text.of(TextColors.AQUA, " - " + pe.getKey().toString() + ": "))
									.append(tx)
									.build());
						}
					}
				}
				
				pages.contents(list);
				pages.sendTo(src);
			}
			else
			{
				sendMessage(src, lang.playerExist);
			}
		}
		else if (opt.equalsIgnoreCase("player") && !args.hasAny("option3"))
		{
			PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
			pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Players")).build());
			
			List<Text> list = new ArrayList<Text>();
			
			for (EPPlayer player : Manager.getPlayers())
			{
				list.add(Text.of(TextColors.AQUA, "- " + player.getName() + ": ", TextColors.BLUE, player.getGroupName()));
			}
			
			pages.contents(list);
			pages.sendTo(src);
		}
		else
		{
			sendMessage(src, lang.commandUsage + "/extraperm info <player|group> [name]");
			return CommandResult.empty();
		}
		
		return CommandResult.success();
	}
	

	private CommandResult cmdUUID(CommandSource src, CommandContext args)
	{
		PaginationList.Builder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
		
		pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player name : UUID")).build());
		
		List<Text> list = new ArrayList<Text>();
		
		for (GameProfile gp : ExtraPermissions.getPlugin().getGame().getServer().getGameProfileManager().getCache().getProfiles())
		{
			list.add(Text.builder().append(
					Text.of(TextColors.GREEN, gp.getName().get() + " : ", TextColors.BLUE, gp.getUniqueId())
					).build());
		}
		
		pages.contents(list);
		pages.sendTo(src);
		
		return CommandResult.success();
	}
	

	private CommandResult cmdReload(CommandSource src, CommandContext args)
	{
		sendMessage(src, lang.startReload);
		players.setup();
		ranks.setup();
		conf.setup();
		lang.setup();
		
		Manager.reload();
		
		rm.realodRanks(ExtraPermissions.getPlugin().getGame());
		sendMessage(src, lang.endReload);
		
		return CommandResult.success();
	}
	
	
	private List<Text> calculateRank(String rank)
	{
		List<Text> temp = new ArrayList<Text>();
		
		EPGroup group = Manager.getGroup(rank);
		
		for (Entry<String, Boolean> e : group.getPermissions().entrySet())
		{
			boolean val = e.getValue();
			Text tx = Text.of(val);
			
			if (val)
			{
				tx = Text.of(TextColors.DARK_GREEN, val);
			}
			
			if (!val)
			{
				tx = Text.of(TextColors.RED, val);
			}
			
			temp.add(Text.builder().append(
					Text.of(TextColors.AQUA, " - " + e.getKey().toString() + ": "))
					.append(tx)
					.build());
		}
		
		return temp;
	}
	
	
	private List<Text> calculateRank(String rank, String worldName)
	{
		List<Text> temp = new ArrayList<Text>();
		
		EPGroup group = Manager.getGroup(rank);
		
		for (Entry<String, Boolean> e : group.getPermissionsWithWorld(worldName).entrySet())
		{
			boolean val = e.getValue();
			Text tx = Text.of(val);
			
			if (val)
			{
				tx = Text.of(TextColors.DARK_GREEN, val);
			}
			
			if (!val)
			{
				tx = Text.of(TextColors.RED, val);
			}
			
			temp.add(Text.builder().append(
					Text.of(TextColors.AQUA, " - " + e.getKey().toString() + ": "))
					.append(tx)
					.build());
		}
		
		return temp;
	}
	

	private void setRank(CommandContext args, EPPlayer p, CommandSource src, String name, String value)
	{
		if (args.hasAny("timeUnit") && args.hasAny("time"))
		{
			int time = args.<Integer>getOne("time").get();
			RanksManager.TimeUnit unit = RanksManager.TimeUnit.converFromString(args.<String>getOne("timeUnit").get());
			
			rm.setPlayerTimeRank(p, value, time, unit);
			
			String out1 = lang.newTimeRankPlayer1.replace("%name%", name).replace("%rank%", value).replace("%time%", time +"").replace("%timeUnit%", unit+"");
			String out2 = lang.newTimeRankPlayer2.replace("%rank%", value).replace("%time%", time +"").replace("%timeUnit%", unit+"");
			
			sendMessage(src, out1);
			sendMessage(p.getPlayer(), out2);
		}
		else
		{
			rm.setPlayerRank(p, value);
			
			String out1 = lang.newRankPlayer1.replace("%name%", name).replace("%rank%", value);
			String out2 = lang.newRankPlayer2.replace("%rank%", value);
			
			sendMessage(src, out1);
			sendMessage(p.getPlayer(), out2);
		}
	}
	
	private void sendMessage(CommandSource src, String s)
	{
		src.sendMessage(Text.builder().append(of(TextSerializers.FORMATTING_CODE.deserialize(s))).build());
	}
	
}
