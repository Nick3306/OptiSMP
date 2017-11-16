package Nick3306.github.io.OptiSMP.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class Stats implements CommandExecutor
{

	private Main plugin;
	private GeneralUtilities util;
	public Stats(Main plugin)
	{
	   this.plugin = plugin;
	   this.util =  this.plugin.util;		   
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2lable, String[] args)
	{
		Player player = (Player) sender; 
		// TODO Auto-generated method stub
		if(cmd.getName().equalsIgnoreCase("stats"))
		{
			if(args.length == 0)
			{
				SMPplayer smpPlayer = util.getSMPPlayer(player);
				int currentTime = (int) (System.currentTimeMillis());
				//get players login time and subtract it from their logout time to get time played
				int sessionTime = currentTime - smpPlayer.loginTime;
				int currentOnlineTime = smpPlayer.getTime_online();
				int timeOnline = currentOnlineTime + sessionTime;
				
				//calculate time online
				long second = (timeOnline / 1000) % 60;
				long minute = (timeOnline  / (1000 * 60)) % 60;
				long hour = (timeOnline / (1000 * 60 * 60));
				
				String time = (hour + " hours " + minute + " minutes " +  second + " seconds.");
				player.sendMessage(ChatColor.GREEN + "Stats for player " + player.getName());
				player.sendMessage(ChatColor.YELLOW + "Join Date: " + smpPlayer.getJoin_date());
				player.sendMessage(ChatColor.YELLOW + "Last Online: " + smpPlayer.getLast_online());
				player.sendMessage(ChatColor.YELLOW + "Total Logins: " + smpPlayer.getTotal_logins());
				player.sendMessage(ChatColor.YELLOW + "Time Online: " + time);
				player.sendMessage(ChatColor.YELLOW + "Total Votes: " + smpPlayer.getTotal_votes());
				player.sendMessage(ChatColor.YELLOW + "Blocks Placed: " + smpPlayer.getBlocks_placed());
				player.sendMessage(ChatColor.YELLOW + "Blocks Broken: " + smpPlayer.getBlocks_broken());
				player.sendMessage(ChatColor.YELLOW + "Players Killed: " + smpPlayer.getPlayers_killed());
				player.sendMessage(ChatColor.YELLOW + "Monsters Killed: " + smpPlayer.getMonsters_killed());
				player.sendMessage(ChatColor.YELLOW + "Animals Killed: " + smpPlayer.getAnimlas_killed());
				player.sendMessage(ChatColor.YELLOW + "Total Deaths: " + smpPlayer.getTotal_deaths());
				player.sendMessage(ChatColor.YELLOW + "Protection blocks left: " + smpPlayer.getProtectionBlocksLeft());
				player.sendMessage(ChatColor.YELLOW + "Protection blocks max: " + smpPlayer.getProtectionBlocksMax());
				
				return true;
			}
			else
			{
				player.sendMessage(ChatColor.RED + "Incorrect Usage: /stats");
			}
		}
		return false;
	}

}
