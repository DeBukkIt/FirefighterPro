package com.blogspot.debukkitsblog.firefighterpro;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
	ERROR_FIRE_DEPT_NOT_AVAILABLE,
	ERROR_COMMAND_NO_CONSOLE,
	ERROR_DISPATCH_UNIT_NOT_EXIST,
	ERROR_NO_EQUIPMENT_SET,
	ERROR_NOT_PART_OF_MISSION,
	ERROR_NO_MISSION_CURRENTLY_DISPATCH,
	ERROR_NO_MISSION_CURRENTLY_RESPOND,
	DISPATCH_UNITS_DISPATCHED,
	DISPATCH_FIREFIGHTER_ROGERED,
	INFO_HEADLINE_DISPATCHERS,
	INFO_HEADLINE_FIREFIGHTERS_ALL,
	INFO_HEADLINE_FIREFIGHTERS_UNIT,
	FIREFIGHTER_EQUIPPED,
	FIREFIGHTER_RESPONDED,
	FIREFIGHTER_QUIT_MISSION,
	FIREFIGHTER_INVENTORY_RESTORED,
	MANAGER_FIRESTATION_LOCATION_SET,
	MANAGER_FIRESTATION_LOCATION_SET_FOR_FIREFIGHTERS,
	MANAGER_AUTODISPATCH_ON,
	MANAGER_AUTODISPATCH_OFF,
	MISSION_ENDED;
	
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
		ERROR_FIRE_DEPT_NOT_AVAILABLE.setMessage("The fire brigade is currently unavailable due to another call. Please try again later!");
		ERROR_COMMAND_NO_CONSOLE.setMessage("This command can only be sent by a player");
		ERROR_DISPATCH_UNIT_NOT_EXIST.setMessage("does not exist. Dispatch cancelled. Edit your command first!");
		ERROR_NO_EQUIPMENT_SET.setMessage("The fire brigade has unfortunately not provided any equipment. You must get along without.");
		ERROR_NOT_PART_OF_MISSION.setMessage("You are not part of the running call and cannot use this command at the moment.");
		ERROR_NO_MISSION_CURRENTLY_DISPATCH.setMessage("At the moment there is no mission to dispatch units for.");
		ERROR_NO_MISSION_CURRENTLY_RESPOND.setMessage("At the moment there is no mission to roger, equip for or respond to.");
		DISPATCH_UNITS_DISPATCHED.setMessage("firefighters dispatched.");
		DISPATCH_FIREFIGHTER_ROGERED.setMessage("Roger that! En route to the fire station.");
		INFO_HEADLINE_DISPATCHERS.setMessage("# Information message for all Dispatchers ++");
		INFO_HEADLINE_FIREFIGHTERS_ALL.setMessage("# Information message for all Firefighters ++");
		INFO_HEADLINE_FIREFIGHTERS_UNIT.setMessage("# Information message for firefighters in unit");
		FIREFIGHTER_EQUIPPED.setMessage("You have been equipped with your most necessary equipment. You'll get your things back later using /ff quit");
		FIREFIGHTER_RESPONDED.setMessage("has moved out and has approached the site");
		FIREFIGHTER_QUIT_MISSION.setMessage("quit the mission and returned to base.");
		FIREFIGHTER_INVENTORY_RESTORED.setMessage("You inventory has been restored.");
		MANAGER_FIRESTATION_LOCATION_SET.setMessage("You set a new location for the fire station.");
		MANAGER_FIRESTATION_LOCATION_SET_FOR_FIREFIGHTERS.setMessage("A new fire station has been set at");
		MANAGER_AUTODISPATCH_ON.setMessage("Autodispatch has been turned on. Alarms will be dispatched to every firefighter automatically now.");
		MANAGER_AUTODISPATCH_OFF.setMessage("Autodispatch has been turned off. A dispatcher is needed to turn an emergency call into an alarm now.");
		MISSION_ENDED.setMessage("The mission is finished, the place of deployment has been handed over to the owner.");
		
		// Write language file to allow users to add a translation
		String content = "";
		for(Messages key : Messages.values()) {
			content += key + ": " + key.getMessage() + "\n";
		}
		
		File langFile = new File(Bukkit.getPluginManager().getPlugin("FirefighterPro").getDataFolder() + java.io.File.separator + "messages.lang");
		try {
			if(!langFile.createNewFile()) {
				FileWriter fw = new FileWriter(langFile);
				fw.write(content.trim());
				fw.flush();
				fw.close();
			}						
		} catch(IOException e) {
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
