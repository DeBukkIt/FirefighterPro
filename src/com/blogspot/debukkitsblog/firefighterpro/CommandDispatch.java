package com.blogspot.debukkitsblog.firefighterpro;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandDispatch implements CommandExecutor {

	private final FirefighterPro plugin;
	
	public CommandDispatch(FirefighterPro plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length == 0) {
			plugin.getCurrentMission().dispatchAuto();
		}
		
		// TODO Implementieren für mehr als 0 Argumente
		
		return true;
	}

}
