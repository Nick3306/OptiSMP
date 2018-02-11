package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
		    	Player player = event.getPlayer();
		    	SMPplayer smpPlayer = util.getSMPPlayer(player);
		    	String lastField = smpPlayer.getLastField();

		    	
		    	ProtectionField currentField = proUtil.getPField(player.getLocation());
		    	if(currentField != null)
		    	{
		    		if(!currentField.getName().equalsIgnoreCase(lastField))
			    	{
		    			if(smpPlayer.getRegionMessages() == true)
		    			{
		    				player.sendMessage(ChatColor.GREEN + "You have entered the field " + currentField.getName() + " owned by player " + plugin.getServer().getOfflinePlayer(currentField.getOwner()).getName());
		    				smpPlayer.setLastField(currentField.getName());	 
		    			}
		    			
		    			/*
			    		if(!currentField.getGreeting().equalsIgnoreCase("default"))
			    		{			    			
			    			player.sendMessage(ChatColor.GREEN + currentField.getGreeting());
			    			smpPlayer.setLastField(currentField.getName());	    		
			    		else
			    		{
			    			player.sendMessage(ChatColor.GREEN + "You have entered the field " + currentField.getName() + " owned by player " + plugin.getServer().getOfflinePlayer(currentField.getOwner()).getName());
			    			smpPlayer.setLastField(currentField.getName());	 
			    		}
			    		*/
			    	}		    
		    	
		    	}	
		    	else
		    	{
		    		if(!lastField.equalsIgnoreCase("none"))
		    		{
		    			smpPlayer.setLastField("none");	
		    		}
		    	}

		    }
		    else
		    {
		    	return;
		    }
	  }
	  
}
