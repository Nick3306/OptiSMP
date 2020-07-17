package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class EntityBreedListener implements Listener
{
	private Main plugin;
	private GeneralUtilities util;
	public EntityBreedListener(Main plugin)
	{
		this.plugin = plugin;
		this. util = this.plugin.util;
	}

	@EventHandler
	public void onEntityBreed(EntityBreedEvent event)
	{
		//player bred an entity, increment animals_bred
		LivingEntity breeder = event.getBreeder();
		if (breeder != null && breeder.getType().equals(EntityType.PLAYER))
		{
			Player player = (Player) breeder;
			SMPplayer smpPlayer = util.getSMPPlayer(player);
			smpPlayer.setAnimals_bred(smpPlayer.getAnimals_bred() + 1);
		}
	}
}
