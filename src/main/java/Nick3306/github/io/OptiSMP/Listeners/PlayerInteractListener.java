package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;

public class PlayerInteractListener implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	public PlayerInteractListener(Main plugin)
	{
		this.plugin = plugin;
		this.proUtil = this.plugin.protectUtil;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) 
	{
		Player player = event.getPlayer();
		Action action = event.getAction();
		
		if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK))
		{
			if (event.getHand().equals(EquipmentSlot.HAND))
			{
				Block block = event.getClickedBlock();
				Location blockLocaction = block.getLocation();
				
				if(this.plugin.protectUtil.inField(blockLocaction))
				{
					ProtectionField pField = proUtil.getPField(blockLocaction);
					if(!pField.isMember(player) && !player.getUniqueId().toString().equals(pField.getOwner().toString()) && !player.hasPermission("optiSMP.protect.staff"))
					{
						// Player is not allowed to interact in field		
						player.sendMessage("You are not allowed to build here!");
						event.setCancelled(true);
						return;
					}
					else
					{
						//They are a member, let them interact
					}
				}
			}
		}
	}
}
