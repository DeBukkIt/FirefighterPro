package com.blogspot.debukkitsblog.firefighterpro.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public abstract class FFProCommand implements CommandExecutor {
	
	protected Player getPlayer(String playerName) {
		for(OfflinePlayer c : Bukkit.getServer().getOfflinePlayers()) {
			if(c.getName().equalsIgnoreCase(playerName)) {
				return (Player) c;
			}
		}
		return null;
	}
	
}