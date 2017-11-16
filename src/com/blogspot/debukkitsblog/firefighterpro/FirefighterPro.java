package com.blogspot.debukkitsblog.firefighterpro;

import org.bukkit.plugin.java.JavaPlugin;

import com.blogspot.debukkitsblog.firefighterpro.commands.CommandAlarm;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandDispatch;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandFF;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandInsurance;
import com.blogspot.debukkitsblog.firefighterpro.commands.CommandManage;
import com.blogspot.debukkitsblog.firefighterpro.economy.Economy;
import com.blogspot.debukkitsblog.firefighterpro.economy.Insurance;
import com.blogspot.debukkitsblog.firefighterpro.events.JoinEventHandler;
import com.blogspot.debukkitsblog.firefighterpro.events.SignEventHandler;
import com.blogspot.debukkitsblog.firefighterpro.worldguard.WorldGuardHandler;

public class FirefighterPro extends JavaPlugin {
	
	private static FirefighterPro instance;
	
	private Mission currentMission;
	private Config config;
	private Broadcaster broadcaster;
	
	private WorldGuardHandler worldGuardHandler;
    private Economy econ;
    private Insurance insurance;
	
	@Override
	public void onEnable() {
		super.onEnable();
		instance = this;
		
		currentMission = null;
		config = new Config();
		broadcaster = new Broadcaster();
		Messages.initMessages();
		
		registerEventListeners();
		registerCommandExecutors();
		
		try {
			worldGuardHandler = new WorldGuardHandler();
		} catch(NoClassDefFoundError e) {
			System.out.println("WorldGuard not found, working without WorldGuard support.");
		}
		
		if(setupEconomy()) {
			insurance = new Insurance();
		} else {
			System.out.println("Could not enable economy system, fire insurance and payment disabled.");
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	private void registerEventListeners() {
		getServer().getPluginManager().registerEvents(new JoinEventHandler(), this);
		getServer().getPluginManager().registerEvents(new SignEventHandler(), this);
	}
	
	private void registerCommandExecutors() {
		getCommand("alarm").setExecutor(new CommandAlarm());
		getCommand("ff").setExecutor(new CommandFF());
		getCommand("ffdispatch").setExecutor(new CommandDispatch());
		getCommand("ffmanage").setExecutor(new CommandManage());
		getCommand("ffinsurance").setExecutor(new CommandInsurance());
	}
	
	private boolean setupEconomy() {
        econ = new Economy();
        return econ.isAvailable();
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
	
	public Insurance getInsurance() {
		return insurance;
	}
	
	public boolean isEconomySupported() {
		return econ.isAvailable();
	}
	
	public Economy getEconomy() {
		return econ;
	}
	
	public boolean isWorldGuardSupported() {
		return worldGuardHandler != null;
	}
	
	public WorldGuardHandler getWorldGuardHandler() {
		return worldGuardHandler;
	}
	
	public static FirefighterPro getInstance() {
		return instance;
	}
	
}