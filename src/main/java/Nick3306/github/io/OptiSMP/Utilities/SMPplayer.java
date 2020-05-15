package Nick3306.github.io.OptiSMP.Utilities;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;

public class SMPplayer
{
	UUID uuid;
	String name;
	boolean pvp = false;
	Player player;
	
	private ProtectUtilities util;
	// currency for later
	int points;
	
	//ProtectionField lastField = new ProtectionField(null, null, null, null, "none");	
	public ArrayList<ProtectionField> protectionFields = new ArrayList<ProtectionField>();
	
	// stats
	private String join_date;
	private String last_online;
	private int total_logins;
	private long time_online;
	private int total_votes;
	private int blocks_placed;
	private int blocks_broken;
	private int lines_spoken;
	private int damage_dealt;
	private int damage_received;
	private int players_killed;
	private int monsters_killed;
	private int animlas_killed;
	private int total_deaths;
	private int fish_caught;
	private int items_enchanted;
	private int animals_bred;
	private int protection_blocks_left;
	private int protection_blocks_max;
	public int loginTime;
	String lastField = "none";
	private boolean regionMessages = true;
		
	public SMPplayer(Player player, UUID uuid, String name, String join_date, String last_online, int total_logins, long time_online,
			int total_votes, int blocks_placed, int blocks_broken, int lines_spoken, int damage_dealt,
			int damage_received, int players_killed, int monsters_killed, int animlas_killed, int total_deaths,
			int fish_caught, int items_enchanted, int animals_bred, int protection_blocks_left, int protection_blocks_max)
	{
		super();
		this.player = player;
		this.uuid = uuid;
		this.name = name;
		this.join_date = join_date;
		this.last_online = last_online;
		//This constructer means player logged in so increment their total logins
		this.total_logins = total_logins +1 ;
		this.time_online = time_online;
		this.total_votes = total_votes;
		this.blocks_placed = blocks_placed;
		this.blocks_broken = blocks_broken;
		this.lines_spoken = lines_spoken;
		this.damage_dealt = damage_dealt;
		this.damage_received = damage_received;
		this.players_killed = players_killed;
		this.monsters_killed = monsters_killed;
		this.animlas_killed = animlas_killed;
		this.total_deaths = total_deaths;
		this.fish_caught = fish_caught;
		this.items_enchanted = items_enchanted;
		this.animals_bred = animals_bred;
		this.protection_blocks_left = protection_blocks_left;
		this.protection_blocks_max = protection_blocks_max;
	}


	public String getJoin_date()
	{
		return join_date;
	}


	public void setJoin_date(String join_date)
	{
		this.join_date = join_date;
	}


	public String getLast_online()
	{
		return last_online;
	}


	public void setLast_online(String last_online)
	{
		this.last_online = last_online;
	}


	public int getTotal_logins()
	{
		return total_logins;
	}


	public void setTotal_logins(int total_logins)
	{
		this.total_logins = total_logins;
	}


	public long getTime_online()
	{
		return time_online;
	}


	public void setTime_online(long time_online)
	{
		this.time_online = time_online;
	}


	public int getTotal_votes()
	{
		return total_votes;
	}


	public void setTotal_votes(int total_votes)
	{
		this.total_votes = total_votes;
	}


	public int getBlocks_placed()
	{
		return blocks_placed;
	}


	public void setBlocks_placed(int blocks_placed)
	{
		this.blocks_placed = blocks_placed;
	}


	public int getBlocks_broken()
	{
		return blocks_broken;
	}


	public void setBlocks_broken(int blocks_broken)
	{
		this.blocks_broken = blocks_broken;
	}


	public int getLines_spoken()
	{
		return lines_spoken;
	}


	public void setLines_spoken(int lines_spoken)
	{
		this.lines_spoken = lines_spoken;
	}


	public int getDamage_dealt()
	{
		return damage_dealt;
	}


	public void setDamage_dealt(int damage_dealt)
	{
		this.damage_dealt = damage_dealt;
	}


	public int getDamage_received()
	{
		return damage_received;
	}


	public void setDamage_received(int damage_received)
	{
		this.damage_received = damage_received;
	}


	public int getPlayers_killed()
	{
		return players_killed;
	}


	public void setPlayers_killed(int players_killed)
	{
		this.players_killed = players_killed;
	}


	public int getMonsters_killed()
	{
		return monsters_killed;
	}


	public void setMonsters_killed(int monsters_killed)
	{
		this.monsters_killed = monsters_killed;
	}


	public int getAnimlas_killed()
	{
		return animlas_killed;
	}


	public void setAnimlas_killed(int animlas_killed)
	{
		this.animlas_killed = animlas_killed;
	}


	public int getTotal_deaths()
	{
		return total_deaths;
	}


	public void setTotal_deaths(int total_deaths)
	{
		this.total_deaths = total_deaths;
	}


	public int getFish_caught()
	{
		return fish_caught;
	}


	public void setFish_caught(int fish_caught)
	{
		this.fish_caught = fish_caught;
	}


	public int getItems_enchanted()
	{
		return items_enchanted;
	}


	public void setItems_enchanted(int items_enchanted)
	{
		this.items_enchanted = items_enchanted;
	}


	public int getAnimals_bred()
	{
		return animals_bred;
	}


	public void setAnimals_bred(int animals_bred)
	{
		this.animals_bred = animals_bred;
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
	
	public int getProtectionBlocksLeft()
	{
		return protection_blocks_left;
	}
	
	public void setProtectionBlocksLeft(int blocks)
	{
		protection_blocks_left = blocks;
	}
	public int getProtectionBlocksMax()
	{
		return protection_blocks_max;
	}
	
	public void setProtectionBlocksMax(int blocks)
	{
		protection_blocks_max = blocks;
	}
	public ArrayList<ProtectionField> getPFields()
	{
		return protectionFields;
	}
	public void removeField(ProtectionField field)
	{
		for(ProtectionField listField : protectionFields)
		{
			if(field.getName().equals(listField.getName()))
			{
				protectionFields.remove(listField);
			}
		}
	}
	public void setLastField(String infield)
	{
		lastField = infield;
	}
	public String getLastField()
	{
		return lastField;
	}
	public void toggleRegionMessages()
	{
		if(regionMessages == true)
		{
			this.regionMessages = false;
		}
		else 
		{
			this.regionMessages = true;
		}
	}
	public boolean getRegionMessages() 
	{
		return this.regionMessages;
	}

}
