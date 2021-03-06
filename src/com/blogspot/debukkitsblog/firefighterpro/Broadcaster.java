package com.blogspot.debukkitsblog.firefighterpro;

import java.util.List;

import org.bukkit.entity.Player;

public class Broadcaster {
	
	public int broadcastToDispatchers(String formattedMessage) {
		// send message to all dispatchers
		List<Player> dispatchers = FirefighterPro.getInstance().getFFConfig().getDispatchers();
		int receiverCount = 0;
		if(dispatchers != null) {
			for(Player dispatcher : dispatchers) {
				if(dispatcher.isOnline()) {
					dispatcher.sendMessage(formattedMessage);
					receiverCount++;
				}
			}
		}
		return receiverCount;
	}
	
	public int broadcastToFirefighters(String formattedMessage) {
		// send message to all firefighters
		List<Player> firefighters = FirefighterPro.getInstance().getFFConfig().getFirefighters();
		int receiverCount = 0;
		if (firefighters != null) {
			for (Player firefighter : firefighters) {
				if (firefighter.isOnline()) {
					firefighter.sendMessage(formattedMessage);
					receiverCount++;
				}
			}
		}
		return receiverCount;
	}
	
	public int broadcastToUnit(String unitName, String formattedMessage) {
		// if unit does not exist
		if(!FirefighterPro.getInstance().getFFConfig().unitExist(unitName)) {
			return -1;
		}
		// send message to all members of the unit
		List<Player> firefightersInUnit = FirefighterPro.getInstance().getFFConfig().getFirefightersInUnit(unitName);
		int receiverCount = 0;
		if(firefightersInUnit != null) {
			for(Player firefighterInUnit : firefightersInUnit) {
				if(firefighterInUnit.isOnline()) {
					firefighterInUnit.sendMessage(formattedMessage);
					receiverCount++;
				}
			}
		}
		return receiverCount;
	}	
}
