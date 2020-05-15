package Nick3306.github.io.OptiSMP.Utilities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Main;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class GeneralUtilities
{
	private Main plugin;

	public GeneralUtilities(Main plugin)
	{
	   this.plugin = plugin;
	}
	public SMPplayer getSMPPlayer(Player player)
	{
		return plugin.players.get(player.getUniqueId());
	}
	
	public void checkRankUp(SMPplayer smpPlayer)
	{
		Player player = smpPlayer.player;
		PermissionUser user = PermissionsEx.getUser(player);
		List<String> groups = user.getParentIdentifiers();
		
		if(groups.get(0).equalsIgnoreCase("Guest"))
		{		
			//Check if they should be promoted to Member
			if((smpPlayer.getTime_online() >= 7200000))
			{
				user.addGroup("Member");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "givehome " + player.getName() + " 1");
			}
			
		}
		else if(groups.get(0).equalsIgnoreCase("Member"))
		{
			//Check if they should be promoted to Resident
			if((smpPlayer.getTime_online() >= 28800000) && ((smpPlayer.getBlocks_broken() + smpPlayer.getBlocks_placed()) >= 10000) && (smpPlayer.getMonsters_killed() >= 90))
			{
				user.addGroup("Resident");
				smpPlayer.setProtectionBlocksLeft(smpPlayer.getProtectionBlocksLeft() + 7812);
				smpPlayer.setProtectionBlocksMax(smpPlayer.getProtectionBlocksMax() + 7812);
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "givehome " + player.getName() + " 1");
			}
			
			
		}
		else if(groups.get(0).equalsIgnoreCase("Resident"))
		{
			//Check if they should be promoted to Citizen
			if((smpPlayer.getTime_online() >= 86400000) && ((smpPlayer.getBlocks_broken() + smpPlayer.getBlocks_placed()) >= 30000) && (smpPlayer.getMonsters_killed() >= 275))
			{
				user.addGroup("Citizen");
				smpPlayer.setProtectionBlocksLeft(smpPlayer.getProtectionBlocksLeft() + 11718);
				smpPlayer.setProtectionBlocksMax(smpPlayer.getProtectionBlocksMax() + 11718);
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "givehome " + player.getName() + " 1");
			}
			
		}
		else if(groups.get(0).equalsIgnoreCase("Citizen"))
		{
			//Check if they should be promoted to Veteran
			if((smpPlayer.getTime_online() >= 360000000) && ((smpPlayer.getBlocks_broken() + smpPlayer.getBlocks_placed()) >= 125000) && (smpPlayer.getMonsters_killed() >= 1120))
			{
				user.addGroup("Veteran");
				smpPlayer.setProtectionBlocksLeft(smpPlayer.getProtectionBlocksLeft() + 17577);
				smpPlayer.setProtectionBlocksMax(smpPlayer.getProtectionBlocksMax() + 17577);
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "givehome " + player.getName() + " 1");
			}
			
		}
		else if(groups.get(0).equalsIgnoreCase("Veteran"))
		{
			//Check if they should be promoted to Champion
			if((smpPlayer.getTime_online() >= 1368000000) && ((smpPlayer.getBlocks_broken() + smpPlayer.getBlocks_placed()) >= 500000) && (smpPlayer.getMonsters_killed() >= 4480))
			{
				user.addGroup("Champion");
				smpPlayer.setProtectionBlocksLeft(smpPlayer.getProtectionBlocksLeft() + 17577);
				smpPlayer.setProtectionBlocksMax(smpPlayer.getProtectionBlocksMax() + 17577);
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "givehome " + player.getName() + " 1");
			}
		}
	}
	/*
	public void updateLastField(SMPplayer player)
	{
		for(SMPplayer playerInList : plugin.players)
		{
			if(playerInList == player)
			{
				playerInList.setLastField(player.getLastField());
			}
		}
	}
	*/
}
