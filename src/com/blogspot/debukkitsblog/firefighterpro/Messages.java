package com.blogspot.debukkitsblog.firefighterpro;

import org.bukkit.ChatColor;

public enum Messages {
	
	ALARM_MESSAGE_INTRO,
	ALARM_MESSAGE_CONTENT_DEFAULT,
	ALARM_MESSAGE_FD_INFORMED,
	ALARM_MESSAGE_DISPATCHER_HELP,
	ALARM_MESSAGE_FIREFIGHTER_HELP_ROGER,
	INFO_FIRST_FIREFIGHTER_EN_ROUTE,
	ERROR_COMMAND_NO_CONSOLE;
	
	private String message;
	
	public static void initMessages() {
		//TODO Implementieren
		ALARM_MESSAGE_INTRO.setMessage("-- " + ChatColor.RED + "EMERGENCY CALL for the FIRE BRIGADE!" + ChatColor.WHITE + " --");
		ALARM_MESSAGE_CONTENT_DEFAULT.setMessage("Unknown emergency situation, no further information.");
		ALARM_MESSAGE_FD_INFORMED.setMessage("Please keep calm! The fire department has been informed. We'll let you know when the first firefighter is on the way to you!");
		ALARM_MESSAGE_DISPATCHER_HELP.setMessage("Use " + ChatColor.GREEN + "/ffdispatch <unit1> [<unit2> [...]] [-m <infoMessage>]" + ChatColor.WHITE + " to call firefighters now!");
		ALARM_MESSAGE_FIREFIGHTER_HELP_ROGER.setMessage("Use " + ChatColor.GREEN + "/ff roger" + ChatColor.WHITE + " to confirm the alarm!");
		INFO_FIRST_FIREFIGHTER_EN_ROUTE.setMessage("The first of our brave firefighters confirmed your emergency alarm. He is en route to the fire station now!");
		ERROR_COMMAND_NO_CONSOLE.setMessage("This command can only be sent by a player");
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
		return this.message;
	}
	
}
