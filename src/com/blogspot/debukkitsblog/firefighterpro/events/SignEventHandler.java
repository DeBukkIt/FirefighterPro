package com.blogspot.debukkitsblog.firefighterpro.events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;

public class SignEventHandler implements Listener {
	
	private final FirefighterPro plugin;
	private HashMap<String, String> signCommands;
	
	public SignEventHandler(FirefighterPro plugin) {
		this.plugin = plugin;
		
		signCommands = new HashMap<String, String>();
		signCommands.put("[Alarm]", "/alarm");
		signCommands.put("[FF Equip]", "/ff equip");
		signCommands.put("[FF Respond]", "/ff respond");
		signCommands.put("[FF Dispatch]", "/ffdispatch");
		signCommands.put("[FF Autodispatch]", "/ffmanage setAutodispatch");
	}
	
	@EventHandler
	public void onSignChanged(SignChangeEvent event) {
		
		// check first line for all possible commands, replace with color if matches
		for (String commandName : signCommands.keySet()) {
			if (event.getLine(0).equalsIgnoreCase(commandName)) {
				event.setLine(0, (plugin.getFFConfig().getSignFontColor() != null ? plugin.getFFConfig().getSignFontColor() : ChatColor.DARK_RED) + commandName);
				break;
			}
		}
		
	}
	
	@EventHandler
	public void onSignClicked(PlayerInteractEvent event) {
		
		// is the block a sign?
		if (event.getClickedBlock() != null && event.getClickedBlock().getState() != null && event.getClickedBlock().getState() instanceof Sign) {
			Sign sign = (Sign) event.getClickedBlock().getState();
			ChatColor signFontColor = (plugin.getFFConfig().getSignFontColor() != null ? plugin.getFFConfig().getSignFontColor() : ChatColor.DARK_RED);
			
			// is the first line a FirefighterPro command?
			for (String commandName : signCommands.keySet()) {
				// if a command matches
				if (sign.getLine(0).equalsIgnoreCase(signFontColor + commandName)) {
					// collect args from other three lines
					String args = "";
					for (int i = 1; i < 4; i++) {
						if (!sign.getLine(i).isEmpty()) {
							args += sign.getLine(i) + " ";
						}
					}
					// dispatch the command with args
					Bukkit.getServer().dispatchCommand(event.getPlayer(), (signCommands.get(commandName) + args).trim());
					break;
				}
			}
		}
		
	}

}
