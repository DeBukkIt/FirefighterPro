package com.blogspot.debukkitsblog.firefighterpro;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Mission {
	
	public static final int EVENT_EMERGENCY_CALL = 0;
	public static final int EVENT_DISPATCH = 1;
	public static final int EVENT_FIRST_FIREFIGHTER_ROGER = 2;
	public static final int EVENT_FIRST_FIREFIGHTER_RESPOND = 3;
	public static final int EVENT_MISSION_END = 4;
	
	private FirefighterPro plugin;
	
	private String[] timeStamps;
	private String emergencyMessage;
	private Location location;
	private Player callingCivilian;
	private Player dispatcher;
	
	public Mission(FirefighterPro plugin, String emergencyMessage, Location location, Player callingCivilian) {
		this.plugin = plugin;
		this.emergencyMessage = emergencyMessage;
		this.location = location;
		this.callingCivilian = callingCivilian;
		
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
		callingCivilian.sendMessage(Messages.format(""));
	}
	
	private String currentTime() {
		return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
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

	public String getTimestamp(int event) {
		if(event < 0 || event > 4) {
			throw new IllegalArgumentException("Parameter must be >= 0 and <= 4");
		}
		return this.timeStamps[event];
	}
	
}