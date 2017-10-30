package com.blogspot.debukkitsblog.firefighterpro.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;

public class CommandDispatch implements CommandExecutor {

	private final FirefighterPro plugin;
	
	public CommandDispatch(FirefighterPro plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// CASE 0: Currently no mission to dispatch units for
		if(plugin.getCurrentMission() == null || plugin.getCurrentMission().isOver()) {
			sender.sendMessage(Messages.format(Messages.ERROR_NO_MISSION_CURRENTLY));
			return true;
		}
		
		// CASE 1: No argument: Dispatch using autodispatch mechanic
		if(args.length == 0) {
			int resultAmount = plugin.getCurrentMission().dispatchAuto();
			sender.sendMessage(Messages.format(ChatColor.GREEN + String.valueOf(resultAmount) + ChatColor.WHITE + " " + Messages.DISPATCH_UNITS_DISPATCHED));
			return true;
		}
		
		// CASE 2: Multiple arguments
		// -end indicates the end of the whole mission, aborting everything else
		if(args[0].equalsIgnoreCase("-end")) {
			plugin.getCurrentMission().end();
			sender.sendMessage(Messages.format(Messages.MISSION_ENDED));
			return true;
		}
		
		List<String> units = new ArrayList<String>(3);
		String additionalMessage = "";
		for(int i = 0; i < args.length; i++) {			
			// every argument is another unit, which should receive the alarm
			if(!args[i].equalsIgnoreCase("-m")) {
				if(!plugin.getFFConfig().unitExist(args[i])) {
					sender.sendMessage(Messages.format(ChatColor.RED + args[i] + ChatColor.WHITE + " " + Messages.ERROR_DISPATCH_UNIT_NOT_EXIST));
					return false;
				}
				units.add(args[i]);
				
			// -m indicates additional information, which are passed as one message
			} else {
				for(int j = i+1; j < args.length; j++) {
					additionalMessage += args[j];
				}
				break;
			}
		}
		// actual dispatch
		for (String unitName : units) {
			int resultAmount = plugin.getCurrentMission().dispatch(unitName, additionalMessage);
			sender.sendMessage(Messages.format(ChatColor.RED + unitName + ": " + ChatColor.GREEN + String.valueOf(resultAmount) + ChatColor.WHITE + " " + Messages.DISPATCH_UNITS_DISPATCHED));
		}
		
		return true;
	}

}
