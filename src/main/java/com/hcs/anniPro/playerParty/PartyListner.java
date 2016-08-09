package com.hcs.anniPro.playerParty;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PartyListner implements Listener
{
	public PartyListner(Plugin p)
	{
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event){
		playerLeave(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerKickEvent event){
		playerLeave(event.getPlayer());
	}
	
	public void playerLeave(Player p){
		if (!PlayerParties.isPlayerLeader(p))
			PlayerParty.playerQuit(p);
		else 
			PlayerParties.removeParty(p);
	}
}
