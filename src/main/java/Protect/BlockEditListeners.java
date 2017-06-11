package Protect;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import Nick3306.github.io.OptiSMP.Main;



public class BlockEditListeners implements Listener
{
	private Main plugin;
	private Utilities util;
	public BlockEditListeners(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.util;
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) 
	{
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		Action action = event.getAction();
		String world = player.getWorld().getName();
		if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR)
		{
			if(this.plugin.util.inField(loc))
			{			
				ProtectionField pField = util.getPField(loc);
				if(!pField.isMember(player) && !player.getUniqueId().toString().equals(pField.getOwner().toString()))
				{
					// Player is not allowed to build in field, check flags
					
					//Chest Access flag
					if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					{
						if(event.getClickedBlock().getType().equals(Material.CHEST))
						{
							if(pField.getChestFlag() == true)
							{
								return;
							}
							else
							{
								player.sendMessage(ChatColor.RED + "You are not allowed in chests here");
								event.setCancelled(true);
							}
						}
					}

					player.sendMessage("You are not allowed to build here!");
					event.setCancelled(true);
				}
				else
				{
					//They are a member, let them build
				}
			}
		}
	}
}
