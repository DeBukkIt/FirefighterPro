package com.blogspot.debukkitsblog.firefighterpro;

import org.bukkit.plugin.java.JavaPlugin;

import com.blogspot.debukkitsblog.firefighterpro.commands.CommandAlarm;

public class FirefighterPro extends JavaPlugin {
	
	private Mission currentMission;
	private Config config;
	
	@Override
	public void onEnable() {
		super.onEnable();
		currentMission = null;
		config = new Config(this, "config.yml");
		Messages.initMessages();
		
		setCommandExecutors();		
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	private void setCommandExecutors() {
		getCommand("alarm").setExecutor(new CommandAlarm(this));
	}
	
	public Config getFFConfig() {
		return this.config;
	}

	public Mission getCurrentMission() {
		return currentMission;
	}

	public void setCurrentMission(Mission currentMission) {
		this.currentMission = currentMission;
	}
	
}