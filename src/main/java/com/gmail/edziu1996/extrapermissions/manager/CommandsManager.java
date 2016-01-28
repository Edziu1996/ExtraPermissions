package com.gmail.edziu1996.extrapermissions.manager;

import static org.spongepowered.api.text.Text.of;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;

import com.gmail.edziu1996.extrapermissions.ExtraPermissions;
import com.gmail.edziu1996.extrapermissions.cmd.CmdExPermExecutor;
import com.gmail.edziu1996.extrapermissions.cmd.CmdGroupExecutor;
import com.gmail.edziu1996.extrapermissions.cmd.CmdInfo;
import com.gmail.edziu1996.extrapermissions.cmd.CmdPlayerExecutor;
import com.gmail.edziu1996.extrapermissions.cmd.CmdReloadExecutor;
import com.gmail.edziu1996.extrapermissions.cmd.CmdUUID;

public class CommandsManager
{
	static CommandSpec cmdGroup = CommandSpec.builder()
			.description(of("Group Manager"))
			.permission("extraperm.experm.group.use")
			.executor(new CmdGroupExecutor())
			.arguments(
					GenericArguments.string(of("name")),
					GenericArguments.string(of("option")),
					GenericArguments.string(of("value")),
					GenericArguments.optional(GenericArguments.bool(of("vale_perm")))
					)
			.build();
	
	static CommandSpec cmdPlayer = CommandSpec.builder()
			.description(of("Player Manager"))
			.permission("extraperm.experm.player.use")
			.executor(new CmdPlayerExecutor())
			.arguments(
					GenericArguments.string(of("name")),
					GenericArguments.string(of("option")),
					GenericArguments.string(of("value")),
					GenericArguments.flags().valueFlag(GenericArguments.seq(GenericArguments.string(of("timeUnit")), GenericArguments.integer(of("time"))), "t").buildWith(null)
					)
			.build();
	
	static CommandSpec cmdReload = CommandSpec.builder()
			.description(of("Reload plugin"))
			.permission("extraperm.experm.reload.use")
			.executor(new CmdReloadExecutor())
			.build();
	
	static CommandSpec cmdUUIDs = CommandSpec.builder()
			.description(of("Show all players uuid"))
			.permission("extraperm.experm.uuid.use")
			.executor(new CmdUUID())
			.build();
	
	static CommandSpec cmdInfo = CommandSpec.builder()
			.description(of("Show all info"))
			.permission("extraperm.experm.info.use")
			.arguments(
					GenericArguments.string(of("option")),
					GenericArguments.optional(GenericArguments.string(of("name")))
					)
			.executor(new CmdInfo())
			.build();
	
	public static CommandSpec cmdExPerm = CommandSpec.builder()
			.description(of("This is main command"))
			.permission("extraperm.experm.use")
			.executor(new CmdExPermExecutor())
			.child(cmdGroup, "group")
			.child(cmdPlayer, "player")
			.child(cmdReload, "reload", "rl")
			.child(cmdUUIDs, "uuid", "id")
			.child(cmdInfo, "info")
			.build();
	

	public static void load(Game game)
	{
		game.getCommandManager().register(ExtraPermissions.getPlugin(), cmdExPerm, "experm", "extraperm");
	}
}
