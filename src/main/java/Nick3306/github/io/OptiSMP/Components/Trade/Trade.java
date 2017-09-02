package Nick3306.github.io.OptiSMP.Components.Trade;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Trade
{
	public Player player1;
	public Player player2;
	
	public ArrayList<ItemStack> player1Items = new ArrayList<ItemStack>();
	public ArrayList<ItemStack> player2Items = new ArrayList<ItemStack>();
	
	boolean player1Accept = false;
	boolean player2Accept = false;
	
	public Trade(Player player1)
	{
		this.player1 = player1;
	}
	public Player getPlayer1()
	{
		return player1;
	}
	public Player getPlayer2()
	{
		return player2;
	}
	public void setPlayer2(Player player2)
	{
		this.player2 = player2;
	}
}
