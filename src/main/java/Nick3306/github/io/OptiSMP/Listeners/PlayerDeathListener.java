package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class PlayerDeathListener implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public PlayerDeathListener(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.util; 
	   this.proUtil = this.plugin.protectUtil;
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
    {
		//Player died, increment their total deaths
        Player player = event.getEntity();
        SMPplayer smpPlayer = util.getSMPPlayer(player);
        smpPlayer.setTotal_deaths(smpPlayer.getTotal_deaths() + 1);
        
        //if player killed by other player, increment the killers player kills stat
        if(player.getKiller() instanceof Player)
        {
        	SMPplayer killer = util.getSMPPlayer(player.getKiller());
        	killer.setPlayers_killed(killer.getPlayers_killed() + 1);
        }
   
    }
}
