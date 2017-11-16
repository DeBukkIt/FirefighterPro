package com.blogspot.debukkitsblog.firefighterpro.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.economy.Insurance;

public class JoinEventHandler implements Listener {
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// withdraw insurance installment if player is insured
		// economy supported?
		if(FirefighterPro.getInstance().isEconomySupported()) {
			// get insurance system
			Insurance insurance = FirefighterPro.getInstance().getInsurance();
			// player insured?
			if(insurance.isInsured(event.getPlayer())) {
				// let him pay (and do not ask first, muhaha!)
				insurance.toCustomer(event.getPlayer()).payInstallment();
			}
		}
	}

}
