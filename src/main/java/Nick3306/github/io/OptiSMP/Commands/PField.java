package Nick3306.github.io.OptiSMP.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;


public class PField implements CommandExecutor
{
	private Main plugin;
	private ProtectUtilities util;
	public HashMap<String, ProtectionField> waitingResponse = new HashMap<String, ProtectionField>();
	public PField(Main plugin)
	{
	   this.plugin = plugin;
	   this.util = this.plugin.protectUtil;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2lable, String[] args) 
	{
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("pfield"))
		{
			if(args.length == 0)
			{
				player.sendMessage(ChatColor.RED + "                      OptiProtect");
				player.sendMessage(ChatColor.RED + "_____________________________________________________");
				player.sendMessage(ChatColor.GREEN + "/pfield create: Start creation of a pfield");
				player.sendMessage(ChatColor.GREEN + "/pfield remove: Remove the pfield you are standing in");
				player.sendMessage(ChatColor.GREEN + "/pfield info: Get info about the pfield you are standing in");
				player.sendMessage(ChatColor.GREEN + "/pfield addmember <playername>: Add specified player to the pfield you are standing in");
				player.sendMessage(ChatColor.GREEN + "/pfield removemember <playername>: Remove specified player from the pfield you are standing in");
				return true;
			}
			if(args[0].equalsIgnoreCase("Create"))
			{
				if(args.length != 1)
				{
					player.sendMessage(ChatColor.RED + "Incorrect usage: /protect create");
					return false;
				}
				else
				{
					ProtectionField newField = new ProtectionField(player.getWorld(),null, null, player.getUniqueId(), util.getNextFieldId());
					plugin.newFields.add(newField);					
					player.sendMessage(ChatColor.GREEN + "Place the first block to define the field");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("info"))
			{
				Location loc = player.getLocation();			
				ProtectionField field = util.getPField(loc);
				if(field != null)
				{
					player.sendMessage(ChatColor.GREEN + "ID: " + ChatColor.YELLOW + field.getId());
					player.sendMessage(ChatColor.GREEN + "Owner: " + ChatColor.YELLOW + Bukkit.getOfflinePlayer(field.getOwner()).getName());
					player.sendMessage(ChatColor.GREEN + "Area: " + ChatColor.YELLOW + field.getArea() + " blocks");
					util.highlightField(field, player);
				}
				else
				{
					Bukkit.getLogger().info("returnfield is null");
					player.sendMessage(ChatColor.RED + "You are not in a field currently!");
					return false;
				}
				
			}
			if(args[0].equalsIgnoreCase("addmember"))
			{
				if(args.length <=1)
				{
					player.sendMessage(ChatColor.RED + "Incorrect usage: /protect addmember <membername>");
					return false;
				}
				if(args.length == 2)
				{
					ProtectionField field = util.getPField(player.getLocation());			
					if(field == null)
					{
						player.sendMessage(ChatColor.RED + "You are not in a protection field!");
						return false;
					}
					else
					{
						if(field.getOwner().toString().equals(player.getUniqueId().toString()))
						{
							if(plugin.getServer().getPlayer(args[1]) != null)
							{
								Player playerToAdd = plugin.getServer().getPlayer(args[1]);
								plugin.sql.addMember(field, playerToAdd);
								return true;
							}
							else
							{
								player.sendMessage(ChatColor.RED + "Player not found!");
								return false;
							}
						}
						else
						{
							player.sendMessage(ChatColor.RED + "You are not the owner of this field!");
						}
					
					}
				}
			}
			if(args[0].equalsIgnoreCase("removemember"))
			{
				if(args.length <=1)
				{
					player.sendMessage(ChatColor.RED + "Incorrect usage: /protect removemember <membername>");
					return false;
				}
				if(args.length == 2)
				{
					ProtectionField field = util.getPField(player.getLocation());
					if(field == null)
					{
						player.sendMessage(ChatColor.RED + "You are not in a protection field!");
						return false;
					}
					else
					{
						if(field.getOwner().toString().equals(player.getUniqueId().toString()))
						{
							if(plugin.getServer().getPlayer(args[1]) != null)
							{
								Player playerToAdd = plugin.getServer().getPlayer(args[1]);
								plugin.sql.removeMember(field, playerToAdd);
								return true;
							}
							else
							{
								player.sendMessage(ChatColor.RED + "Player not found!");
								return false;
							}
						}
						else
						{
							player.sendMessage(ChatColor.RED + "You are not the owner of this field!");
						}
					
					}
				}
			}
			if(args[0].equalsIgnoreCase("remove"))
			{
				Location loc = player.getLocation();			
				ProtectionField field = util.getPField(loc);
				if(field != null)
				{
					if(field.getOwner().toString().equals(player.getUniqueId().toString()) || player.hasPermission("optiSMP.protect.staff"))
					{
						waitingResponse.put(player.getName(), field);
						player.sendMessage(ChatColor.RED + "WARNING: You are about to delete this pfield. Type '/pfield yes' to confirm or '/pfield no' to cancel");
						return true;
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You are not the owner of this field!");
						return false;
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You are not in a field currently!");
					return false;
				}
				
			}
			if(args[0].equalsIgnoreCase("yes"))
			{
				if(waitingResponse.get(player.getName()) != null)
				{
					ProtectionField fieldToRemove = waitingResponse.get(player.getName());
					plugin.sql.removeField(fieldToRemove);
					util.removeField(fieldToRemove);
					waitingResponse.remove(player.getName());
					player.sendMessage(ChatColor.GREEN + "Field Removed");
					Bukkit.getLogger().info("Size after command returns is: " + util.sizeOfFields());
					return true;
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Pfield is not waiting for a response from you");
					return false;
				}
				
			}
			if(args[0].equalsIgnoreCase("no"))
			{
				if(waitingResponse.get(player.getName()) != null)
				{
					player.sendMessage("Remove canceled");
					waitingResponse.remove(player.getName());
					return true;
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Pfield is not waiting for a response from you");
					return false;
				}
			}
			if(args[0].equalsIgnoreCase("visualize"))
			{
				ProtectionField field = util.getPField(player.getLocation());
				if(field != null)
				{
					util.highlightField(field, player);
					player.sendMessage(ChatColor.GREEN + "Field is now visualized");
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You are not in a protection field!");
					return false;
				}
			}
		}
		return false;
	}
	
}
