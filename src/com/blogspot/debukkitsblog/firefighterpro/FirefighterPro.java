package com.blogspot.debukkitsblog.firefighterpro;

import org.bukkit.plugin.java.JavaPlugin;

import com.blogspot.debukkitsblog.firefighterpro.commands.CommandAlarm;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandDispatch;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandFF;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandManage;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandDebug;
import com.blogspot.debukkitsblog.firefighterpro.events.SignEventHandler;
import com.blogspot.debukkitsblog.firefighterpro.worldguard.WorldGuardHandler;

public class FirefighterPro extends JavaPlugin {
	
	private Mission currentMission;
	private Config config;
	private Broadcaster broadcaster;
	
	private WorldGuardHandler worldGuardHandler;
	
	@Override
	public void onEnable() {
		super.onEnable();
		currentMission = null;
		config = new Config(this, "config.yml");
		broadcaster = new Broadcaster(this);
		Messages.initMessages();
		
		getServer().getPluginManager().registerEvents(new SignEventHandler(this), this);
		registerCommandExecutors();
		
		try {
			worldGuardHandler = new WorldGuardHandler(this);
		} catch(NoClassDefFoundError e) {
			System.out.println(Messages.format("WorldGuard not found, working without WorldGuard support."));
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	private void registerCommandExecutors() {
		getCommand("alarm").setExecutor(new CommandAlarm(this));
		getCommand("ff").setExecutor(new CommandFF(this));
		getCommand("ffdispatch").setExecutor(new CommandDispatch(this));
		getCommand("ffmanage").setExecutor(new CommandManage(this));
		getCommand("ffdebug").setExecutor(new CommandDebug(this));
	}
	
	public Broadcaster getBroadcaster() {
		return broadcaster;
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
	
	public boolean isWorldGuardSupported() {
		return worldGuardHandler != null;
	}
	
	public WorldGuardHandler getWorldGuardHandler() {
		return worldGuardHandler;
	}
	
}