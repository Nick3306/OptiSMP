package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class PlayerLeaveListener implements Listener
{
	private Main plugin;
	//private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public PlayerLeaveListener(Main plugin)
	{
	   this.plugin = plugin;
	   //this.proUtil = this.plugin.protectUtil;
	   this.util = plugin.util;
	   
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) 
	{
		Player player = event.getPlayer();
		SMPplayer smpPlayer = util.getSMPPlayer(player);
		//before saving the player to the databse, update the time online
		long logoutTime = (int) (System.currentTimeMillis());
		//get players login time and subtract it from their logout time to get time played
		long sessionTime = logoutTime - smpPlayer.loginTime;
		long currentOnlineTime = smpPlayer.getTime_online();
		long timeOnline = currentOnlineTime + sessionTime;
		smpPlayer.setTime_online(timeOnline);
		plugin.sql.savePlayer(smpPlayer);		
		plugin.players.remove(smpPlayer);
	}
}
