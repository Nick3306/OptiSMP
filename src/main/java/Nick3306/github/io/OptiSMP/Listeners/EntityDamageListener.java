package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;

public class EntityDamageListener implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	public EntityDamageListener(Main plugin)
	{
		this.plugin = plugin;
		this.proUtil = this.plugin.protectUtil;
	}

	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent event)
	{
		Entity entity = event.getEntity();
		Location loc = entity.getLocation();
		if(entity instanceof Animals || entity instanceof ItemFrame || entity instanceof Villager)
		{
			if(proUtil.inField(loc))
			{
				if(event.getDamager() instanceof Player)
				{
					Player player = (Player) event.getDamager();
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
