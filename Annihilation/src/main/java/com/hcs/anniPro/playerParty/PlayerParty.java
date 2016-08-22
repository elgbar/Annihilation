package com.hcs.anniPro.playerParty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.main.TeamCommand;

public final class PlayerParty
{
	private static final String META_KEY = "isInParty";

	private List<Player> players;
	private List<OfflinePlayer> invited;
	private AnniTeam team = null;
	private final Player partyLeader;
	private final int MAX_PLAYERS_IN_A_PARTY = 10;

	public PlayerParty(Player leader, @Nullable AnniTeam team)
	{
		if (PlayerParties.getParty(leader.getUniqueId()) != null)
		{
			throw new IllegalArgumentException("This player is already a leader in another party.");
		}
		this.players = new ArrayList<Player>();
		this.invited = new ArrayList<OfflinePlayer>();

		this.partyLeader = leader;
		if (team != null)
			this.setAnniTeam(team);
		PlayerParties.addParty(this);
		this.addPlayer(leader);
	}

	/** @return the players */
	public List<Player> getPlayers()
	{
		return players;
	}

	/**
	 * @param player
	 *            the players to add
	 */
	public boolean addPlayer(Player player)
	{

		if (!isInAParty(player) && getPlayers().size() < MAX_PLAYERS_IN_A_PARTY)
		{
			AnniPlayer ap = AnniPlayer.getPlayer(player.getUniqueId());
			if (ap.getTeam() != null)
			{
				ap.getTeam().leaveTeam(ap);
			}
			if (getAnniTeam() != null)
			{
				TeamCommand.joinTeam(ap, team);
			}
			player.setMetadata(META_KEY, new FixedMetadataValue(AnnihilationMain.getInstance(), getPartyLeader().getUniqueId()));
			players.add(player);
			removeInvited(player);
			return PlayerParties.updateParty(this);
		}
		return false;
	}

	public boolean removePlayer(Player player)
	{
		if (players.isEmpty())
		{
			return false;
		} else if (!players.contains(player))
		{
			return false;
		}
		players.remove(player);
		player.removeMetadata(META_KEY, AnnihilationMain.getInstance());

		return PlayerParties.updateParty(this);
	}

	/** @return the AnniTeam the party is a part of (or will be if the game is not started) */
	public AnniTeam getAnniTeam()
	{
		return this.team;
	}

	/**
	 * @param team
	 *            the team to set
	 */
	public boolean setAnniTeam(AnniTeam team)
	{
		this.team = team;
		for (Player p : this.getPlayers())
		{
			if (p.isOnline())
			{
				AnniPlayer ap = AnniPlayer.getPlayer(p.getUniqueId());
				if (team != null)
					TeamCommand.joinTeam(ap, team);
				else
					ap.getTeam().leaveTeam(ap);
			} else
			{
				this.removePlayer(p);
			}
		}
		return PlayerParties.updateParty(this);
	}

	/** @return the partyLeader */
	public Player getPartyLeader()
	{
		return partyLeader;
	}

	public boolean isInThisParty(Player player)
	{
		for (Player p : getPlayers())
		{
			if (p.equals(player))
				return true;
		}
		return false;
	}

	/** @return If a player is invited to join this team */
	public boolean getIfInvited(Player player)
	{
		if (!invited.isEmpty())
		{
			for (OfflinePlayer op : invited)
			{
				if (op.getUniqueId().equals(player.getUniqueId()))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param invited
	 *            the player to invite
	 */
	public void addInvite(Player invited)
	{
		if (!getIfInvited(invited))
		{
			this.invited.add(invited);
		}
	}

	/**
	 * @param invited
	 *            the player to remove from invted invite
	 */
	public void removeInvited(Player player)
	{
		for (OfflinePlayer op : this.invited)
		{
			if (op.getUniqueId().equals(player.getUniqueId()))
			{
				this.invited.remove(op);
				return;
			}
		}
	}

	public List<OfflinePlayer> getInvited()
	{
		return invited;
	}

	/*
	 * Can be accessed outside the player party
	 */

	public static boolean isInAParty(Player player)
	{

		return (player.hasMetadata(PlayerParty.META_KEY) && getParty(player) != null);
	}

	public static PlayerParty getParty(Player player)
	{
		String uuidString;
		try
		{
			uuidString = player.getMetadata(PlayerParty.META_KEY).get(0).asString();
		} catch (Exception e)
		{
			return null;
		}
		UUID uuid = UUID.fromString(uuidString);
		return PlayerParties.getParty(uuid);
	}

	public static void removeMetadata(Player player)
	{
		player.removeMetadata(META_KEY, AnnihilationMain.getInstance());
	}

}
