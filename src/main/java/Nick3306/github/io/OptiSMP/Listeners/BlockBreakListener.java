package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class BlockBreakListener implements Listener
{
	private Main plugin;
	private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public BlockBreakListener(Main plugin)
	{
		this.plugin = plugin;
		this. util = this.plugin.util;
		this.proUtil = this.plugin.protectUtil;
	}
	@EventHandler
	public void onBlockBroken(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Location blockBroken = event.getBlock().getLocation();
		
		if(this.plugin.protectUtil.inField(blockBroken))
		{			
			ProtectionField pField = proUtil.getPField(blockBroken);
			if(!pField.isMember(player) && !player.getUniqueId().toString().equals(pField.getOwner().toString()) && !player.hasPermission("optiSMP.protect.staff"))
			{
				// Player is not allowed to build in field		
				player.sendMessage("You are not allowed to build here!");
				event.setCancelled(true);
			}
			else
			{
				//They are a member, let them build
			}
		}
		
		//Decrement the blocks_broken stat
		SMPplayer smpPlayer = util.getSMPPlayer(player);
		smpPlayer.setBlocks_broken(smpPlayer.getBlocks_broken() + 1);
	}
}
