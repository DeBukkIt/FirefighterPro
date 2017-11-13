package com.blogspot.debukkitsblog.firefighterpro.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;

public class CommandFF extends FFProCommand {

	public CommandFF(FirefighterPro plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				
		// No or too many arguments: Error message
		if(args.length != 1) {
			return false;
		}
		
		// Just the info command
		if(args[0].equalsIgnoreCase("info")) {
			sender.sendMessage(Messages.format(plugin.getDescription().getName() + " " + plugin.getDescription().getVersion()));
			sender.sendMessage(Messages.format("made by " + plugin.getDescription().getAuthors().get(0) + " since October 2017"));
			sender.sendMessage(Messages.format(ChatColor.GREEN + "Online: " + ChatColor.WHITE
					+ plugin.getFFConfig().getFirefighters().size() + " firefighters and "
					+ plugin.getFFConfig().getDispatchers().size() + " dispatchers"));
			return true;
		}
		
		// Sender must be a player in every other case (because the console cannot respond!)
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(plugin.getCurrentMission() == null || plugin.getCurrentMission().isOver()) {
				player.sendMessage(Messages.format(Messages.ERROR_NO_MISSION_CURRENTLY_RESPOND));
				return true;
			} else {

					// ROGER
				if (args[0].equalsIgnoreCase("roger")) {
					plugin.getCurrentMission().roger(player);

					// EQUIP
				} else if (args[0].equalsIgnoreCase("equip")) {
					plugin.getCurrentMission().equip(player);

					// RESPOND
				} else if (args[0].equalsIgnoreCase("respond")) {
					plugin.getCurrentMission().respond(player);

					// QUIT
				} else if (args[0].equalsIgnoreCase("quit")) {
					plugin.getCurrentMission().quit(player);

				} else {
					return false;
				}
				
				return true;
				
			}
			
		} else {
			sender.sendMessage(Messages.format(Messages.ERROR_COMMAND_NO_CONSOLE));
			return true;
		}
	}

}