package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;

public class HangingBreakEvent implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public HangingBreakEvent(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.util; 
	   this.proUtil = this.plugin.protectUtil;
	}
	@EventHandler
	public void onHangingBreak(HangingBreakByEntityEvent  event) {
		if(event.getRemover() instanceof Player)
		{
			Player player = (Player) event.getRemover();
			Location loc = event.getEntity().getLocation();
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
					player.sendMessage(ChatColor.RED + "You cannot break that here.");
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
