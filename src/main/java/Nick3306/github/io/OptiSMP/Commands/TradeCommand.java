package Nick3306.github.io.OptiSMP.Commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.Trade.Trade;

public class TradeCommand implements CommandExecutor
{

	private Main plugin;
	//private TradeUtilities util;
	//waiting response player get stored here
	public HashMap<String, Trade> waitingResponse = new HashMap<String, Trade>();	
	public TradeCommand(Main plugin)
	{
	   this.plugin = plugin;
	   //this.util = this.plugin.tradeUtil;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2lable, String[] args)
	{
		Player player = (Player) sender;
		if(args.length == 0)
		{
			player.sendMessage("/trade <playername>");
		}
		if(args[0].equalsIgnoreCase("trade"))
		{
			if(args.length == 2)
			{
				if(args[1].equalsIgnoreCase("accept"))
				{
					if(waitingResponse.get(player.getName()) != null)
					{
						//open trade window and add the trade to the trades list
						Trade tradeToAdd = waitingResponse.get(player.getName());
						tradeToAdd.setPlayer2(player);
						plugin.trades.add(tradeToAdd);
						
						
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You do not have any trade requests at this time");
						return false;
					}
				}
				if(args[1].equalsIgnoreCase("deny"))
				{
					if(waitingResponse.get(player.getName()) != null)
					{
						Trade tradeToDeny = waitingResponse.get(player.getName());
						tradeToDeny.getPlayer1().sendMessage(ChatColor.RED + player.getName() + " has declined your trade request");
						return false;
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You do not have any trade requests at this time");
						return false;
					}
				}
				else
				{
					
					if(plugin.getServer().getPlayer(args[1]) != null)
					{
						player.sendMessage(ChatColor.YELLOW +"Asking " +args[1] + " to trade");		
						Player playerToAsk = plugin.getServer().getPlayer(args[1]);
						Trade newTrade = new Trade(player);
						waitingResponse.put(args[1], newTrade);
						playerToAsk.sendMessage(ChatColor.YELLOW + player.getName() + " has asked you to trade. /trade accept or /trade deny");
						return true;
					}
					else
					{
						player.sendMessage(ChatColor.RED + "That player is not online!");
						return false;
					}
				}
			}
			
		}
		return false;
	}

	
}
