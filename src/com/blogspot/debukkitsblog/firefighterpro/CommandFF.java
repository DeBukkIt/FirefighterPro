package com.blogspot.debukkitsblog.firefighterpro;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandFF implements CommandExecutor {

	private final FirefighterPro plugin;

	public CommandFF(FirefighterPro plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length == 0) {
			plugin.notify();
			return false;
		}
				
		return true;
	}

}