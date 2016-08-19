package com.gmail.nuclearcat1337.xpSystem.shop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu.Size;
import com.gmail.nuclearcat1337.anniPro.kits.Kit;
import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;
import com.gmail.nuclearcat1337.xpSystem.main.XPSystem;

public class Shop implements CommandExecutor
{
	static String purchasedMessage;
	static String forsaleMessage;
	static String confirmMessage;
	static String confirmExpired;
	static String notEnoughXP;
	static String kitPurchased;
	static String noKitsToPurchase;
	private KitShopMenuItem[] items;
	private final Map<UUID,ItemMenu> menus;
	private final ConfigurationSection configSec;
	
	public Shop(XPSystem system, ConfigurationSection shopSection)
	{
		configSec = shopSection;
		menus = new HashMap<UUID,ItemMenu>();
		setMessages();
		if(!shopSection.isConfigurationSection("Kits"))
			shopSection.createSection("Kits");
		ConfigurationSection kitSec = shopSection.getConfigurationSection("Kits");
		Collection<Kit> kits = Kit.getKits();
		items = new KitShopMenuItem[kits.size()];
		int c =0;
		for(Kit k : kits)
		{
			ConfigManager.setDefaultIfNotSet(kitSec, k.getName(), 10000);
			KitShopMenuItem item = new KitShopMenuItem(new KitWrapper(k,kitSec.getInt(k.getName())),system);
			items[c] = item;
			c++;
		}
	}
	
	private void setMessages(){
		purchasedMessage = getString(configSec,"Already-Purchased-Kit");
		forsaleMessage = getString(configSec,"Not-Yet-Purchased-Kit");
		confirmMessage = getString(configSec,"Confirm-Purchase-Kit");
		confirmExpired = getString(configSec,"Confirmation-Expired");
		notEnoughXP = getString(configSec,"Not-Enough-XP");
		kitPurchased = getString(configSec,"Kit-Purchased");
		noKitsToPurchase = getString(configSec,"No-Kits-To-Purchase");
	}
	
	
	private String getString(ConfigurationSection section, String path)
	{
		return ChatColor.translateAlternateColorCodes('&', section.getString(path));
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
				menu = new ItemMenu(player.getName()+"'s Kit Shop",Size.fit(items.length));
				menus.put(player.getUniqueId(), menu);
			}
			menu.clearAllItems();
			int counter = 0;
			for(KitShopMenuItem m : items)
			{
				if(!m.getKit().hasPermission(player))
				{
					menu.setItem(counter, m);
					counter++;
				}
			}
			if(counter == 0)
				sender.sendMessage(noKitsToPurchase);
			else
				menu.open(player);
		}
		return true;
	}
}
