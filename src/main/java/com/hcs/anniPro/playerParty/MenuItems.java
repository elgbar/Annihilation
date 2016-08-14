package com.hcs.anniPro.playerParty;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.itemMenus.ActionMenuItem;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickHandler;
import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;

public class MenuItems
{
	private final static MenuItem create = (new ActionMenuItem(ChatColor.AQUA + "Create Party", new ItemClickHandler()
	{
		@Override
		public void onItemClick(ItemClickEvent event)
		{
			event.getPlayer().performCommand("party create");
			event.setWillClose(true);
		}
	}, new ItemStack(Material.CHEST), new String[] {}));

	private final static MenuItem invite = (new ActionMenuItem(ChatColor.AQUA + "Invite Players", new ItemClickHandler()
	{
		@Override
		public void onItemClick(ItemClickEvent event)
		{
			event.getPlayer().performCommand("party invite");
			event.setWillClose(true);
		}
	}, new ItemStack(Material.BOOK_AND_QUILL), new String[] {}));

	private final static MenuItem kick = (new ActionMenuItem(ChatColor.AQUA + "Kick Players", new ItemClickHandler()
	{
		@Override
		public void onItemClick(ItemClickEvent event)
		{
			event.getPlayer().performCommand("party kick");
			event.setWillClose(true);
		}
	}, new ItemStack(Material.LEATHER_BOOTS), new String[] {}));

	private final static MenuItem leave = (new ActionMenuItem(ChatColor.AQUA + "Leave Party", new ItemClickHandler()
	{
		@Override
		public void onItemClick(ItemClickEvent event)
		{
			event.getPlayer().performCommand("party leave");
			event.setWillClose(true);
		}
	}, new ItemStack(Material.TNT), new String[] {}));

	private final static MenuItem accept = (new ActionMenuItem(ChatColor.AQUA + "Accept invite", new ItemClickHandler()
	{
		@Override
		public void onItemClick(ItemClickEvent event)
		{
			event.getPlayer().performCommand("party accept");
			event.setWillClose(true);
		}
	}, new ItemStack(Material.IRON_CHESTPLATE), new String[] {}));

	private final static MenuItem view = (new ActionMenuItem(ChatColor.AQUA + "View Your Party", new ItemClickHandler()
	{
		@Override
		public void onItemClick(ItemClickEvent event)
		{
			event.getPlayer().performCommand("party show");
			event.setWillClose(true);
		}
	}, new ItemStack(Material.BOOK), new String[] {}));

	private final static MenuItem cancelInvite = (new ActionMenuItem(ChatColor.AQUA + "Cancel Invitations", new ItemClickHandler()
	{
		@Override
		public void onItemClick(ItemClickEvent event)
		{
			event.getPlayer().performCommand("party cancelInvite");
			event.setWillClose(true);
		}
	}, new ItemStack(Material.BARRIER), new String[] {}));

	/**
	 * @return the cancelInvite
	 */
	public static MenuItem getCancelInviteItem()
	{
		return cancelInvite;
	}

	/**
	 * @return the view
	 */
	public static MenuItem getViewItem()
	{
		return view;
	}

	/**
	 * @return the accept
	 */
	public static MenuItem getAcceptItem()
	{
		return accept;
	}

	/**
	 * @return the leave
	 */
	public static MenuItem getLeaveItem()
	{
		return leave;
	}

	/**
	 * @return the kick
	 */
	public static MenuItem getKickItem()
	{
		return kick;
	}

	/**
	 * @return the invite
	 */
	public static MenuItem getInviteItem()
	{
		return invite;
	}

	/**
	 * @return the create
	 */
	public static MenuItem getCreateItem()
	{
		return create;
	}

}
