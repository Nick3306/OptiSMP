package Nick3306.github.io.OptiSMP.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectUtilities;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;
import Nick3306.github.io.OptiSMP.Utilities.GeneralUtilities;
import Nick3306.github.io.OptiSMP.Utilities.SMPplayer;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;


public class PField implements CommandExecutor
{
	private Main plugin;
	private ProtectUtilities proUtil;
	private GeneralUtilities util;
	public HashMap<String, ProtectionField> waitingResponse = new HashMap<String, ProtectionField>();
	public PField(Main plugin)
	{
	   this.plugin = plugin;
	   this.util =  this.plugin.util;		   
	   this.proUtil = this.plugin.protectUtil;
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
				player.sendMessage(ChatColor.GREEN + "/pfield create <fieldname>: Start creation of a pfield");
				player.sendMessage(ChatColor.GREEN + "/pfield remove: Remove the pfield you are standing in");
				player.sendMessage(ChatColor.GREEN + "/pfield info: Get info about the pfield you are standing in");
				player.sendMessage(ChatColor.GREEN + "/pfield info <fieldname>: Get info about a specific pfield you own");
				player.sendMessage(ChatColor.GREEN + "/pfield addmember <playername> <fieldname>: Add specified player to the pfield you are standing in");
				player.sendMessage(ChatColor.GREEN + "/pfield removemember <playername> <fieldname>: Remove specified player from the pfield you are standing in");
				player.sendMessage(ChatColor.GREEN + "/pfield list: Get a list of your current protection fields");
				return true;
			}
			if(args[0].equalsIgnoreCase("Create"))
			{
				if(args.length != 2)
				{
					player.sendMessage(ChatColor.RED + "Incorrect usage: /protect create <fieldname>");
					return false;
				}
				else
				{
					
					ProtectionField newField = new ProtectionField(player.getWorld(),null, null, player.getUniqueId(), args[1]);
					if(!proUtil.duplicateName(newField))
					{
						plugin.newFields.add(newField);					
						player.sendMessage(ChatColor.GREEN + "Place the first block to define the field");
						return true;
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You already have a field with this name!");
						return false;
					}

				}
			}
			if(args[0].equalsIgnoreCase("info"))
			{
				if(args.length == 1)
				{
					Location loc = player.getLocation();			
					ProtectionField field = proUtil.getPField(loc);
					if(field != null)
					{
						player.sendMessage(ChatColor.GREEN + "Name: " + ChatColor.YELLOW + field.getName());
						player.sendMessage(ChatColor.GREEN + "Owner: " + ChatColor.YELLOW + Bukkit.getOfflinePlayer(field.getOwner()).getName());
						player.sendMessage(ChatColor.GREEN + "Area: " + ChatColor.YELLOW + field.getArea() + " blocks");
						String members = "";
						for(UUID member : field.members)
						{
							members = members + plugin.getServer().getOfflinePlayer(member).getName() + ", ";
						}
						player.sendMessage(ChatColor.GREEN + "Members: " + members);
						proUtil.highlightField(field, player);
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You are not in a field currently!");
						return false;
					}
				}
				if(args.length == 2)
				{
					ProtectionField field = proUtil.getPFieldByName(player.getUniqueId(), args[1]);
					if(field != null)
					{
						player.sendMessage(ChatColor.GREEN + "Name: " + ChatColor.YELLOW + field.getName());
						player.sendMessage(ChatColor.GREEN + "Owner: " + ChatColor.YELLOW + Bukkit.getOfflinePlayer(field.getOwner()).getName());
						player.sendMessage(ChatColor.GREEN + "Area: " + ChatColor.YELLOW + field.getArea() + " blocks");
						player.sendMessage(ChatColor.GREEN + "World: " + ChatColor.YELLOW + field.getWorld().getName());
						player.sendMessage(ChatColor.GREEN + "Location: " + ChatColor.YELLOW + field.getBlock1().toString());
						//proUtil.highlightField(field, player);
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You do not own a field by that name!");
						return false;
					}
				}
				
			}
			if(args[0].equalsIgnoreCase("addmember"))
			{
				if(args.length != 3)
				{
					player.sendMessage(ChatColor.RED + "Incorrect usage: /protect addmember <membername> <fieldname>");
					return false;
				}
				if(args.length == 3)
				{
					ProtectionField field = proUtil.getPFieldByName(player.getUniqueId(), args[2]);			
					if(field == null)
					{
						player.sendMessage(ChatColor.RED + "You do not have a field by that name!");
						return false;
					}
					else
					{
							if(plugin.getServer().getOfflinePlayer(args[1]) != null)
							{
								OfflinePlayer playerToAdd = plugin.getServer().getOfflinePlayer(args[1]);								
								
								if(!field.members.contains(playerToAdd.getUniqueId()))
								{
									plugin.sql.addMember(field, playerToAdd);
									field.members.add(playerToAdd.getUniqueId());
									player.sendMessage(ChatColor.GREEN + "Player added");
									return true;
								}
								else
								{
									player.sendMessage(ChatColor.RED + "This person is already added to your field");
									return false;
								}
							}
							else
							{
								player.sendMessage(ChatColor.RED + "Player not found!");
								return false;
							}
						}
					
					}				
			}
			if(args[0].equalsIgnoreCase("removemember"))
			{
				if(args.length !=3)
				{
					player.sendMessage(ChatColor.RED + "Incorrect usage: /protect removemember <membername> <fieldname>");
					return false;
				}
				if(args.length == 3)
				{
					ProtectionField field = proUtil.getPFieldByName(player.getUniqueId(), args[2]);
					if(field == null)
					{
						player.sendMessage(ChatColor.RED + "You do not have a field by this name!");
						return false;
					}
					else
					{						
							if(plugin.getServer().getOfflinePlayer(args[1]) != null)
							{
								OfflinePlayer playerToRemove = plugin.getServer().getOfflinePlayer(args[1]);
								if(field.members.contains(playerToRemove.getUniqueId()))
								{
									field.members.remove(playerToRemove.getUniqueId());
									plugin.sql.removeMember(field, playerToRemove); player.sendMessage(ChatColor.GREEN + "Player removed");
									return true;
								}
								else
								{
									player.sendMessage(ChatColor.RED + "This person is not a member of that field");
									return false;
								}								
							}
							else
							{
								player.sendMessage(ChatColor.RED + "Player not found!");
								return false;
							}																
					}
				}
			}
			if(args[0].equalsIgnoreCase("remove"))
			{
				if(args.length != 1)
				{
					player.sendMessage(ChatColor.RED + "Incorrect usage: /pfield remove");
					player.sendMessage(ChatColor.RED + "Remember, you must be standing in your field to remove it");
					return false;
				}
				Location loc = player.getLocation();			
				ProtectionField field = proUtil.getPField(loc);
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
					player.sendMessage(ChatColor.RED + "You must be standing in the field to remove it!");
					return false;
				}				
			}
			if(args[0].equalsIgnoreCase("yes"))
			{
				if(waitingResponse.get(player.getName()) != null)
				{
					ProtectionField fieldToRemove = waitingResponse.get(player.getName());
					//remove field from database
					plugin.sql.removeField(fieldToRemove);
					//remove field from plugins list of total fields
					proUtil.removeField(fieldToRemove);
					SMPplayer smpPlayer = util.getSMPPlayer(player);
					//give the player back the number of protected blocks for the field removed
					smpPlayer.setProtectionBlocksLeft(smpPlayer.getProtectionBlocksLeft() + fieldToRemove.getArea());
					waitingResponse.remove(player.getName());
					player.sendMessage(ChatColor.GREEN + "Field Removed");
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
				ProtectionField field = proUtil.getPField(player.getLocation());
				if(field != null)
				{
					proUtil.highlightField(field, player);
					player.sendMessage(ChatColor.GREEN + "Field is now visualized");
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You are not in a protection field!");
					return false;
				}
			}
			if(args[0].equalsIgnoreCase("list"))
			{	
				player.sendMessage(ChatColor.GREEN + "Fields:");
				SMPplayer smpPlayer = util.getSMPPlayer(player);
				ArrayList<ProtectionField> playerFields = smpPlayer.getPFields();
				int count = 1;
				for(ProtectionField field : plugin.fields)
				{
					if(field.getOwner().equals(player.getUniqueId()))
					{
						player.sendMessage(ChatColor.GREEN + Integer.toString(count) + ". " + field.getName());
						count++;
					}
				}	
				return true;
			}
			if(args[0].equalsIgnoreCase("members"))
			{
				if(args.length == 2)
				{
					ProtectionField field = proUtil.getPFieldByName(player.getUniqueId(), args[1]);					
					if(field != null)
					{
						if(field.members.size() != 0)
						{
							for(UUID uuid : field.members)
							{
								player.sendMessage(ChatColor.GREEN + Bukkit.getOfflinePlayer(uuid).getName());
							}
							return true;
						}						
						else
						{
							player.sendMessage(ChatColor.RED + "That field has no members!");
							return false;
						}
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You do not have a field by that name!");
						return false;
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Incorrect usage! /pfield members <fieldname>");
					return false;
				}
			}
		}
		if(args[0].equalsIgnoreCase("flag"))
		{
			
		}
		
		return false;
	}
	
}
