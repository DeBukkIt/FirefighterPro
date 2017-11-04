package com.blogspot.debukkitsblog.firefighterpro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.worldguard.WorldGuardHandler;

public class CommandDebug implements CommandExecutor {

	private final FirefighterPro plugin;
	
	public CommandDebug(FirefighterPro plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		WorldGuardHandler wgHandler = null;
		
		if(!plugin.isWorldGuardSupported()) {
			sender.sendMessage("No WorldGuard supported.");
			return true;
		} else {
			wgHandler = plugin.getWorldGuardHandler();
		}
		
		if(args.length == 0) {
			return false;
		}
		
		if(args[0].equalsIgnoreCase("allow")) {
			wgHandler.setAllowBuild(plugin.getCurrentMission().getLocation(), plugin.getCurrentMission());
			sender.sendMessage("Allowed.");
		} else if(args[0].equalsIgnoreCase("deny")) {
			wgHandler.setOldBuildPermissions(plugin.getCurrentMission().getLocation(), plugin.getCurrentMission());
			sender.sendMessage("Denied.");
		}
		
		return true;
	}

}
