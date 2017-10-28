package com.debukkitsblog.blogspot.firefighterpro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Config {
	
	private final FirefighterPro plugin;
	private File configFile;
	private YamlConfiguration configData;
	
	public Config(FirefighterPro pPlugin, String configFilename) {
		plugin = pPlugin;
		try {
			// Loading (or creating the config file)
			configFile = new File(plugin.getDataFolder(), configFilename);
			
			// Generate a brand new file
			if(!configFile.exists()) {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				configData = YamlConfiguration.loadConfiguration(configFile);
				
				generateInitialConfiguration();
				
			} else {
				// or: Reading an existing config file
				configData = YamlConfiguration.loadConfiguration(configFile);
			}
			
		} catch (IOException e) {
			System.err.println("Could not create or load config file!");
			e.printStackTrace();
		}
	}

	private void generateInitialConfiguration() {
		configData.addDefault("firedepartment.name", "Mine City Volunteer Fire Co.");
		configData.addDefault("firedepartment.location", plugin.getServer().getWorlds().get(0).getSpawnLocation());
		configData.addDefault("firedepartment.personnel.members", new UUID[0]);
		configData.addDefault("firedepartment.personnel.dispatchers", new UUID[0]);
		configData.addDefault("firedepartment.units.station1.members", new UUID[0]);
		saveConfig();
	}
	
	private void saveConfig() {
		try {
			configData.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Player> getDispatchers() {
		List<Player> result = new ArrayList<Player>();
		UUID[] dispatcherIDs = (UUID[]) configData.get("firedepartment.personnel.dispatchers");
		for(UUID id : dispatcherIDs) {
			result.add(plugin.getServer().getPlayer(id));
		}
		return result;
	}
	
}