package com.blogspot.debukkitsblog.firefighterpro.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;
import com.blogspot.debukkitsblog.firefighterpro.Mission;

public class CommandAlarm implements CommandExecutor {

	private final FirefighterPro plugin;
	
	public CommandAlarm(FirefighterPro plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player playerSender = (Player) sender;
			
			// Only one mission at a time is permitted
			if(plugin.getCurrentMission() != null && !plugin.getCurrentMission().isOver()) {
				sender.sendMessage(Messages.format(Messages.ERROR_FIRE_DEPT_NOT_AVAILABLE));
				return true;
			}
			
			if(args.length == 0) {
				// No alarm message provided
				Mission mission = new Mission(plugin, Messages.ALARM_MESSAGE_CONTENT_DEFAULT.getMessage(), playerSender.getLocation(), playerSender);
				plugin.setCurrentMission(mission);
				broadcastAlarm(mission);
			} else {
				// Alarm message provided in args
				String message = "";
				for(String word : args) {
					message += word + " ";
				}
				Mission mission = new Mission(plugin, message, playerSender.getLocation(), playerSender);
				plugin.setCurrentMission(mission);
				broadcastAlarm(mission);				
			}
			
			playerSender.sendMessage(Messages.format(Messages.ALARM_MESSAGE_FD_INFORMED));
			
		} else {
			sender.sendMessage(Messages.format(Messages.ERROR_COMMAND_NO_CONSOLE));
		}
		
		return true;
	}
	
	private void broadcastAlarm(Mission mission) {
		
		if(plugin.getFFConfig().getAutodispatch()) {
			// Dispatch without waiting for a dispatcher to dispatch
			mission.dispatchAuto();
		} else {
			// Inform only the dispatchers
			List<Player> dispatchers = plugin.getFFConfig().getDispatchers();
			for(Player player : dispatchers) {
				if(player.isOnline()) {
					Location location = mission.getLocation();
					player.sendMessage(Messages.format(Messages.ALARM_MESSAGE_INTRO));
					player.sendMessage(Messages.format(mission.getCallingCivilian().getDisplayName() + ": " + mission.getEmergencyMessage()));
					player.sendMessage(Messages.format("@ " + location.getWorld() + ", (" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ() + ")"));
					player.sendMessage(Messages.format(Messages.ALARM_MESSAGE_DISPATCHER_HELP));
				}
			}
		}	
		
	}

}
