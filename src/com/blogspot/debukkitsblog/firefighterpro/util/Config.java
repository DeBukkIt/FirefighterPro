package com.blogspot.debukkitsblog.firefighterpro.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;

public class Config {
	
	private final FirefighterPro plugin;
	private FileConfiguration configData;
	
	public Config(FirefighterPro pPlugin) {
		plugin = pPlugin;
		
		// Loading (or creating the config file)
		configData = plugin.getConfig();
		if (!new File(plugin.getDataFolder() + File.separator + "config.yml").exists()) {
			generateInitialConfiguration();
		}
	}

	private void generateInitialConfiguration() {
		configData.addDefault("firedepartment.name", "Mine City Volunteer Fire Co.");
		configData.addDefault("firedepartment.location", plugin.getServer().getWorlds().get(0).getSpawnLocation());
		configData.addDefault("firedepartment.economy.singleMissionCompensation", 25.0);
		configData.addDefault("firedepartment.economy.salary.firefighters", 1800.0);
		configData.addDefault("firedepartment.economy.salary.dispatchers", 1800.0);
		
			ArrayList<String> equipmentItems = new ArrayList<String>(3);
			equipmentItems.add(Material.LADDER + " 16");
			equipmentItems.add(Material.WATER_BUCKET + " 5");
			equipmentItems.add(Material.IRON_AXE + " 1");
		configData.addDefault("firedepartment.equipment", equipmentItems);
		configData.addDefault("firedepartment.signs.fontcolor", '4');
		configData.addDefault("firedepartment.personnel.members", new ArrayList<String>());
		configData.addDefault("firedepartment.personnel.dispatchers", new ArrayList<String>());
		configData.addDefault("firedepartment.dispatch.autoDispatch", true);
		
		configData.addDefault("firedepartment.units.fire-station1.name", "Station 1");
		configData.addDefault("firedepartment.units.fire-station1.members", new ArrayList<String>());
		configData.addDefault("firedepartment.units.ems-central.name", "EMS Station Central");
		configData.addDefault("firedepartment.units.ems-central.members", new ArrayList<String>());
		
		configData.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public double getSingleMissionCompensation() {
		return configData.getDouble("firedepartment.economy.singleMissionCompensation");
	}
	
	public double getSalaryFirefighters() {
		return configData.getDouble("firedepartment.economy.salary.firefighters");
	}
	
	public double getSalaryDispatchers() {
		return configData.getDouble("firedepartment.economy.salary.dispatchers");
	}
	
	public ChatColor getSignFontColor() {
		return ChatColor.getByChar(((String) configData.get("firedepartment.signs.fontcolor")).toCharArray()[0]);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ItemStack> getEquipment() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for(String itemString : (ArrayList<String>) configData.get("firedepartment.equipment")) {
			items.add(new ItemStack(Material.getMaterial(itemString.split(" ")[0]), Integer.parseInt(itemString.split(" ")[1])));
		}
		return items;
	}
	
	public void setAutodispatch(boolean autodispatch) {
		configData.set("firedepartment.dispatch.autoDispatch", autodispatch);
		plugin.saveConfig();
	}
	
	public boolean getAutodispatch() {
		return configData.getBoolean("firedepartment.dispatch.autoDispatch");
	}
	
	public void setStationLocation(Location loc) {
		configData.set("firedepartment.location", loc);
		plugin.saveConfig();
	}
	
	public Location getStationLocation() {
		return (Location) configData.get("firedepartment.location");
	}
	
	@SuppressWarnings("unchecked")
	public List<Player> getDispatchers() {
		List<Player> result = new ArrayList<Player>();
		ArrayList<String> dispatcherIDs = (ArrayList<String>) configData.get("firedepartment.personnel.dispatchers");
		if (dispatcherIDs != null) {
			for (String id : dispatcherIDs) {
				result.add(plugin.getServer().getPlayer(UUID.fromString(id)));
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getDispatchersUUIDStrings() {
		return (ArrayList<String>) configData.get("firedepartment.personnel.dispatchers");
	}
	
	@SuppressWarnings("unchecked")
	public List<Player> getFirefighters() {
		List<Player> result = new ArrayList<Player>();
		ArrayList<String> firefighterIDs = (ArrayList<String>) configData.get("firedepartment.personnel.members");
		if (firefighterIDs != null) {
			for (String id : firefighterIDs) {
				result.add(plugin.getServer().getPlayer(UUID.fromString(id)));
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getFirefightersUUIDStrings() {
		return (ArrayList<String>) configData.get("firedepartment.personnel.members");
	}
	
	@SuppressWarnings("unchecked")
	public List<Player> getFirefightersInUnit(String unitName) {
		List<Player> result = new ArrayList<Player>();
		ArrayList<String> firefighterIDs = (ArrayList<String>) configData.get("firedepartment.units." + unitName + ".members");
		if (firefighterIDs != null) {
			for (String id : firefighterIDs) {
				result.add(plugin.getServer().getPlayer(UUID.fromString(id)));
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getFirefightersInUnitUUIDStrings(String unitName) {
		return (ArrayList<String>) configData.get("firedepartment.units." + unitName + ".members");
	}
	
	public void removeFirefighter(Player p) {
		List<String> ffs = getFirefightersUUIDStrings();
		for(String id : ffs) {
			if(UUID.fromString(id).equals(p.getUniqueId())) {
				ffs.remove(id);
				configData.set("firedepartment.personnel.members", ffs);
				plugin.saveConfig();
				return;
			}
		}
	}
	
	public void addFirefighter(Player p) {
		List<String> ffs = getFirefightersUUIDStrings();
		ffs.add(p.getUniqueId().toString());
		configData.set("firedepartment.personnel.members", ffs);
		plugin.saveConfig();
	}
	
	public void removeDispatcher(Player p) {
		List<String> dps = getDispatchersUUIDStrings();
		for(String id : dps) {
			if(UUID.fromString(id).equals(p.getUniqueId())) {
				dps.remove(id);
				configData.set("firedepartment.personnel.dispatchers", dps);
				plugin.saveConfig();
				return;
			}
		}
	}
	
	public void addDispatcher(Player p) {
		List<String> dps = getDispatchersUUIDStrings();
		dps.add(p.getUniqueId().toString());
		configData.set("firedepartment.personnel.dispatchers", dps);
		plugin.saveConfig();
	}
	
	public boolean isFirefighter(Player p) {
		for(Player c : getFirefighters()) {
			if(c.getUniqueId().equals(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDispatcher(Player p) {
		for(Player c : getDispatchers()) {
			if(c.getUniqueId().equals(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isMemberOfUnit(Player p, String unitName) {
		for(Player c : getFirefightersInUnit(unitName)) {
			if(c.getUniqueId().equals(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	
	public void removeFromUnit(Player p, String unitName) {
		List<String> ffs = getFirefightersInUnitUUIDStrings(unitName);
		for(String id : ffs) {
			if(UUID.fromString(id).equals(p.getUniqueId())) {
				ffs.remove(id);
				configData.set("firedepartment.units." + unitName + ".members", ffs);
				plugin.saveConfig();
				return;
			}
		}
	}
	
	public void addToUnit(Player p, String unitName) {
		List<String> ffs = getFirefightersInUnitUUIDStrings(unitName);
		if (ffs != null) {
			ffs.add(p.getUniqueId().toString());
			configData.set("firedepartment.units." + unitName + ".members", ffs);
			plugin.saveConfig();
		}
	}
	
	public void addUnit(String unitName, String unitDisplayName) {
		configData.set("firedepartment.units." + unitName + ".name", unitDisplayName);
		configData.set("firedepartment.units." + unitName + ".members", new ArrayList<Player>());
		plugin.saveConfig();
	}
	
	public String getUnitDisplayName(String unitName) {
		return configData.getString("firedepartment.units." + unitName + ".name", null);
	}
	
	public void removeUnit(String unitName) {
		configData.set("firedepartment.units." + unitName + ".name", null);
		configData.set("firedepartment.units." + unitName + ".members", null);
		configData.set("firedepartment.units." + unitName, null);
		plugin.saveConfig();
	}	
	
	public boolean unitExist(String unitName) {
		return configData.get("firedepartment.units." + unitName + ".members") != null;
	}
	
}