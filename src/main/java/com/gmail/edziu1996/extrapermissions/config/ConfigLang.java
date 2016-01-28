package com.gmail.edziu1996.extrapermissions.config;

import com.gmail.edziu1996.extrapermissions.manager.ConfigManager;

public class ConfigLang extends ConfigManager
{
	public static ConfigLang instance;
	
	//Build
	public String buildPermmision = "You do not have permission to build";
	
	//Group
	public String newPrefixGroup = "Rank %group% has a new prefix %prefix%";
	public String newSuffixGroup = "Rank %group% has a new suffix %suffix%";
	public String newPermGroup = "Rank %group% has a new permission %perm% (%value%)";
	public String newInheGroup = "Rank %group% has a new inheritance %inhe%";
	
	public String removeValGroup = "From %group% removed %value%";
	
	//Player
	public String newPrefixPlayer1 = "%name% has a new prefix: %prefix%";
	public String newPrefixPlayer2 = "You have a new prefix: %prefix%";
	
	public String newSuffixPlayer1 = "%name% has a new prefix: %suffix%";
	public String newSuffixPlayer2 = "You have a new prefix: %suffix%";
	
	public String newRankPlayer1 = "%name% is already %rank%";
	public String newRankPlayer2 = "You are already %rank%";
	
	public String newTimeRankPlayer1 = "%name% is already %rank% for %time% %timeUnit%";
	public String newTimeRankPlayer2 = "You are already %rank% for %time% %timeUnit%";
	
	public String removeValPlayer = "From %name% removed %value%";
	
	public String mustPlayerOnline = "Player must be online!";
	
	//Reload
	public String startReload = "Reloading plugin";
	public String endReload = "Reload plugin";
	
	//Other
	public String rankExist = "This rank does'n exist!";
	public String playerExist = "This player does'n exist!";
	
	public ConfigLang(String folder, String string, String lang)
	{
		super(folder, string, lang);
	}

	@Override
	public void populate()
	{
		get().getNode("message", "buildPerm").setValue(buildPermmision);
		
		get().getNode("message", "newGroupSuffix").setValue(newSuffixGroup);
		get().getNode("message", "newGroupPrefix").setValue(newPrefixGroup);
		get().getNode("message", "newGroupPerm").setValue(newPermGroup);
		get().getNode("message", "newGroupInhe").setValue(newInheGroup);
		get().getNode("message", "removeGroupValue").setValue(removeValGroup);
		
		get().getNode("message", "newPlayerPrefix1").setValue(newPrefixPlayer1);
		get().getNode("message", "newPlayerPrefix2").setValue(newPrefixPlayer2);
		get().getNode("message", "newPlayerSuffix1").setValue(newSuffixPlayer1);
		get().getNode("message", "newPlayerSuffix2").setValue(newSuffixPlayer2);
		get().getNode("message", "newPlayerRank1").setValue(newRankPlayer1);
		get().getNode("message", "newPlayerRank2").setValue(newRankPlayer2);
		get().getNode("message", "newPlayerTimeRank1").setValue(newTimeRankPlayer1);
		get().getNode("message", "newPlayerTimeRank2").setValue(newTimeRankPlayer2);
		get().getNode("message", "removePlayerValue").setValue(removeValPlayer);
		get().getNode("message", "mustPlayerOnline").setValue(mustPlayerOnline);
		
		get().getNode("message", "startReload").setValue(startReload);
		get().getNode("message", "endReload").setValue(endReload);
		
		get().getNode("message", "rankNotExist").setValue(rankExist);
		get().getNode("message", "playerNotExist").setValue(playerExist);
	}
	
	@Override
	public void init()
	{
		buildPermmision = getVal("message", "buildPerm", buildPermmision);
		
		newSuffixGroup = getVal("message", "newGroupSuffix", newSuffixGroup);
		newPrefixGroup = getVal("message", "newGroupPrefix", newPrefixGroup);
		newPermGroup = getVal("message", "newGroupPerm", newPermGroup);
		newInheGroup = getVal("message", "newGroupInhe", newInheGroup);
		removeValGroup = getVal("message", "removeGroupValue", removeValGroup);
		
		newPrefixPlayer1 = getVal("message", "newPlayerPrefix1", newPrefixPlayer1);
		newPrefixPlayer2 = getVal("message", "newPlayerPrefix2", newPrefixPlayer2);
		newSuffixPlayer1 = getVal("message", "newPlayerSuffix1", newSuffixPlayer1);
		newSuffixPlayer2 = getVal("message", "newPlayerSuffix2", newSuffixPlayer2);
		newRankPlayer1 = getVal("message", "newPlayerRank1", newRankPlayer1);
		newRankPlayer2 = getVal("message", "newPlayerRank2", newRankPlayer2);
		newTimeRankPlayer1 = getVal("message", "newPlayerTimeRank1", newTimeRankPlayer1);
		newTimeRankPlayer2 = getVal("message", "newPlayerTimeRank2", newTimeRankPlayer2);
		removeValPlayer = getVal("message", "removePlayerValue", removeValPlayer);
		mustPlayerOnline = getVal("message", "mustPlayerOnline", mustPlayerOnline);
		
		startReload = getVal("message", "startReload", startReload);
		endReload = getVal("message", "endReload", endReload);
		
		rankExist = getVal("message", "rankNotExist", rankExist);
		rankExist = getVal("message", "playerNotExist", playerExist);
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
