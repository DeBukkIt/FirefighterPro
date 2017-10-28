package com.debukkitsblog.blogspot.firefighterpro;

import org.bukkit.plugin.java.JavaPlugin;

public class FirefighterPro extends JavaPlugin {
	
	private Config config;
	
	@Override
	public void onEnable() {
		super.onEnable();
		config = new Config(this, "config.yml");
		
		getCommand("alarm").setExecutor(new CommandAlarm(this));
		
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	private void setCommandExecutors() {
		
	}
	
	public Config getFFConfig() {
		return this.config;
	}
	
}
