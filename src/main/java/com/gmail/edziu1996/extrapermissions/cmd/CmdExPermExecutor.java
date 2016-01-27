package com.gmail.edziu1996.extrapermissions.cmd;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;

public class CmdExPermExecutor implements CommandExecutor
{

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		PaginationBuilder pages = ExtraPermissions.getPlugin().getGame().getServiceManager().provide(PaginationService.class).get().builder();
		
		pages.title(Text.builder().color(TextColors.GREEN).append(Text.of(TextColors.AQUA, "Extra Permissions Sub-Commands")).build());
		
		List<Text> list = new ArrayList<Text>();
		
		if (src.hasPermission("ExtraPerm.experm.group"))
		{
			list.add(Text.builder().append(Text.of("/experm group")).onClick(TextActions.suggestCommand("/experm group")).build());
		}
		
		if (src.hasPermission("ExtraPerm.experm.reload"))
		{
			list.add(Text.builder().append(Text.of("/experm reload")).onClick(TextActions.suggestCommand("/experm reload")).build());
		}
		
		if (src.hasPermission("ExtraPerm.experm.player"))
		{
			list.add(Text.builder().append(Text.of("/experm player")).onClick(TextActions.suggestCommand("/experm player")).build());
		}
		
		pages.contents(list);
		pages.sendTo(src);
		
		return CommandResult.success();
	}

}
