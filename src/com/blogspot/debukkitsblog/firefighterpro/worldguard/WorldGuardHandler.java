package com.blogspot.debukkitsblog.firefighterpro.worldguard;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Mission;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardHandler {
	
	private Plugin worldguard;
	
	public WorldGuardHandler(FirefighterPro plugin) {
		try {
			worldguard = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
		} catch(NoClassDefFoundError ex) {
			System.err.println("WorldGuard not found, working without WorldGuard support.");
		}		
	}
	
	public boolean isAvailable() {
		return worldguard != null;
	}
	
	private WorldGuardPlugin getWorldGuard() {
		if(worldguard != null && worldguard instanceof WorldGuardPlugin) {
			return (WorldGuardPlugin) worldguard;
		}
		return null;
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
	
	public ProtectedRegion getLowestLevelRegion(Location loc) {
		ProtectedRegion region;
		try {
			region = getRegionListAt(loc).get(0);
		} catch(Exception e) {
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
		List<Player> players = mission.getFirefighters();
		DefaultDomain members = region.getMembers();
		
		mission.setRegionOldMembers(members);
		
		for(Player player : players) {
			System.out.println("Adding " + player.getDisplayName() + " to member list.");
			members.addPlayer(getWorldGuard().wrapPlayer(player));
		}
		System.out.println("Setting member list (size=" + members.size() + ") to region " + region.getId());
		region.setMembers(members);
	}
	
	public void setOldBuildPermissions(Location loc, Mission mission) {
		ProtectedRegion region = mission.getRegion();
		if(mission.getRegionOldMembers() == null) {
			region.setMembers(new DefaultDomain());
		} else {
			// TODO fix totally weird NullPointerException
			System.out.println("Setting old build permissions...");
			System.out.println("** FOR INFORMATION **");
			System.out.println("is region null? :: " + region == null);
			System.out.println("is mission null? :: " + mission == null);
			System.out.println("is  mission.getRegionOldMembers() null? :: " + mission.getRegionOldMembers() == null);
			System.out.println("** INFORMATION END **");
			if(region == null || mission == null || mission.getRegionOldMembers() == null) {
				System.out.println("Could not set old build permissions.");
			} else {
				region.setMembers(mission.getRegionOldMembers());
				System.out.println("Old build permissions set.");
			}
		}
	}

}
