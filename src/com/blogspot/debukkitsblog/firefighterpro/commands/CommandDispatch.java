package com.blogspot.debukkitsblog.firefighterpro.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;

public class CommandDispatch extends FFProCommand {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// CASE 0: Currently no mission to dispatch units for
		if(FirefighterPro.getInstance().getCurrentMission() == null || FirefighterPro.getInstance().getCurrentMission().isOver()) {
			sender.sendMessage(Messages.format(Messages.ERROR_NO_MISSION_CURRENTLY_DISPATCH));
			return true;
		}
		
		// CASE 1: No argument: Dispatch using autodispatch mechanic
		if(args.length == 0) {
			// dispatch all firefighters
			int resultAmount = FirefighterPro.getInstance().getCurrentMission().dispatchAuto();
			// inform all dispatchers (including command sender)
			FirefighterPro.getInstance().getBroadcaster().broadcastToDispatchers(Messages.format(
					"[" + sender.getName() + "] " + ChatColor.GREEN + String.valueOf(resultAmount)
					+ ChatColor.WHITE + " " + Messages.DISPATCH_UNITS_DISPATCHED
			));
			return true;
		}
		
		// CASE 2: Multiple arguments
		// -end indicates the end of the whole mission, aborting everything else
		if(args[0].equalsIgnoreCase("-end")) {
			FirefighterPro.getInstance().getCurrentMission().end();
			sender.sendMessage(Messages.format(Messages.MISSION_ENDED));
			return true;
		} else if(args[0].equalsIgnoreCase("-payoutInsurance")) {
			// number of arguments correct
			if(args.length == 2) {
				// economy supported (Vault installed)
				if(FirefighterPro.getInstance().isEconomySupported()) {
					// find target player
					Player target = getPlayer(args[1]);
					if(target != null) {
						// is target player insured at all?
						if(FirefighterPro.getInstance().getInsurance().isInsured(target)) {
							FirefighterPro.getInstance().getInsurance().toCustomer(target).payoffSumInsured();
							sender.sendMessage(Messages.format(Messages.INSURANCE_SUM_PAYED_OUT.getMessage()
									.replaceAll("%p", target.getDisplayName())
									.replaceAll("%a", String.valueOf(FirefighterPro.getInstance().getInsurance().toCustomer(target).getSumInsured()))
							));
							target.sendMessage(Messages.format(Messages.INSURANCE_SUM_RECEIVED.getMessage()
									.replaceAll("%a", String.valueOf(FirefighterPro.getInstance().getInsurance().toCustomer(target).getSumInsured()))
							));
						} else {
							sender.sendMessage(Messages.format(Messages.INSURANCE_TARGET_NOT_INSURED));
						}
					} else {
						sender.sendMessage(Messages.format(Messages.ERROR_PLAYER_NOT_FOUND));
					}
				} else {
					sender.sendMessage(Messages.format(Messages.ERROR_VAULT_NOT_INSTALLED));
				}
			} else {
				return false;
			}
		}
		
		List<String> units = new ArrayList<String>(3);
		String additionalMessage = "";
		for(int i = 0; i < args.length; i++) {			
			// every argument is another unit, which should receive the alarm
			if(!args[i].equalsIgnoreCase("-m")) {
				if(!FirefighterPro.getInstance().getFFConfig().unitExist(args[i])) {
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
			int resultAmount = FirefighterPro.getInstance().getCurrentMission().dispatch(unitName, additionalMessage);
			sender.sendMessage(Messages.format(ChatColor.RED + unitName + ": " + ChatColor.GREEN + String.valueOf(resultAmount) + ChatColor.WHITE + " " + Messages.DISPATCH_UNITS_DISPATCHED));
		}
		
		return true;
	}

}