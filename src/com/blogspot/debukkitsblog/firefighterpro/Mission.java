package com.blogspot.debukkitsblog.firefighterpro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Mission {
	
	public static final int EVENT_EMERGENCY_CALL = 0;
	public static final int EVENT_DISPATCH = 1;
	public static final int EVENT_FIRST_FIREFIGHTER_ROGER = 2;
	public static final int EVENT_FIRST_FIREFIGHTER_RESPOND = 3;
	public static final int EVENT_MISSION_END = 4;
	
	private FirefighterPro plugin;

	private Player callingCivilian;
	private String emergencyMessage;
	private Location location;
	private Player dispatcher;
	
	private boolean isOver;
	private String[] timeStamps;		
	private int manpower;
	private HashMap<UUID, ItemStack[]> firefightersEquipped;
	
	public Mission(FirefighterPro plugin, String emergencyMessage, Location location, Player callingCivilian) {
		this.plugin = plugin;
		this.emergencyMessage = emergencyMessage;
		this.location = location;
		this.callingCivilian = callingCivilian;
		this.firefightersEquipped = new HashMap<UUID, ItemStack[]>();
		
		this.dispatcher = null;
		// Index	0		1			2		3		4
		// Status	CALL	DISPATCH	ROGER	RESPOND	END
		this.timeStamps = new String[5];
		this.timeStamps[0] = currentTime();
	}
	
	public int dispatch(String unitName, String additionalMessage) {
		// check whether unit exists
		if(plugin.getFFConfig().unitExist(unitName)) {
			// get member list of the unit
			List<Player> firefighters = plugin.getFFConfig().getFirefightersInUnit(unitName);
			if (firefighters != null) {
				// send Message to every member
				for (Player firefighter : firefighters) {
					firefighter.sendMessage(Messages.format(Messages.ALARM_MESSAGE_INTRO));
					firefighter.sendMessage(Messages.format(getCallingCivilian().getDisplayName() + ": " + getEmergencyMessage()));
					firefighter.sendMessage(Messages.format("@ " + location.getWorld() + ", (" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ() + ")"));
					firefighter.sendMessage(Messages.format(additionalMessage));
				}
				if(this.timeStamps[1] == null) this.timeStamps[1] = currentTime();
				
				return firefighters.size();
			}
		}
		return -1; // if the unit does not exist
	}
	
	public int dispatchAuto() {
		this.timeStamps[1] = currentTime();
		List<Player> firefighters = plugin.getFFConfig().getFirefighters();
		// get member list of the unit
		if (firefighters != null) {
			// send Message to every member
			for (Player firefighter : firefighters) {
				firefighter.sendMessage(Messages.format(Messages.ALARM_MESSAGE_INTRO));
				firefighter.sendMessage(Messages.format(getCallingCivilian().getDisplayName() + ": " + getEmergencyMessage() + " @ " + location.getWorld().getName() + " ( " + location.getBlockX() + " | " + location.getBlockY() + " | " + location.getBlockZ() + " )"));
				firefighter.sendMessage(Messages.format(Messages.ALARM_MESSAGE_FIREFIGHTER_HELP_ROGER));
			}
		}
		return firefighters.size();
	}
	
	public void roger(Player rogeringFirefighter) {
		this.timeStamps[2] = currentTime();
		manpower++;
		// Teleport the firefighter
		rogeringFirefighter.teleport(plugin.getFFConfig().getStationLocation());
		// Notify the civilian waiting for help
		if(callingCivilian != null) callingCivilian.sendMessage(Messages.format(ChatColor.GREEN + rogeringFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.ALARM_INFO_FIREFIGHTER_ROGERED));
		// Notify the dispatcher who 
		if(dispatcher != null) dispatcher.sendMessage(Messages.format(ChatColor.GREEN + rogeringFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.DISPATCH_FIREFIGHTER_ROGERED));
		// Notify the firefighter him-/herself that the command was successful
		rogeringFirefighter.sendMessage(Messages.format(ChatColor.GREEN + rogeringFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.DISPATCH_FIREFIGHTER_ROGERED));
	}
	
	public void equip(Player equippingFirefighter) {
		// load equipment
		ArrayList<ItemStack> equipment = plugin.getFFConfig().getEquipment();
		if(equipment != null) {
			// save and empty inventory
			firefightersEquipped.put(equippingFirefighter.getUniqueId(), equippingFirefighter.getInventory().getContents());
			equippingFirefighter.getInventory().clear();
			// insert equipment
			for(int i = 0; i < equipment.size(); i++) {
				equippingFirefighter.getInventory().setItem(i, equipment.get(i));
			}
			equippingFirefighter.sendMessage(Messages.format(Messages.FIREFIGHTER_EQUIPPED));
		} else {
			equippingFirefighter.sendMessage(Messages.format(Messages.ERROR_NO_EQUIPMENT_SET));
		}		
	}
	
	public void respond(Player respondingFirefighter) {
		// teleport the firefighter to the site
		respondingFirefighter.teleport(location);
		// confirm the responding to him-/herself and the dispatcher
		respondingFirefighter.sendMessage(Messages.format(respondingFirefighter.getDisplayName() + " " + Messages.FIREFIGHTER_RESPONDED));
		if(dispatcher != null) dispatcher.sendMessage(Messages.format(respondingFirefighter.getDisplayName() + " " + Messages.FIREFIGHTER_RESPONDED));
		// inform the calling civilian
		if(callingCivilian != null) callingCivilian.sendMessage(Messages.format(Messages.ALARM_INFO_FIREFIGHTER_RESPONDED));
	}
	
	public void quit(Player quittingFirefighter) {		
		// confirm the quitting to the firefighter him-/herself and the dispatcher
		quittingFirefighter.sendMessage(Messages.format(ChatColor.RED + quittingFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.FIREFIGHTER_QUIT_MISSION));
		if(dispatcher != null) dispatcher.sendMessage(Messages.format(ChatColor.RED + quittingFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.FIREFIGHTER_QUIT_MISSION));
		// restore the inventory
		quittingFirefighter.getInventory().setContents(firefightersEquipped.get(quittingFirefighter.getUniqueId()));
		quittingFirefighter.sendMessage(Messages.format(Messages.FIREFIGHTER_INVENTORY_RESTORED));
	}	
	
	public void end() {
		// mark the mission as over
		isOver = true;
		// send every firefighter who equipped for this mission a message
		for(UUID id : firefightersEquipped.keySet()) {
			Player pl = Bukkit.getPlayer(id);
			if(pl != null) pl.sendMessage(Messages.format(Messages.MISSION_ENDED));
		}
		if(dispatcher != null) dispatcher.sendMessage(Messages.format(Messages.MISSION_ENDED));
	}
	
	public boolean hasBeenAtLocation(Player player) {
		return firefightersEquipped.containsKey(player.getUniqueId());
	}
	
	private String currentTime() {
		return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
	}
	
	public boolean isOver() {
		return this.isOver;
	}

	public String getEmergencyMessage() {
		return emergencyMessage;
	}

	public Location getLocation() {
		return location;
	}

	public Player getCallingCivilian() {
		return callingCivilian;
	}

	public Player getDispatcher() {
		return dispatcher;
	}
	
	public int getManpower() {
		return manpower;
	}

	public String getTimestamp(int event) {
		if(event < 0 || event > 4) {
			throw new IllegalArgumentException("Parameter must be >= 0 and <= 4");
		}
		return this.timeStamps[event];
	}
	
}