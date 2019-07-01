package com.blogspot.debukkitsblog.firefighterpro.worldguard;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.sk89q.worldguard.domains.PlayerDomain;

public class PlayerDomainWrapper {
	
	private Set<UUID> uniqueIDs;
	
	public PlayerDomainWrapper(PlayerDomain original) {
		uniqueIDs = new HashSet<UUID>();
		for(UUID id : original.getUniqueIds()) {
			uniqueIDs.add(id);
		}
	}

	public PlayerDomain get() {
		PlayerDomain pd = new PlayerDomain();
		for(UUID id : uniqueIDs) {
			pd.addPlayer(id);
		}
		return pd;
	}

}
