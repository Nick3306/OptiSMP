package Nick3306.github.io.OptiSMP.Listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;

public class BlockPlaceListener implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public BlockPlaceListener(Main plugin)
	{
		this.plugin = plugin;
		this.util = this.plugin.util; 
		this.proUtil = this.plugin.protectUtil;
	}

	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Location placedBlock = event.getBlockPlaced().getLocation();
		//Check if player is defining field
		if(this.plugin.protectUtil.definingField(player))
		{

			Location block = event.getBlockPlaced().getLocation();		
			ProtectionField field = proUtil.getNewField(player);
			if (field.getBlock1() == null && field.getDefineType().equalsIgnoreCase("cuboid"))
			{
				field.setBlock1(block);
				event.setCancelled(true);
				player.sendMessage("Now place the second block!");
			}
			else if (field.getBlock2() == null)
			{
				if (field.getDefineType().equalsIgnoreCase("radius"))
				{
					// set block one and two on the field based on the radius.
					int radius = field.getRadius();
					Location block1 = new Location(block.getWorld(), block.getX() - radius, block.getY() - radius, block.getBlockZ() - radius);
					field.setBlock1(block1);
					Location block2 = new Location(block.getWorld(), block.getX() + radius, block.getY() + radius, block.getBlockZ() + radius);
					field.setBlock2(block2);					
				}
				else
				{
					field.setBlock2(block);
				}
				for(ProtectionField otherField : plugin.fields)
				{				
					if(proUtil.fieldOverlap(field.getBlock1(), otherField.getBlock1(), field.getBlock2(), otherField.getBlock2()))
					{
						player.sendMessage("Your field overlaps an existing field! Try again!");
						proUtil.removeNewField(field);
						event.setCancelled(true);
						return;
					}
				}										
				field.setArea();
				SMPplayer smpPlayer = util.getSMPPlayer(player);
				//check if the player has enough protection blocks left to make field
				if(smpPlayer.getProtectionBlocksLeft() >= field.getArea())
				{	
					//Subtract the blocks from their remaining protection blocks
					smpPlayer.setProtectionBlocksLeft(smpPlayer.getProtectionBlocksLeft() - field.getArea());

					//save player
					plugin.sql.savePlayer(smpPlayer);
					//Add field to the global pfield list
					plugin.fields.add(field);
					//Add field to the players own list of their fields
					smpPlayer.protectionFields.add(field);
					//Remove the field from the fields being defined
					proUtil.removeNewField(field);
					player.sendMessage("Field added!");
					event.setCancelled(true);
					//Add field to the database
					this.plugin.sql.addField(field);
					//Highlight field so player can see it
					proUtil.highlightField(field, player);		

					//log created field
					String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					proUtil.logFieldAction(date + ": Player " + player.getName() + " CREATED the field " + field.getName() + " using " + field.getArea() + " blocks. They have " + smpPlayer.getProtectionBlocksLeft() + " block left." );
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You do not have enough protection blocks left to make a field of size " + field.getArea() +   ". You have " + smpPlayer.getProtectionBlocksLeft() + " blocks left.");
					proUtil.removeNewField(field);
					event.setCancelled(true);				
				}											
			}
		}

		//Make sure player can build in area
		if(this.plugin.protectUtil.inField(placedBlock))
		{			
			ProtectionField pField = proUtil.getPField(placedBlock);
			if(!pField.isMember(player) && !player.getUniqueId().toString().equals(pField.getOwner().toString()) && !player.hasPermission("optiSMP.protect.staff"))
			{
				// Player is not allowed to build in field		
				player.sendMessage("You are not allowed to build here!");
				event.setCancelled(true);
			}
			else
			{
				//They are a member, let them build
			}
		}

		//Increment their blocks placed stat
		SMPplayer smpPlayer = util.getSMPPlayer(player);
		smpPlayer.setBlocks_placed(smpPlayer.getBlocks_placed() + 1);
		util.checkRankUp(smpPlayer);
	}
}
