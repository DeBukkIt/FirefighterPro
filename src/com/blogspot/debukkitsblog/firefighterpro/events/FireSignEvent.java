package com.blogspot.debukkitsblog.firefighterpro.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class FireSignEvent implements Listener {
	
	@EventHandler
	public void onSignChanged(SignChangeEvent event) {
		
		if (event.getLine(0).equalsIgnoreCase("[Dispatch]")) {
			event.setLine(0, ChatColor.DARK_RED + "[Dispatch]");
		}
		
	}
	
	@EventHandler
	public void onSignClicked(PlayerInteractEvent event) {
		
		// is the block a sign?
		if (event.getClickedBlock() != null && event.getClickedBlock().getState() != null && event.getClickedBlock().getState() instanceof Sign) {
			Sign sign = (Sign) event.getClickedBlock().getState();

			// is the first line [DISPATCH]?
			if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_RED + "[Dispatch]")) {
				// collect args from other three lines
				String args = "";
				for(int i = 1; i < 4; i++) {
					if(!sign.getLine(i).isEmpty()) {
						args += sign.getLine(i) + " ";
					}
				}
				// dispatch the /ffdispatch command
				Bukkit.getServer().dispatchCommand(event.getPlayer(), "ffdispatch " + args.trim());
			}
		}
		
	}

}
