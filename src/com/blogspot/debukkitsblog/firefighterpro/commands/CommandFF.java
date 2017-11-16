package com.blogspot.debukkitsblog.firefighterpro.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;
import com.blogspot.debukkitsblog.firefighterpro.economy.InsuranceCustomer;

public class CommandFF extends FFProCommand {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// no arguments: Error message
		if(args.length < 1) {
			return false;
		}
		
		// Just the info command
		if(args[0].equalsIgnoreCase("info")) {
			sender.sendMessage(Messages.format(FirefighterPro.getInstance().getDescription().getName() + " " + FirefighterPro.getInstance().getDescription().getVersion()));
			sender.sendMessage(Messages.format("made by " + FirefighterPro.getInstance().getDescription().getAuthors().get(0) + " since October 2017"));
			sender.sendMessage(Messages.format(ChatColor.GREEN + "Online: " + ChatColor.WHITE
					+ FirefighterPro.getInstance().getFFConfig().getFirefighters().size() + " firefighters and "
					+ FirefighterPro.getInstance().getFFConfig().getDispatchers().size() + " dispatchers"));
			return true;
		}
		
		// Just the list command
		if(args[0].equalsIgnoreCase("list")) {
			if(args.length != 2) {
				return false;
			}
			if(args[1].equalsIgnoreCase("firefighters")) {
				sender.sendMessage("-- " + ChatColor.GREEN + "List of Firefighters" + ChatColor.WHITE + " --");
				List<Player> firefighters = FirefighterPro.getInstance().getFFConfig().getFirefighters();
				if (firefighters != null) {
					for (int i = 0; i < firefighters.size(); i++) {
						ChatColor color = firefighters.get(i).isOnline() ? ChatColor.DARK_GREEN : ChatColor.RED;
						sender.sendMessage(Messages.format((i+1) + ". " + color + firefighters.get(i).getDisplayName()));
					}
				}
				return true;
			} else if(args[1].equalsIgnoreCase("dispatchers")) {
				sender.sendMessage("-- " + ChatColor.GREEN + "List of Dispatchers" + ChatColor.WHITE + " --");
				List<Player> dispatchers = FirefighterPro.getInstance().getFFConfig().getDispatchers();
				if (dispatchers != null) {
					for (int i = 0; i < dispatchers.size(); i++) {
						ChatColor color = dispatchers.get(i).isOnline() ? ChatColor.DARK_GREEN : ChatColor.RED;
						sender.sendMessage(Messages.format((i+1) + ". " + color + dispatchers.get(i).getDisplayName()));
					}
				}
				return true;
			} else if(args[1].equalsIgnoreCase("insurancecustomers")) {
				if (FirefighterPro.getInstance().isEconomySupported() && FirefighterPro.getInstance().getFFConfig().isInsuranceEnabled()) {
					sender.sendMessage("-- " + ChatColor.GREEN + "List of Insurance Customers" + ChatColor.WHITE + " --");
					List<InsuranceCustomer> customers = FirefighterPro.getInstance().getInsurance().getCustomers();
					if (customers != null) {
						for (int i = 0; i < customers.size(); i++) {
							sender.sendMessage(
									Messages.format((i + 1) + ". " + customers.get(i).getPlayer().getDisplayName()));
						}
					}
				} else {
					sender.sendMessage(Messages.format(Messages.ERROR_INSURANCE_NOT_ENABLED));
				}
				return true;
			}
		}
		
		// too many arguments: Error message
		if(args.length > 1) {
			return false;
		}
		
		// Sender must be a player in every other case (because the console cannot respond!)
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(FirefighterPro.getInstance().getCurrentMission() == null || FirefighterPro.getInstance().getCurrentMission().isOver()) {
				player.sendMessage(Messages.format(Messages.ERROR_NO_MISSION_CURRENTLY_RESPOND));
				return true;
			} else {

					// ROGER
				if (args[0].equalsIgnoreCase("roger")) {
					FirefighterPro.getInstance().getCurrentMission().roger(player);

					// EQUIP
				} else if (args[0].equalsIgnoreCase("equip")) {
					FirefighterPro.getInstance().getCurrentMission().equip(player);

					// RESPOND
				} else if (args[0].equalsIgnoreCase("respond")) {
					FirefighterPro.getInstance().getCurrentMission().respond(player);

					// QUIT
				} else if (args[0].equalsIgnoreCase("quit")) {
					FirefighterPro.getInstance().getCurrentMission().quit(player);

				} else {
					return false;
				}
				
				return true;
				
			}
			
		} else {
			sender.sendMessage(Messages.format(Messages.ERROR_COMMAND_NO_CONSOLE));
			return true;
		}
	}

}