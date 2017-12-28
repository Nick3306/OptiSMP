package Nick3306.github.io.OptiSMP.Utilities;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.zaxxer.hikari.*;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;

// Uses Hikari to implement connection pooling so a new connection doesnt have to be opened every time I need it
public class MySql 
{
	private Main plugin;
	private ProtectUtilities util;
	HikariDataSource dataSource;
	public MySql(Main plugin)
	{
		dataSource = new HikariDataSource();
		this.plugin = plugin;
		this.util = this.plugin.protectUtil;
				
		
		//loads the jdbc driver
	/*
		dataSource.setJdbcUrl("jdbc:mysql://144.217.68.13:3306/mc30875?autoReconnect=true&useSSL=false");
		dataSource.setUsername("mc30875");
		dataSource.setPassword("cad6a753e0");	
		dataSource.setMaximumPoolSize(5);
		dataSource.setIdleTimeout(0);
	*/
		
		dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		//
        dataSource.setMaximumPoolSize(15);
	   // dataSource.setIdleTimeout(0);
	}
public void getFields()
{

	Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
	{
		public void run() 
		{
			String name;
			UUID owner;
			String block1String, block2String;
			Location block1, block2;
			World world;
			String greeting;
			
			try 
			{	
				Connection myConn = dataSource.getConnection();
				PreparedStatement myStatement = myConn.prepareStatement("SELECT * FROM ProtectionFields;");
				ResultSet fieldsResult = myStatement.executeQuery();
				while(fieldsResult.next() != false)
				{
					//Grab field variables
					name = fieldsResult.getString("field_name");
					owner = UUID.fromString(fieldsResult.getString("Owner"));
					block1String = fieldsResult.getString("block1");
					world = plugin.getServer().getWorld(fieldsResult.getString("World"));				
					String[] block1Coords = block1String.split(",");
					block1 = new Location(world, Double.parseDouble(block1Coords[0]),Double.parseDouble(block1Coords[1]),Double.parseDouble(block1Coords[2]));
					
					block2String = fieldsResult.getString("block2");
					String[] block2Coords = block2String.split(",");
					block2 = new Location(world, Double.parseDouble(block2Coords[0]),Double.parseDouble(block2Coords[1]),Double.parseDouble(block2Coords[2]));
					
					ProtectionField fieldToAdd = new ProtectionField(world, block1, block2, owner, name);
					
					
					//Grab field members for current field					
					PreparedStatement fieldMembers = myConn.prepareStatement("SELECT * FROM FieldMembers WHERE field_name =? AND field_owner =?;");
					fieldMembers.setString(1, name);
					fieldMembers.setString(2, owner.toString());	
					ResultSet fieldMembersResult = fieldMembers.executeQuery();
					
					//add all field members to fields member arraylist
					while(fieldMembersResult.next() != false)
					{
						fieldToAdd.members.add(UUID.fromString(fieldMembersResult.getString("uuid")));
					}
					
					//Add field to list
					fieldToAdd.setArea();
					plugin.fields.add(fieldToAdd);
					
					
				}
				myConn.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();	
			}
			
		}});
}
	public void addField(final ProtectionField field)
	{
		
		Bukkit.getLogger().info("Adding field to database");
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
		{
			public void run() 
			{
				try
				{
					Bukkit.getLogger().info(field.getWorld().getName());
					//add field to the database
					Connection myConn = dataSource.getConnection();
					PreparedStatement myStatement = myConn.prepareStatement("INSERT INTO ProtectionFields VALUES (?,?,?,?,?);");
					myStatement.setString(1, field.getName());
					myStatement.setString(2, field.getOwner().toString());
					Location block1 = field.getBlock1();
					String block1String = block1.getBlockX() + "," + block1.getBlockY() + "," + block1.getBlockZ();
					myStatement.setString(3, block1String);
					Location block2 = field.getBlock2();
					String block2String = block2.getBlockX() + "," + block2.getBlockY() + "," + block2.getBlockZ();
					myStatement.setString(4, block2String);
					myStatement.setString(5, field.getWorld().getName());			
					myStatement.execute();
					myConn.close();
					Bukkit.getLogger().info("Field added to database successfully");
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Bukkit.getLogger().info("FAILED to insert into DB");
					
				}
				
			}
		});
	}
	public void addMember(final ProtectionField field, final OfflinePlayer playerToAdd)
	{
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
		{
			public void run() 
			{
				try
				{
					//add field to the database
					Connection myConn = dataSource.getConnection();
					PreparedStatement myStatement = myConn.prepareStatement("INSERT INTO FieldMembers VALUES (?,?,?);");
					myStatement.setString(1, playerToAdd.getUniqueId().toString());
					myStatement.setString(2, field.getName());
					myStatement.setString(3, field.getOwner().toString());
					myStatement.execute();
					myConn.close();
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Bukkit.getLogger().info("FAILED to add to DB");
				}
			}
		});
	}
	public void removeMember(final ProtectionField field, final OfflinePlayer playerToRemove)
	{
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
		{
			public void run() 
			{
				try
				{
					//add field to the database
					Connection myConn = dataSource.getConnection();
					PreparedStatement myStatement = myConn.prepareStatement("DELETE FROM FieldMembers WHERE uuid =? AND field_name =? AND field_owner =?;");
					myStatement.setString(1, playerToRemove.getUniqueId().toString());
					myStatement.setString(2, field.getName());
					myStatement.setString(3, field.getOwner().toString());
					myStatement.execute();
					myConn.close();
					
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Bukkit.getLogger().info("FAILED to remove from DB");
				}
			}
		});
	}
	public void removeField(final ProtectionField field)
	{
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
		{
			public void run() 
			{
				try
				{
					Bukkit.getLogger().info("Removing field from the DB");
					//Remove field from ProtectionField table
					Connection myConn = dataSource.getConnection();
					PreparedStatement myStatement = myConn.prepareStatement("DELETE FROM ProtectionFields WHERE field_name =? AND owner =?;");
					myStatement.setString(1, field.getName());
					myStatement.setString(2, field.getOwner().toString());
					myStatement.execute();
					
					//Remove all members of deleted field from the field members table
				
					myStatement = myConn.prepareStatement("DELETE FROM FieldMembers WHERE field_name = ? AND field_owner=?;");						
		    		myStatement.setString(1, field.getName());
					myStatement.setString(2, field.getOwner().toString());
					myStatement.execute();	
					
					myConn.close();
					Bukkit.getLogger().info("Successfully Removed field from the DB");
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Bukkit.getLogger().info("FAILED to remove field from DB");					
				}
			}
		});
	}
	public void getPlayer(final Player player)
	{
		
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
		{
			public void run() 
			{
				try
				{
					Connection myConn = dataSource.getConnection();
					PreparedStatement myStatement = myConn.prepareStatement("SELECT * FROM player_data WHERE uuid = ?;");
					myStatement.setString(1, player.getUniqueId().toString());
					ResultSet playerResult = myStatement.executeQuery();
					if(playerResult.next() == false)
					{
						Bukkit.getLogger().info("player doesnt exist in DB, creating entry");
						//player doesnt exist, create them
						myStatement = myConn.prepareStatement("INSERT INTO player_data " + "VALUES (?,?,?,?,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,15625,15625)");
						myStatement.setString(1, player.getUniqueId().toString());
						myStatement.setString(2, player.getName());
						myStatement.setString(3, LocalDateTime.now().toString());
						myStatement.setString(4, LocalDateTime.now().toString());
						
						
						Bukkit.getLogger().info("Creating new player object from data");
						//create smp player object
						SMPplayer toAdd = new SMPplayer(player, player.getUniqueId(), player.getName(), LocalDateTime.now().toString(), LocalDateTime.now().toString(), 1, 0, 0, 0, 0,0 , 0,
								0, 0, 0, 0, 0, 0, 0, 0, 15625, 15625);
						//set layer login time
						toAdd.loginTime = (int) (System.currentTimeMillis());
						//Put smp player into the hashmap
						plugin.players.put(player.getUniqueId(), toAdd);
						
						myStatement.execute();
						myConn.close();
					}
					else
					{	
						Bukkit.getLogger().info("player exists in DB, grabbing data");
						//player exists, create their SMPplayer class			
						String uuid = playerResult.getString("uuid");
						String name = playerResult.getString("current_name");
						String join_date = playerResult.getString("join_date");
						String last_online = playerResult.getString("last_online");
						int total_logins = playerResult.getInt("total_logins");
						int time_online = playerResult.getInt("time_online");
						int total_votes = playerResult.getInt("total_votes");
						int blocks_placed = playerResult.getInt("blocks_placed");
						int blocks_broken = playerResult.getInt("blocks_broken");
						int lines_spoken = playerResult.getInt("lines_spoken");
						int damage_dealt = playerResult.getInt("damage_dealt");
						int damage_received = playerResult.getInt("damage_received");
						int players_killed = playerResult.getInt("players_killed");
						int monsters_killed = playerResult.getInt("monsters_killed");
						int animals_killed = playerResult.getInt("animals_killed");
						int total_deaths = playerResult.getInt("total_deaths");
						int fish_caught = playerResult.getInt("fish_caught");
						int items_enchanted = playerResult.getInt("items_enchanted");
						int animals_bred = playerResult.getInt("animals_bred");
						int protection_blocks_left = playerResult.getInt("protection_blocks_left");
						int protection_blocks_max = playerResult.getInt("protection_blocks_max");
						

						Bukkit.getLogger().info("Creating new player object from data");
						//create smp player object
						SMPplayer toAdd = new SMPplayer(player, UUID.fromString(uuid), name, join_date, last_online, total_logins, time_online, total_votes, blocks_placed, blocks_broken, lines_spoken, damage_dealt,
								damage_received, players_killed, monsters_killed, animals_killed, total_deaths, fish_caught, items_enchanted, animals_bred, protection_blocks_left, protection_blocks_max);
						//set layer login time
						toAdd.loginTime = (int) (System.currentTimeMillis());
						toAdd.setLast_online(LocalDateTime.now().toString());
						//Put smp player into the hashmap
						plugin.players.put(player.getUniqueId(), toAdd);		
						Bukkit.getLogger().info("player retrieved from the DB");
						myConn.close();
						
						PermissionUser user = PermissionsEx.getUser(player);
						Bukkit.getLogger().info("Prefix is: " + user.getPrefix());
						List<String> groups = user.getParentIdentifiers();
						Bukkit.getLogger().info("Rank is is: " + groups.get(0));
						
						// Check joining players stats to see if they should be promoted
						if(groups.get(0).equalsIgnoreCase("Guest"))
						{
							Bukkit.getLogger().info("player is a guest");
							//Check if they should be promoted to Member
							if((toAdd.getTime_online() >= 7200000))
							{
								user.addGroup("Member");
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "givehome " + player.getName() + " 1");
							}
							
						}
						else if(groups.get(0).equalsIgnoreCase("Member"))
						{
							Bukkit.getLogger().info("player is a member");
							//Check if they should be promoted to Resident
							if((toAdd.getTime_online() >= 28800000) && ((toAdd.getBlocks_broken() + toAdd.getBlocks_placed()) >= 10000) && (toAdd.getMonsters_killed() >= 90))
							{
								user.addGroup("Resident");
								toAdd.setProtectionBlocksLeft(toAdd.getProtectionBlocksLeft() + 7812);
								toAdd.setProtectionBlocksMax(toAdd.getProtectionBlocksMax() + 7812);
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "givehome " + player.getName() + " 1");
							}
							
							
						}
						else if(groups.get(0).equalsIgnoreCase("Resident"))
						{
							Bukkit.getLogger().info("player is a resident");
							//Check if they should be promoted to Citizen
							if((toAdd.getTime_online() >= 86400000) && ((toAdd.getBlocks_broken() + toAdd.getBlocks_placed()) >= 30000) && (toAdd.getMonsters_killed() >= 275))
							{
								user.addGroup("Citizen");
								toAdd.setProtectionBlocksLeft(toAdd.getProtectionBlocksLeft() + 11718);
								toAdd.setProtectionBlocksMax(toAdd.getProtectionBlocksMax() + 11718);
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "givehome " + player.getName() + " 1");
							}
							
						}
						else if(groups.get(0).equalsIgnoreCase("Citizen"))
						{
							Bukkit.getLogger().info("player is a citizen");
							//Check if they should be promoted to Veteran
							if((toAdd.getTime_online() >= 360000000) && ((toAdd.getBlocks_broken() + toAdd.getBlocks_placed()) >= 125000) && (toAdd.getMonsters_killed() >= 1120))
							{
								user.addGroup("Veteran");
								toAdd.setProtectionBlocksLeft(toAdd.getProtectionBlocksLeft() + 17577);
								toAdd.setProtectionBlocksMax(toAdd.getProtectionBlocksMax() + 17577);
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "givehome " + player.getName() + " 1");
							}
							
						}
						
					}
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Bukkit.getLogger().info("FAILED to retrieve player");
				}
			}
		});
	}
	public void savePlayer(final SMPplayer player)
	{
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
		{
			public void run() 
			{
				try
				{
					Bukkit.getLogger().info("Attempting to save player to the DB");
					Connection myConn = dataSource.getConnection();
					Bukkit.getLogger().info("After connection grabbed");
					PreparedStatement myStatement = myConn.prepareStatement("UPDATE player_data SET current_name=?, last_online=?, total_logins=?,time_online=?,total_votes=?, blocks_placed=?, blocks_broken=?, lines_spoken=?, damage_dealt=?, damage_received=?, players_killed=?, monsters_killed=?, animals_killed=?, total_deaths=?, fish_caught=?, items_enchanted=?, animals_bred=?, protection_blocks_left=?, protection_blocks_max=? WHERE uuid = ?;");
					Bukkit.getLogger().info("After prepared statement");
					myStatement.setString(1,player.getName());
					myStatement.setString(2,player.getLast_online());
					myStatement.setInt(3,player.getTotal_logins());
					myStatement.setInt(4,player.getTime_online());
					myStatement.setInt(5,player.getTotal_votes());
					myStatement.setInt(6,player.getBlocks_placed());
					myStatement.setInt(7,player.getBlocks_broken());
					myStatement.setInt(8,player.getLines_spoken());
					myStatement.setInt(9,player.getDamage_dealt());
					myStatement.setInt(10,player.getDamage_received());
					myStatement.setInt(11, player.getPlayers_killed());
					myStatement.setInt(12, player.getMonsters_killed());
					myStatement.setInt(13, player.getAnimlas_killed());
					myStatement.setInt(14, player.getTotal_deaths());
					myStatement.setInt(15, player.getFish_caught());
					myStatement.setInt(16, player.getItems_enchanted());
					myStatement.setInt(17, player.getAnimals_bred());			
					myStatement.setInt(18, player.getProtectionBlocksLeft());
					myStatement.setInt(19, player.getProtectionBlocksMax());
					myStatement.setString(20, player.getUuid().toString());
					Bukkit.getLogger().info("Before execute query");
					myStatement.executeUpdate();	
					Bukkit.getLogger().info("player updated in the DB");
					myConn.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Bukkit.getLogger().info("FAILED to update player");
				}
			}
		});
	}
	public void savePlayerShutdown(SMPplayer player)
	{
		Bukkit.getLogger().info("Attempting to save player to the DB after unexpected shutdown");
		try 
		{
			Bukkit.getLogger().info("Attempting to save player to the DB");
			Connection myConn = dataSource.getConnection();
			Bukkit.getLogger().info("After connection grabbed");
			PreparedStatement myStatement = myConn.prepareStatement("UPDATE player_data SET current_name=?, last_online=?, total_logins=?,time_online=?,total_votes=?, blocks_placed=?, blocks_broken=?, lines_spoken=?, damage_dealt=?, damage_received=?, players_killed=?, monsters_killed=?, animals_killed=?, total_deaths=?, fish_caught=?, items_enchanted=?, animals_bred=?, protection_blocks_left=?, protection_blocks_max=? WHERE uuid = ?;");
			Bukkit.getLogger().info("After prepared statement");
			myStatement.setString(1,player.getName());
			myStatement.setString(2,player.getLast_online());
			myStatement.setInt(3,player.getTotal_logins());
			myStatement.setInt(4,player.getTime_online());
			myStatement.setInt(5,player.getTotal_votes());
			myStatement.setInt(6,player.getBlocks_placed());
			myStatement.setInt(7,player.getBlocks_broken());
			myStatement.setInt(8,player.getLines_spoken());
			myStatement.setInt(9,player.getDamage_dealt());
			myStatement.setInt(10,player.getDamage_received());
			myStatement.setInt(11, player.getPlayers_killed());
			myStatement.setInt(12, player.getMonsters_killed());
			myStatement.setInt(13, player.getAnimlas_killed());
			myStatement.setInt(14, player.getTotal_deaths());
			myStatement.setInt(15, player.getFish_caught());
			myStatement.setInt(16, player.getItems_enchanted());
			myStatement.setInt(17, player.getAnimals_bred());			
			myStatement.setInt(18, player.getProtectionBlocksLeft());
			myStatement.setInt(19, player.getProtectionBlocksMax());
			myStatement.setString(20, player.getUuid().toString());
			Bukkit.getLogger().info("Before execute query");
			myStatement.executeUpdate();	
			Bukkit.getLogger().info("player updated in the DB");
			myConn.close();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Bukkit.getLogger().info("FAILED to save player");
		}
	}
	
	
	public void getPlayerByUUID(final UUID uuid, final Player player)
	{
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
		{
			public void run() 
			{
				try
				{
					Bukkit.getLogger().info("Attempting to get player from the DB by UUID");
					Connection myConn = dataSource.getConnection();
					Bukkit.getLogger().info("After connection grabbed");
					PreparedStatement myStatement = myConn.prepareStatement("SELECT * FROM player_data WHERE uuid = ?;");
					myStatement.setString(1, uuid.toString());
					ResultSet playerResult = myStatement.executeQuery();
					if(playerResult.next())
					{
						String current_name = playerResult.getString("current_name");
						String join_date = playerResult.getString("join_date");
						String last_online = playerResult.getString("last_online");
						int total_logins = playerResult.getInt("total_logins");
						int time_online = playerResult.getInt("time_online");
						int total_votes = playerResult.getInt("total_votes");
						int blocks_placed = playerResult.getInt("blocks_placed");
						int blocks_broken = playerResult.getInt("blocks_broken");
						int lines_spoken = playerResult.getInt("lines_spoken");
						int damage_dealt = playerResult.getInt("damage_dealt");
						int damage_received = playerResult.getInt("damage_received");
						int players_killed = playerResult.getInt("players_killed");
						int monsters_killed = playerResult.getInt("monsters_killed");
						int animals_killed = playerResult.getInt("animals_killed");
						int total_deaths = playerResult.getInt("total_deaths");
						int fish_caught = playerResult.getInt("fish_caught");
						int items_enchanted = playerResult.getInt("items_enchanted");
						int animals_bred = playerResult.getInt("animals_bred");
					
					
						player.sendMessage(ChatColor.GREEN + "Stats for player " + current_name);
						player.sendMessage(ChatColor.YELLOW + "Join Date: " + join_date);
						player.sendMessage(ChatColor.YELLOW + "Last Online: " + last_online);
						player.sendMessage(ChatColor.YELLOW + "Total Logins: " + total_logins);					
						long second = (time_online / 1000) % 60;
						long minute = (time_online  / (1000 * 60)) % 60;
						long hour = (time_online / (1000 * 60 * 60));
						String time = (hour + " hours " + minute + " minutes " +  second + " seconds.");
						player.sendMessage(ChatColor.YELLOW + "Time Online: " + time);
						player.sendMessage(ChatColor.YELLOW + "lines_spoken: " + lines_spoken);
						player.sendMessage(ChatColor.YELLOW + "Blocks Placed: " + blocks_placed);
						player.sendMessage(ChatColor.YELLOW + "Blocks Broken: " + blocks_broken);
						player.sendMessage(ChatColor.YELLOW + "Players Killed: " + players_killed);
						player.sendMessage(ChatColor.YELLOW + "Monsters Killed: " + monsters_killed);
						player.sendMessage(ChatColor.YELLOW + "Animals Killed: " + animals_killed);
						player.sendMessage(ChatColor.YELLOW + "Total Deaths: " + total_deaths);
					}
					Bukkit.getLogger().info("player grabbed from the DB");
					myConn.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Bukkit.getLogger().info("FAILED toget player stats from DB");
				}
			}
		});		
	}
	public void closeConnections()
	{
		dataSource.close();
	}
	
}

		
