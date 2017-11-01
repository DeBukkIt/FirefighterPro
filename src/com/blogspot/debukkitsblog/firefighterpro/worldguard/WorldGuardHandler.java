package com.blogspot.debukkitsblog.firefighterpro.worldguard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardHandler {
	
	private final FirefighterPro plugin;
	private Plugin worldguard;
	
	public WorldGuardHandler(FirefighterPro plugin) {
		this.plugin = plugin;
		try {
			worldguard = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
		} catch(NoClassDefFoundError ex) {
			System.err.println("WorldGuard not found, working without WorldGuard support.");
		}		
	}
	
	public WorldGuardPlugin getWorldGuard() {
		if(worldguard != null && worldguard instanceof WorldGuardPlugin) {
			return (WorldGuardPlugin) worldguard;
		}
		return null;
	}
	
	public void setAllowBuild(Location loc, List<Player> players) {
		List<ProtectedRegion> regions = getRegionListAt(loc);
		for(ProtectedRegion region : regions) {
			// TODO Weitermachen
//			region.setFlag(DefaultFlag.BUILD.);
		}
	}
	
	private List<ProtectedRegion> getRegionListAt(Location loc) {
		List<ProtectedRegion> result = new ArrayList<ProtectedRegion>();
		RegionContainer container = getWorldGuard().getRegionContainer();
		RegionManager regions = container.get(loc.getWorld());
		if(regions != null) {
			ApplicableRegionSet set = regions.getApplicableRegions(loc);
			for(ProtectedRegion region : set) {
				result.add(region);
			}
		}
		return result;
	}
	
	public boolean canBuild(Player player, Location loc) {
		return getWorldGuard().canBuild(player, loc);
	}

}
