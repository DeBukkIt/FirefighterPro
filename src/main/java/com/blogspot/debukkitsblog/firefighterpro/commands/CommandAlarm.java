package com.blogspot.debukkitsblog.firefighterpro.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;
import com.blogspot.debukkitsblog.firefighterpro.Mission;

public class CommandAlarm extends FFProCommand {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player playerSender = (Player) sender;
			
			// Only one mission at a time is permitted
			if(FirefighterPro.getInstance().getCurrentMission() != null && !FirefighterPro.getInstance().getCurrentMission().isOver()) {
				sender.sendMessage(Messages.format(Messages.ERROR_FIRE_DEPT_NOT_AVAILABLE));
				return true;
			}
			
			if(args.length == 0) {
				// No alarm message provided
				Mission mission = new Mission(Messages.ALARM_MESSAGE_CONTENT_DEFAULT.getMessage(), playerSender.getLocation(), playerSender);
				FirefighterPro.getInstance().setCurrentMission(mission);
				broadcastAlarm(mission);
			} else {
				// Alarm message provided in args
				String message = "";
				for(String word : args) {
					message += word + " ";
				}
				Mission mission = new Mission(message, playerSender.getLocation(), playerSender);
				FirefighterPro.getInstance().setCurrentMission(mission);
				broadcastAlarm(mission);				
			}
			
			playerSender.sendMessage(Messages.format(Messages.ALARM_MESSAGE_FD_INFORMED));
			
			if(FirefighterPro.getInstance().isEconomySupported()) {
				FirefighterPro.getInstance().getEconomy().withdraw(playerSender, FirefighterPro.getInstance().getFFConfig().getEmergencyCallFee());
			}
			
		} else {
			sender.sendMessage(Messages.format(Messages.ERROR_COMMAND_NO_CONSOLE));
		}
		
		return true;
	}
	
	private void broadcastAlarm(Mission mission) {
		
		if(FirefighterPro.getInstance().getFFConfig().getAutodispatch()) {
			// Dispatch without waiting for a dispatcher to dispatch
			mission.dispatchAuto();
		} else {
			// Inform only the dispatchers
			Location location = mission.getLocation();
			FirefighterPro.getInstance().getBroadcaster().broadcastToDispatchers(Messages.format(Messages.ALARM_MESSAGE_INTRO));
			FirefighterPro.getInstance().getBroadcaster().broadcastToDispatchers(Messages.format(mission.getCallingCivilian().getDisplayName() + ": " + mission.getEmergencyMessage()));
			FirefighterPro.getInstance().getBroadcaster().broadcastToDispatchers(Messages.format("@ " + location.getWorld() + ", (" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ() + ")"));
			FirefighterPro.getInstance().getBroadcaster().broadcastToDispatchers(Messages.format(Messages.ALARM_MESSAGE_DISPATCHER_HELP));			
		}	
		
	}

}