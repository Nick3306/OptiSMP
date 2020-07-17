package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class PlayerFishListener implements Listener
{
	private Main plugin;
	private GeneralUtilities util;
	public PlayerFishListener(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.util; 
	}
	
	@EventHandler
	public void onPlayercatchFish(PlayerFishEvent event)
    {
		if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH)
		{
            // player caught fish, increment fish_caught
			Player player = event.getPlayer();
			SMPplayer smpPlayer = util.getSMPPlayer(player);
			smpPlayer.setFish_caught(smpPlayer.getFish_caught() + 1);
        } 
    }
}
