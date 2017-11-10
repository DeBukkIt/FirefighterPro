package com.blogspot.debukkitsblog.firefighterpro.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.blogspot.debukkitsblog.firefighterpro.Mission;
import com.blogspot.debukkitsblog.firefighterpro.util.Messages;

public class UIManager {
	
	public static Scoreboard getScoreboard(Mission mission) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective("aaa", "bbb");
		
		obj.setDisplayName(Messages.UI_HEADLINE.getMessage());
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		List<Score> scores = new ArrayList<Score>();
		
		scores.add(obj.getScore(Messages.UI_CALLING_CIVILIAN + ":"));
		scores.add(obj.getScore(ChatColor.GREEN + " " + mission.getCallingCivilian().getDisplayName()));
		scores.add(obj.getScore(Messages.UI_DISPATCHER + ":"));
		scores.add(obj.getScore(ChatColor.YELLOW + " " + ((mission.getDispatcher() != null) ? mission.getDispatcher().getDisplayName() : "System")));
		scores.add(obj.getScore(Messages.UI_UNITS_DISPATCHED + ":"));
		for(String cUnitName : mission.getUnitsInMission()) {
			scores.add(obj.getScore(ChatColor.RED + " " + cUnitName));
		}
		scores.add(obj.getScore(Messages.UI_FIREFIGHTERS_RESPONDED + ":"));
		for(Player cPlayer : mission.getFirefighters()) {
			scores.add(obj.getScore(ChatColor.RED + " " + cPlayer.getDisplayName()));
		}
		
		for(int i = 0; i < scores.size(); i++) {
			scores.get(i).setScore(scores.size() - i);
		}
		
		return sb;
	}
	
}