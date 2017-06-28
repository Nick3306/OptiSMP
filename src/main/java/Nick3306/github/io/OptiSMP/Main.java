package Nick3306.github.io.OptiSMP;

import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.naming.NamingException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Nick3306.github.io.OptiSMP.Commands.PField;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Listeners.BlockEditListeners;
import Nick3306.github.io.OptiSMP.Listeners.RegionDefineListener;
import Nick3306.github.io.OptiSMP.Utilities.MySql;



public class Main extends JavaPlugin
{
	public ArrayList<ProtectionField> fields = new ArrayList<ProtectionField>();
	public ArrayList<ProtectionField> newFields = new ArrayList<ProtectionField>();
	public ProtectUtilities protectUtil;
	public MySql sql;
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		protectUtil = new ProtectUtilities(this);
		sql = new MySql(this);		 
		sql.getFields();
		
		
		pm.registerEvents(new BlockEditListeners(this), this);
		pm.registerEvents(new RegionDefineListener(this), this);
		
		getCommand("pfield").setExecutor(new PField(this));
		
	}

	public void onDisable()
	{
		sql.closeConnections();
	}
}
