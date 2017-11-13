package com.blogspot.debukkitsblog.firefighterpro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;
import com.blogspot.debukkitsblog.firefighterpro.insurance.InsuranceCustomer;

public class CommandInsurance extends FFProCommand {

	public CommandInsurance(FirefighterPro plugin) {
		super(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length == 0 || args.length == 2 || args.length >= 4) {
			return false;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Messages.format(Messages.ERROR_COMMAND_NO_CONSOLE));
			return true;
		}
		
		Player p = (Player) sender;
		
		// CASE 1: CANCEL
		if(args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
			if(plugin.isEconomySupported()) {
				if(plugin.getInsurance().isInsured(p)) {
					plugin.getInsurance().removeCustomer(p);
					p.sendMessage(Messages.format(Messages.INSURANCE_CANCELLED));
					return true;
				} else {
					p.sendMessage(Messages.format(Messages.INSURANCE_NOT_INSURED));
					return true;
				}
			} else {
				p.sendMessage(Messages.format(Messages.ERROR_VAULT_NOT_INSTALLED));
				return true;
			}
			
		// CASE 2: CONTRACT
		} else if(args.length == 3 && args[0].equalsIgnoreCase("contract")) {
			
			try {
				int installment = Integer.parseInt(args[1]);
				int dayInterval = Integer.parseInt(args[2]);
				
				if(plugin.isEconomySupported()) {
					if(!plugin.getInsurance().isInsured(p)) {
						plugin.getInsurance().addCustomer(new InsuranceCustomer(p, installment, installment*(50/dayInterval), dayInterval, plugin));
						p.sendMessage(Messages.format(Messages.INSURANCE_CONTRACTED) + " " + installment*(50/dayInterval));
						return true;					
					} else {
						p.sendMessage(Messages.format(Messages.INSURANCE_ALREADY_INSURED));
						return true;
					}
				} else {
					p.sendMessage(Messages.format(Messages.ERROR_VAULT_NOT_INSTALLED));
					return true;
				}
			} catch (Exception e) {
				// usually occurs if args[1 or 2] is not an integer
				// --> return false (below) should help the player
			}			
		}
		
		return false;
	}

}