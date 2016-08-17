package com.gmail.edziu1996.extrapermissions.core.config;

import com.gmail.edziu1996.extrapermissions.api.ConfigManager;

public class ConfigLang extends ConfigManager
{
	public static ConfigLang instance;
	
	//Group
	public String newPrefixGroup = "Rank %group% has a new prefix: %prefix%";
	public String newSuffixGroup = "Rank %group% has a new suffix: %suffix%";
	public String newPrefixWorldGroup = "Rank %group% has a new prefix in %world%: %prefix%";
	public String newSuffixWorldGroup = "Rank %group% has a new suffix in %world%: %suffix%";
	public String newPermGroup = "Rank %group% has a new permission: %perm% (%value%)";
	public String newPermWorldGroup = "Rank %group% has a new permission in %world%: %perm% (%value%)";
	public String newInheGroup = "Rank %group% has a new inheritance: %inhe%";
	
	public String removeValGroup = "From %group% removed %value%";
	public String removeValWorldGroup = "Grom %group% removed %value% in %world%";
	
	//Player
	public String buildPermmision = "You do not have permission to build";
	
	public String newPrefixPlayer1 = "%name% has a new prefix: %prefix%";
	public String newPrefixPlayer2 = "You have a new prefix: %prefix%";
	
	public String newPrefixWorldPlayer1 = "%name% has a new prefix in %world%: %prefix%";
	public String newPrefixWorldPlayer2 = "You have a new prefix in %world%: %prefix%";
	
	public String newSuffixPlayer1 = "%name% has a new prefix: %suffix%";
	public String newSuffixPlayer2 = "You have a new prefix: %suffix%";
	
	public String newSuffixWorldPlayer1 = "%name% has a new prefix in %world%: %suffix%";
	public String newSuffixWorldPlayer2 = "You have a new prefix in %world%: %suffix%";
	
	public String newRankPlayer1 = "%name% is already %rank%";
	public String newRankPlayer2 = "You are already %rank%";
	
	public String newTimeRankPlayer1 = "%name% is already %rank% for %time% %timeUnit%";
	public String newTimeRankPlayer2 = "You are already %rank% for %time% %timeUnit%";
	
	public String newPermPlayer1 = "%name% has a new permission: %perm% (%value%)";
	public String newPermPlayer2 = "You have a new permission: %perm% (%value%)";
	
	public String newPermWorldPlayer1 = "%name% has a new permission in %world%: %perm% (%value%)";
	public String newPermWorldPlayer2 = "You have a new permission in %world%: %perm% (%value%)";
	
	public String removeValPlayer = "From %name% removed %value%";
	public String removeValWorldPlayer = "From %name% removed %value% in %world%";
	
	public String mustPlayerOnline = "Player must be online!";
	
	//Reload
	public String startReload = "Reloading plugin";
	public String endReload = "Reload plugin";
	
	//Other
	public String rankExist = "This rank does'n exist!";
	public String playerExist = "This player does'n exist!";
	public String newRankPlayerPerm = "You do not have permission to give this range";

	public String dontHave = "You don't have permission: %perm%";
	
	public String commandUsage = "&4Usage: ";
	
	public ConfigLang(String folder, String string, String lang)
	{
		super(folder, string, lang);
	}

	@Override
	public void populate()
	{
		//Group
		get().getNode("group", "newGroupSuffix").setValue(newSuffixGroup);
		get().getNode("group", "newGroupPrefix").setValue(newPrefixGroup);
		get().getNode("group", "newGroupWorldSuffix").setValue(newSuffixWorldGroup);
		get().getNode("group", "newGroupWorldPrefix").setValue(newPrefixWorldGroup);
		get().getNode("group", "newGroupPerm").setValue(newPermGroup);
		get().getNode("group", "newGroupWorldPerm").setValue(newPermWorldGroup);
		get().getNode("group", "newGroupInhe").setValue(newInheGroup);
		get().getNode("group", "removeGroupValue").setValue(removeValGroup);
		get().getNode("group", "removeGroupWorldValue").setValue(removeValWorldGroup);
		
		//Player
		get().getNode("player", "buildPerm").setValue(buildPermmision);
		
		get().getNode("player", "newPlayerPrefix1").setValue(newPrefixPlayer1);
		get().getNode("player", "newPlayerPrefix2").setValue(newPrefixPlayer2);
		get().getNode("player", "newPlayerWorldPrefix1").setValue(newPrefixWorldPlayer1);
		get().getNode("player", "newPlayerWorldPrefix2").setValue(newPrefixWorldPlayer2);
		
		get().getNode("player", "newPlayerSuffix1").setValue(newSuffixPlayer1);
		get().getNode("player", "newPlayerSuffix2").setValue(newSuffixPlayer2);
		get().getNode("player", "newPlayerWorldSuffix1").setValue(newSuffixWorldPlayer1);
		get().getNode("player", "newPlayerWorldSuffix2").setValue(newSuffixWorldPlayer2);
		
		get().getNode("player", "newPlayerRank1").setValue(newRankPlayer1);
		get().getNode("player", "newPlayerRank2").setValue(newRankPlayer2);
		
		get().getNode("player", "newPlayerTimeRank1").setValue(newTimeRankPlayer1);
		get().getNode("player", "newPlayerTimeRank2").setValue(newTimeRankPlayer2);
		
		get().getNode("player", "removePlayerValue").setValue(removeValPlayer);
		get().getNode("player", "removePlayerValueWorld").setValue(removeValWorldPlayer);
		
		get().getNode("player", "mustPlayerOnline").setValue(mustPlayerOnline);
		
		get().getNode("player", "newPlayerPerm1").setValue(newPermPlayer1);
		get().getNode("player", "newPlayerPerm2").setValue(newPermPlayer2);
		get().getNode("player", "newPlayerWorldPerm1").setValue(newPermWorldPlayer1);
		get().getNode("player", "newPlayerWorldPerm2").setValue(newPermWorldPlayer2);
		
		get().getNode("player", "startReload").setValue(startReload);
		get().getNode("player", "endReload").setValue(endReload);
		
		//Others
		get().getNode("message", "rankNotExist").setValue(rankExist);
		get().getNode("message", "playerNotExist").setValue(playerExist);
		get().getNode("message", "playerRankWarring").setValue(newRankPlayerPerm);
		get().getNode("message", "dontHavePerm").setValue(dontHave);
		get().getNode("message", "commandUsage").setValue(commandUsage);
	}
	
	@Override
	public void init()
	{
		//Group
		newSuffixGroup = getVal("group", "newGroupSuffix", newSuffixGroup);
		newPrefixGroup = getVal("group", "newGroupPrefix", newPrefixGroup);
		newSuffixWorldGroup = getVal("group", "newGroupWorldSuffix", newSuffixWorldGroup);
		newPrefixWorldGroup = getVal("group", "newGroupWorldPrefix", newPrefixWorldGroup);
		newPermGroup = getVal("group", "newGroupPerm", newPermGroup);
		newPermWorldGroup = getVal("group", "newGroupWorldPerm", newPermWorldGroup);
		newInheGroup = getVal("group", "newGroupInhe", newInheGroup);
		removeValGroup = getVal("group", "removeGroupValue", removeValGroup);
		removeValWorldGroup = getVal("group", "removeGroupWorldValue", removeValWorldGroup);
		
		//Player
		buildPermmision = getVal("player", "buildPerm", buildPermmision);
		
		newPrefixPlayer1 = getVal("player", "newPlayerPrefix1", newPrefixPlayer1);
		newPrefixPlayer2 = getVal("player", "newPlayerPrefix2", newPrefixPlayer2);
		newPrefixWorldPlayer1 = getVal("player", "newPlayerWorldPrefix1", newPrefixWorldPlayer1);
		newPrefixWorldPlayer2 = getVal("player", "newPlayerWorldPrefix2", newPrefixWorldPlayer2);
		
		newSuffixPlayer1 = getVal("player", "newPlayerSuffix1", newSuffixPlayer1);
		newSuffixPlayer2 = getVal("player", "newPlayerSuffix2", newSuffixPlayer2);
		newSuffixWorldPlayer1 = getVal("player", "newPlayerWorldSuffix1", newSuffixWorldPlayer1);
		newSuffixWorldPlayer2 = getVal("player", "newPlayerWorldSuffix2", newSuffixWorldPlayer2);
		
		newRankPlayer1 = getVal("player", "newPlayerRank1", newRankPlayer1);
		newRankPlayer2 = getVal("player", "newPlayerRank2", newRankPlayer2);
		newTimeRankPlayer1 = getVal("player", "newPlayerTimeRank1", newTimeRankPlayer1);
		newTimeRankPlayer2 = getVal("player", "newPlayerTimeRank2", newTimeRankPlayer2);
		
		removeValPlayer = getVal("player", "removePlayerValue", removeValPlayer);
		removeValWorldPlayer = getVal("player", "removePlayerValueWorld", removeValWorldPlayer);
		
		mustPlayerOnline = getVal("player", "mustPlayerOnline", mustPlayerOnline);
		
		newPermPlayer1 = getVal("player", "newPlayerPerm1", newPermPlayer1);
		newPermPlayer2 = getVal("player", "newPlayerPerm2", newPermPlayer2);
		newPermWorldPlayer1 = getVal("player", "newPlayerWorldPerm1", newPermWorldPlayer1);
		newPermWorldPlayer2 = getVal("player", "newPlayerWorldPerm2", newPermWorldPlayer2);
		
		//Others
		startReload = getVal("message", "startReload", startReload);
		endReload = getVal("message", "endReload", endReload);
		
		rankExist = getVal("message", "rankNotExist", rankExist);
		playerExist = getVal("message", "playerNotExist", playerExist);
		newRankPlayerPerm = getVal("message", "playerRankWarring", newRankPlayerPerm);
		dontHave = getVal("message", "dontHavePerm", dontHave);
		
		commandUsage = getVal("message", "commandUsage", commandUsage);
	}

	private String getVal(String name, String val, String def)
	{
		String out = def;
		
		if (get().hasMapChildren())
		{
			if (get().getChildrenMap().containsKey(name))
			{
				if (get().getNode(name).hasMapChildren())
				{
					if (get().getNode(name).getChildrenMap().containsKey(val))
					{
						if (get().getNode(name, val).getValue() != null)
						{
							out = get().getNode(name, val).getString();
						}
					}
				}
			}
		}
		
		return out;
	}
}
