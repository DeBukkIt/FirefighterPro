package com.blospot.debukkitsblog.firefighterpro.ui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.blogspot.debukkitsblog.firefighterpro.Mission;

public class UIManager {
	
	public static Scoreboard getScoreboard(Mission mission) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard sb = manager.getNewScoreboard();
		
		Team firefighters = sb.registerNewTeam("Firefighters");
		List<Player> firefightersInMission = mission.getFirefighters();
		for(Player firefighter : firefightersInMission) {
			firefighters.addEntry(firefighter.getDisplayName());
		}
		
		Objective obj = sb.registerNewObjective("Objective", "dummy");
		obj.setDisplayName("Objectives");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		return sb;
	}
	
}
