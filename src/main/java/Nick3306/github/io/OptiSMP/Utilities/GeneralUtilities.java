package Nick3306.github.io.OptiSMP.Utilities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	
	public void sendStats(Player player, Player target)
	{
		
		SMPplayer smpPlayer = getSMPPlayer(target);
		
		int currentTime = (int) (System.currentTimeMillis());
		//get players login time and subtract it from their logout time to get time played
		int sessionTime = currentTime - smpPlayer.loginTime;
		long currentOnlineTime = smpPlayer.getTime_online();
		long timeOnline = currentOnlineTime + sessionTime;
		
		//calculate time online
		long second = (timeOnline / 1000) % 60;
		long minute = (timeOnline  / (1000 * 60)) % 60;
		long hour = (timeOnline / (1000 * 60 * 60));
		String time = (hour + " hours " + minute + " minutes " +  second + " seconds.");
		
		player.sendMessage(ChatColor.GREEN + "Stats for player " + target.getName());
		player.sendMessage(ChatColor.YELLOW + "Join Date: " + smpPlayer.getJoin_date());
		player.sendMessage(ChatColor.YELLOW + "Last Online: " + smpPlayer.getLast_online());
		player.sendMessage(ChatColor.YELLOW + "Total Logins: " + smpPlayer.getTotal_logins());
		player.sendMessage(ChatColor.YELLOW + "Time Online: " + time);
		player.sendMessage(ChatColor.YELLOW + "Lines Spoken: " + smpPlayer.getLines_spoken());
		player.sendMessage(ChatColor.YELLOW + "Blocks Placed: " + smpPlayer.getBlocks_placed());
		player.sendMessage(ChatColor.YELLOW + "Blocks Broken: " + smpPlayer.getBlocks_broken());
		player.sendMessage(ChatColor.YELLOW + "Monsters Killed: " + smpPlayer.getMonsters_killed());
		player.sendMessage(ChatColor.YELLOW + "Animals Killed: " + smpPlayer.getAnimlas_killed());
		player.sendMessage(ChatColor.YELLOW + "Animals Bred: " + smpPlayer.getAnimals_bred());
		player.sendMessage(ChatColor.YELLOW + "Fish Caught: " + smpPlayer.getFish_caught());
		player.sendMessage(ChatColor.YELLOW + "Items Enchanted: " + smpPlayer.getItems_enchanted());
		player.sendMessage(ChatColor.YELLOW + "Total Deaths: " + smpPlayer.getTotal_deaths());
		player.sendMessage(ChatColor.YELLOW + "Protection blocks: " + smpPlayer.getProtectionBlocksLeft());
	}
}
