package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class EnchantItemListener implements Listener
{

	private Main plugin;
	//private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public EnchantItemListener(Main plugin)
	{
		this.plugin = plugin;
		this. util = this.plugin.util;
		//this.proUtil = this.plugin.protectUtil;
	}
	@EventHandler
	public void onPlayerEnchant(EnchantItemEvent event)
	{
		//player enchanted an item, increment items_enchanted
		Player player = event.getEnchanter();
		SMPplayer smpPlayer = util.getSMPPlayer(player);
		smpPlayer.setItems_enchanted(smpPlayer.getItems_enchanted() + 1);
	}
}
