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
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		Action action = event.getAction();
		String world = player.getWorld().getName();
		if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK)
		{
			//Player tried to place or break a block, check to make sure block is not in pfield.
			Location blockInteracted;
			
			blockInteracted = event.getClickedBlock().getLocation();
			
			if(this.plugin.protectUtil.inField(blockInteracted))
			{			
				ProtectionField pField = util.getPField(blockInteracted);
				if(!pField.isMember(player) && !player.getUniqueId().toString().equals(pField.getOwner().toString()) && !player.hasPermission("optiSMP.protect.staff"))
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
