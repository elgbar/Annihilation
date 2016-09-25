package com.hcs.convertXP;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nuclearcat1337.anniPro.itemMenus.ActionMenuItem;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickHandler;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu.Size;
import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;

public class Convert implements CommandExecutor
{
	//	private Plugin plugin;
	private static String CMD = "convert";

	public Convert(JavaPlugin plugin)
	{
		plugin.getCommand(CMD).setExecutor(this);
		//		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (args.length == 0)
			{
				openExchanger(player);
				return true;
			}
			for (Exchange e : Exchange.values())
			{
				if (args[0].equalsIgnoreCase(e.name()))
				{
					player.sendMessage(e.name() + " Sucess!");
					return true;
				}
			}

		}
		return true;
	}

	public void openExchanger(Player player)
	{
		player.sendMessage(""+Exchange.LARGE.returnItem.getAmount());
		ItemMenu menu = new ItemMenu("exchange xp for gold", Size.ONE_LINE);
		
		menu.setItem(0, Exchange.SMALL.toMenuItem());
		menu.setItem(4, Exchange.MEDIUM.toMenuItem());
		menu.setItem(8, Exchange.LARGE.toMenuItem());
		menu.open(player);
	}

	enum Exchange
	{
		SMALL(1000, new ItemStack(Material.GOLD_INGOT, 64), Material.GOLD_INGOT),
		MEDIUM(10000, new ItemStack(Material.GOLD_BLOCK, 80), Material.GOLD_BLOCK),
		LARGE(25000, new ItemStack(Material.GOLD_BLOCK, 225), Material.DIAMOND_BLOCK);
		
		private int xpCost;
		private ItemStack returnItem;
		private Material icon;

		Exchange(int xp, ItemStack item, Material icon)
		{
			this.xpCost = xp;
			this.returnItem = item;
			this.icon = icon;
		}

		/**
		 * First letter to uppercase rest is lowercase
		 */
		@Override
		public String toString()
		{
			return this.name().substring(0, 1).toUpperCase() + this.name().substring(1, this.name().length()).toLowerCase();
		}

		public MenuItem toMenuItem()
		{
			String name = this.toString();
			return (new ActionMenuItem(ChatColor.AQUA + this.toString() + " Package", new ItemClickHandler()
			{
				@Override
				public void onItemClick(ItemClickEvent event)
				{
					event.getPlayer().performCommand(CMD + " " + name);
					event.setWillClose(true);
				}
			}, new ItemStack(icon), new String[] {}));
		}
		
		public boolean canTrade(Player player){
			final Inventory inv = player.getInventory();			
			return false;
		}

	}

}
