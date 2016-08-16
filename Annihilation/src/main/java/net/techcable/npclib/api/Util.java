package net.techcable.npclib.api;

import com.gmail.nuclearcat1337.anniPro.utils.VersionUtils;
import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Util
{
	private Util()
	{
	}

	private static NMS nms;

	public static NMS getNMS()
	{
		if (nms == null)
		{
			try
			{
				String version = VersionUtils.getVersion();
				String name = "net.techcable.npclib.versions." + version + ".NMS";
				Class<?> rawClass = Class.forName(name);
				Class<? extends NMS> nmsClass = rawClass.asSubclass(NMS.class);
				Constructor<? extends NMS> constructor = nmsClass.getConstructor();
				return constructor.newInstance();
			} catch (ClassNotFoundException ex)
			{
				throw new UnsupportedOperationException("Unsupported nms version", ex);
			} catch (InvocationTargetException ex)
			{
				throw Throwables.propagate(ex.getTargetException());
			} catch (Exception ex)
			{
				throw Throwables.propagate(ex);
			}
		}
		return nms;
	}

	public static Player[] getNearbyPlayers(int range, Location l)
	{
		List<Player> nearby = new ArrayList<>(12);
		for (Player p : Bukkit.getOnlinePlayers())
		{
			double distance = p.getLocation().distanceSquared(l);
			if (distance <= range)
			{
				nearby.add(p);
			}
		}
		return nearby.toArray(new Player[nearby.size()]);
	}
}