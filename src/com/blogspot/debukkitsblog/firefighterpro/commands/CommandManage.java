package com.blogspot.debukkitsblog.firefighterpro.commands;

import org.bukkit.Location;
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
		}
		
		return true;
	}

}
