package com.gmail.nuclearcat1337.kitSystem.achievementViewer;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu.Size;
import com.gmail.nuclearcat1337.kitSystem.kitAchievement.KitAchivement;
import com.gmail.nuclearcat1337.kitSystem.kitAchievement.KitSystem;

public class AchievementViewer implements CommandExecutor
{
	private HashMap<UUID, ItemMenu> menus;

	private KitAchievementMenuItem[] items;
	
	public AchievementViewer(KitSystem system)
	{
		menus = new HashMap<UUID,ItemMenu>();
		items = new KitAchievementMenuItem[KitAchivement.values().length];
		int c =0;
		for(KitAchivement k : KitAchivement.values())
		{
			KitAchievementMenuItem item = new KitAchievementMenuItem(new AchievementWrapper(k),system);
			items[c] = item;
			c++;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			ItemMenu menu = menus.get(player.getUniqueId());
			if(menu == null)
			{
				menu = new ItemMenu(player.getName()+"'s Achievements",Size.fit(items.length));
				menus.put(player.getUniqueId(), menu);
			}
			menu.clearAllItems();
			int counter = 0;
			for(KitAchievementMenuItem m : items)
			{
				
				menu.setItem(counter, m);
				counter++;
			}
			menu.open(player);
		}
		return true;
	}
}
