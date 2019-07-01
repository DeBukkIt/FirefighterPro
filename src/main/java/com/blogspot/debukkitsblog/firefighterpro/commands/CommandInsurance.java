package com.blogspot.debukkitsblog.firefighterpro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;
import com.blogspot.debukkitsblog.firefighterpro.economy.InsuranceCustomer;

public class CommandInsurance extends FFProCommand {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!FirefighterPro.getInstance().getFFConfig().isInsuranceEnabled()) {
			sender.sendMessage(Messages.format(Messages.ERROR_INSURANCE_NOT_ENABLED));
			return true;
		}
		
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
			if(FirefighterPro.getInstance().isEconomySupported()) {
				if(FirefighterPro.getInstance().getInsurance().isInsured(p)) {
					FirefighterPro.getInstance().getInsurance().removeCustomer(p);
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
				
				if(FirefighterPro.getInstance().isEconomySupported()) {
					if(!FirefighterPro.getInstance().getInsurance().isInsured(p)) {
						FirefighterPro.getInstance().getInsurance().addCustomer(new InsuranceCustomer(p, installment, installment*(50/dayInterval), dayInterval));
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