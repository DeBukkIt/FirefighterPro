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

import com.blogspot.debukkitsblog.firefighterpro.ui.UIManager;
import com.blogspot.debukkitsblog.firefighterpro.worldguard.PlayerDomainWrapper;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Mission {
	
	public static final int EVENT_EMERGENCY_CALL = 0;
	public static final int EVENT_DISPATCH = 1;
	public static final int EVENT_FIRST_FIREFIGHTER_ROGER = 2;
	public static final int EVENT_FIRST_FIREFIGHTER_RESPOND = 3;
	public static final int EVENT_MISSION_END = 4;

	private Player callingCivilian;
	private String emergencyMessage;
	private Location location;
	private Player dispatcher;
	
	private boolean isOver;
	private String[] timeStamps;		
	private int manpower;
	private HashMap<UUID, ItemStack[]> firefightersInMission;
	private List<String> unitsInMission;
	
	private boolean buildingAllowed;
	private ProtectedRegion region;
	private PlayerDomainWrapper regionOldMembers;
	
	public Mission(String emergencyMessage, Location location, Player callingCivilian) {
		this.emergencyMessage = emergencyMessage;
		this.location = location;
		this.callingCivilian = callingCivilian;
		this.buildingAllowed = false;
		this.firefightersInMission = new HashMap<UUID, ItemStack[]>();
		this.unitsInMission = new ArrayList<String>();
		this.regionOldMembers = null;
		
		this.dispatcher = null;
		// Index	0		1			2		3		4
		// Status	CALL	DISPATCH	ROGER	RESPOND	END
		this.timeStamps = new String[5];
		this.timeStamps[0] = currentTime();
	}
	
	public List<String> getUnitsInMission() {
		return unitsInMission;
	}

	public int dispatch(String unitName, String additionalMessage) {
		// Do not dispatch for mission which are over
		if(isOver) {
			return -2;
		}
		// Allow firefighters to build in the emergency's WorldGuard region
		if(FirefighterPro.getInstance().isWorldGuardSupported() && !buildingAllowed) {
			FirefighterPro.getInstance().getWorldGuardHandler().setAllowBuild(location, this);
			buildingAllowed = true;
		}
		// check whether unit exists
		if(FirefighterPro.getInstance().getFFConfig().unitExist(unitName)) {
			// remember timestamp for statistics
			if(this.timeStamps[1] == null) this.timeStamps[1] = currentTime();
			// remember what unit has been dispatched
			unitsInMission.add(FirefighterPro.getInstance().getFFConfig().getUnitDisplayName(unitName));
			// send message to all members of the unit
			FirefighterPro.getInstance().getBroadcaster().broadcastToUnit(unitName, Messages.format(Messages.ALARM_MESSAGE_INTRO));
			FirefighterPro.getInstance().getBroadcaster().broadcastToUnit(unitName, Messages.format(getCallingCivilian().getDisplayName() + ": " + getEmergencyMessage()));
			FirefighterPro.getInstance().getBroadcaster().broadcastToUnit(unitName, Messages.format("@ " + location.getWorld() + ", (" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ() + ")"));
			return FirefighterPro.getInstance().getBroadcaster().broadcastToUnit(unitName, Messages.format(additionalMessage));
		}
		return -1; // if the unit does not exist
	}
	
	public int dispatchAuto() {
		// Do not dispatch for mission which are over
		if(isOver) {
			return -2;
		}
		// Allow firefighters to build in the emergency's WorldGuard region
		if(FirefighterPro.getInstance().isWorldGuardSupported() && !buildingAllowed) {
			FirefighterPro.getInstance().getWorldGuardHandler().setAllowBuild(location, this);
			buildingAllowed = true;
		}	
		// remember timestamp for statistics
		if(this.timeStamps[1] == null) this.timeStamps[1] = currentTime();
		// remember what unit has been dispatched
		unitsInMission.add("All units");
		// send message to all firefighters
		FirefighterPro.getInstance().getBroadcaster().broadcastToFirefighters(Messages.format(Messages.ALARM_MESSAGE_INTRO));
		FirefighterPro.getInstance().getBroadcaster().broadcastToFirefighters(Messages.format(getCallingCivilian().getDisplayName() + ": " + getEmergencyMessage() + " @ " + location.getWorld().getName() + " ( " + location.getBlockX() + " | " + location.getBlockY() + " | " + location.getBlockZ() + " )"));
		return FirefighterPro.getInstance().getBroadcaster().broadcastToFirefighters(Messages.format(Messages.ALARM_MESSAGE_FIREFIGHTER_HELP_ROGER));
	}
	
	public void roger(Player rogeringFirefighter) {
		this.timeStamps[2] = currentTime();
		manpower++;
		// Teleport the firefighter
		rogeringFirefighter.teleport(FirefighterPro.getInstance().getFFConfig().getStationLocation());
		// Notify the civilian waiting for help
		if(callingCivilian != null) callingCivilian.sendMessage(Messages.format(ChatColor.GREEN + rogeringFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.ALARM_INFO_FIREFIGHTER_ROGERED));
		// Notify the dispatcher who 
		if(dispatcher != null) dispatcher.sendMessage(Messages.format(ChatColor.GREEN + rogeringFirefighter.getDisplayName() + ChatColor.WHITE + ": " + Messages.DISPATCH_FIREFIGHTER_ROGERED));
		// Notify the firefighter him-/herself that the command was successful
		rogeringFirefighter.sendMessage(Messages.format(ChatColor.GREEN + rogeringFirefighter.getDisplayName() + ChatColor.WHITE + ": " + Messages.DISPATCH_FIREFIGHTER_ROGERED));
	}
	
	public void equip(Player equippingFirefighter) {
		// load equipment
		ArrayList<ItemStack> equipment = FirefighterPro.getInstance().getFFConfig().getEquipment();
		if(equipment != null) {
			// save and empty inventory
			firefightersInMission.put(equippingFirefighter.getUniqueId(), equippingFirefighter.getInventory().getContents());
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
		if(!firefightersInMission.containsKey(respondingFirefighter.getUniqueId())) {
			firefightersInMission.put(respondingFirefighter.getUniqueId(), respondingFirefighter.getInventory().getContents());
		}
		// teleport the firefighter to the site
		respondingFirefighter.teleport(location);
		// confirm the responding to him-/herself and the dispatcher
		respondingFirefighter.sendMessage(ChatColor.GREEN + Messages.format(respondingFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.FIREFIGHTER_RESPONDED));
		if(dispatcher != null) dispatcher.sendMessage(Messages.format(respondingFirefighter.getDisplayName() + " " + Messages.FIREFIGHTER_RESPONDED));
		// inform the calling civilian
		if(callingCivilian != null) callingCivilian.sendMessage(Messages.format(respondingFirefighter.getDisplayName() + " " + Messages.ALARM_INFO_FIREFIGHTER_RESPONDED));
		// Update scoreboard
		updateScoreboards();
	}

	public void quit(Player quittingFirefighter) {	
		// confirm the quitting to the firefighter him-/herself and the dispatcher
		quittingFirefighter.sendMessage(Messages.format(ChatColor.RED + quittingFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.FIREFIGHTER_QUIT_MISSION));
		if(dispatcher != null) dispatcher.sendMessage(Messages.format(ChatColor.RED + quittingFirefighter.getDisplayName() + ChatColor.WHITE + " " + Messages.FIREFIGHTER_QUIT_MISSION));
		// restore the inventory
		quittingFirefighter.getInventory().setContents(firefightersInMission.get(quittingFirefighter.getUniqueId()));
		quittingFirefighter.sendMessage(Messages.format(Messages.FIREFIGHTER_INVENTORY_RESTORED));
		// Teleport back to fire station
		quittingFirefighter.teleport(FirefighterPro.getInstance().getFFConfig().getStationLocation());
		firefightersInMission.remove(quittingFirefighter.getUniqueId());
		// Update scoreboard
		updateScoreboards();
		// Remove scoreboard from quitting firefighter
		quittingFirefighter.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}	
	
	private void updateScoreboards() {
		for(Player firefighter : getFirefighters()) {
			firefighter.setScoreboard(UIManager.getScoreboard(this));
		}
	}
	
	private void payCompensation(Player p) {
		if(FirefighterPro.getInstance().isEconomySupported()) {
			if(FirefighterPro.getInstance().getEconomy().deposit(p, FirefighterPro.getInstance().getFFConfig().getSingleMissionCompensation())) {
				p.sendMessage(Messages.format(Messages.FIREFIGHTER_COMPENSATION_PAYEDOFF));
			}
		}
	}
	
	public void end() {		
		// mark the mission as over
		isOver = true;
		// Deny firefighters to build in the emergency's region, reset old members
		if(FirefighterPro.getInstance().isWorldGuardSupported()) {
			FirefighterPro.getInstance().getWorldGuardHandler().setOldBuildPermissions(location, this);
		}
		// send end message to every firefighter and dispatcher
		FirefighterPro.getInstance().getBroadcaster().broadcastToDispatchers(Messages.format(Messages.INFO_HEADLINE_DISPATCHERS));
		FirefighterPro.getInstance().getBroadcaster().broadcastToDispatchers(Messages.format(Messages.MISSION_ENDED));
		FirefighterPro.getInstance().getBroadcaster().broadcastToFirefighters(Messages.format(Messages.INFO_HEADLINE_FIREFIGHTERS_ALL));
		FirefighterPro.getInstance().getBroadcaster().broadcastToFirefighters(Messages.format(Messages.MISSION_ENDED));
		// remove all firefighters from this mission, give them their inventory back
		for(UUID id : firefightersInMission.keySet()) {
			Player pl = Bukkit.getServer().getPlayer(id);
			// pay compensation
			payCompensation(pl);
			if(pl.isOnline()) {
				quit(pl);
			}
		}
	}
	
	public List<Player> getFirefighters() {
		List<Player> result = new ArrayList<Player>();
		if(firefightersInMission != null) {
			for (UUID id : firefightersInMission.keySet()) {
				result.add(Bukkit.getServer().getPlayer(id));
			}
		}
		return result;
	}
	
	public boolean hasBeenAtLocation(Player player) {
		return firefightersInMission.containsKey(player.getUniqueId());
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
	
	public void setRegionOldMembers(PlayerDomainWrapper oldMembers) {
		this.regionOldMembers = oldMembers;
	}
	
	public PlayerDomainWrapper getRegionOldMembers() {
		return regionOldMembers;
	}
	
}