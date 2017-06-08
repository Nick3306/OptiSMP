package Protect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Main;


//Class contains functions that will have to be used over and over again on a general basis.
public class Utilities 
{
	private Main plugin;
	public Utilities(Main plugin)
	{
	   this.plugin = plugin;
	}
	
	// Checks is location is inside of any field
	// Will be upgraded later to only check fields in the same world as the location
	public boolean inField(Location loc)
	{
		Bukkit.getLogger().info("In inField");
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
		for(ProtectionField field : plugin.fields)
		{
			if(field.inPField(loc))
			{
				return field;
			}
		}

		return null;
	}
	
	//Used to determine if a player is currently defining a field
	public boolean definingField(Player player)
	{
		player.sendMessage("In defining field");
		for(int i = 0; i < plugin.newFields.size(); i++)
		{
			player.sendMessage(plugin.newFields.get(i).getOwner() + " : " + player.getUniqueId());
			if (plugin.newFields.get(i).getOwner().equals(player.getUniqueId()))
			{
				return true;
			}
		}
		player.sendMessage("After for in defining field");
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
			if(field.getId() == plugin.fields.get(i).getId())
			{
				plugin.fields.remove(i);
			}
		}
	}
	
	// Checks is the defined field overlaps with another
	public boolean fieldOverlap(Location loc1Block1, Location loc2Block1, Location loc1Block2, Location loc2Block2)
	{
		int block1Xmax = (int) Math.max(loc1Block1.getX(), loc2Block1.getX());
		int block1Xmin = (int) Math.min(loc1Block1.getX(), loc2Block1.getX());
		int block1Ymax = (int) Math.max(loc1Block1.getY(), loc2Block1.getY());
		int block1Ymin = (int) Math.min(loc1Block1.getY(), loc2Block1.getY());
		int block1Zmax = (int) Math.max(loc1Block1.getZ(), loc2Block1.getZ());
		int block1Zmin = (int) Math.min(loc1Block1.getZ(), loc2Block1.getZ());
		
		int block2Xmax = (int) Math.max(loc1Block2.getX(), loc2Block2.getX());
		int block2Xmin = (int) Math.min(loc1Block2.getX(), loc2Block2.getX());
		int block2Ymax  = (int) Math.max(loc1Block2.getY(),loc2Block2.getY());
		int block2Ymin = (int) Math.min(loc1Block2.getY(), loc2Block2.getY());
		int block2Zmax = (int) Math.max(loc1Block2.getZ(), loc2Block2.getZ());
		int block2Zmin = (int) Math.min(loc1Block2.getZ(), loc2Block2.getZ());
		
		
		 if(!intersectsDimension(block1Xmin, block1Xmax, block2Xmin, block2Xmax))
		        return false;
		 
		    if(!intersectsDimension(block1Ymin, block1Ymax, block2Ymin, block2Ymax))
		        return false;
		 
		    if(!intersectsDimension(block1Zmin, block1Zmax, block2Zmin, block2Zmax))
		        return false;
		 
		    return true;
		
	}
	// used for help with fieldOverlap
	public boolean intersectsDimension(int aMin, int aMax, int bMin, int bMax)
	{
	    return aMin <= bMax && aMax >= bMin;
	}
}
