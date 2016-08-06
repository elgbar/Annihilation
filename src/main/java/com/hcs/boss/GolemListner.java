package com.hcs.boss;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class GolemListner implements Listener
{

	private final Plugin plugin;

	public GolemListner (Plugin p)
	{
		Bukkit.getPluginManager().registerEvents(this, p);
		this.plugin = p;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onGolemAttack (EntityDamageByEntityEvent event)
	{
		Entity attacker = event.getDamager();
		Entity victim = event.getEntity();
		EntityType victimType = victim.getType();
		EntityType attackerType = attacker.getType();

		// System.out.println("atkr " + attacker.toString());
		// System.out.println("victim " + victim.toString());

		if (victimType.equals(EntityType.IRON_GOLEM)) // victim is an iron golem
		{
			if (attackerType.equals(EntityType.ARROW) && !event.isCancelled())
			{
				System.out.println("1Final dmg:" + event.getFinalDamage() + " or " + event.getDamage());
				event.setDamage(event.getFinalDamage() / 4); // cut the final
																// dammage in
																// four
				System.out.println("2Final dmg:" + event.getFinalDamage() + " or " + event.getDamage());
				// event.setCancelled(true);
				// Player p = (Player) attacker;
				// ItemStack item = p.getItemInHand();
				// if (item.getType().equals(Material.BOW))
				// {
				//
				// }
			}

			victim.setVelocity(new Vector());
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{
				public void run ()
				{
					victim.setVelocity(new Vector());
				}
			}, 1L);
		}
		// else if ((Player) victim != null &&
		// attacker.getType().equals(EntityType.IRON_GOLEM)){ //victim is a
		// player & attacker is an iron golem
		// System.out.println("victim is player");
		// }
	}
}
