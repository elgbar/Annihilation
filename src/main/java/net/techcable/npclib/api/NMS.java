package net.techcable.npclib.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NMS
{
	public Player spawnPlayer(Player player, Location location, NPC npc);

	public void onDespawn(NPC npc);
}
