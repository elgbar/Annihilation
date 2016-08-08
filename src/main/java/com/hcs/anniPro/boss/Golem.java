package com.hcs.anniPro.boss;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;

/**
 * Golems are bosses in annihiliation that will spawn in the forth phase. They
 * are iron golems that have a high amount of health. Arrows do little damage to
 * the golems and therefore the only way to kill the golems quickly, are to go
 * into melee with the golems. The golems does not take any knockback damage and
 * will not go off the map. They have no restriction when it comes to where they
 * walk. Player may lure the golem into your own base to get the kill. It can
 * also be used as an distraction when luring it into an enemy base and then
 * attack their nexus.
 * 
 * Two golems is spawned between two team bases. This promotes rivaly between
 * those two teams.
 * 
 * It will respawn 10 minutes form when its killed.
 * 
 * @author kh498
 *
 */
public class Golem
{
	public static final Golem Red = new Golem(ChatColor.RED, "Drakons");
	public static final Golem Blue = new Golem(ChatColor.BLUE, "Brizo");
	public static final Golem[] Golems = new Golem[] { Red, Blue };

	private final ChatColor color;
	private final String internalName;
	private String displayName;
	private Loc spawn;
	private Boolean alive;

	public Golem(ChatColor color, String displayName)
	{
		checkNotNull(displayName, "displayName");
		if (color == ChatColor.RED)
		{
			internalName = "Red";
		} else if (color == ChatColor.BLUE)
		{
			internalName = "Blue";
		} else
		{
			throw new IllegalArgumentException("Golems only support red and blue team color");
		}
		this.displayName = color + displayName; // red: Drakons, blue: Brizo
		this.color = color;
		this.alive = false;
	}

	public interface GolemBlockHandler
	{
		void onBlockClick(Player player, Golem golem, Action action, Block block);
	}

	public DyeColor getWoolColor()
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

	public String getDisplayName()
	{
		return this.displayName;
	}

	public void setDisplayName(final String newName)
	{
		this.displayName = this.color + ChatColor.stripColor(newName);
		return;
	}

	/**
	 * Spawn both golems
	 */
	public static void spawnGolems()
	{
		for (Golem golem : Golem.Golems)
		{
			spawnGolem(golem);
		}
	}

	/**
	 * 
	 * @param golem
	 *            The golem you want to spawn
	 */
	public static void spawnGolem(Golem golem)
	{
		if (golem.isAlive())
		{ // do not spawn the golem if it is present
			return;
		}
		Location loc = golem.spawn.toLocation();
		if (!loc.getChunk().isLoaded())
		{
			if (loc.getChunk().load())
			{
				AnnihilationMain.getInstance().getLogger().log(Level.WARNING,
						"Could not load " + golem.getInternalName() + " golem due the server could not load the check where it spawns.");
				return;
			}
		}
		Damageable golemEnt = (Damageable) loc.getWorld().spawnEntity(loc, EntityType.IRON_GOLEM);
		golemEnt.setCustomName(golem.getDisplayName());

		// set it's health to twice as much normal
		golemEnt.setMaxHealth(golemEnt.getMaxHealth() * 2);
		golemEnt.setHealth(golemEnt.getHealth() * 2);

		golemEnt.setMetadata(golem.getInternalName() + "Golem", new FixedMetadataValue(AnnihilationMain.getInstance(), true));

		golem.setAlive(true);
	}

	public Loc getSpawn()
	{
		return spawn;
	}

	public void setSpawn(Loc spawn)
	{
		this.spawn = spawn;
	}

	public void setSpawn(Location loc)
	{
		setSpawn(new Loc(loc, true));
		return;
	}

	public ChatColor getColor()
	{
		return color;
	}

	public String getInternalName()
	{
		return internalName;
	}

	/**
	 * @return the alive
	 */
	public Boolean isAlive()
	{
		return alive;
	}

	/**
	 * @param alive
	 *            Set if the golem is alive or not
	 */
	public void setAlive(Boolean alive)
	{
		this.alive = alive;
	}
}
