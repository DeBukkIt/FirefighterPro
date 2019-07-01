package com.blogspot.debukkitsblog.firefighterpro.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.blogspot.debukkitsblog.firefighterpro.FirefighterPro;

public class SignEventHandler implements Listener {
	
	private HashMap<String, String> signCommands;
	
	private Block lastInteractRedstoneBlock;
	private Player lastInteractRedstonePlayer;
	private List<Block> checkedRedstoneBlocks = new ArrayList<Block>();
	
	public SignEventHandler() {
		signCommands = new HashMap<String, String>();
		signCommands.put("[Alarm]", "alarm");
		signCommands.put("[FF Equip]", "ff equip");
		signCommands.put("[FF Respond]", "ff respond");
		signCommands.put("[FF Dispatch]", "ffdispatch");
		signCommands.put("[FF Autodispatch]", "ffmanage setAutodispatch");
	}
	
	@EventHandler
	public void onSignChanged(SignChangeEvent event) {
		
		// check first line for all possible commands, replace with color if matches
		for (String commandName : signCommands.keySet()) {
			if (event.getLine(0).equalsIgnoreCase(commandName)) {
				event.setLine(0, (FirefighterPro.getInstance().getFFConfig().getSignFontColor() != null ? FirefighterPro.getInstance().getFFConfig().getSignFontColor() : ChatColor.DARK_RED) + commandName);
				break;
			}
		}
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onAnyBlockClicked(PlayerInteractEvent event) {
		// save block and player for later processing by findRedstoneSourcePlayer method
		lastInteractRedstoneBlock = event.getClickedBlock();
		lastInteractRedstonePlayer = event.getPlayer();
		
		// TODO Debug output
		System.out.println("player :: " + lastInteractRedstonePlayer);
		System.out.println("blockData :: " + lastInteractRedstoneBlock.getBlockData().toString());
		System.out.println("powerable? :: " + (lastInteractRedstoneBlock.getBlockData() instanceof Powerable));
	}
	
	@EventHandler
	public void onSignClicked(PlayerInteractEvent event) {
				
		// is the block a sign at all?
		if (isSign(event.getClickedBlock())) {
			Sign sign = (Sign) event.getClickedBlock().getState();
			ChatColor signFontColor = (FirefighterPro.getInstance().getFFConfig().getSignFontColor() != null ? FirefighterPro.getInstance().getFFConfig().getSignFontColor() : ChatColor.DARK_RED);
			
			// is the sign a FirefighterPro command sign?
			if(isCommandSign(sign)) {
				// extract command name
				String commandName = sign.getLine(0).replace(signFontColor.toString(), "");
				// collect args from other three lines
				String args = "";
				for (int i = 1; i < 4; i++) {
					if (!sign.getLine(i).isEmpty()) {
						args += sign.getLine(i) + " ";
					}
				}
				// dispatch the command (with args if any)
				if(event.getPlayer() != null) {
					Bukkit.getServer().dispatchCommand(event.getPlayer(), (signCommands.get(commandName) + " " + args).trim());
				} else {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), (signCommands.get(commandName) + " " + args).trim());
				}
			}
		}
		
	}
	
	@EventHandler
	public void onSignRedstone(BlockRedstoneEvent event) {
		
		// get the Redstone-powered block
		Block block = event.getBlock();
		int blockX = block.getX();
		int blockY = block.getY();
		int blockZ = block.getZ();
		
		// examine its environment
		for(int x = (blockX-1); x <= (blockX+1); x++) {
			for(int y = (blockY-1); y <= (blockY+1); y++) {
				for(int z = (blockZ-1); z <= (blockZ+1); z++) {					
					// get every sign in the nearest environment
					Block target = block.getWorld().getBlockAt(x, y, z);					
					// if its a FirefighterPro command sign, dispatch its command
					if(isSign(target) && isCommandSign((Sign) target.getState()) && event.getNewCurrent() > event.getOldCurrent()) {
						onSignClicked(new PlayerInteractEvent(findRedstoneSourcePlayer(target), null, null, target, null));
					}
					
				}
			}
		}
		
	}
	
	private Player findRedstoneSourcePlayer(Block block) {
		// add block to list of checked blocks
		checkedRedstoneBlocks.add(block);
		
		// if this block equals the last redstone block somebody interacted on...
		if(block.getLocation().equals(lastInteractRedstoneBlock.getLocation())) {
			// reset the list of checked blocks for the next time
			checkedRedstoneBlocks = new ArrayList<Block>();
			// return the player who interacted on this block
			return lastInteractRedstonePlayer;
		}
		
		// Locate the block
		int blockX = block.getX();
		int blockY = block.getY();
		int blockZ = block.getZ();
		
		// search its environment
		for(int x = (blockX-2); x <= (blockX+2); x++) {
			for(int y = (blockY-2); y <= (blockY+2); y++) {
				for(int z = (blockZ-2); z <= (blockZ+2); z++) {
					
					Block target = block.getWorld().getBlockAt(x, y, z);
					// recursive search for the next redstone block between target and firing player
					if(isPowerableBlock(target) && !target.equals(block) && !checkedRedstoneBlocks.contains(target)) {
						Player result = findRedstoneSourcePlayer(target);
						if(result != null)
							return result; // TODO Repair this method often returning null when it should not
					}
					
				}
			}
		}
		
		return null;
	}
	
	private boolean isPowerableBlock(Block block) {
		return block.getBlockData() instanceof Powerable;
	}
	
	private boolean isSign(Block block) {
		return (block != null && block.getState() != null && block.getState() instanceof Sign);
	}
	
	private boolean isCommandSign(Sign sign) {
		ChatColor signFontColor = (FirefighterPro.getInstance().getFFConfig().getSignFontColor() != null ? FirefighterPro.getInstance().getFFConfig().getSignFontColor() : ChatColor.DARK_RED);
		for (String commandName : signCommands.keySet()) {
			if (sign.getLine(0).equalsIgnoreCase(signFontColor + commandName)) {
				return true;
			}
		}
		return false;
	}

}