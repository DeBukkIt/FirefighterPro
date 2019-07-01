package com.blogspot.debukkitsblog.firefighterpro.worldguard;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Messages;
import com.blogspot.debukkitsblog.firefighterpro.Mission;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class WorldGuardHandler {

	private Plugin worldguard;

	public WorldGuardHandler() {
		try {
			worldguard = FirefighterPro.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
		} catch (NoClassDefFoundError ex) {
			System.err.println("WorldGuard not found, working without WorldGuard support.");
		}
	}

	private WorldGuardPlugin getWorldGuard() {
		if (worldguard != null && worldguard instanceof WorldGuardPlugin) {
			return (WorldGuardPlugin) worldguard;
		}
		return null;
	}

	private List<ProtectedRegion> getRegionListAt(Location loc) {
		List<ProtectedRegion> result = new ArrayList<ProtectedRegion>();
		//TODO Replace deprecated methods
//		RegionContainer container = getWorldGuard().getRegionContainer();
//		RegionManager regions = container.get(loc.getWorld());
//		if (regions != null) {
//			ApplicableRegionSet set = regions.getApplicableRegions(loc);
//			for (ProtectedRegion region : set) {
//				result.add(region);
//			}
//		}
		return result;
	}

	public ProtectedRegion getLowestLevelRegion(Location loc) {
		ProtectedRegion region;
		try {
			region = getRegionListAt(loc).get(0);
		} catch (Exception e) {
			return null;
		}

		if (region == null || region.getParent() == null) {
			return region;
		}

		List<ProtectedRegion> inheritance = new ArrayList<ProtectedRegion>();

		ProtectedRegion r = region;
		inheritance.add(r);
		while (r.getParent() != null) {
			r = r.getParent();
			inheritance.add(r);
		}

		ListIterator<ProtectedRegion> it = inheritance.listIterator(inheritance.size());

		ProtectedRegion cur = null;
		while (it.hasPrevious()) {
			cur = it.previous();
		}

		return cur;
	}

	public void setAllowBuild(Location loc, Mission mission) {
		ProtectedRegion region = getLowestLevelRegion(loc);
		if (region == null) {
			return;
		}
		List<Player> allFirefighters = FirefighterPro.getInstance().getFFConfig().getFirefighters();
		DefaultDomain regionMembers = region.getMembers();

		mission.setRegion(region);
		mission.setRegionOldMembers(new PlayerDomainWrapper(regionMembers.getPlayerDomain()));

		for (Player currentFirefighter : allFirefighters) {
			regionMembers.addPlayer(getWorldGuard().wrapPlayer(currentFirefighter));
		}
		region.setMembers(regionMembers);

		saveRegionChanges(loc.getWorld());
		System.out.println("All firefighters are members of region " + region.getId() + " for the running mission.");
	}

	public void restorePreviousBuildPermissions(Location loc, Mission mission) {
		ProtectedRegion region = mission.getRegion();
		if (region == null) {
			return;
		}
		if (mission.getRegionOldMembers() != null) {
			region.getMembers().setPlayerDomain(mission.getRegionOldMembers().get());
			saveRegionChanges(loc.getWorld());
		}
		System.out.println("Members of region " + region.getId() + " reset after mission ended.");
	}

	private void saveRegionChanges(World world) {
		try {
			// TODO Replace deprecated methods
			throw new StorageException();
//			getWorldGuard().getRegionManager(world).saveChanges();
		} catch (StorageException e) {
			System.err.println(Messages.format("WorldGuard could not save region changes"));
		}
	}

}
