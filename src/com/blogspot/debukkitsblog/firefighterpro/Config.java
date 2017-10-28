package com.blogspot.debukkitsblog.firefighterpro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
		configData.addDefault("firedepartment.personnel.members", new ArrayList<String>());
		configData.addDefault("firedepartment.personnel.dispatchers", new ArrayList<String>());
		configData.addDefault("firedepartment.dispatch.autoDispatch", false);
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("3c331cf1-d8f1-417f-b3b3-0b7bc2f9b2c0");
		configData.addDefault("firedepartment.units.station1.members", ids);
		configData.options().copyDefaults(true);
		plugin.saveConfig();
	}

	public boolean getAutodispatch() {
		return configData.getBoolean("firedepartment.dispatch.autoDispatch");
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