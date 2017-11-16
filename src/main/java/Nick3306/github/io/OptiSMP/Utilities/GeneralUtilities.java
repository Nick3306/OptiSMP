package Nick3306.github.io.OptiSMP.Utilities;

import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Main;

public class GeneralUtilities
{
	private Main plugin;

	public GeneralUtilities(Main plugin)
	{
	   this.plugin = plugin;
	}
	public SMPplayer getSMPPlayer(Player player)
	{
		return plugin.players.get(player.getUniqueId());
	}
	/*
	public void updateLastField(SMPplayer player)
	{
		for(SMPplayer playerInList : plugin.players)
		{
			if(playerInList == player)
			{
				playerInList.setLastField(player.getLastField());
			}
		}
	}
	*/
}
