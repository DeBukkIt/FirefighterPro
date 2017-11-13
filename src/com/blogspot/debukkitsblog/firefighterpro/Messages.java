package com.blogspot.debukkitsblog.firefighterpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public enum Messages {
	
	ALARM_MESSAGE_INTRO,
	ALARM_MESSAGE_CONTENT_DEFAULT,
	ALARM_MESSAGE_FD_INFORMED,
	ALARM_MESSAGE_DISPATCHER_HELP,
	ALARM_MESSAGE_FIREFIGHTER_HELP_ROGER,
	ALARM_INFO_FIREFIGHTER_ROGERED,
	ALARM_INFO_FIREFIGHTER_RESPONDED,
	DISPATCH_UNITS_DISPATCHED,
	DISPATCH_FIREFIGHTER_ROGERED,
	ERROR_FIRE_DEPT_NOT_AVAILABLE,
	ERROR_COMMAND_NO_CONSOLE,
	ERROR_DISPATCH_UNIT_NOT_EXIST,
	ERROR_NO_EQUIPMENT_SET,
	ERROR_NOT_PART_OF_MISSION,
	ERROR_NO_MISSION_CURRENTLY_DISPATCH,
	ERROR_NO_MISSION_CURRENTLY_RESPOND,
	ERROR_UNIT_NOT_EXIST,
	ERROR_PLAYER_NOT_FIREFIGHTER,
	ERROR_PLAYER_NOT_FOUND,
	ERROR_NOT_MEMBER_OF_UNIT,
	ERROR_IS_ALREADY_FIREFIGHTER,
	ERROR_PLAYER_NOT_DISPATCHER,
	ERROR_IS_ALREADY_DISPATCHER,
	ERROR_UNIT_ALREADY_EXISTS,
	ERROR_VAULT_NOT_INSTALLED,
	FIREFIGHTER_EQUIPPED,
	FIREFIGHTER_RESPONDED,
	FIREFIGHTER_QUIT_MISSION,
	FIREFIGHTER_INVENTORY_RESTORED,
	INFO_HEADLINE_DISPATCHERS,
	INFO_HEADLINE_FIREFIGHTERS_ALL,
	INFO_HEADLINE_FIREFIGHTERS_UNIT,
	INSURANCE_PAYED,
	INSURANCE_INFORMATION,
	INSURANCE_CANCELLED,
	INSURANCE_NOT_INSURED,
	INSURANCE_TARGET_NOT_INSURED,
	INSURANCE_ALREADY_INSURED,
	INSURANCE_CONTRACTED,
	INSURANCE_SUM_PAYED_OUT,
	INSURANCE_SUM_RECEIVED,
	MANAGER_FIRESTATION_LOCATION_SET,
	MANAGER_FIRESTATION_LOCATION_SET_FOR_FIREFIGHTERS,
	MANAGER_AUTODISPATCH_ON,
	MANAGER_AUTODISPATCH_OFF,
	MANAGER_PLAYER_ASSIGNED_TO_UNIT,
	MANAGER_ADDED_FIREFIGHTER,
	MANAGER_PLAYER_REMOVED_FROM_UNIT,
	MANAGER_REMOVED_FIREFIGHTER,
	MANAGER_ADDED_DISPATCHER,
	MANAGER_REMOVED_DISPATCHER,
	MANAGER_UNIT_ADDED,
	MANAGER_UNIT_REMOVED,
	MANAGER_SALARIES_PAYED,
	MISSION_ENDED,
	UI_HEADLINE,
	UI_CALLING_CIVILIAN,
	UI_DISPATCHER,
	UI_UNITS_DISPATCHED,
	UI_FIREFIGHTERS_RESPONDED,
	YOU_HAVE_BEEN_REMOVED_FROM_UNIT,
	YOU_ARE_NO_LONGER_A_FIREFIGHTER,
	YOU_HAVE_BEEN_ASSIGNED_TO_UNIT,
	YOU_ARE_A_FIREFIGHTER,
	YOU_ARE_A_DISAPTCHER,
	YOU_ARE_NO_LONGER_A_DISPATCHER;
	
	private String message;
	
	public static void initMessages() {
		// Set default messages
		ALARM_MESSAGE_INTRO.setMessage("-- " + ChatColor.RED + "EMERGENCY CALL for the FIRE BRIGADE!" + ChatColor.WHITE + " --");
		ALARM_MESSAGE_CONTENT_DEFAULT.setMessage("Unknown emergency situation, no further information.");
		ALARM_MESSAGE_FD_INFORMED.setMessage("Please keep calm! The fire department has been informed. We'll let you know when the first firefighter is on the way to you!");
		ALARM_MESSAGE_DISPATCHER_HELP.setMessage("Use " + ChatColor.GREEN + "/ffdispatch <unit1> [<unit2> [...]] [-m <infoMessage>]" + ChatColor.WHITE + " to call firefighters now!");
		ALARM_MESSAGE_FIREFIGHTER_HELP_ROGER.setMessage("Use " + ChatColor.GREEN + "/ff roger" + ChatColor.WHITE + " to confirm the alarm!");
		ALARM_INFO_FIREFIGHTER_ROGERED.setMessage("confirmed your emergency call and is en route to the fire station now!");
		ALARM_INFO_FIREFIGHTER_RESPONDED.setMessage("responed to your call and is currently arriving at the site. Keep calm!");
		DISPATCH_UNITS_DISPATCHED.setMessage("firefighters dispatched.");
		DISPATCH_FIREFIGHTER_ROGERED.setMessage("Roger that! En route to the fire station.");
		ERROR_FIRE_DEPT_NOT_AVAILABLE.setMessage("The fire brigade is currently unavailable due to another call. Please try again later!");
		ERROR_COMMAND_NO_CONSOLE.setMessage("This command can only be sent by a player");
		ERROR_DISPATCH_UNIT_NOT_EXIST.setMessage("does not exist. Dispatch cancelled. Edit your command first!");
		ERROR_NO_EQUIPMENT_SET.setMessage("The fire brigade has unfortunately not provided any equipment. You must get along without.");
		ERROR_NOT_PART_OF_MISSION.setMessage("You are not part of the running call and cannot use this command at the moment.");
		ERROR_NO_MISSION_CURRENTLY_DISPATCH.setMessage("At the moment there is no mission to dispatch units for.");
		ERROR_NO_MISSION_CURRENTLY_RESPOND.setMessage("At the moment there is no mission to roger, equip for or respond to.");
		ERROR_UNIT_NOT_EXIST.setMessage("There is no unit with this name yet.");
		ERROR_PLAYER_NOT_FIREFIGHTER.setMessage("This player is not a firefighter.");
		ERROR_PLAYER_NOT_FOUND.setMessage("This player has never played on this server.");
		ERROR_NOT_MEMBER_OF_UNIT.setMessage("This player is not member of that unit.");
		ERROR_IS_ALREADY_FIREFIGHTER.setMessage("This player is already member of the fire department.");
		ERROR_PLAYER_NOT_DISPATCHER.setMessage("This player is not a dispatcher.");
		ERROR_IS_ALREADY_DISPATCHER.setMessage("This player is already a dispatcher.");
		ERROR_UNIT_ALREADY_EXISTS.setMessage("This unit already exists.");
		ERROR_VAULT_NOT_INSTALLED.setMessage("Economy is not supported on this server. Plugin 'Vault' is missing.");
		FIREFIGHTER_EQUIPPED.setMessage("You have been equipped with your most necessary equipment. You'll get your things back later using /ff quit");
		FIREFIGHTER_RESPONDED.setMessage("has moved out and has approached the site");
		FIREFIGHTER_QUIT_MISSION.setMessage("quit the mission and returned to base.");
		FIREFIGHTER_INVENTORY_RESTORED.setMessage("You inventory has been restored.");
		INFO_HEADLINE_DISPATCHERS.setMessage("# Information message for all Dispatchers ++");
		INFO_HEADLINE_FIREFIGHTERS_ALL.setMessage("# Information message for all Firefighters ++");
		INFO_HEADLINE_FIREFIGHTERS_UNIT.setMessage("# Information message for firefighters in unit");
		INSURANCE_PAYED.setMessage("Your insurance contribution has been withdrawn. The next one is due in %d days.");
		INSURANCE_INFORMATION.setMessage("You are insured for fire damage with an insurance fee of %ai euros every %di days and receive %as euros in case of damage. The next installment is due in %dr days.");
		INSURANCE_CANCELLED.setMessage("You successfully cancelled your fire insurance.");
		INSURANCE_NOT_INSURED.setMessage("You are not insured yet.");
		INSURANCE_TARGET_NOT_INSURED.setMessage("This player does not have insurance.");
		INSURANCE_ALREADY_INSURED.setMessage("You are already insured. Cancel your current insurance first!");
		INSURANCE_CONTRACTED.setMessage("You are fire insured now. In case of damage you will receive a compensation of");
		INSURANCE_SUM_PAYED_OUT.setMessage("The fire insurance payed %p an insurance sum of %a");
		INSURANCE_SUM_RECEIVED.setMessage("You received an insurance sum of %a");
		MANAGER_FIRESTATION_LOCATION_SET.setMessage("You set a new location for the fire station.");
		MANAGER_FIRESTATION_LOCATION_SET_FOR_FIREFIGHTERS.setMessage("A new fire station has been set at");
		MANAGER_AUTODISPATCH_ON.setMessage("Autodispatch has been turned on. Alarms will be dispatched to every firefighter automatically now.");
		MANAGER_AUTODISPATCH_OFF.setMessage("Autodispatch has been turned off. A dispatcher is needed to turn an emergency call into an alarm now.");
		MANAGER_PLAYER_ASSIGNED_TO_UNIT.setMessage("has been assigned to unit");
		MANAGER_PLAYER_REMOVED_FROM_UNIT.setMessage("has been removed from unit");
		MANAGER_ADDED_FIREFIGHTER.setMessage("is a firefighter now.");
		MANAGER_REMOVED_FIREFIGHTER.setMessage("is no longer a firefighter.");
		MANAGER_ADDED_DISPATCHER.setMessage("is a dispatcher now.");
		MANAGER_REMOVED_DISPATCHER.setMessage("is no longer a dispatcher.");
		MANAGER_UNIT_ADDED.setMessage("You created the unit");
		MANAGER_UNIT_REMOVED.setMessage("You removed the unit");
		MANAGER_SALARIES_PAYED.setMessage("Salaries have been payed to firefighters and dispatchers. Total value:");
		MISSION_ENDED.setMessage("The mission is finished, the place of deployment has been handed over to the owner.");
		UI_HEADLINE.setMessage(ChatColor.DARK_RED + "EMERGENCY MISSION");
		UI_CALLING_CIVILIAN.setMessage("Calling Civilian");
		UI_DISPATCHER.setMessage("Dispatcher");
		UI_UNITS_DISPATCHED.setMessage("Units dispatched");
		UI_FIREFIGHTERS_RESPONDED.setMessage("Firefighters responded");
		YOU_ARE_A_DISAPTCHER.setMessage("Congratulations! You are a dispatcher now!");
		YOU_ARE_NO_LONGER_A_DISPATCHER.setMessage("You are no longer a dispatcher.");
		YOU_HAVE_BEEN_ASSIGNED_TO_UNIT.setMessage("You have been assigned to unit");
		YOU_HAVE_BEEN_REMOVED_FROM_UNIT.setMessage("You have been removed from unit");
		YOU_ARE_A_FIREFIGHTER.setMessage("Congratulations! You are a firefighter now!");
		YOU_ARE_NO_LONGER_A_FIREFIGHTER.setMessage("You are no longer a firefighter.");
		
		// Write language file to allow users to add a translation
		String content = "### Do not use the Windows Notepad for editing, because it ignores the (really important) line breaks in this file ###\n" 
				+ "### Edit messages in this file. Only change the part on the right hand side. ###\n"
				+ "### Variable names and message content will be split at ': ', so leave the whitespace where it is and do not use ': ' in your messages! ###\n\n";
		for(Messages key : Messages.values()) {
			content += key.name() + ": " + key.getMessage() + "\n";
		}
		
		File langFile = new File(Bukkit.getPluginManager().getPlugin("FirefighterPro").getDataFolder() + java.io.File.separator + "messages.lang");
		try {
			// Check whether file already exists
			if(!langFile.exists()) {
				// if not: create and write default content (from above)
				langFile.createNewFile();
				
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(langFile));
				osw.write(content.trim());
				osw.flush();
				osw.close();
			} else {
				
				// if file already exists, read the content
				Scanner s = new Scanner(langFile,"UTF-8");
				// Iterate every line of the file
				while(s.hasNextLine()) {
					String line = s.nextLine();
					// ignore empty or comment lines
					if(line != null && !line.isEmpty() && !line.startsWith("###")) {
						// split lines at ': '
						if(line.contains(": "))					 {
							String[] parts = line.split(": ");
							// filter lines where more or less than 2 ': ' were found
							if(parts != null && parts.length == 2) {
								// apply message content to the right Message
								String keyFromFile = parts[0];
								String contentFromFile = parts[1];
								/// ...using a loop over all Messages
								for(Messages currentMessage : Messages.values()) {
									if(currentMessage.name().equalsIgnoreCase(keyFromFile)) {
										currentMessage.setMessage(contentFromFile);
										break;
									}
								}
							}
						}
					}
				}
				s.close();
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setMessage(String msg) {
		this.message = msg;
	}
	
	public String getMessage() {
		return message;
	}
	
	public static String format(Messages rawInput) {
		return format(rawInput.getMessage());
	}
	
	public static String format(String rawInput) {
		return ChatColor.YELLOW + "[" + ChatColor.RED + "FFPro" + ChatColor.YELLOW + "] " + ChatColor.WHITE + rawInput;
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
	
}
