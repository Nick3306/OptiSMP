package Nick3306.github.io.OptiSMP.Utilities;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        dataSource.addDataSourceProperty("serverName", "144.217.68.13");
        dataSource.addDataSourceProperty("port", "3306");
        dataSource.addDataSourceProperty("databaseName", "mc30875");
        dataSource.addDataSourceProperty("user", "mc30875");
        dataSource.addDataSourceProperty("password", "cad6a753e0");
       // dataSource.setMaximumPoolSize(5);
	   // dataSource.setIdleTimeout(0);
	}
public void getFields()
{

	Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
	{
		public void run() 
		{
			int id;
			UUID owner;
			String block1String, block2String;
			Location block1, block2;
			World world;

			try 
			{
				//Connection myConn = DriverManager.getConnection("jdbc:mysql://144.217.68.13:3306/mc30874","mc30875","cad6a753e0");
				Connection myConn = dataSource.getConnection();
				PreparedStatement myStatement = myConn.prepareStatement("SELECT * FROM ProtectionFields;");
				ResultSet fieldsResult = myStatement.executeQuery();
				while(fieldsResult.next() != false)
				{
					//Grab field variables
					id = fieldsResult.getInt("id");
					owner = UUID.fromString(fieldsResult.getString("Owner"));
					block1String = fieldsResult.getString("block1");
					world = plugin.getServer().getWorld(fieldsResult.getString("World"));
					String[] block1Coords = block1String.split(",");
					block1 = new Location(world, Double.parseDouble(block1Coords[0]),Double.parseDouble(block1Coords[1]),Double.parseDouble(block1Coords[2]));
					
					block2String = fieldsResult.getString("block2");
					String[] block2Coords = block2String.split(",");
					block2 = new Location(world, Double.parseDouble(block2Coords[0]),Double.parseDouble(block2Coords[1]),Double.parseDouble(block2Coords[2]));
					
					ProtectionField fieldToAdd = new ProtectionField(world, block1, block2, owner, id);
					
					
					//Grab field members for current field					
					PreparedStatement fieldMembers = myConn.prepareStatement("SELECT * FROM FieldMembers WHERE fieldID =?;");
					fieldMembers.setString(1, owner.toString());	
					ResultSet fieldMembersResult = fieldMembers.executeQuery();
					
					//add all field members to fields member arraylist
					while(fieldMembersResult.next() != false)
					{
						fieldToAdd.members.add(UUID.fromString(fieldMembersResult.getString("uuid")));
					}
					
					//Add field to list
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
					myStatement.setInt(1, field.getId());
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
	public void addMember(final ProtectionField field, final Player player)
	{
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
		{
			public void run() 
			{
				try
				{
					//add field to the database
					Connection myConn = dataSource.getConnection();
					PreparedStatement myStatement = myConn.prepareStatement("INSERT INTO FieldMembers VALUES (?,?);");
					myStatement.setString(1, player.getUniqueId().toString());
					myStatement.setInt(2, field.getId());
					myStatement.execute();
					
				}
				catch(Exception e)
				{
					
				}
			}
		});
	}
	public void removeMember(final ProtectionField field, final Player player)
	{
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable()
		{
			public void run() 
			{
				try
				{
					//add field to the database
					Connection myConn = dataSource.getConnection();
					PreparedStatement myStatement = myConn.prepareStatement("DELETE FROM FieldMembers WHERE uuid =? AND fieldID = ?;");
					myStatement.setString(1, player.getUniqueId().toString());
					myStatement.setInt(2, field.getId());
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
					PreparedStatement myStatement = myConn.prepareStatement("DELETE FROM ProtectionFields WHERE id =?;");
					myStatement.setInt(1, field.getId());
					myStatement.execute();
					
					//Remove all members of deleted field from the field members table
				
					myStatement = myConn.prepareStatement("DELETE FROM FieldMembers WHERE fieldID = ?;");						
					myStatement.setInt(1, field.getId());
					myStatement.execute();
					
					
					
				
					
				}
				catch(Exception e)
				{
					
				}
			}
		});
	}
	public void closeConnections()
	{
		dataSource.close();
	}
	
}

		
