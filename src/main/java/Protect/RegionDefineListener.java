package Protect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import Nick3306.github.io.OptiSMP.Main;

public class RegionDefineListener implements Listener
{
	private Main plugin;
	private Utilities util;
	public RegionDefineListener(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.util;
	}
	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		if(this.plugin.util.definingField(player))
		{
			player.sendMessage("In first if");
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
				double area = field.getArea();
				plugin.fields.add(field);
				util.removeNewField(field);
				player.sendMessage("Field added!!!");
				event.setCancelled(true);
				
				this.plugin.sql.addField(field);
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
