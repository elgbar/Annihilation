package com.hcs.anniPro.playerParty;

import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public final class PlayerParties
{
	private static TreeMap<UUID, PlayerParty> parties = new TreeMap<UUID, PlayerParty>();

	/** @return the parties */
	public static TreeMap<UUID, PlayerParty> getParties()
	{
		return parties;
	}

	public static void addParty(PlayerParty pp)
	{
		UUID u = pp.getTeamLeader().getUniqueId();
		parties.put(u, pp);
	}

	public static boolean updateParty(PlayerParty pp)
	{
		UUID u = pp.getTeamLeader().getUniqueId();

		if (parties.containsKey(u))
		{
			parties.replace(u, pp);
			return true;
		}
		return false;
	}

	public static boolean removeParty(UUID u)
	{
		if (parties.containsKey(u))
		{
			parties.remove(u);
			return true;
		}
		return false;
	}

	public static boolean removeParty(Player p)
	{
		return removeParty(p.getUniqueId());
	}

	public static boolean removeParty(PlayerParty pp)
	{
		return removeParty(pp.getTeamLeader().getUniqueId());
	}

	public static void clearParties()
	{
		// TODO test if this works
		parties = new TreeMap<UUID, PlayerParty>();
	}

	public static boolean isPlayerLeader(Player player)
	{
		UUID u = player.getUniqueId();
		return parties.containsKey(u);
	}

	public static void leaderQuit(Player p)
	{
		// TODO Auto-generated method stub

	}
}
