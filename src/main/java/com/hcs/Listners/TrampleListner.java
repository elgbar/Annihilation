package com.hcs.Listners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class TrampleListner implements Listener {
	
    public TrampleListner(Plugin p)  {
        Bukkit.getPluginManager().registerEvents(this,p);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSoilTrample(PlayerInteractEvent event) {
    	if (!event.isCancelled()){
    		
    		if (event.getAction() != Action.PHYSICAL) {
    			return;
    		}
    		
    		Block block = event.getClickedBlock();
            if(block == null) {
                return;
            }
            
            Material blockType = block.getType();
            if(blockType == Material.SOIL)
            {
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                event.setCancelled(true);
            }
    	}
    }
}
