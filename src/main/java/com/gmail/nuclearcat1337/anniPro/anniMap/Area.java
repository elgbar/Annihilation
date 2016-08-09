package com.gmail.nuclearcat1337.anniPro.anniMap;

import com.gmail.nuclearcat1337.anniPro.utils.Loc;
import javafx.util.Pair;
import org.bukkit.configuration.ConfigurationSection;

public class Area
{
	private boolean allowPVP = true;
	private boolean allowDamage = true;
	private boolean allowHunger = true;
	private Loc corner1;
	private Loc corner2;
	private String name;

	public Area(Loc one, Loc two, String name)
	{
		this.corner1 = one;
		this.corner2 = two;
		this.name = name;
	}

	public Area(ConfigurationSection section)
	{
		assert section != null;
		this.name = section.getString("Name");
		corner1 = new Loc(section.getConfigurationSection("Corner1"));
		corner2 = new Loc(section.getConfigurationSection("Corner2"));
		if (section.isSet("AllowPVP"))
			this.setAllowPVP(section.getBoolean("AllowPVP"));
		if (section.isSet("AllowHunger"))
			this.setAllowHunger(section.getBoolean("AllowHunger"));
		if (section.isSet("AllowDamage"))
			this.setAllowDamage(section.getBoolean("AllowDamage"));
	}

	public String getName()
	{
		return name;
	}

	public Pair<Loc, Loc> getCorners()
	{
		return new Pair<>(corner1, corner2);
	}

	public void setAllowPVP(boolean allowPvp)
	{
		this.allowPVP = allowPvp;
	}

	public void setAllowDamage(boolean allowDamage)
	{
		this.allowDamage = allowDamage;
	}

	public void setAllowHunger(boolean allowHunger)
	{
		this.allowHunger = allowHunger;
	}

	public boolean getAllowPVP()
	{
		return allowPVP;
	}

	public boolean getAllowDamage()
	{
		return allowDamage;
	}

	public boolean getAllowHunger()
	{
		return allowHunger;
	}

	public void saveToConfig(ConfigurationSection section)
	{
		section.set("Name", this.getName());
		corner1.saveToConfig(section.createSection("Corner1"));
		corner2.saveToConfig(section.createSection("Corner2"));
		section.set("AllowPVP", getAllowPVP());
		section.set("AllowHunger", getAllowHunger());
		section.set("AllowDamage", getAllowDamage());
	}

	private boolean fallsBetween(int one, int two, int num)
	{
		int min, max;
		if (one < two)
		{
			min = one;
			max = two;
		} else
		{
			min = two;
			max = one;
		}

		return num >= min && num <= max;
	}

	public boolean isInArea(Loc loc)
	{
		if (!corner1.getWorld().equals(loc.getWorld()))
			return false;

		return fallsBetween(corner1.getBlockX(), corner2.getBlockX(), loc.getBlockX())
				&& fallsBetween(corner1.getBlockY(), corner2.getBlockY(), loc.getBlockY())
				&& fallsBetween(corner1.getBlockZ(), corner2.getBlockZ(), loc.getBlockZ());
	}
}