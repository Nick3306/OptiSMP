package Nick3306.github.io.OptiSMP.Utilities;
import java.util.UUID;

public class SMPplayer
{
	UUID uuid;
	String name;
	boolean pvp = false;
	// currency for later
	int points;
	
	
	public SMPplayer()
	{
		
	}


	public UUID getUuid()
	{
		return uuid;
	}


	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public boolean isPvp()
	{
		return pvp;
	}


	public void setPvp(boolean pvp)
	{
		this.pvp = pvp;
	}
	
}
