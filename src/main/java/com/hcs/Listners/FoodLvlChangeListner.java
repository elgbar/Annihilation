package com.hcs.Listners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class FoodLvlChangeListner implements Listener {
	
	private final static String FOOD_KEY = "FoodLevel"; //metadata keyword
	private final static int FOOD_SKIP = 4; //For every FOOD_SKIP times it tries to decrease the health it will actually be decreased
	private final static float FOOD_SAT_ADD = 0.2F; //the food saturation to add when failed to loose hunger
	private final static int FOOD_MIN = 1; //when to loose hunger
	private Plugin plugin; //plugin that is used by the metadata
	
    public FoodLvlChangeListner(Plugin p)  {
        Bukkit.getPluginManager().registerEvents(this,p);
        plugin = p;
    }
	
    @EventHandler(priority = EventPriority.HIGHEST)
	public void onFoodLevelChange(FoodLevelChangeEvent event){
		Player p = (Player) event.getEntity();
		
		//player gains food
		if (p.getFoodLevel() < event.getFoodLevel()) {
			return;
		}
		
		int cancelFoodChange = FOOD_MIN; //first time cancel event
		if (p.hasMetadata(FOOD_KEY)) {
			try { //sometimes it fails idk why...
				cancelFoodChange = p.getMetadata(FOOD_KEY).get(0).asInt();
			} catch (Exception e) {
			}
			
		}
		
		if (cancelFoodChange > FOOD_MIN) {
			event.setCancelled(true);
			p.setSaturation(p.getSaturation() + FOOD_SAT_ADD);
			cancelFoodChange--;
		} else {
			cancelFoodChange = FOOD_SKIP; //reset food
		}
		
		p.setMetadata(FOOD_KEY, new FixedMetadataValue(plugin, cancelFoodChange));
	}
}
