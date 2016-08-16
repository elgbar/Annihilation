package com.hcs.anniPro.playerParty;

import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.anniPro.main.Lang;

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
						p.sendMessage(Lang.PARTYREMOVED.toString());
					} else {
						p.sendMessage(Lang.PARTYREMOVEDPLAYER.toStringReplacement(leader.getName()));
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
