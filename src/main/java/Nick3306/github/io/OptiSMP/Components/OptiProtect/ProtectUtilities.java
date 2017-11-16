package Nick3306.github.io.OptiSMP.Components.OptiProtect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Main;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;


//Class contains functions that will have to be used over and over again on a general basis.
public class ProtectUtilities 
{
	private Main plugin;

	public ProtectUtilities(Main plugin)
	{
	   this.plugin = plugin;
	}
	
	// Checks is location is inside of any field
	// Will be upgraded later to only check fields in the same world as the location
	public boolean inField(Location loc)
	{
		//Bukkit.getLogger().info("In inField");
		for(ProtectionField field : plugin.fields)
		{
			if(field.inPField(loc))
			{
				return true;
			}
		}
		return false;
	}
	
	// Gets the protection field the location is in, null if not in a protection field
	public ProtectionField getPField(Location loc)
	{
		//Bukkit.getLogger().info("In getPField");
		for(ProtectionField field : plugin.fields)
		{
			if(field.inPField(loc))
			{
				return field;
			}
		}

		return null;
	}
	public ProtectionField getPFieldByName(UUID owner, String name)
	{
		for(ProtectionField field : plugin.fields)
		{
			if(field.getOwner().equals(owner) && field.getName().equalsIgnoreCase(name))
			{
				return field;
			}
		}

		return null;
	}
	
	//Used to determine if a player is currently defining a field
	public boolean definingField(Player player)
	{
		//player.sendMessage("In defining field");
		for(int i = 0; i < plugin.newFields.size(); i++)
		{
			if (plugin.newFields.get(i).getOwner().equals(player.getUniqueId()))
			{
				return true;
			}
		}
		//player.sendMessage("After for in defining field");
		return false;
	}
	
	// Gets the field that a player is currently defining
	public ProtectionField getNewField(Player player)
	{
		for(int i = 0; i < plugin.newFields.size(); i++)
		{
			if (plugin.newFields.get(i).getOwner().equals(player.getUniqueId()))
			{
				return plugin.newFields.get(i);
			}
		}
		return null;
	}
	
	// Removes a new field from the new field list when a player is either done defining it or cancels it
	public void removeNewField(ProtectionField field)
	{
		for (int i = 0; i < plugin.newFields.size(); i++)
		{
			if (field.getOwner().equals(plugin.newFields.get(i).getOwner()))
			{
				plugin.newFields.remove(i);
			}
		}
	}
	
	// Removes a field from the fields list
	public void removeField(ProtectionField field)
	{
		for(int i = 0; i < plugin.fields.size(); i++)
		{
			if((field.getOwner() == plugin.fields.get(i).getOwner())&& field.getName().equalsIgnoreCase(plugin.fields.get(i).getName()))
			{
				plugin.fields.remove(i);
			}
		}

	}
	
	// Checks is the defined field overlaps with another
	public boolean fieldOverlap(Location loc1Block1, Location loc2Block1, Location loc1Block2, Location loc2Block2)
	{
		int block1Xmax = (int) Math.max(loc1Block1.getX(), loc1Block2.getX());
		int block1Xmin = (int) Math.min(loc1Block1.getX(), loc1Block2.getX());
		int block1Ymax = (int) Math.max(loc1Block1.getY(), loc1Block2.getY());
		int block1Ymin = (int) Math.min(loc1Block1.getY(), loc1Block2.getY());
		int block1Zmax = (int) Math.max(loc1Block1.getZ(), loc1Block2.getZ());
		int block1Zmin = (int) Math.min(loc1Block1.getZ(), loc1Block2.getZ());
		
		int block2Xmax = (int) Math.max(loc2Block1.getX(), loc2Block2.getX());
		int block2Xmin = (int) Math.min(loc2Block1.getX(), loc2Block2.getX());
		int block2Ymax = (int) Math.max(loc2Block1.getY(), loc2Block2.getY());
		int block2Ymin = (int) Math.min(loc2Block1.getY(), loc2Block2.getY());
		int block2Zmax = (int) Math.max(loc2Block1.getZ(), loc2Block2.getZ());
		int block2Zmin = (int) Math.min(loc2Block1.getZ(), loc2Block2.getZ());
		
		
		if(block1Xmax < block2Xmin || block1Xmin > block2Xmax || block1Zmin > block2Zmax || block1Zmax < block2Zmin || block1Ymax < block2Ymin || block1Ymin > block2Ymax)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	// Takes a protection field and spawns particles that only the player can see to show the protection field he just created.
	public void highlightField(ProtectionField field, Player player)
	{	
		Location lowBlock;
		Location highBlock;
		if(field.getBlock1().getY() <= field.getBlock2().getY())
		{
			lowBlock = field.getBlock1().clone();
			highBlock = field.getBlock2().clone();
		}
		else
		{
			lowBlock =  field.getBlock2().clone();
			highBlock = field.getBlock1().clone();
		}
		Location low = lowBlock;
		Location high = highBlock;
		ArrayList<Location> toReturn = new ArrayList<Location>();
		//These variables are the components of the displacement between the two corners
		//The difference in x, y, and z
		int dx = (int) Math.abs(highBlock.getX() - lowBlock.getX());
		int dy = (int) Math.abs(highBlock.getY() - lowBlock.getY());
		int dz = (int) Math.abs(highBlock.getZ() - lowBlock.getZ());
		
		
		for(int x = 0; x <= dx; x++)
		{
			Location temp1 = highBlock.clone();
			Location temp2 = lowBlock.clone();
			
			Location tempLowUp = lowBlock.clone();
			tempLowUp.setY(tempLowUp.getY() + dy);
			
			Location tempHighDown = highBlock.clone();
			tempHighDown.setY(tempHighDown.getY() - dy);

			if(highBlock.getX() >= lowBlock.getX())
			{
				
				toReturn.add(new Location(null,temp1.getX() - x, temp1.getY(), temp1.getZ()));
				
				toReturn.add(new Location(null,temp2.getX() + x, temp2.getY(), temp2.getZ()));
				
				//Add block above low block
				toReturn.add(new Location(null,tempLowUp.getX() + x, tempLowUp.getY(), tempLowUp.getZ()));
				
				//Add block below high
				toReturn.add(new Location(null,tempHighDown.getX() - x, tempHighDown.getY(), tempHighDown.getZ()));
				
			}
			else if(highBlock.getX() <= lowBlock.getX())
			{
				 toReturn.add(new Location(null,temp1.getX() + x, temp1.getY(), temp1.getZ()));
				 toReturn.add(new Location(null,temp2.getX() - x, temp2.getY(), temp2.getZ()));
				 
				//Add block above low block
				toReturn.add(new Location(null,tempLowUp.getX() - x, tempLowUp.getY(), tempLowUp.getZ()));
				
				//Add block below high
				toReturn.add(new Location(null,tempHighDown.getX() + x, tempHighDown.getY(), tempHighDown.getZ()));
			}			 
		}
		for(int y = 0; y <= dy; y++)
		{
			Location temp1 = highBlock.clone();
			Location temp2 = lowBlock.clone();	
			
			Location lowBlockUp = new Location(null, lowBlock.getX(), highBlock.getY(), highBlock.getZ());
			
			if(highBlock.getY() >= lowBlock.getY())
			{
				
				toReturn.add(new Location(null,temp1.getX(), temp1.getY() - y, temp1.getZ()));
				
				toReturn.add(new Location(null,temp2.getX(), temp2.getY() + y, temp2.getZ()));
			}
			else if(highBlock.getY() <= lowBlock.getY())
			{
				 toReturn.add(new Location(null,temp1.getX(), temp1.getY() + y, temp1.getZ()));
				 
				 toReturn.add(new Location(null,temp2.getX(), temp2.getY() - y, temp2.getZ()));
			}
			
			if(highBlock.getX() != lowBlock.getX())
			{
				toReturn.add(new Location(null, highBlock.getX(), highBlock.getY() - y, lowBlock.getZ()));
				toReturn.add(new Location(null, lowBlock.getX(), highBlock.getY() - y, highBlock.getZ()));
			}
			else
			{
				toReturn.add(new Location(null, lowBlock.getX(), highBlock.getY() - y, highBlock.getZ()));
				toReturn.add(new Location(null, highBlock.getX(), highBlock.getY() - y, lowBlock.getZ()));
			}
		}
		for(int z = 0; z <= dz; z++)
		{
			Location temp1 = highBlock.clone();
			Location temp2 = lowBlock.clone();
			
			Location tempLowUp = lowBlock.clone();
			tempLowUp.setY(tempLowUp.getY() + dy);
			
			Location tempHighDown = highBlock.clone();
			tempHighDown.setY(tempHighDown.getY() - dy);
			
			if(highBlock.getZ() >= lowBlock.getZ())
			{

				toReturn.add(new Location(null,temp1.getX(), temp1.getY(), temp1.getZ() - z));
				
				toReturn.add(new Location(null,temp2.getX(), temp2.getY(), temp2.getZ() + z));
				
				//Add block above low block
				toReturn.add(new Location(null,tempLowUp.getX(), tempLowUp.getY(), tempLowUp.getZ() + z));
				
				//Add block below high
				toReturn.add(new Location(null,tempHighDown.getX(), tempHighDown.getY(), tempHighDown.getZ() - z));
			}
			else if(highBlock.getZ() <= lowBlock.getZ())
			{
				 toReturn.add(new Location(null,temp1.getX(), temp1.getY(), temp1.getZ() + z));
				 
				 toReturn.add(new Location(null,temp2.getX(), temp2.getY(), temp2.getZ() - z));
				 
				//Add block above low block
				toReturn.add(new Location(null,tempLowUp.getX(), tempLowUp.getY(), tempLowUp.getZ() - z));
				
				//Add block below high
				toReturn.add(new Location(null,tempHighDown.getX(), tempHighDown.getY(), tempHighDown.getZ() + z));
			}			 
		}
					  
		  // Spawn particles to display area
		  final ArrayList<Location> toIterate = toReturn;
		  final Player p = player;		 		  
		  final int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() { 	
			  
	            @Override
	            public void run() 
	            {
	            	for(Location loc : toIterate)
					  {					 
						  PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float)loc.getX() + .5f, (float)loc.getY() +.5f, (float)loc.getZ() +.5f, 0, 0, 0, 0, 1);
						  ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);				
					  }
	            }
	            
	        }, 0, 15L);
		  Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
		  {
			  
			  public void run() 
			  {
				  Bukkit.getScheduler().cancelTask(taskId);
			  }
			  }, 135L);
		  
		  
		  
		  
		  
		  
		  
		
		  
	}
	public int sizeOfFields()
	{
		return plugin.fields.size();
	}
	
	public boolean duplicateName(ProtectionField field)
	{
		for(ProtectionField fieldToCheck : plugin.fields)
		{
			if(fieldToCheck.getName().equals(field.getName()) && fieldToCheck.getOwner().equals(field.getOwner()))
			{
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<ProtectionField> getPlayerFields(Player player)
	{
		ArrayList<ProtectionField> toReturn = new ArrayList<ProtectionField>();
		for(ProtectionField field:plugin.fields)
		{
			if(field.getOwner().equals(player.getUniqueId()))
			{
				toReturn.add(field);
			}
		}
		return toReturn;
	}
			

}
