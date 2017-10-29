package com.blogspot.debukkitsblog.firefighterpro.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;

public class CommandFF implements CommandExecutor {

	private final FirefighterPro plugin;

	public CommandFF(FirefighterPro plugin) {
		this.plugin = plugin;
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
			return true;
		}
		
		// Sender must be a player in every other case (because the console cannot respond!)
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(args[0].equalsIgnoreCase("roger")) {
				
				
			} else if(args[0].equalsIgnoreCase("equip")) {
				
				
			} else if(args[0].equalsIgnoreCase("respond")) {
				
				 
			}
			
		}

		return false;
	}
	
	private void informDispatchers(String message) {
		List<Player> dispatchers = plugin.getFFConfig().getDispatchers();
		if (dispatchers != null) {
			for (Player dispatcher : dispatchers) {
				dispatcher.sendMessage(Messages.format(message));
			}
		}
	}

}