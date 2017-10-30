package com.blogspot.debukkitsblog.firefighterpro;

import org.bukkit.plugin.java.JavaPlugin;

import com.blogspot.debukkitsblog.firefighterpro.commands.CommandAlarm;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandDispatch;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandFF;
import com.blogspot.debukkitsblog.firefighterpro.events.FireSignEvent;

public class FirefighterPro extends JavaPlugin {
	
	private Mission currentMission;
	private Config config;
	
	@Override
	public void onEnable() {
		super.onEnable();
		currentMission = null;
		config = new Config(this, "config.yml");
		Messages.initMessages();
		
		getServer().getPluginManager().registerEvents(new FireSignEvent(), this);
		registerCommandExecutors();		
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	private void registerCommandExecutors() {
		getCommand("alarm").setExecutor(new CommandAlarm(this));
		getCommand("ff").setExecutor(new CommandFF(this));
		getCommand("ffdispatch").setExecutor(new CommandDispatch(this));
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