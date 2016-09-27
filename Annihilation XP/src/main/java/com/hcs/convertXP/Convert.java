package com.hcs.convertXP;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
import com.gmail.nuclearcat1337.xpSystem.utils.Acceptor;
import com.gmail.nuclearcat1337.xpSystem.xp.XPSystem;

/**
 * Converts annihilation xp to in game gold
 * 
 * @author kh498
 */
public class Convert implements CommandExecutor
{
	//	private Plugin plugin;
	private static String CMD = "convert";
	private static XPSystem xpSystem;

	public Convert (JavaPlugin plugin, XPSystem system)
	{
		plugin.getCommand (CMD).setExecutor (this);
		Convert.xpSystem = system;
		//		this.plugin = plugin;
	}

	@ Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			//display gui when there is no arguments
			if (args.length == 0)
			{
				openExchanger (player);
				return true;
			}
			//check if one of the arguments match the enums
			for (Exchange e : Exchange.values ())
			{
				if (args[0].equalsIgnoreCase (e.name ()))
				{
					if (e.hasEnoughSpace (player))
					{
						if (xpSystem.isActive ())
						{
							//Get the player's xp  (this takes some times as we are speaking to an external db)
							xpSystem.getXP (player.getUniqueId (), new Acceptor<Integer> ()
							{
								@ Override
								public void accept (Integer amount)
								{

									if (amount >= e.getXpCost ())
									{
										player.getInventory ().addItem (e.getItem ());
										xpSystem.removeXP (player.getUniqueId (), e.getXpCost ());
										player.sendMessage (ChatColor.GREEN + "You successfully bought " + e.getItemName () + "!");
									} else
									{
										player.sendMessage (
												ChatColor.RED + "You do not have enough Annihilation xp to buy " + e.getItemName () + ".");
									}
								}
							});
						}

					} else
					{
						player.sendMessage (ChatColor.RED + "You do not have the space to buy " + e.getItemName () + ".");
					}
					break;
				}
			}

		}
		return true;
	}

	/**
	 * Opens a shop GUI to the player
	 * 
	 * @param player
	 *            The player that will see the gui
	 */
	public void openExchanger (Player player)
	{
		ItemMenu menu = new ItemMenu ("Gold Shop", Size.ONE_LINE);

		menu.setItem (0, Exchange.SMALL.toMenuItem ());
		menu.setItem (4, Exchange.MEDIUM.toMenuItem ());
		menu.setItem (8, Exchange.LARGE.toMenuItem ());
		menu.open (player);
	}

	/**
	 * All possible exchanges that can be made from Anni xp to gold (as item)
	 * 
	 * @author kh498
	 */
	enum Exchange
	{
		SMALL(1000, new ItemStack (Material.GOLD_INGOT, 64), Material.GOLD_INGOT),
		MEDIUM(10000, new ItemStack (Material.GOLD_BLOCK, 80), Material.GOLD_BLOCK),
		LARGE(25000, new ItemStack (Material.GOLD_BLOCK, 224), Material.DIAMOND_BLOCK);

		private final int xpCost;
		private final ItemStack returnItem;
		private final Material icon;

		Exchange (int xp, ItemStack item, Material icon)
		{
			this.xpCost = xp;
			this.returnItem = item;
			this.icon = icon;
		}

		/**
		 * @return <tt>this.name()</tt> with the first case uppercase
		 */
		@ Override
		public String toString ()
		{
			return this.name ().substring (0, 1).toUpperCase () + this.name ().substring (1, this.name ().length ()).toLowerCase ();
		}

		/**
		 * @return The MenuItem of the exchange item
		 */
		public MenuItem toMenuItem ()
		{
			String name = this.toString ();
			String[] lore = new String[] { ChatColor.GRAY + "Buy " + this.getItemName () + " for " + this.getXpCost () + " xp" };
			//			lore[0] = "Buy " + this.getItemName () + " for " + this.getXpCost () + " xp";

			return (new ActionMenuItem (ChatColor.AQUA + this.toString () + " Package", new ItemClickHandler ()
			{
				@ Override
				public void onItemClick (ItemClickEvent event)
				{
					event.getPlayer ().performCommand (CMD + " " + name);
					event.setWillClose (true);

				}
			}, new ItemStack (icon), lore));
		}

		/**
		 * Check if a player have enough xp to trade
		 * 
		 * @param player
		 *            The player to check
		 * @return True if the amount of xp needed is equal to less than the player's xp
		 */
		public boolean hasEnoughXp (Player player)
		{

			return false;
		}

		/**
		 * Check if a player have enough xp to trade.
		 * 
		 * @param player
		 *            The player to check
		 * @return True if the player can trade
		 */
		public boolean hasEnoughSpace (Player player)
		{
			final Inventory inv = player.getInventory ();
			final ItemStack item = this.returnItem;

			//There is an empty slot and you can fit the whole item in the slot
			if (inv.firstEmpty () != -1 && item.getAmount () <= item.getMaxStackSize ())
			{
				return true;
			}

			int nrOfEmptySlots = 0;
			for (Iterator<ItemStack> i = inv.iterator (); i.hasNext ();)
			{
				if (i.next () == null)
				{
					nrOfEmptySlots++;
				}
			}

			if (nrOfEmptySlots >= Math.ceil (item.getAmount () / item.getMaxStackSize ()))
			{
				return true;
			}
			return false;
		}

		/**
		 * @return The amount of xp it costs
		 */
		public int getXpCost ()
		{
			return xpCost;
		}

		/**
		 * @return The item to return upon a sucsessfull trade
		 */
		public ItemStack getItem ()
		{
			return returnItem;
		}

		/**
		 * @return A propper name for the return item
		 */
		public String getItemName ()
		{
			return this.getItem ().getAmount () + " " + this.getItem ().getType ().toString ().replace ("_", " ").toLowerCase () + "s";
			//			return name + (this.getItem ().getAmount () > 1) != null
			//					? ("s").equalsIgnoreCase (name.substring (name.length () - 1, name.length ())) ? "'" : "'s" : "";
		}

		/**
		 * @return The item to return upon a sucsessfull trade
		 */
		public Material getIcon ()
		{
			return icon;
		}

	}

}
