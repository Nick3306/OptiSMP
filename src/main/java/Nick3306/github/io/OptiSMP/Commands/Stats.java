package Nick3306.github.io.OptiSMP.Commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;

public class Stats implements CommandExecutor
{

	private Main plugin;
	private GeneralUtilities util;
	public Stats(Main plugin)
	{
	   this.plugin = plugin;
	   this.util =  this.plugin.util;		   
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2lable, String[] args)
	{
		Player player = (Player) sender; 
		// TODO Auto-generated method stub
		if(cmd.getName().equalsIgnoreCase("stats"))
		{
			if(args.length == 0)
			{
				util.sendStats(player, player);
				return true;
			}
			if(args.length == 1)
			{
				Player target = plugin.getServer().getPlayer(args[0]);
				if(target != null)
				{				
					//target is online
					util.sendStats(player, target);
					return true;
				}
				else
				{
					//target is offline
					OfflinePlayer targetOffline = plugin.getServer().getOfflinePlayer(args[0]);
					if(targetOffline != null)
					{
						plugin.sql.getPlayerByUUID(targetOffline.getUniqueId(), player);
						return true;
					}
					else
					{
						player.sendMessage(ChatColor.RED + "Player not found");
					}
					
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED + "Incorrect Usage: /stats");
			}
		}
		return false;
	}

}
