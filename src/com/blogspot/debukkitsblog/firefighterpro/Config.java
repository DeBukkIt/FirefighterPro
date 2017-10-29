package com.blogspot.debukkitsblog.firefighterpro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Config {
	
	private final FirefighterPro plugin;
	private FileConfiguration configData;
	
	public Config(FirefighterPro pPlugin, String configFilename) {
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
		
		ArrayList<String> equipmentItems = new ArrayList<String>(3);
		equipmentItems.add("65 16");
		equipmentItems.add("326 5");
		equipmentItems.add("258 1");
		configData.addDefault("firedepartment.equipment", equipmentItems);
		configData.addDefault("firedepartment.personnel.members", new ArrayList<String>());
		configData.addDefault("firedepartment.personnel.dispatchers", new ArrayList<String>());
		configData.addDefault("firedepartment.dispatch.autoDispatch", false);
		
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("3c331cf1-d8f1-417f-b3b3-0b7bc2f9b2c0");
		configData.addDefault("firedepartment.units.station1.members", ids);
		configData.options().copyDefaults(true);
		plugin.saveConfig();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ItemStack> getEquipment() {
		return (ArrayList<ItemStack>) configData.get("firedepartment.equipment");
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
	
	public boolean unitExist(String unitName) {
		return configData.get("firedepartment.units." + unitName + ".members") != null;
	}
	
}