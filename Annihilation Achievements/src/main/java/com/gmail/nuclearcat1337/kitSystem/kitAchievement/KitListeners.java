package com.gmail.nuclearcat1337.kitSystem.kitAchievement;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.gmail.nuclearcat1337.anniPro.anniEvents.NexusHitEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.PlayerKilledEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;

public class KitListeners implements Listener
{
	private final KitSystem KitSystem;

	public KitListeners(KitSystem system)
	{
		this.KitSystem = system;
	}
	
	
//	Gotta do lowest, or else it wont get called (NO idea why)
	@EventHandler(priority = EventPriority.LOWEST)
	public void kitBlockBreak(BlockBreakEvent e)
	{
		if (KitSystem.isActive() && Game.isGameRunning()){
			Material blockMat = e.getBlock().getType();
			if (blockMat.equals(Material.LOG) || blockMat.equals(Material.LOG_2)){
				KitSystem.addKitAchivementProgess(e.getPlayer().getUniqueId(), KitAchivement.LUMBERJACK, 1);
			} else if (isOre(blockMat)){
				KitSystem.addKitAchivementProgess(e.getPlayer().getUniqueId(), KitAchivement.MINER, 1);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void kitNexusHit(NexusHitEvent e)
	{
		if (KitSystem.isActive() && Game.isGameRunning() && !e.isCancelled())
		{
			UUID id = e.getPlayer().getID();
			if (id != null) {
				KitSystem.addKitAchivementProgess(id, KitAchivement.SCOUT, 1);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void KitKill(PlayerKilledEvent e)
	{
		if (KitSystem.isActive() && Game.isGameRunning())
		{
			DamageCause lastDmg = e.getPlayer().getPlayer().getLastDamageCause().getCause();
			/*
			 * If the damaged that killed the player was caused by melee
			 */
			if (lastDmg.equals(DamageCause.ENTITY_ATTACK)) {
				KitSystem.addKitAchivementProgess(e.getKiller().getID(), KitAchivement.WARRIOR, 1);
//				System.out.println("A player gained progress to warrior");
			/*
			 * If the damaged that killed the player was caused by any projectile
			 */
			}else if (lastDmg.equals(DamageCause.PROJECTILE)){
				KitSystem.addKitAchivementProgess(e.getKiller().getID(), KitAchivement.ARCHER, 1);
//				System.out.println("A player gained progress to archer");
			}
		}
	}
	
	private boolean isOre(Material mat){
		switch (mat) {
			case COAL_ORE:
			case DIAMOND_ORE:
			case EMERALD_ORE:
			case REDSTONE_ORE:
			case GLOWING_REDSTONE_ORE:
			case GOLD_ORE:
			case IRON_ORE:
			case LAPIS_ORE:
			case QUARTZ_ORE:
				return true;
			default:
				return false;
		}
	}
}
