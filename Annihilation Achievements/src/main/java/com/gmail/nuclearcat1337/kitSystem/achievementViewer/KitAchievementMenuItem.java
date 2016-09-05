package com.gmail.nuclearcat1337.kitSystem.achievementViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.kitSystem.kitAchievement.KitAchivement;
import com.gmail.nuclearcat1337.kitSystem.kitAchievement.KitSystem;
import com.gmail.nuclearcat1337.kitSystem.main.KitMain;
import com.gmail.nuclearcat1337.kitSystem.utils.Acceptor;

public class KitAchievementMenuItem extends MenuItem implements Listener
{
	private final AchievementWrapper wrapper;
	private final KitSystem kitSystem;
	private Map<UUID, String> cachedLore;
	private Map<UUID, Long> lastCachedLore;

	public KitAchievementMenuItem(final AchievementWrapper wrapper, final KitSystem system)
	{
		super(wrapper.kit.toString(), wrapper.kit.getRewardKit().getIconPackage().getIcon());
		this.wrapper = wrapper;
		this.kitSystem = system;
		this.cachedLore = new HashMap<UUID, String>();
		this.lastCachedLore = new HashMap<UUID, Long>();
		//todo register the event in this plugin
		Bukkit.getPluginManager().registerEvents(this, AnnihilationMain.getInstance());

		for (Player p : Bukkit.getOnlinePlayers())
		{
			cacheProgress(p.getUniqueId());
		}
	}

	public KitAchivement getKit()
	{
		return wrapper.kit;
	}

	@Override
	public ItemStack getFinalIcon(Player player)
	{
		cacheProgress(player.getUniqueId());

		List<String> str = new ArrayList<String>(getLore());
		if (cachedLore.get(player.getUniqueId()) != null)
		{
			String[] cached = KitMain.toStringArray(cachedLore.get(player.getUniqueId()));
			for (String s : cached)
			{
				str.add(s);
			}
		} else
		{
			str.add(ChatColor.RED + "Click an item to update the status");
		}
		if (wrapper.kit.getRewardKit().hasPermission(player))
			str.add(ChatColor.GREEN + "You have achieved this kit's goal!");

		String name = getDisplayName();
		name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		return setNameAndLore(getIcon().clone(), name, str);
	}

	private void cacheProgress(UUID uuid)
	{
		/*
		 * Can only update their stats every 1 second
		 */
		if (lastCachedLore.get(uuid) == null || lastCachedLore.get(uuid) + 1000 < System.currentTimeMillis()) 
		{
			lastCachedLore.put(uuid, System.currentTimeMillis());

			String pre;
			String post;
			switch (wrapper.kit)
			{
				case WARRIOR:
					pre = "melee killed";
					post = "players";
					break;
				case ARCHER:
					pre = "ranged killed";
					post = "players";
					break;
				case LUMBERJACK:
					pre = "cut down";
					post = "logs";
					break;
				case MINER:
					pre = "mined";
					post = "ores";
					break;
				case SCOUT:
					pre = "hit";
					post = "nexuses";
					break;
				default:
					return;
			}

			kitSystem.getKitProgress(uuid, wrapper.kit, new Acceptor<Integer>()
			{

				@Override
				public void accept(Integer amount)
				{
					double displayAmount =  (double) amount;

					if (amount > wrapper.kit.getTotalActions())
						displayAmount = wrapper.kit.getTotalActions();
					
					double percent = (displayAmount / wrapper.kit.getTotalActions()) * 100f;
					
					percent = (double)Math.round(percent * 100d) / 100d; //makes the number always have a precition of 2 decimanls
					
//					System.out.println("(" +displayAmount + " / " +wrapper.kit.getTotalActions() + ") * " + 100f + " = (" 
//					+ displayAmount / wrapper.kit.getTotalActions() +") * " + 100f +" = " + percent );
					
					String lore = ChatColor.GRAY + "You have " + pre + " " + (int) displayAmount + "/" + wrapper.kit.getTotalActions() + " " + post + ".%n"
							+ ChatColor.GRAY + "That is " + percent + " percent.";
					cachedLore.put(uuid, lore);
				}
			});
		}
	}

	@Override
	public void onItemClick(ItemClickEvent event)
	{
		event.setWillUpdate(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void cachePlayersAchievements(PlayerJoinEvent e)
	{
		cacheProgress(e.getPlayer().getUniqueId());
	}
	
}
