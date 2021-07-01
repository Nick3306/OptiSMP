package Nick3306.github.io.OptiSMP.Components.OptiProtect;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ProtectionField
{
	String name;
	UUID owner;
	public ArrayList<UUID> members = new ArrayList<UUID>();
	Location block1;
	Location block2;
	World world;
	int area;
	String defineType = null;
	int radius;
	//String greeting = "default" ;
	
	
	//flags
	Boolean pvp = false;
	boolean chestFlag;

	public ProtectionField(World world, Location block1, Location block2, UUID owner, String name, String type)
	{
		this.world = world;
		this.block1 = block1;
		this.block2 = block2;
		this.owner = owner;
		this.name = name;
		this.defineType = type;

	}
	public boolean getChestFlag()
	{
		return chestFlag;
	}
	public boolean pvp()
	{
		return pvp;
	}
	public UUID getOwner()
	{
		return owner;
	}
	public World getWorld()
	{
		return world;
	}
	public Location getBlock1()
	{
		return block1;
	}
	public Location getBlock2()
	{
		return block2;
	}
	public void setBlock1(Location block1)
	{
		this.block1 = block1;
	}
	public void setBlock2(Location block2)
	{
		this.block2 = block2;
	}
	public boolean isMember(Player a)
	{
		if(members.contains(a.getUniqueId()))
		{
			return true;
		}
		return false;
	}
	public String getName()
	{
		return name;
	}
	public void setArea()
	{
		int dx = (int) Math.abs((block1.getX() - block2.getX())) + 1;
		int dy = (int) Math.abs((block1.getY() - block2.getY())) + 1;
		int dz = (int) Math.abs((block1.getZ() - block2.getZ())) + 1;
		//Bukkit.getLogger().info("DX DY AND DZ IN AREA ARE" + dx + " " + dy + " " + dz);

		this.area = dx * dy * dz;

	}
	public int getArea()
	{
		return this.area;
	}
	public boolean inPField(Location loc)
	{
		//Bukkit.getLogger().info("Player location is: " + loc);
		if(this.world != null)
		{
		if(!loc.getWorld().getName().equalsIgnoreCase(this.world.getName()))
		{
			return false;
		}
		
		int maxX = Math.max(block1.getBlockX(), block2.getBlockX());
		int minX = Math.min(block1.getBlockX(), block2.getBlockX());
		int maxY = Math.max(block1.getBlockY(), block2.getBlockY());
		int minY = Math.min(block1.getBlockY(), block2.getBlockY());
		int maxZ = Math.max(block1.getBlockZ(), block2.getBlockZ());
		int minZ = Math.min(block1.getBlockZ(), block2.getBlockZ());
		//Bukkit.getLogger().info("mins and maxs are: " + Integer.toString(maxX) + " " + Integer.toString(minX) + " " + Integer.toString(maxY) + " " + Integer.toString(minY) + " " + Integer.toString(maxZ) + " " + Integer.toString(minZ) + " " );
		if(loc.getBlockX() >= minX && loc.getBlockX() <= maxX && loc.getBlockY() >= minY && loc.getBlockY() <= maxY && loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ)
		{
			//Bukkit.getLogger().info("inPField in the object returned true");
			return true;
		}
		}
		//Bukkit.getLogger().info("inPField in the object returned false");
		return false;
	}
	/*
	public void setGreeting(String toSet)
	{
		this.greeting = toSet;
	}
	public String getGreeting()
	{
		return this.greeting;
	}
	*/
	public String getDefineType() 
	{
		return this.defineType;
	}
	public void setDefineType(String type) 
	{
		this.defineType = type;
	}
	public void setRadius(int radius)
	{
		this.radius = radius;
	}
	public int getRadius()
	{
		return this.radius;
	}
}
