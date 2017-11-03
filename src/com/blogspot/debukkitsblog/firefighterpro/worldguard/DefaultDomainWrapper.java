package com.blogspot.debukkitsblog.firefighterpro.worldguard;

import java.util.Set;
import java.util.UUID;

import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.domains.GroupDomain;
import com.sk89q.worldguard.domains.PlayerDomain;

public class DefaultDomainWrapper {
	
	private GroupDomain groupDomain;
	private Set<String> groups;
	private PlayerDomain playerDomain;
	private Set<String> players;
	private Set<UUID> uniqueIDs;
	
	public DefaultDomainWrapper(DefaultDomain original) {
		this.groupDomain = original.getGroupDomain();
		this.groups = original.getGroups();
		this.playerDomain = original.getPlayerDomain();
		this.players = original.getPlayers();
		this.uniqueIDs = original.getUniqueIds();
	}

	public GroupDomain getGroupDomain() {
		return groupDomain;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public PlayerDomain getPlayerDomain() {
		return playerDomain;
	}

	public Set<String> getPlayers() {
		return players;
	}

	public Set<UUID> getUniqueIDs() {
		return uniqueIDs;
	}

}
