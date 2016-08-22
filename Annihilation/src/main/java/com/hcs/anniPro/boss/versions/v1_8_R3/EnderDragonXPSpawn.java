package com.hcs.anniPro.boss.versions.v1_8_R3;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

public class EnderDragonXPSpawn
{
	private final static int OFFICIAL_XP_TO_DROP = 12000; //http://minecraft.gamepedia.com/Ender_Dragon
	private final static int MAX_DROP_AT_ONCE = 1000;
	private final static int RADIUS = 5;
	//	
	//	
	//	private final static int XP_TO_DROP
	//	public static void mimicEnderdragonXPdrop(Location loc)
	//	{
	//
	//		
	//
	//		final World world = ((CraftWorld) loc.getWorld()).getHandle();
	//
	//		int expToDrop = OFFICIAL_XP_TO_DROP; // found on the mc wiki ()
	//
	////		final int k = -RADIUS; // makes sure it can be a number between -5 and 5
	//		// System.out.println("" + expToDrop);
	//
	//		for (int l = 0; l < -RADIUS; l++)
	//		{
	//			expToDrop = 1000;
	//			while (expToDrop > 0)
	//			{
	//				int j = EntityExperienceOrb.getOrbValue(expToDrop);
	//
	//				expToDrop -= j;
	//				// System.out.println(expToDrop + " | " + j);
	//				AnnihilationMain.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new Runnable()
	//				{
	//					public void run()
	//					{
	//						// System.out.println("Spawned xp!");
	//						world.addEntity(new EntityExperienceOrb(world, loc.getX() + ,
	//								loc.getY() + 1 + rand.nextInt(20) / 10, loc.getZ() + (RADIUS + rand.nextInt(100) / 10), j));
	//					}
	//				}, rand.nextInt(50)); // spawn all the xp in a timespan of 50 tick
	//			}
	//		}
	//	}

	public static void mimicEnderdragonXPdrop(Location loc)
	{
		final Random rand = new Random(System.currentTimeMillis());
		int currentXpLeft = OFFICIAL_XP_TO_DROP;
		Location dropLoc = loc;
		while (currentXpLeft > 0)
		{
			dropLoc.setX(loc.getX() + getNumberBetween(RADIUS)); //get  a number between -RADIUS and +RADIUS
			dropLoc.setY(loc.getY() + rand.nextInt(RADIUS));
			dropLoc.setZ(loc.getX() + getNumberBetween(RADIUS));
			
			int xpToDrop = rand.nextInt(MAX_DROP_AT_ONCE);
			ExperienceOrb orb = (ExperienceOrb) dropLoc.getWorld().spawnEntity(dropLoc, EntityType.EXPERIENCE_ORB);
			orb.setExperience(xpToDrop);
			System.out.println(orb.isValid());
			

			currentXpLeft -= xpToDrop;
			System.out.println(currentXpLeft);
		}
	}
	//TODO need a better name
	/**
	 * 
	 * @return A number between -i and i
	 */
	private static double getNumberBetween(int i){
		final Random rand = new Random(System.currentTimeMillis());
		int j = i * 2;
		int k = j * 10;
		return (rand.nextInt(k) / j) - i;
	}
}
