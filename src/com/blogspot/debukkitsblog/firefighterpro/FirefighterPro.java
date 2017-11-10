package com.blogspot.debukkitsblog.firefighterpro;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.blogspot.debukkitsblog.firefighterpro.commands.*;
import com.blogspot.debukkitsblog.firefighterpro.events.SignEventHandler;
import com.blogspot.debukkitsblog.firefighterpro.insurance.Insurance;
import com.blogspot.debukkitsblog.firefighterpro.util.Broadcaster;
import com.blogspot.debukkitsblog.firefighterpro.util.Config;
import com.blogspot.debukkitsblog.firefighterpro.util.Messages;
import com.blogspot.debukkitsblog.firefighterpro.worldguard.WorldGuardHandler;

import net.milkbowl.vault.economy.Economy;

public class FirefighterPro extends JavaPlugin {
	
	private Mission currentMission;
	private Config config;
	private Broadcaster broadcaster;
	
	private WorldGuardHandler worldGuardHandler;
    private Economy econ;
    private Insurance insurance;
	
	@Override
	public void onEnable() {
		super.onEnable();
		currentMission = null;
		config = new Config(this);
		broadcaster = new Broadcaster(this);
		Messages.initMessages();
		
		getServer().getPluginManager().registerEvents(new SignEventHandler(this), this);
		registerCommandExecutors();
		
		try {
			worldGuardHandler = new WorldGuardHandler(this);
		} catch(NoClassDefFoundError e) {
			System.out.println("WorldGuard not found, working without WorldGuard support.");
		}
		
		setupEconomy();
		if(isEconomySupported()) {
			insurance = new Insurance(this);
		} else {
			System.out.println("Could not enable economy system, fire insurance and payment disabled.");
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
		// TODO Add a command to manage a fire insurance
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        
        return econ != null;
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
		return econ != null;
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
	
}