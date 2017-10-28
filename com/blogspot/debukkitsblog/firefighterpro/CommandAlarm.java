package com.debukkitsblog.blogspot.firefighterpro;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAlarm implements CommandExecutor {

	private final FirefighterPro plugin;
	
	public CommandAlarm(FirefighterPro plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 0) {
			// No alarm message provided
			List<Player> dispatchers = plugin.getFFConfig().getDispatchers();
			for(Player player : dispatchers) {
				//player.sendMessage(Messages.);
			}
			
		} else {
			// Alarm message provided in args
		}
		
		return true;
	}

}
