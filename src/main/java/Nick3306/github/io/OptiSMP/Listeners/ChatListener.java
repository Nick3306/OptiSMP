package Nick3306.github.io.OptiSMP.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;

public class ChatListener implements Listener 
{
	private Main plugin;
	//private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public ChatListener(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.util; 
	   //this.proUtil = this.plugin.protectUtil;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		SMPplayer smpPlayer = util.getSMPPlayer(player);
		smpPlayer.setLines_spoken(smpPlayer.getLines_spoken()+ 1);
	}
	
}
