package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;

public class InteractEntityListener implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	//private GeneralUtilities util;
	public InteractEntityListener(Main plugin)
	{
	   this.plugin = plugin;
	   //this.util = this.plugin.util;
	   this.proUtil = this.plugin.protectUtil;
	}
	@EventHandler
	public void onPlayerEntityInteract(PlayerInteractEntityEvent event)
	{
		Player player = event.getPlayer();		 
		Entity e = event.getRightClicked();	
		 
		if(e instanceof ItemFrame){				
			Location loc = e.getLocation();
			if(proUtil.inField(loc))
			{
				ProtectionField pfield = proUtil.getPField(loc);
				if(pfield.members.contains(player.getUniqueId()) || pfield.getOwner().equals(player.getUniqueId()))
				{
					//player can break all the shit here
					return;
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You cannot interact with that here.");
					event.setCancelled(true);
				}
			}
			else 
			{
				return;
			}
		}
	}
}
