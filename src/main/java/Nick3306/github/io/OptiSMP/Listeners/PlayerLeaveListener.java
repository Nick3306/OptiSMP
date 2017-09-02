package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class PlayerLeaveListener implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public PlayerLeaveListener(Main plugin)
	{
	   this.plugin = plugin;
	   this.proUtil = this.plugin.protectUtil;
	   this.util = plugin.util;
	   
	}
	@EventHandler
	public void onPlayerJoin(PlayerQuitEvent event) 
	{
		Player player = event.getPlayer();
		SMPplayer smpPlayer = util.getSMPPlayer(player);
		plugin.sql.savePlayer(smpPlayer);		
		plugin.players.remove(smpPlayer);
	}
}
