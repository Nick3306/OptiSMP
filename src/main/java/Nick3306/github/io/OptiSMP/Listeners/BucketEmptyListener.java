package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import Nick3306.github.io.OptiSMP.Main;

public class BucketEmptyListener implements Listener
{
	//private Main plugin;
	//private ProtectUtilities proUtil;
	//private GeneralUtilities util;
	public BucketEmptyListener(Main plugin)
	{
	   //this.plugin = plugin;
	   //this.util = this.plugin.util; 
	   //this.proUtil = this.plugin.protectUtil;
	}
	@EventHandler
	public void onEntityDeath(PlayerBucketEmptyEvent event)
	{
		Player player = event.getPlayer();
		if((event.getBucket() == Material.LAVA_BUCKET))
		{
			if(!player.hasPermission("optiSMP.lavabucket"))
			{
				player.sendMessage(ChatColor.RED + "You are not a high enough rank to place lava");
				event.setCancelled(true);
			}
		}
		else if((event.getBucket() == Material.WATER_BUCKET))
		{
			if(!player.hasPermission("optiSMP.waterbucket"))
			{
				player.sendMessage(ChatColor.RED + "You are not a high enough rank to place water");
				event.setCancelled(true);
			}
		}
	}
	
}
