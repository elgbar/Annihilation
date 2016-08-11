package com.hcs.anniPro.playerParty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

public final class PlayerParty
{
	private static final String META_KEY = "isInParty";

	private List<Player> players;
	private AnniTeam team = null;
	private final Player teamLeader;

	public PlayerParty(Player leader, @Nullable AnniTeam team)
	{
		if (PlayerParties.getParty(leader.getUniqueId()) != null){
			throw new IllegalArgumentException("This player is already a leader in another party.");
		}
		this.players = new ArrayList<Player>();
		
		this.teamLeader = leader;
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

	/** @param player
	 *            the players to add */
	public void addPlayer(Player player)
	{
		player.setMetadata(META_KEY, new FixedMetadataValue(AnnihilationMain.getInstance(), getTeamLeader().getUniqueId()));
		players.add(player);
		PlayerParties.updateParty(this);
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
		return team != null ? team : null;
	}

	/** @param team
	 *            the team to set */
	public boolean setAnniTeam(AnniTeam team)
	{
		this.team = team;
		return PlayerParties.updateParty(this);
	}

	/** @return the teamLeader */
	public Player getTeamLeader()
	{
		return teamLeader;
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
	
	/*
	 * Can be accessed outside the player party
	 */
	
	public static boolean isInATeam(Player player)
	{
		
		if (player.hasMetadata(PlayerParty.META_KEY) && getParty(player) != null){
			return true;
		}
		return false;
	}
	
	public static PlayerParty getParty(Player player){
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
}
