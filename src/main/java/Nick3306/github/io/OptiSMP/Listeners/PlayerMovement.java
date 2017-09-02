package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class PlayerMovement implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public PlayerMovement(Main plugin)
	{
	   this.plugin = plugin;
	   this.proUtil = this.plugin.protectUtil;
	   this.util = plugin.util;
	   
	}
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	  public void playerMove(PlayerMoveEvent event)
	  {
		 	org.bukkit.Location from = event.getFrom();
		    org.bukkit.Location to = event.getTo();
		    int x2;
		    if(Math.round(from.getX()) != (x2 = (int) Math.round(to.getX())))
		    {
		    	Bukkit.getLogger().info("Player moved a block");
		    	Player player = event.getPlayer();
		    	SMPplayer smpPlayer = util.getSMPPlayer(player);

		    	
		    	ProtectionField currentField = proUtil.getPField(player.getLocation());
		    	ProtectionField lastField = smpPlayer.getLastField();
		    	
		    	if(currentField != lastField)	
		    	{
		    		Bukkit.getLogger().info("Player changed fields");
		    		// They changed fields, check them
		    		//set the players last field to the current field
		    		smpPlayer.setLastField(currentField);
		    		util.updateLastField(smpPlayer);
		    		
		    		if(!currentField.getName().equalsIgnoreCase("none"))
		    		{
		    			player.sendMessage("You have entered field " + currentField.getName());
		    			// display field name here
		    		}		    				    	
		    	}
		    	else
		    	{
		    		return;
		    	}
		    }
	  }
}
