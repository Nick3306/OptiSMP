package Nick3306.github.io.OptiSMP.Listeners;

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
	//private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public EntityBreedListener(Main plugin)
	{
		this.plugin = plugin;
		this. util = this.plugin.util;
		//this.proUtil = this.plugin.protectUtil;
	}
	@EventHandler
	public void onEntityBreed(EntityBreedEvent event)
	{
		//player bred an entity, increment animals_bred
		Player player = (Player) event.getBreeder();
		SMPplayer smpPlayer = util.getSMPPlayer(player);
		smpPlayer.setAnimals_bred(smpPlayer.getAnimals_bred() + 1);
	}
}
