package Nick3306.github.io.OptiSMP;

import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.naming.NamingException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Nick3306.github.io.OptiSMP.Commands.PField;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Components.Trade.Trade;
import Nick3306.github.io.OptiSMP.Components.Trade.TradeUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Listeners.BlockEditListeners;
import Nick3306.github.io.OptiSMP.Listeners.PlayerJoinListener;
import Nick3306.github.io.OptiSMP.Listeners.PlayerLeaveListener;
import Nick3306.github.io.OptiSMP.Listeners.PlayerMovement;
import Nick3306.github.io.OptiSMP.Listeners.RegionDefineListener;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.MySql;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;



public class Main extends JavaPlugin
{
	public ArrayList<ProtectionField> fields = new ArrayList<ProtectionField>();
	public ArrayList<ProtectionField> newFields = new ArrayList<ProtectionField>();
	public ArrayList<SMPplayer> players = new ArrayList<SMPplayer>();
	
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
		
		
		pm.registerEvents(new BlockEditListeners(this), this);
		pm.registerEvents(new RegionDefineListener(this), this);
		pm.registerEvents(new PlayerMovement(this), this);
		pm.registerEvents(new PlayerJoinListener(this), this);
		pm.registerEvents(new PlayerLeaveListener(this), this);
		
		getCommand("pfield").setExecutor(new PField(this));
		
	}

	public void onDisable()
	{
		sql.closeConnections();
	}
}
