package com.hcs.anniPro.playerParty;

import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PlayerParties
{
	private static TreeMap<UUID, PlayerParty> parties = new TreeMap<UUID, PlayerParty>();

	/** @return the parties */
	public static TreeMap<UUID, PlayerParty> getParties()
	{
		return parties;
	}
	
	public static PlayerParty getParty(UUID id)
	{
		return parties.get(id);
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
			PlayerParty pp = getParty(u);
			Player leader = pp.getTeamLeader();
			for (Player p : pp.getPlayers()){
				if (p.isOnline()) {
					if (!leader.equals(p)) {
						p.sendMessage(ChatColor.DARK_PURPLE + "You party was removed due to the host (" + ChatColor.GOLD + leader + ChatColor.LIGHT_PURPLE + ") leaving.");
					} else {
						p.sendMessage(ChatColor.DARK_PURPLE + "Your party was removed.");
					}
				}
			}
			parties.remove(u);
			return true;
		}
		return false;
	}

	public static boolean removeParty(Player p)
	{
		return removeParty(p.getUniqueId());
	}

	public static void clearParties()
	{
		parties.clear();
	}

	public static boolean isPlayerLeader(Player player)
	{
		UUID u = player.getUniqueId();
		return parties.containsKey(u);
	}
}
