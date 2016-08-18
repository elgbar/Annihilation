package com.gmail.nuclearcat1337.anniPro.anniMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public final class Areas implements Iterable<Area>, Listener
{
	private final Map<String, Area> areas;

	public Areas(String world)
	{
		areas = new HashMap<String, Area>();
	}

	public void addArea(Area a)
	{
		this.areas.put(a.getName().toLowerCase(), a);
	}

	public void removeArea(String name)
	{
		this.areas.remove(name.toLowerCase());
	}

	public boolean hasArea(String name)
	{
		return areas.containsKey(name.toLowerCase());
	}

	public Areas loadAreas(ConfigurationSection areaSection)
	{
		if (areaSection != null)
		{
			for (String key : areaSection.getKeys(false))
			{
				ConfigurationSection area = areaSection.getConfigurationSection(key);
				Area a = new Area(area);
				areas.put(a.getName().toLowerCase(), a);
			}
		}
		return this;
	}

	public Area getArea(String name)
	{
		return areas.get(name.toLowerCase());
	}

	public Area getArea(Loc loc)
	{
		for (Area a : areas.values())
		{
			if (a.isInArea(loc))
				return a;
		}
		return null;
	}

	public void saveToConfig(ConfigurationSection areaSection)
	{
		int counter = 1;
		for (Area a : areas.values())
		{
			ConfigurationSection sec = areaSection.createSection(counter + "");
			a.saveToConfig(sec);
			counter++;
		}
	}

	public void registerListener(Plugin p)
	{
		Bukkit.getPluginManager().registerEvents(this, p);
	}

	public void unregisterListener()
	{
		HandlerList.unregisterAll(this);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void checkBreaks(EntityDamageByEntityEvent e)
	{
		if (e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER)
		{
			Area a = this.getArea(new Loc(e.getDamager().getLocation(), false));
			if (a != null && !a.getAllowPVP())
			{
				e.setCancelled(true);
				return;
			}
			a = this.getArea(new Loc(e.getEntity().getLocation(), false));
			if (a != null && !a.getAllowPVP())
			{
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void checkFood(FoodLevelChangeEvent event)
	{
		Area a = getArea(new Loc(event.getEntity().getLocation(), false));
		if (a != null && !a.getAllowHunger())
		{
			event.setFoodLevel(20);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void checkDamage(EntityDamageEvent event)
	{
		Area a = getArea(new Loc(event.getEntity().getLocation(), false));
		if (a != null && !a.getAllowDamage())
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void checkBreaks(BlockBreakEvent e)
	{
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			Area a = this.getArea(new Loc(e.getBlock().getLocation(), false));
			if (a != null)
			{
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void checkBreaks(BlockPlaceEvent e)
	{
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			Area a = this.getArea(new Loc(e.getBlock().getLocation(), false));
			if (a != null)
			{
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void checkBreaks(PlayerBucketEmptyEvent event)
	{
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			Area a = this.getArea(new Loc(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation(), false));
			if (a != null)
			{
				event.setCancelled(true);
			}
		}
	}

	@Override
	public Iterator<Area> iterator()
	{
		return areas.values().iterator();
	}
}
