package com.blogspot.debukkitsblog.firefighterpro.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.util.Messages;

public class JoinEventHandler implements Listener {
	
	private final FirefighterPro plugin;
	
	public JoinEventHandler(FirefighterPro plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// withdraw insurance installment if player is insured
		if(plugin.isEconomySupported() && plugin.getInsurance().isInsured(event.getPlayer())) {
			plugin.getInsurance().toCustomer(event.getPlayer()).payInstallment();
			event.getPlayer().sendMessage(Messages.format(Messages.INSURANCE_PAYED));
		}
	}

}
