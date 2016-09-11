package com.hcs.anniPro.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;

public class GolemListner implements Listener
{

	private final Plugin plugin;
	private final Random rand;
	private final long RESPAWN_TIME = 12000L; //in ticks,  10 min (20 tick in one sec* 60 s in one min * 10 min)

	public GolemListner(Plugin p)
	{
		Bukkit.getPluginManager().registerEvents(this, p);
		rand = new Random(System.currentTimeMillis());
		this.plugin = p;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onGolemAttack(EntityDamageByEntityEvent event)
	{
		Entity attacker = event.getDamager();
		Entity victim = event.getEntity();
		EntityType victimType = victim.getType();
		EntityType attackerType = attacker.getType();

		// System.out.println("atkr " + attacker.toString());
		// System.out.println("victim " + victim.toString());

		if (victimType.equals(EntityType.IRON_GOLEM)) // victim is an iron golem
		{
			double dmg = event.getFinalDamage();

			// All golems take half damage
			dmg = dmg / 2;

			if (attackerType.equals(EntityType.ARROW) && !event.isCancelled())
			{
				// Arrows will only do 1/8 of normal damge
				dmg = dmg / 4;
			}

			victim.setVelocity(new Vector());
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{
				public void run()
				{
					victim.setVelocity(new Vector());
				}
			}, 1L);

			event.setDamage(dmg);
		}
	}

	@EventHandler
	public void onGolemDeath(EntityDeathEvent event)
	{
		for (Golem golem : Golem.Golems)
		{
			Entity golEnt = event.getEntity();
			if (golEnt.hasMetadata(golem.getInternalName() + "Golem"))
			{
				Boolean meta = golEnt.getMetadata(golem.getInternalName() + "Golem").get(0).asBoolean();
				if (meta)
				{
					golem.setAlive(false);

					Location spawnLoc = golem.getSpawn().toLocation();
					Player killer = event.getEntity().getKiller();
					String team = null;
					if (killer != null)
					{
						AnniPlayer ap = AnniPlayer.getPlayer(killer.getUniqueId());
						if (ap != null)
						{
							try
							{
								team = ap.getTeam().getColoredName() + " team";
							} catch (NullPointerException e)
							{

							}
						}
					}

					if (team == null)
					{
						team = ChatColor.WHITE + "Mother Earth";
					}

					for (Player p : spawnLoc.getWorld().getPlayers())
					{
						p.sendMessage(golem.getColor() + golem.getDisplayName() + ChatColor.LIGHT_PURPLE + " was killed by " + team + "!");
					}

					event.getDrops().clear();
					event.setDroppedExp(2500);

					spawnLoc.getBlock().setType(Material.CHEST);

					Chest chest = (Chest) spawnLoc.getBlock().getState();
					Inventory inv = chest.getInventory();

					if (chest != null && inv != null)
					{
						List<ItemStack> list = new ArrayList<ItemStack>();

						List<GolemReward> common = GolemReward.getAllRarity(Rarity.COMMON);
						List<GolemReward> uncommon = GolemReward.getAllRarity(Rarity.UNCOMMON);
						List<GolemReward> rare = GolemReward.getAllRarity(Rarity.RARE);

						int randomItem, randomItem2;

						//add two common items
						randomItem = rand.nextInt(common.size());
						randomItem2 = rand.nextInt(common.size());
						list.add(GolemReward.toItemStack(common.get(randomItem)));
						while (randomItem == randomItem2)
							randomItem2 = rand.nextInt(common.size());
						list.add(GolemReward.toItemStack(common.get(randomItem2)));

						//add two uncommon items
						randomItem = rand.nextInt(uncommon.size());
						randomItem2 = rand.nextInt(uncommon.size());
						list.add(GolemReward.toItemStack(uncommon.get(randomItem)));
						while (randomItem == randomItem2)
							randomItem2 = rand.nextInt(uncommon.size());
						list.add(GolemReward.toItemStack(uncommon.get(randomItem2)));

						//add one rare item
						randomItem = rand.nextInt(rare.size());
						list.add(GolemReward.toItemStack(rare.get(randomItem)));

						
						int[] filledSlots = new int[inv.getSize()];
						for (ItemStack item : list)
						{
							int slot = 0;
							boolean acceptValue = false;
							while (!acceptValue)
							{
								slot = rand.nextInt(inv.getSize());
								if (filledSlots[slot] == 0)
								{
									inv.setItem(slot, item);
									filledSlots[slot] = 1;
									acceptValue = true;
								}
							}
						}

						//						inv.setContents(list.toArray(new ItemStack[l.size()]));
					}
					/*
					 * Spawns the golem after 10 minutes of being dead
					 */
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
					{
						public void run()
						{
							if (spawnLoc.getBlock().getType().equals(Material.CHEST))
							{
								Chest chest = (Chest) spawnLoc.getBlock().getState();
								Inventory inv = chest.getInventory();
								inv.setContents(null);
							}

							spawnLoc.getBlock().setType(Material.AIR);
							Golem.spawnGolem(golem);
						}
					}, RESPAWN_TIME);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGolemDespawn(ItemDespawnEvent event)
	{
		Entity ent = event.getEntity();
		if (ent.getType().equals(EntityType.IRON_GOLEM))
		{
			for (Golem golem : Golem.Golems)
			{
				Entity golEnt = event.getEntity();
				if (golEnt.hasMetadata(golem.getInternalName() + "Golem"))
				{
					event.setCancelled(true);
				}
			}
		}
	}

}
