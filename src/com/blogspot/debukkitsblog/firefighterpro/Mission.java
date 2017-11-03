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

import com.blogspot.debukkitsblog.firefighterpro.worldguard.DefaultDomainWrapper;
import com.blospot.debukkitsblog.firefighterpro.ui.UIManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

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
	
	private ProtectedRegion region;
	private DefaultDomainWrapper regionOldMembers;
	
	public Mission(FirefighterPro plugin, String emergencyMessage, Location location, Player callingCivilian) {
		this.plugin = plugin;
		this.emergencyMessage = emergencyMessage;
		this.location = location;
		this.callingCivilian = callingCivilian;
		this.firefightersEquipped = new HashMap<UUID, ItemStack[]>();
		this.regionOldMembers = null;
		
		this.dispatcher = null;
		// Index	0		1			2		3		4
		// Status	CALL	DISPATCH	ROGER	RESPOND	END
		this.timeStamps = new String[5];
		this.timeStamps[0] = currentTime();
	}
	
	public int dispatch(String unitName, String additionalMessage) {
		// check whether unit exists
		if(plugin.getFFConfig().unitExist(unitName)) {
			// remember timestamp for statistics
			if(this.timeStamps[1] == null) this.timeStamps[1] = currentTime();
			// update scoreboards
			updateScoreboards();
			// send message to all members of the unit
			plugin.getBroadcaster().broadcastToUnit(unitName, Messages.format(Messages.ALARM_MESSAGE_INTRO));
			plugin.getBroadcaster().broadcastToUnit(unitName, Messages.format(getCallingCivilian().getDisplayName() + ": " + getEmergencyMessage()));
			plugin.getBroadcaster().broadcastToUnit(unitName, Messages.format("@ " + location.getWorld() + ", (" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ() + ")"));
			return plugin.getBroadcaster().broadcastToUnit(unitName, Messages.format(additionalMessage));
		}
		return -1; // if the unit does not exist
	}
	
	public int dispatchAuto() {		
		// remember timestamp for statistics
		if(this.timeStamps[1] == null) this.timeStamps[1] = currentTime();
		// update scoreboards
		updateScoreboards();
		// send message to all firefighters
		plugin.getBroadcaster().broadcastToFirefighters(Messages.format(Messages.ALARM_MESSAGE_INTRO));
		plugin.getBroadcaster().broadcastToFirefighters(Messages.format(getCallingCivilian().getDisplayName() + ": " + getEmergencyMessage() + " @ " + location.getWorld().getName() + " ( " + location.getBlockX() + " | " + location.getBlockY() + " | " + location.getBlockZ() + " )"));
		return plugin.getBroadcaster().broadcastToFirefighters(Messages.format(Messages.ALARM_MESSAGE_FIREFIGHTER_HELP_ROGER));
	}
	
	public void roger(Player rogeringFirefighter) {
		this.timeStamps[2] = currentTime();
		manpower++;
		// Teleport the firefighter
		rogeringFirefighter.teleport(plugin.getFFConfig().getStationLocation());
		// Notify the civilian waiting for help
		if(callingCivilian != null) callingCivilian.sendMessage(Messages.format(ChatColor.GREEN + rogeringFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.ALARM_INFO_FIREFIGHTER_ROGERED));
		// Notify the dispatcher who 
		if(dispatcher != null) dispatcher.sendMessage(Messages.format(ChatColor.GREEN + rogeringFirefighter.getDisplayName() + ChatColor.WHITE + ": " + Messages.DISPATCH_FIREFIGHTER_ROGERED));
		// Notify the firefighter him-/herself that the command was successful
		rogeringFirefighter.sendMessage(Messages.format(ChatColor.GREEN + rogeringFirefighter.getDisplayName() + ChatColor.WHITE + ": " + Messages.DISPATCH_FIREFIGHTER_ROGERED));
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
		// for not-equipped firefighters: add to the list
		if(!firefightersEquipped.containsKey(respondingFirefighter.getUniqueId())) {
			firefightersEquipped.put(respondingFirefighter.getUniqueId(), respondingFirefighter.getInventory().getContents());
		}
		// teleport the firefighter to the site
		respondingFirefighter.teleport(location);
		// confirm the responding to him-/herself and the dispatcher
		respondingFirefighter.sendMessage(ChatColor.GREEN + Messages.format(respondingFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.FIREFIGHTER_RESPONDED));
		if(dispatcher != null) dispatcher.sendMessage(Messages.format(respondingFirefighter.getDisplayName() + " " + Messages.FIREFIGHTER_RESPONDED));
		// inform the calling civilian
		if(callingCivilian != null) callingCivilian.sendMessage(Messages.format(respondingFirefighter.getDisplayName() + " " + Messages.ALARM_INFO_FIREFIGHTER_RESPONDED));
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
		// send end message to every firefighter and dispatcher
		plugin.getBroadcaster().broadcastToDispatchers(Messages.format(Messages.INFO_HEADLINE_DISPATCHERS));
		plugin.getBroadcaster().broadcastToDispatchers(Messages.format(Messages.MISSION_ENDED));
		plugin.getBroadcaster().broadcastToFirefighters(Messages.format(Messages.INFO_HEADLINE_FIREFIGHTERS_ALL));
		plugin.getBroadcaster().broadcastToFirefighters(Messages.format(Messages.MISSION_ENDED));
		// remove all firefighters from this mission, give them their inventory back
		for(UUID id : firefightersEquipped.keySet()) {
			Player pl = Bukkit.getServer().getPlayer(id);
			if(pl.isOnline()) {
				quit(pl);
			}
		}
	}
	
	public List<Player> getFirefighters() {
		List<Player> result = new ArrayList<Player>();
		if(firefightersEquipped != null) {
			for (UUID id : firefightersEquipped.keySet()) {
				result.add(Bukkit.getServer().getPlayer(id));
			}
		}
		return result;
	}
	
	private void updateScoreboards() {
		//TODO Make Scoreboards work
		for(Player firefighter : getFirefighters()) {
			firefighter.setScoreboard(UIManager.getScoreboard(this));
		}
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
	
	public void setRegion(ProtectedRegion region) {
		this.region = region;
	}
	
	public ProtectedRegion getRegion() {
		return region;
	}
	
	public void setRegionOldMembers(DefaultDomainWrapper oldMembers) {
		this.regionOldMembers = oldMembers;
	}
	
	public DefaultDomainWrapper getRegionOldMembers() {
		return regionOldMembers;
	}
	
}