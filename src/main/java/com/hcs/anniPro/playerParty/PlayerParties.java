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
		UUID u = pp.getPartyLeader().getUniqueId();
		parties.put(u, pp);
	}

	public static boolean updateParty(PlayerParty pp)
	{
		UUID u = pp.getPartyLeader().getUniqueId();

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
			Player leader = pp.getPartyLeader();
			for (Player p : pp.getPlayers()){
				if (p.isOnline()) {
					if (leader.equals(p)) {
						p.sendMessage(ChatColor.DARK_PURPLE + "Your party was removed.");
					} else {
						p.sendMessage(ChatColor.DARK_PURPLE + "You party was disbanded due to the host," + ChatColor.GOLD + leader.getName() + ChatColor.DARK_PURPLE + ", leaving.");
					}
				}
				PlayerParty.removeMetadata(p);
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
	
	public static boolean removeParty(PlayerParty pp)
	{
		return removeParty(pp.getPartyLeader().getUniqueId());
	}

//	public static void clearParties()
//	{
//		if (parties != null && !parties.isEmpty()) {
//			for (Entry<UUID, PlayerParty> entry : parties.entrySet()){
//				PlayerParty pp = entry.getValue();
//				removeParty(pp);
//			}
//		}
//		parties.clear();
//	}

	public static boolean isPlayerLeader(Player player)
	{
		UUID u = player.getUniqueId();
		return parties.containsKey(u);
	}
	
	public static void playerLeave(Player p)
	{
		if (PlayerParty.isInAParty(p)) {
			PlayerParty pp = PlayerParty.getParty(p);
			if (!isPlayerLeader(p))
			{
				pp.removePlayer(p);
			} else
			{	
				removeParty(p);
			}
		}
	}
}
