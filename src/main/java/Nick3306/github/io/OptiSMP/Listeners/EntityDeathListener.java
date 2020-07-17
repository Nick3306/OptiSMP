package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class EntityDeathListener implements Listener
{
	private Main plugin;
	private GeneralUtilities util;
	public EntityDeathListener(Main plugin)
	{
		this.plugin = plugin;
		this.util = this.plugin.util; 
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		Entity e = event.getEntity();
		if(e.getLastDamageCause() instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e.getLastDamageCause();
			if(nEvent.getDamager() instanceof Player)
			{
				Player player = (Player)nEvent.getDamager();
				SMPplayer smpPlayer = util.getSMPPlayer(player);             
				if(e instanceof Monster)
				{		
					// player killed a monster increment monsters killed
					smpPlayer.setMonsters_killed(smpPlayer.getMonsters_killed() + 1);
					util.checkRankUp(smpPlayer);
				}
				if(e instanceof Animals)
				{
					//player killed an animal increment animals killed
					smpPlayer.setAnimlas_killed(smpPlayer.getAnimlas_killed() + 1);
				}
			}
			else if(nEvent.getDamager() instanceof Arrow)
			{
				Arrow arrow = (Arrow)nEvent.getDamager();
				if(arrow.getShooter() instanceof Player) 
				{
					Player player = (Player) arrow.getShooter();

					SMPplayer smpPlayer = util.getSMPPlayer(player);             
					if(e instanceof Monster)
					{		
						// player killed a monster increment monsters killed
						smpPlayer.setMonsters_killed(smpPlayer.getMonsters_killed() + 1);
						util.checkRankUp(smpPlayer);
					}
					if(e instanceof Animals)
					{
						//player killed an animal increment animals killed
						smpPlayer.setAnimlas_killed(smpPlayer.getAnimlas_killed() + 1);
					}
				}
			}
		}
	}
}
