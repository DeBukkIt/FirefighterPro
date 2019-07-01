package com.blogspot.debukkitsblog.firefighterpro.economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Economy {
	
	private boolean available;
	private net.milkbowl.vault.economy.Economy econ;
	
	public Economy() {
		try {
			if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
				available = false;
			}
			RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (rsp == null) {
				available = false;
			}
			econ = rsp.getProvider();

			available = (econ != null);
		} catch (NoClassDefFoundError e) {
			System.err.println("[FFPro] Vault not found, working without economy support.");
			econ = null;
			available = false;
		}
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public boolean withdraw(OfflinePlayer player, double value) {
		try {
			econ.withdrawPlayer(player, value);
		} catch(Exception e) {
			System.err.println("Could not withdraw money from a player. Is Vault and another economy plugin installed? Are they compatible with each other and the current Bukkit version and are they up to date?");
			return false;
		}
		return true;
	}
	
	public boolean deposit(OfflinePlayer player, double value) {
		try {
			econ.depositPlayer(player, value);
		} catch(Exception e) {
			System.err.println("Could not deposit money to a player. Is Vault and another economy plugin installed? Are they compatible with each other and the current Bukkit version and are they up to date?");
			return false;
		}
		return true;
	}

}
