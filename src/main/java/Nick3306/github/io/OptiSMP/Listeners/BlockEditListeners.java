package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;



public class BlockEditListeners implements Listener
{
	private Main plugin;
	private ProtectUtilities util;
	public BlockEditListeners(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.protectUtil;
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) 
	{
		
		//Used to be used for building restictions but not anymore
		/*
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		Action action = event.getAction();
		String world = player.getWorld().getName();
		*/

	}
}
