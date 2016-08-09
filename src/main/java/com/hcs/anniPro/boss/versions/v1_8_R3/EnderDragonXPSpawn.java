package com.hcs.anniPro.boss.versions.v1_8_R3;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.World;

public class EnderDragonXPSpawn
{
	public static void mimicEnderdragonXPdrop(Location loc)
	{

		final Random rand = new Random(System.currentTimeMillis());

		final World world = ((CraftWorld) loc.getWorld()).getHandle();

		int expToDrop = 12000; // found on the mc wiki (http://minecraft.gamepedia.com/Ender_Dragon)

//		int i = expToDrop / 1200;

		final int k = -5; // makes sure it can be a number between -5 and 5
//		System.out.println("" + expToDrop);

		for (int l = 0; l < 5; l++)
		{
			expToDrop = 1000;
			while (expToDrop > 0)
			{
				int j = EntityExperienceOrb.getOrbValue(expToDrop);

				expToDrop -= j;
//				System.out.println(expToDrop + " | " + j);
				AnnihilationMain.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new Runnable()
				{
					public void run()
					{
//						System.out.println("Spawned xp!");
						world.addEntity(new EntityExperienceOrb(world, loc.getX() + (k + rand.nextInt(100) / 10),
								loc.getY() + 1 + rand.nextInt(20) / 10, loc.getZ() + (k + rand.nextInt(100) / 10), j));
					}
				}, rand.nextInt(50)); // spawn all the xp in a timespan of 50 tick
			}
		}
	}
}
