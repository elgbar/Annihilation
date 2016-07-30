package com.hcs.Listners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;

public class BlockBreakListner implements Listener {
	
    public BlockBreakListner(Plugin p)  {
        Bukkit.getPluginManager().registerEvents(this,p);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
    	if (!event.isCancelled()){
    		Player p = event.getPlayer();
    		if (p.getGameMode() != GameVars.getDefaultGamemode()) {
    			return;
    		}
    		Material bb = event.getBlock().getType(); //bb = broken block
        	String bbName = bb.name();
        	
        	//Can be broken with anything
    		if (bb.equals(Material.SNOW) ||
    				bb.equals(Material.GLOWSTONE) ||
    				
    				//plants & flowers
    				bb.equals(Material.LONG_GRASS) ||
    				bb.equals(Material.DOUBLE_PLANT) ||
    				bb.equals(Material.YELLOW_FLOWER) ||
    				bb.equals(Material.RED_ROSE) ||
    				
    				//edible stuff
    				bb.equals(Material.CROPS) ||
    				bb.equals(Material.COCOA) ||
    				bb.equals(Material.NETHER_WARTS) ||
    				bb.equals(Material.CARROT) ||
    				bb.equals(Material.POTATO) ||
    				bb.equals(Material.RED_MUSHROOM) ||
    				bb.equals(Material.BROWN_MUSHROOM) ||
    				bb.equals(Material.SUGAR_CANE_BLOCK) ||
					bb.equals(Material.MELON) ||
    				
    				bb.equals(Material.DAYLIGHT_DETECTOR) ||
    				bb.equals(Material.DAYLIGHT_DETECTOR_INVERTED) ||
    				bb.equals(Material.TORCH) ||
    				bb.equals(Material.TRIPWIRE_HOOK) ||
    				bb.equals(Material.TRIPWIRE) ||
    				bb.equals(Material.LEVER) ||
    				bb.equals(Material.CARPET) ||
    				
    				//These cannot in the original adventure mode (adv mode 1.7 and earlier)
    				bb.equals(Material.FIRE) ||
    				bb.equals(Material.CAKE) ||
    				bb.equals(Material.TNT) ||
    				bb.equals(Material.CACTUS) ||
    				bb.equals(Material.BED) ||
    				bb.equals(Material.SPONGE) ||
    				bb.equals(Material.HAY_BLOCK) ||
    				
    				bbName.contains("GLASS") ||
    				bbName.contains("PISTON")
    				){
    			return;
    		}
    		
        	String toolMat = p.getInventory().getItemInHand().getType().name();
    		if (toolMat != null){
    			if (toolMat.contains("_PICKAXE")) {
    				if (bbName.contains("ORE") || //anything that is ore/stone(inc Cobble and sandstone)//nether/iron
    						bbName.contains("STONE") ||
    						bbName.contains("NETHER") ||
    						bbName.contains("IRON") ||
    						bbName.contains("QUARTZ") ||
    						bbName.contains("BRICK") ||
    						bbName.contains("SMOOTH") ||
    						bbName.contains("FURNACE") ||
    						bbName.contains("RAIL") ||
    						bb.equals(Material.COAL_BLOCK) ||
    						bb.equals(Material.OBSIDIAN) ||
    						bb.equals(Material.HOPPER) ||
    						bb.equals(Material.ENDER_CHEST) ||
    						bb.equals(Material.DISPENSER) ||
    						bb.equals(Material.DROPPER) ||
    						bb.equals(Material.ANVIL) ||
    						bb.equals(Material.MOB_SPAWNER) ||
    						
    						//pressure plates
    						bb.equals(Material.STONE_PLATE) ||
    						bb.equals(Material.IRON_PLATE) ||
    						bb.equals(Material.GOLD_PLATE)
    						){
    					return;
    				}
    			} else if (toolMat.contains("_AXE")) { //need the _ so it wont be confused with pickaxe
    				if (bbName.contains("WOOD") || //anything that contains wood
    						bbName.contains("DOOR") ||
    						bb.equals(Material.BOOKSHELF) ||
    						bb.equals(Material.CHEST) ||
    						bb.equals(Material.WORKBENCH) ||
    						bb.equals(Material.FENCE_GATE) ||
    						bb.equals(Material.FENCE) ||
    						bb.equals(Material.HUGE_MUSHROOM_1) ||
    						bb.equals(Material.HUGE_MUSHROOM_2) ||
    						bb.equals(Material.JUKEBOX) ||
    						bb.equals(Material.SIGN) ||
    						bb.equals(Material.SIGN_POST) ||
    						bb.equals(Material.WALL_SIGN) ||
    						bb.equals(Material.LADDER)
    						){
    					return;
    				}
    			} else if (toolMat.contains("_SPADE")) {
    				if (bb.equals(Material.GRASS) ||
    						bb.equals(Material.GRAVEL) ||
    						bb.equals(Material.DIRT) ||
    						bb.equals(Material.SAND) ||
    						bb.equals(Material.SNOW_BLOCK)
    						){
    					return;
    				}
    			} else if (toolMat.contains("SHEARS")) {
    				if (bb.equals(Material.LEAVES) ||
    						bb.equals(Material.LEAVES_2) ||
    						bb.equals(Material.WOOL)
    						){
    					return;
    				}
    			} else if (toolMat.contains("_SWORD")) {
    				if (bb.equals(Material.JACK_O_LANTERN) ||
    						bb.equals(Material.PUMPKIN) ||
    						bb.equals(Material.LEAVES) ||
    						bb.equals(Material.LEAVES_2) ||
    						bb.equals(Material.WEB)
    						){
    					return;
    				}
    			}
    		}
    		
    		//If it does not furfull the requirements above then cancel the event
    		event.setCancelled(true);
    	}
    }
}
