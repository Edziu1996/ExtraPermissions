package com.gmail.edziu1996.extrapermissions.cmd;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;

public class CmdUUID implements CommandExecutor
{

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
		
		pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Player name : UUID")).build());
		
		List<Text> list = new ArrayList<Text>();
		
		for (GameProfile gp : ExtraPermissions.getPlugin().getGame().getServer().getGameProfileManager().getCachedProfiles())
		{
			list.add(Text.of(gp.getName() + " : " + gp.getUniqueId()));
		}
		pages.contents(list);
		pages.sendTo(src);
		
		return CommandResult.success();
	}

}
