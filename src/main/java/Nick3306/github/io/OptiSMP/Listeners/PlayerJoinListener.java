package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Nick3306.github.io.OptiSMP.Main;

public class PlayerJoinListener implements Listener
{
	private Main plugin;
	public PlayerJoinListener(Main plugin)
	{
	   this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
		Player player = event.getPlayer();
		plugin.sql.getPlayer(player);					
	}
}
