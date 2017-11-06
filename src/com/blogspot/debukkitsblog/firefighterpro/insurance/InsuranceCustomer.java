package com.blogspot.debukkitsblog.firefighterpro.insurance;

import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;

public class InsuranceCustomer {
	
	private FirefighterPro plugin;
	
	private Player player; // the insured player
	private int installment; // the amount to be paid regularly
	private int sumInsured; // the sum that is paid out in the event of damage.
	private int dayInterval; // the interval of payments
	private long nextPayday; // the next payday in milliseconds of UNIX time

	public InsuranceCustomer(Player player, int installment, int sumInsured, int dayInterval, FirefighterPro plugin) {
		this.player = player;
		this.installment = installment;
		this.sumInsured = sumInsured;
		this.dayInterval = dayInterval;
		nextPayday = System.currentTimeMillis() + daysToMillis(dayInterval);
	}
	
	public void sendInsuranceInformation() {
		player.sendMessage(Messages.format(Messages.INSURANCE_INFORMATION.getMessage()
				.replaceAll("%ai", "" + installment)
				.replaceAll("%di", "" + dayInterval)
				.replaceAll("%as", "" + sumInsured)
				.replaceAll("%dr", "" + getDaysToNextPayday())
		));
	}
	
	public int getDaysToNextPayday() {
		return millisToDays(nextPayday - System.currentTimeMillis());
	}
	
	public boolean hasToPay() {
		return System.currentTimeMillis() > nextPayday;
	}
	
	public void payInstallment() {
		if(plugin.isEconomySupported() && getDaysToNextPayday() >= 0) {
			plugin.getEconomy().withdrawPlayer(player, installment);
			nextPayday += daysToMillis(dayInterval);
		}
	}
	
	public void payoffSumInsured() {
		if(plugin.isEconomySupported()) {
			plugin.getEconomy().depositPlayer(player, sumInsured);
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getInstallment() {
		return this.getInstallment();
	}
	
	public int getSumInsured() {
		return sumInsured;
	}

	public long getNextPayday() {
		return nextPayday;
	}

	private long daysToMillis(int d) {
		return d * 24 * 60 * 60 * 1000;
	}
	
	private int millisToDays(long ms) {
		return (int) ((((ms/1000L)/60L)/60L)/24L);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof InsuranceCustomer) {
			((InsuranceCustomer) obj).getPlayer().getUniqueId().equals(this.getPlayer().getUniqueId());
		}
		return false;
	}
	
}
