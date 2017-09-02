package Nick3306.github.io.OptiSMP.Utilities;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.zaxxer.hikari.*;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
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
        dataSource.addDataSourceProperty("serverName", "127.0.0.1");
        dataSource.addDataSourceProperty("port", "3306");
        dataSource.addDataSourceProperty("databaseName", "mdb_2");
        dataSource.addDataSourceProperty("user", "mdb_2");
        dataSource.addDataSourceProperty("password", "3ad657fda1");
       // dataSource.setMaximumPoolSize(5);
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
						myStatement = myConn.prepareStatement("INSERT INTO player_data " + "VALUES (?,?,?,?,1,?,0,0,0,0,0,0,0,0,0,0,0,0,0)");
						myStatement.setString(1, player.getUniqueId().toString());
						myStatement.setString(2, player.getName());
						myStatement.setString(3, LocalDateTime.now().toString());
						myStatement.setString(4, LocalDateTime.now().toString());
						myStatement.setInt(5, (int) java.lang.System.currentTimeMillis());
						
						myStatement.execute();
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
						String time_online = playerResult.getString("time_online");
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
						
						SMPplayer toAdd = new SMPplayer(UUID.fromString(uuid), name, join_date, last_online, total_logins, time_online, total_votes, blocks_placed, blocks_broken, lines_spoken, damage_dealt,
								damage_received, players_killed, monsters_killed, animals_killed, total_deaths, fish_caught, items_enchanted, animals_bred);
						plugin.players.add(toAdd);
						
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
					Connection myConn = dataSource.getConnection();
					PreparedStatement myStatement = myConn.prepareStatement("UPDATE player_data SET current_name=?,last_online=?, total_logins=?,time_online=?,total_votes=?, blocks_placed=?, blocks_broken=?" +
							"lines_spoken=?, damage_dealt=?, damage_received=?, players_killed=?, monsters_killed=?, animals_killed=?, total_deaths=?, fish_caight=?, items_enchanted=?, animals_bred=? WHERE uuid = ?;");
					myStatement.setString(1,player.getName());
					myStatement.setString(2,player.getLast_online());
					myStatement.setInt(3,player.getTotal_logins());
					myStatement.setString(4,player.getTime_online());
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
					myStatement.setString(18, player.getUuid().toString());
					myStatement.executeQuery();
						
					
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Bukkit.getLogger().info("FAILED to update player");
				}
			}
		});
	}
	public void closeConnections()
	{
		dataSource.close();
	}
	
}

		
