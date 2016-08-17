package com.gmail.edziu1996.extrapermissions.core.manager;

import static org.spongepowered.api.text.Text.of;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;

import com.gmail.edziu1996.extrapermissions.core.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.core.cmd.CmdExtraPermExecutor;

public class CommandsManager
{
	public static CommandSpec cmdExtraPerm = CommandSpec.builder()
			.description(of(""))
			.permission("extraperm.experm.use")
			.executor(new CmdExtraPermExecutor())
			.arguments(
					GenericArguments.optional(GenericArguments.string(of("option"))),
					GenericArguments.optional(GenericArguments.string(of("option2"))),
					GenericArguments.optional(GenericArguments.string(of("option3"))),
					GenericArguments.optional(GenericArguments.string(of("value"))),
					GenericArguments.optional(GenericArguments.string(of("value2"))),
					GenericArguments.flags().valueFlag(GenericArguments.seq(GenericArguments.string(of("timeUnit")), GenericArguments.integer(of("time"))), "t").buildWith(null),
					GenericArguments.flags().valueFlag(GenericArguments.string(of("world")), "w").buildWith(null)
					)
			.build();

	public static void load(Game game)
	{
		game.getCommandManager().register(ExtraPermissions.getPlugin(), cmdExtraPerm, "experm", "extraperm");
	}
}
