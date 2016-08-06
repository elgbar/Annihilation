package com.hcs.boss;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public class Golem
{
	public static final Golem Red = new Golem(ChatColor.RED);
	public static final Golem Blue = new Golem(ChatColor.BLUE);
	public static final Golem[] Golems = new Golem[] { Red, Blue };

	private final ChatColor color;
	private final String name;
	private final String displayName;
	private Loc spawn;

	public Golem (ChatColor color)
	{
		if (color == ChatColor.RED)
		{
			name = "Red";
			displayName = color + "Drakons";
		} else if (color == ChatColor.BLUE)
		{
			name = "Blue";
			displayName = color + "Brizo";
		} else
		{
			throw new IllegalArgumentException("Golems only support red and blue team color");
		}
		this.color = color;
	}

	public interface GolemBlockHandler
	{
		void onBlockClick (Player player, Golem golem, Action action, Block block);
	}

	public DyeColor getWoolColor ()
	{
		if (color.equals(ChatColor.RED))
		{
			return DyeColor.RED;
		} else if (color.equals(ChatColor.BLUE))
		{
			return DyeColor.BLUE;
		} else
		{
			return DyeColor.BLACK; // So you can see if something fucks up
		}
	}

	public String getDisplayName ()
	{
		return this.color + this.displayName;
	}

	public static void spawnGolems ()
	{
		for (Golem golem : Golem.Golems)
		{
			Location loc = golem.spawn.toLocation();
			Entity golemEnt = loc.getWorld().spawnEntity(loc, EntityType.IRON_GOLEM);
			golemEnt.setCustomName(golem.getDisplayName());
		}
	}

	public Loc getSpawn ()
	{
		return spawn;
	}

	public void setSpawn (Loc spawn)
	{
		this.spawn = spawn;
	}

	public void setSpawn (Location loc)
	{
		setSpawn(new Loc(loc, true));
		return;
	}

	public ChatColor getColor ()
	{
		return color;
	}

	public String getName ()
	{
		return name;
	}
}
