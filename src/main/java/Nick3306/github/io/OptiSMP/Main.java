package Nick3306.github.io.OptiSMP;

import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.naming.NamingException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Protect.BlockEditListeners;
import Protect.CommandHandler;
import Protect.ProtectionField;
import Protect.RegionDefineListener;
import Protect.Utilities;



public class Main extends JavaPlugin
{
	public ArrayList<ProtectionField> fields = new ArrayList<ProtectionField>();
	public ArrayList<ProtectionField> newFields = new ArrayList<ProtectionField>();
	public Utilities util;
	public MySql sql;
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		util = new Utilities(this);
		sql = new MySql(this);		 
		sql.getFields();
		
		
		pm.registerEvents(new BlockEditListeners(this), this);
		pm.registerEvents(new RegionDefineListener(this), this);
		
		getCommand("Protect").setExecutor(new CommandHandler(this));
		
	}
	public void onDisable()
	{
		sql.closeConnections();
	}
}
