package net.techcable.npclib.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NMS
{
	Player spawnPlayer(Player player, Location location, NPC npc);

	void onDespawn(NPC npc);
}
