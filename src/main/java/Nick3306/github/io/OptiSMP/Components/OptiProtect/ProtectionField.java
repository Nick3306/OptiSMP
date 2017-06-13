package Nick3306.github.io.OptiSMP.Components.OptiProtect;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ProtectionField
{
	int id;
	UUID owner;
	public ArrayList<UUID> members = new ArrayList<UUID>();
	Location block1;
	Location block2;
	World world;
	Boolean pvp = false;
	
	public ProtectionField(World world, Location block1, Location block2, UUID owner, int id)
	{
		this.world = world;
		this.block1 = block1;
		this.block2 = block2;
		this.owner = owner;
		this.id = id;
		
	}
	boolean chestFlag;
	public boolean getChestFlag()
	{
		return chestFlag;
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
	public int getId()
	{
		return id;
	}
	public double getArea()
	{
		return (Math.abs(block2.getX())-Math.abs(block1.getBlockX())) * (Math.abs(block2.getY())-Math.abs(block1.getY())) * (Math.abs(block2.getZ())-Math.abs(block1.getZ()));
	}
	public boolean inPField(Location loc)
	{
		int maxX = Math.max(block1.getBlockX(), block2.getBlockX());
		int minX = Math.min(block1.getBlockX(), block2.getBlockX());
		int maxY = Math.max(block1.getBlockY(), block2.getBlockY());
		int minY = Math.min(block1.getBlockY(), block2.getBlockY());
		int maxZ = Math.max(block1.getBlockZ(), block2.getBlockZ());
		int minZ = Math.min(block1.getBlockZ(), block2.getBlockZ());
		if(loc.getBlockX() >= minX && loc.getBlockX() <= maxX && loc.getBlockY() >= minY && loc.getBlockY() <= maxY && loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ)
		{
			return true;
		}
		return false;
	}
}
