package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;

public class RegionDefineListener implements Listener
{
	private Main plugin;
	private ProtectUtilities util;
	public RegionDefineListener(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.protectUtil;
	}
	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		if(this.plugin.protectUtil.definingField(player))
		{
			
			Location block = event.getBlockPlaced().getLocation();		
			ProtectionField field = util.getNewField(player);
			if (field.getBlock1() == null)
			{
				field.setBlock1(block);
				event.setCancelled(true);
				player.sendMessage("Now place the second block!");
			}
			else if (field.getBlock2() == null)
			{
				field.setBlock2(block);
				for(ProtectionField otherField : plugin.fields)
				{				
					if(util.fieldOverlap(field.getBlock1(), otherField.getBlock1(), field.getBlock2(), otherField.getBlock2()))
					{
						player.sendMessage("Your filed overlaps an existing field! Try again!");
						util.removeNewField(field);
						event.setCancelled(true);
						return;
					}
				}
				field.setArea();
				plugin.fields.add(field);
				util.removeNewField(field);
				player.sendMessage("Field added!");
				// Now that this field is officially added, increment the next fields ID
				event.setCancelled(true);				
				this.plugin.sql.addField(field);
				Bukkit.getLogger().info("Block locations before highlight are: " + field.getBlock1().toString() + "  " + field.getBlock2().toString());
				util.highlightField(field, player);			
				Bukkit.getLogger().info("Block locations after highlight are: " + field.getBlock1().toString() + "  " + field.getBlock2().toString());
				
				// Check if they have enough to buy the protection or if its free
				//if it's free
				/*
				if(plugin.getConfig().getInt("PricePerBlock") == 0)
				{
					plugin.fields.add(field);
					util.removeNewField(field);
				}
				else
				{
					
				}
				*/
			}

		}
	}
}
