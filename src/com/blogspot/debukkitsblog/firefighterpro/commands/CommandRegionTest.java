package com.blogspot.debukkitsblog.firefighterpro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.worldguard.WorldGuardHandler;

public class CommandRegionTest implements CommandExecutor {

	private final FirefighterPro plugin;
	
	public CommandRegionTest(FirefighterPro plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		WorldGuardHandler wgHandler = new WorldGuardHandler(plugin);
		if(!wgHandler.isAvailable()) {
			sender.sendMessage("WorldGuard not installed.");
			return true;
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
