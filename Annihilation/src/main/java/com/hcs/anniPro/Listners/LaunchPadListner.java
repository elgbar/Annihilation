package com.hcs.anniPro.Listners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class LaunchPadListner implements Listener
{

	private final float MAX_HOR_VEL = 4F;
	private final float MIN_HOR_VEL = -4F;

	private final String LAUNCHPAD_KEY = "LaunchPad_isStillInAir";
	private Plugin plugin;

	public LaunchPadListner(Plugin p)
	{
		Bukkit.getPluginManager().registerEvents(this, p);
		this.plugin = p;
	}

	@EventHandler(ignoreCancelled = true)
	public void onLaunchPadWalk(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.PHYSICAL)
		{
			Block b = event.getClickedBlock();
			Material mat = b.getType(); // the block walked on's type
			if (mat.equals(Material.GOLD_PLATE) || mat.equals(Material.IRON_PLATE) || mat.equals(Material.STONE_PLATE)
					|| mat.equals(Material.WOOD_PLATE))
			{
				Location loc = b.getLocation();
				Block belowB = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
				Material belowMat = belowB.getType();
				Player player = event.getPlayer();

				Vector oldVec = player.getVelocity();
				Vector newVec = player.getLocation().getDirection();

				// System.out.println("OLD X: " + oldVec.getX() + " Y: " +
				// oldVec.getY() + " Z: " + oldVec.getZ());
				if (belowMat.equals(Material.IRON_BLOCK))
				{
					newVec = newVec.multiply(3);
					newVec.setY(0);
				} else if (belowMat.equals(Material.REDSTONE_LAMP_OFF) || belowMat.equals(Material.REDSTONE_LAMP_ON))
				{

					newVec = newVec.multiply(10);
					newVec.setY(0);
				} else if (belowMat.equals(Material.EMERALD_BLOCK))
				{ // Make the player go vertical up
					float VERT_VEL = 1.5F; // Found while testing
					newVec = new Vector(oldVec.getX(), VERT_VEL, oldVec.getZ());

				} else
				{
					newVec = null;
				}

				if (newVec != null)
				{
					// Don't let the vector become greater than 2 or less than
					// -2
					if (newVec.getX() > MAX_HOR_VEL)
					{
						newVec.setX(MAX_HOR_VEL);
					} else if (newVec.getX() < MIN_HOR_VEL)
					{
						newVec.setX(MIN_HOR_VEL);
					}
					if (newVec.getZ() > MAX_HOR_VEL)
					{
						newVec.setZ(MAX_HOR_VEL);
					} else if (newVec.getZ() < MIN_HOR_VEL)
					{
						newVec.setZ(MIN_HOR_VEL);
					}
					// System.out.println("NEW X: " + newVec.getX() + " Y: " +
					// newVec.getY() + " Z: " + newVec.getZ());
					Player p = event.getPlayer();

					p.setVelocity(newVec);

					p.setMetadata(LAUNCHPAD_KEY, new FixedMetadataValue(plugin, true));
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLaunchLand(EntityDamageEvent event)
	{
		if (!(event.getEntity() instanceof Player))
		{
			return;
		}

		if (event.getCause() == DamageCause.FALL)
		{
			Player p = (Player) event.getEntity();
			if (p.hasMetadata(LAUNCHPAD_KEY))
			{
				Boolean bool = p.getMetadata(LAUNCHPAD_KEY).get(0).asBoolean();
				if (bool.equals(true))
				{
					event.setCancelled(true);
					p.setMetadata(LAUNCHPAD_KEY, new FixedMetadataValue(plugin, false));
				}
			}
		}

	}
}
