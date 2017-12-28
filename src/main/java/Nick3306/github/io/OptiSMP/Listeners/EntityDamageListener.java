package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;

public class EntityDamageListener implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public EntityDamageListener(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.util; 
	   this.proUtil = this.plugin.protectUtil;
	}
	
	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent event)
	{
		Entity entity = event.getEntity();
		Location loc = entity.getLocation();
		if(entity instanceof Animals || entity instanceof ItemFrame)
		{	
			if(proUtil.inField(loc))
			{						
				EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
				if(nEvent.getDamager() instanceof Player)
				{
					Player player = (Player) nEvent.getDamager();
					ProtectionField pfield = proUtil.getPField(loc);
					
					if(pfield.members.contains(player.getUniqueId()) || pfield.getOwner().equals(player.getUniqueId()))
					{
						// player is allowed to kill innocent animals in this field
						return;
					}
					else 
					{
						if(entity instanceof Animals)
						{
							player.sendMessage(ChatColor.RED + "You are not allowed to damage animals here.");
						}
						else if(entity instanceof ItemFrame)
						{
							player.sendMessage(ChatColor.RED + "You are not allowed to damage item frames here.");
						}
						else
						{
							player.sendMessage(ChatColor.RED + "You are not allowed to do that here.");
						}
						event.setCancelled(true);
					}

				}						
			}
			else
			{
				return;
			}
		}
		else
		{
			return;
		}
	}
	
}
