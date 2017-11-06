package com.blogspot.debukkitsblog.firefighterpro.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;

public class CommandManage implements CommandExecutor {

	private final FirefighterPro plugin;
	
	public CommandManage(FirefighterPro plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// filter too few or too many arguments
		if(args.length == 0 || args.length > 3) {
			return false;
		}
		
		// sender must be player for some commands
		Player player = null;
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		
		// correct number of arguments:
		// 1. setStation
		if(args[0].equalsIgnoreCase("setStation")) {
			if(player != null) {
				// apply changes to config, confirm to command sender
				Location location = player.getLocation();
				plugin.getFFConfig().setStationLocation(location);
				player.sendMessage(Messages.format(Messages.MANAGER_FIRESTATION_LOCATION_SET));
				// inform all firefighters
				plugin.getBroadcaster().broadcastToFirefighters(Messages.format(Messages.INFO_HEADLINE_FIREFIGHTERS_ALL));
				plugin.getBroadcaster().broadcastToFirefighters(Messages.format(Messages.MANAGER_FIRESTATION_LOCATION_SET_FOR_FIREFIGHTERS + " " + location.getWorld().getName() + ", (" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ() + ")"));
			} else {
				sender.sendMessage(Messages.format(Messages.ERROR_COMMAND_NO_CONSOLE));
			}
			
		// 2. setAutodispatch
		} else if(args[0].equalsIgnoreCase("setAutodispatch")) {
			if(args.length >= 2 && (args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("true"))) {
				// apply changes to config, confirm to command sender
				plugin.getFFConfig().setAutodispatch(Boolean.valueOf(args[1]));
				sender.sendMessage(Messages.format(Boolean.valueOf(args[1]) ? Messages.MANAGER_AUTODISPATCH_ON : Messages.MANAGER_AUTODISPATCH_OFF));
				// inform all dispatchers
				plugin.getBroadcaster().broadcastToDispatchers(Messages.format(Messages.INFO_HEADLINE_DISPATCHERS));
				plugin.getBroadcaster().broadcastToDispatchers(Messages.format(Boolean.valueOf(args[1]) ? Messages.MANAGER_AUTODISPATCH_ON : Messages.MANAGER_AUTODISPATCH_OFF));
			} else {
				return false;
			}			
		} else if (args[0].equalsIgnoreCase("assignToUnit")) {
			// add firefighter to a unit
			if (args.length == 3) {				
				Player p = getPlayer(args[1]);
				if (p != null) {
					if (plugin.getFFConfig().isFirefighter(p)) {
						if (plugin.getFFConfig().unitExist(args[2])) {
							plugin.getFFConfig().addToUnit(p, args[2]);
							sender.sendMessage(Messages.format(args[1] + " " + Messages.MANAGER_PLAYER_ASSIGNED_TO_UNIT + " " + args[2]));
							p.sendMessage(Messages.format(Messages.YOU_HAVE_BEEN_ASSIGNED_TO_UNIT + " " + args[2]));
						} else {
							sender.sendMessage(Messages.format(Messages.ERROR_UNIT_NOT_EXIST));
						}
					} else {
						sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_FIREFIGHTER));
					}
				} else {
					sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_FOUND));
				}
			} else {
				return false;
			}
		} else if (args[0].equalsIgnoreCase("removeFromUnit")) {
			// remove firefighter from a unit
			if (args.length == 3) {
				Player p = getPlayer(args[1]);
				if (p != null) {
					if (plugin.getFFConfig().unitExist(args[2])) {
						if (plugin.getFFConfig().isMemberOfUnit(p, args[2])) {
							plugin.getFFConfig().removeFromUnit(p, args[2]);
							sender.sendMessage(Messages.format(args[1] + " " + Messages.MANAGER_PLAYER_REMOVED_FROM_UNIT + " " + args[2]));
							p.sendMessage(Messages.format(Messages.YOU_HAVE_BEEN_REMOVED_FROM_UNIT + " " + args[2]));
						} else {
							sender.sendMessage(Messages.format(Messages.ERROR_NOT_MEMBER_OF_UNIT));
						}
					} else {
						sender.sendMessage(Messages.format(Messages.ERROR_UNIT_NOT_EXIST));
					}
				} else {
					sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_FOUND));
				}
			} else {
				return false;
			}
		} else if(args[0].equalsIgnoreCase("firefighterAdd")) {
			// add a new firefighter
			if(args.length == 2) {
				Player p = getPlayer(args[1]);
				if(p != null) {
					if(!plugin.getFFConfig().isFirefighter(p)) {
						plugin.getFFConfig().addFirefighter(p);
						sender.sendMessage(Messages.format(args[1] + " " + Messages.MANAGER_ADDED_FIREFIGHTER));
						p.sendMessage(Messages.format(Messages.YOU_ARE_A_FIREFIGHTER));
					} else {
						sender.sendMessage(Messages.format(Messages.ERROR_IS_ALREADY_FIREFIGHTER));
					}
				} else {
					sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_FOUND));
				}
			} else {
				return false;
			}
		} else if(args[0].equalsIgnoreCase("firefighterRemove")) {
			// remove a firefighter
			if(args.length == 2) {
				Player p = getPlayer(args[1]);
				if(p != null) {
					if(plugin.getFFConfig().isFirefighter(p)) {
						plugin.getFFConfig().removeFirefighter(p);
						sender.sendMessage(Messages.format(args[1] + " " + Messages.MANAGER_REMOVED_FIREFIGHTER));
						p.sendMessage(Messages.format(Messages.YOU_ARE_NO_LONGER_A_FIREFIGHTER));
					} else {
						sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_FIREFIGHTER));
					}
				} else {
					sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_FOUND));
				}
			} else {
				return false;
			}			
		} else if(args[0].equalsIgnoreCase("dispatcherAdd")) {
			// add a new dispatcher
			if(args.length == 2) {
				Player p = getPlayer(args[1]);
				if(p != null) {
					if(!plugin.getFFConfig().isDispatcher(p)) {
						plugin.getFFConfig().addDispatcher(p);
						sender.sendMessage(Messages.format(args[1] + " " + Messages.MANAGER_ADDED_DISPATCHER));
						p.sendMessage(Messages.format(Messages.YOU_ARE_A_DISAPTCHER));
					} else {
						sender.sendMessage(Messages.format(Messages.ERROR_IS_ALREADY_DISPATCHER));
					}
				} else {
					sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_FOUND));
				}
			} else {
				return false;
			}
		} else if(args[0].equalsIgnoreCase("dispatcherRemove")) {
			// remove a dispatcher
			if(args.length == 2) {
				Player p = getPlayer(args[1]);
				if(p != null) {
					if(plugin.getFFConfig().isDispatcher(p)) {
						plugin.getFFConfig().removeDispatcher(p);
						sender.sendMessage(Messages.format(args[1] + " " + Messages.MANAGER_REMOVED_DISPATCHER));
						p.sendMessage(Messages.format(Messages.YOU_ARE_NO_LONGER_A_DISPATCHER));
					} else {
						sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_DISPATCHER));
					}
				} else {
					sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_FOUND));
				}
			} else {
				return false;
			}			
		} else if(args[0].equalsIgnoreCase("unitAdd")) {
			// add a new unit
			if(args.length > 2) {
				if(!plugin.getFFConfig().unitExist(args[1])) {
					// collect unit name from further arguments
					String unitDisplayName = "";
					for(int i = 2; i < args.length; i++) {
						unitDisplayName += args[i] + " ";
					}					
					plugin.getFFConfig().addUnit(args[1], unitDisplayName.trim());
					sender.sendMessage(Messages.format(Messages.MANAGER_UNIT_ADDED + " " + args[1] + " ('" + unitDisplayName + "')"));
				} else  {
					sender.sendMessage(Messages.format(Messages.ERROR_UNIT_ALREADY_EXISTS));
				}
			} else {
				return false;
			}
		} else if(args[0].equalsIgnoreCase("unitRemove")) {
			// remove a unit
			if(args.length == 2) {
				if(plugin.getFFConfig().unitExist(args[1])) {
					plugin.getFFConfig().removeUnit(args[1]);
					sender.sendMessage(Messages.format(Messages.MANAGER_UNIT_REMOVED + " " + args[1]));
				} else {
					sender.sendMessage(Messages.format(Messages.ERROR_UNIT_NOT_EXIST));
				}
			} else {
				return false;
			}
		} else if(args[0].equalsIgnoreCase("paySalaries")) {
			if(plugin.isEconomySupported()) {
				double firefightersSalary = plugin.getFFConfig().getSalaryFirefighters();
				double dispatchersSalary = plugin.getFFConfig().getSalaryDispatchers();
				for(Player firefighter : plugin.getFFConfig().getFirefighters()) {
					plugin.getEconomy().depositPlayer(firefighter, firefightersSalary);
				}
				for(Player dispatcher : plugin.getFFConfig().getDispatchers()) {
					plugin.getEconomy().depositPlayer(dispatcher, dispatchersSalary);
				}
				double totalSalary = 	plugin.getFFConfig().getFirefighters().size() * firefightersSalary
										+ plugin.getFFConfig().getDispatchers().size() * dispatchersSalary;
				sender.sendMessage(Messages.format(Messages.MANAGER_SALARIES_PAYED + " " + String.valueOf(Math.round(totalSalary))));
			} else {
				sender.sendMessage(Messages.format(Messages.ERROR_VAULT_NOT_INSTALLED));
			}
		}
		
		return true;
	}
	
	private Player getPlayer(String playerName) {
		for(OfflinePlayer c : Bukkit.getServer().getOfflinePlayers()) {
			if(c.getName().equalsIgnoreCase(playerName)) {
				return (Player) c;
			}
		}
		return null;
	}

}
