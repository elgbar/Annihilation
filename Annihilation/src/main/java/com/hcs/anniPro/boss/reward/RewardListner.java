package com.hcs.anniPro.boss.reward;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class RewardListner implements Listener
{
//	private final Plugin plugin;

	public RewardListner(Plugin p)
	{
		Bukkit.getPluginManager().registerEvents(this, p);
//		this.plugin = p;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onGolemAttack(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player)
		{
			
			Player attacker = (Player) event.getDamager();

			if (attacker.getItemInHand().toString().equals(GolemReward.toItemStack(GolemReward.GOLDEN_HEALING_SWORD).toString()))
			{
				double newHealth = attacker.getHealth() + event.getFinalDamage();
				
				if (newHealth < attacker.getMaxHealth())
				{
					attacker.setHealth(newHealth);
				} else
				{
					attacker.setHealth(attacker.getMaxHealth());
				}
			}
		}
	}
}
