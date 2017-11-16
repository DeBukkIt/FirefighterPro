package com.blogspot.debukkitsblog.firefighterpro.economy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.util.FileStorage;

public class Insurance {

	private List<InsuranceCustomer> customers;
	private FileStorage db;

	@SuppressWarnings("unchecked")
	public Insurance() {
		try {

			// Load customers from database file
			db = new FileStorage(new File(FirefighterPro.getInstance().getDataFolder().getAbsolutePath() + File.separator + "insurance.dat"));
			if (db.hasKey("customers")) {
				customers = (List<InsuranceCustomer>) db.get("customers");
			} else {
				customers = new ArrayList<InsuranceCustomer>();
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addCustomer(InsuranceCustomer c) {
		customers.add(c);
		save();
	}
	
	public void removeCustomer(InsuranceCustomer c) {
		customers.remove(c);
		save();
	}
	
	public void removeCustomer(Player p) {
		removeCustomer(toCustomer(p));
	}
	
	public boolean isInsured(Player p) {
		for(InsuranceCustomer c : customers) {
			if(c.getPlayer().getUniqueId().equals(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	
	public InsuranceCustomer toCustomer(Player p) {
		for(InsuranceCustomer c : customers) {
			if(c.getPlayer().getUniqueId().equals(p.getUniqueId())) {
				return c;
			}
		}
		return null;
	}
	
	public List<InsuranceCustomer> getCustomers() {
		return customers;
	}
	
	private void save() {
		db.store("customers", customers);
	}
	
}