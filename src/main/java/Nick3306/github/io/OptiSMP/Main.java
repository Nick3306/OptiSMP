package Nick3306.github.io.OptiSMP;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.naming.NamingException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Nick3306.github.io.OptiSMP.Commands.PField;
import Nick3306.github.io.OptiSMP.Commands.Stats;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Components.Trade.Trade;
import Nick3306.github.io.OptiSMP.Components.Trade.TradeUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Listeners.BlockBreakListener;
import Nick3306.github.io.OptiSMP.Listeners.BlockEditListeners;
import Nick3306.github.io.OptiSMP.Listeners.PlayerJoinListener;
import Nick3306.github.io.OptiSMP.Listeners.PlayerLeaveListener;
import Nick3306.github.io.OptiSMP.Listeners.PlayerMovement;
import Nick3306.github.io.OptiSMP.Listeners.BlockPlaceListener;
import Nick3306.github.io.OptiSMP.Listeners.BucketEmptyListener;
import Nick3306.github.io.OptiSMP.Listeners.ChatListener;
import Nick3306.github.io.OptiSMP.Listeners.EnchantItemListener;
import Nick3306.github.io.OptiSMP.Listeners.EntityDeathListener;
import Nick3306.github.io.OptiSMP.Listeners.PlayerDeathListener;
import Nick3306.github.io.OptiSMP.Listeners.PlayerFishListener;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.MySql;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;



public class Main extends JavaPlugin
{
	public ArrayList<ProtectionField> fields = new ArrayList<ProtectionField>();
	public ArrayList<ProtectionField> newFields = new ArrayList<ProtectionField>();
	//public ArrayList<SMPplayer> players = new ArrayList<SMPplayer>();
	public HashMap<UUID, SMPplayer> players = new HashMap<UUID, SMPplayer>();
	
	public ArrayList<Trade> trades = new ArrayList<Trade>();
	
	//Utilities
	public GeneralUtilities util;
	public ProtectUtilities protectUtil;
	public TradeUtilities tradeUtil;
	public MySql sql;
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		util = new GeneralUtilities(this);
		protectUtil = new ProtectUtilities(this);
		tradeUtil = new TradeUtilities(this);
		
		sql = new MySql(this);		 
		sql.getFields();
		
		// Register listeners
		pm.registerEvents(new BlockEditListeners(this), this);
		pm.registerEvents(new BlockPlaceListener(this), this);
		pm.registerEvents(new BlockBreakListener(this), this);
		pm.registerEvents(new PlayerMovement(this), this);
		pm.registerEvents(new PlayerJoinListener(this), this);
		pm.registerEvents(new PlayerLeaveListener(this), this);
		pm.registerEvents(new EnchantItemListener(this), this);
		pm.registerEvents(new PlayerFishListener(this), this);
		pm.registerEvents(new PlayerDeathListener(this), this);
		pm.registerEvents(new EntityDeathListener(this), this);
		pm.registerEvents(new ChatListener(this), this);
		pm.registerEvents(new BucketEmptyListener(this), this);
		
		
		getCommand("pfield").setExecutor(new PField(this));
		getCommand("stats").setExecutor(new Stats(this));
		
	}

	public void onDisable()
	{
		// Save all players
		for(SMPplayer player : players.values())
		{
			sql.savePlayerShutdown(player);
		}
		
		sql.closeConnections();
	}
}
