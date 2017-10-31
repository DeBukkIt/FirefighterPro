package com.blospot.debukkitsblog.firefighterpro.ui;

import org.bukkit.scoreboard.Scoreboard;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;
import com.blogspot.debukkitsblog.firefighterpro.Mission;

public class UIManager {

	private FirefighterPro plugin;
	
	public UIManager(FirefighterPro plugin) {
		this.plugin = plugin;
	}
	
	public Scoreboard getScoreboardForMission(Mission mission) {
		// TODO Implement UI with mission overview using scoreboard
		return null;
	}
	
}
