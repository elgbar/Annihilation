package com.hcs.anniPro.playerParty;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ActionMenuItem;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickHandler;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu.Size;
import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;
import com.gmail.nuclearcat1337.anniPro.kits.CustomItem;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.main.Lang;
import com.hcs.anniPro.util.Math2;

public class PartyCommands implements CommandExecutor, Listener
{
	private final String MAGIC_REOPEN_KEY = "openAgain";

	private final Plugin plugin;

	public PartyCommands(JavaPlugin plugin)
	{
		plugin.getCommand("Party").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void openMenuCheck(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			final Player player = event.getPlayer();
			if (KitUtils.itemHasName(player.getItemInHand(), CustomItem.PARTYMAP.getName()))
			{
				event.getPlayer().performCommand("party");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		PlayerParties.playerLeave(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKicked(PlayerKickEvent event)
	{
		PlayerParties.playerLeave(event.getPlayer());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (args.length == 0)
			{
				ItemMenu menu = new ItemMenu(Lang.PLAYERPARTY.toString(), Size.ONE_LINE);
				if (!Game.isGameRunning())
				{

					if (!PlayerParty.isInAParty(player))
					{
						menu.setItem(0, MenuItems.getAcceptItem());
						menu.setItem(8, MenuItems.getCreateItem());
					} else
					{
						menu.setItem(0, MenuItems.getViewItem());
						menu.setItem(8, MenuItems.getLeaveItem());
					}

					if (PlayerParties.isPlayerLeader(player))
					{
						menu.setName(Lang.PLAYERPARTY.toString() + " - " + Lang.HOST.toString());
						menu.setItem(3, MenuItems.getInviteItem());
						menu.setItem(4, MenuItems.getKickItem());
						menu.setItem(5, MenuItems.getCancelInviteItem());
					}
				} else if (PlayerParty.isInAParty(player))
				{
					menu.setItem(4, MenuItems.getViewItem());
				} else
				{
					player.sendMessage(Lang.NOCREATE.toString());
					return true;
				}
				menu.open(player);
				return true;
			}

			if (Game.isGameRunning() && !args[0].equalsIgnoreCase("show"))
			{
				player.sendMessage(Lang.NOEDIT.toString());
				return true;
			}
			if (args[0].equalsIgnoreCase("Create"))
			{
				if (!PlayerParty.isInAParty(player))
				{
					AnniPlayer ap = AnniPlayer.getPlayer(player.getUniqueId());
					AnniTeam at = null;
					try
					{
						at = ap.getTeam();
					} catch (NullPointerException e)
					{
					}

					new PlayerParty(player, at);
					sender.sendMessage(Lang.CREATESUCCESS.toString());

					delayedOpenGUI(player, null);

				} else
				{
					sender.sendMessage(Lang.CREATEFAILED.toString());
				}
			} else if (args[0].equalsIgnoreCase("leave"))
			{
				if (PlayerParty.isInAParty(player))
				{
					if (!PlayerParties.isPlayerLeader(player))
					{
						sender.sendMessage(Lang.LEFTPARTY.toString());
					}
					PlayerParties.playerLeave(player);
				} else
				{
					sender.sendMessage(Lang.NOPARTY.toString());
				}
			} else if (args[0].equalsIgnoreCase("invite"))
			{
				String modifyErr = checkCanModify(player);
				if (modifyErr != null)
				{
					sender.sendMessage(modifyErr);
					return true;
				}
				if (args.length < 2)
				{
					List<Player> playerList = new ArrayList<Player>();
					PlayerParty pp = PlayerParty.getParty(player);
					List<OfflinePlayer> invitedPlayers = pp.getInvited();
					for (Player p : Bukkit.getServer().getOnlinePlayers())
					{
						if (!PlayerParty.isInAParty(p) && !invitedPlayers.contains(p))
						{
							playerList.add(p);
						}
					}
					if (playerList.isEmpty())
					{
						sender.sendMessage(Lang.NOONETOINVITE.toString());
					} else
					{
						ItemMenus(Lang.INVITEPLAYERS.toString(), args[0], playerList, player, Lang.HEADLOREINVITE.toStringArray());
					}
					return true;
				}
				Player target = Bukkit.getServer().getPlayer(args[1]);
				String errMsg = checkTarget(player, target, false);
				if (errMsg == null)
				{
					PlayerParty.getParty(player).addInvite(target);
					sender.sendMessage(Lang.INVITEHOST.toStringReplacement(target.getName()));
					target.sendMessage(Lang.INVITETARGET.toStringReplacement(player.getName()));
					if (args.length == 3 && args[2].equals(MAGIC_REOPEN_KEY))
					{
						delayedOpenGUI(player, args[0]);
					}
				} else
				{

					sender.sendMessage(errMsg);
				}
			} else if (args[0].equalsIgnoreCase("kick"))
			{
				String modifyErr = checkCanModify(player);
				if (modifyErr != null)
				{
					sender.sendMessage(modifyErr);
					return true;
				}
				if (args.length < 2)
				{
					PlayerParty pp = PlayerParty.getParty(player);
					List<Player> list = pp.getPlayers();
					list.remove(player); // We can do this safely because the leader will always be in the list
					if (list.isEmpty())
					{
						sender.sendMessage(Lang.NOPLAYERSINPARTY.toString());
					} else
					{
						ItemMenus(Lang.KICKPLAYERS.toString(), args[0], list, player, Lang.HEADLOREKICK.toStringArray());
					}
					return true;
				}
				Player target = Bukkit.getServer().getPlayer(args[1]);
				String errMsg = checkTarget(player, target, true);
				if (errMsg == null)
				{
					PlayerParty.getParty(player).removePlayer(target);
					sender.sendMessage(Lang.KICKEDAPLAYER.toStringReplacement(target.getName()));
					target.sendMessage(Lang.PLAYERKICKED.toStringReplacement(player.getName()));
					if (args.length == 3 && args[2].equals(MAGIC_REOPEN_KEY))
					{
						delayedOpenGUI(player, args[0]);
					}
				} else
				{
					sender.sendMessage(errMsg);
				}
			} else if (args[0].equalsIgnoreCase("accept"))
			{
				if (PlayerParties.isPlayerLeader(player) || PlayerParty.isInAParty(player))
				{
					sender.sendMessage(Lang.NOJOINWHENINPARTY.toString());
					return true;
				}
				if (args.length < 2)
				{
					List<Player> playerList = new ArrayList<Player>();
					for (Entry<UUID, PlayerParty> entry : PlayerParties.getParties().entrySet())
					{
						PlayerParty pp = entry.getValue();
						if (pp.getIfInvited(player))
						{
							playerList.add(pp.getPartyLeader());
						}
					}
					if (playerList.isEmpty())
					{
						sender.sendMessage(Lang.NOTINVITED.toString());
					} else
					{
						ItemMenus(Lang.ACCEPTINVITE.toString(), args[0], playerList, player, Lang.HEADLOREACCEPT.toStringArray());
					}
					return true;
				}

				Player teamLeader = Bukkit.getServer().getPlayer(args[1]);
				if (teamLeader != null)
				{
					PlayerParty pp = PlayerParty.getParty(teamLeader);
					if (pp != null)
					{
						if (pp.getIfInvited(player))
						{
							pp.addPlayer(player);
							teamLeader.sendMessage(Lang.ACCEPTHOST.toStringReplacement(player.getName()));
							sender.sendMessage(Lang.ACCEPTPLAYER.toStringReplacement(teamLeader.getName()));
						} else
						{
							sender.sendMessage(Lang.NOTINVITEDPLAYER.toString());
						}
					} else
					{
						sender.sendMessage(Lang.TARGETHAVENOPARTY.toString());
					}

				} else
				{
					sender.sendMessage(Lang.NOSUCHPLAYER.toString());
				}
			} else if (args[0].equalsIgnoreCase("cancelInvite"))
			{
				String modifyErr = checkCanModify(player);
				if (modifyErr != null)
				{
					sender.sendMessage(modifyErr);
					return true;
				}
				if (args.length < 2)
				{
					PlayerParty pp = PlayerParty.getParty(player);
					if (pp.getInvited().isEmpty())
					{
						sender.sendMessage(Lang.NOPENDINGINVITES.toString());
					} else
					{
						List<Player> list = new ArrayList<Player>();
						for (OfflinePlayer op : pp.getInvited())
						{
							if (op.isOnline())
							{
								list.add(op.getPlayer());
							}
						}
						ItemMenus(Lang.CANCELINVITE.toString(), args[0], list, player, Lang.HEADLORECANCELINV.toStringArray());
					}
					return true;
				}
				Player target = Bukkit.getServer().getPlayer(args[1]);
				if (target != null)
				{
					String errMsg = checkCanModify(player);
					if (errMsg == null)
					{
						PlayerParty pp = PlayerParty.getParty(player);
						if (pp.getIfInvited(target))
						{
							pp.removeInvited(target);
							player.sendMessage(Lang.NOLONGERINVITEDHOST.toStringReplacement(target.getName()));
							target.sendMessage(Lang.NOLONGERINVITEDTARGET.toStringReplacement(player.getName()));
							if (args.length == 3 && args[2].equals(MAGIC_REOPEN_KEY))
							{
								delayedOpenGUI(player, args[0]);
							}
						} else
						{
							sender.sendMessage(Lang.NOTINVITEDTARGET.toString());
						}
					} else
					{
						sender.sendMessage(errMsg);
					}
				} else
				{
					sender.sendMessage(Lang.NOSUCHPLAYER.toString());
				}

			} else if (args[0].equalsIgnoreCase("show"))
			{
				if (PlayerParty.isInAParty(player))
				{
					PlayerParty pp = PlayerParty.getParty(player);
					Player leader = pp.getPartyLeader();

					sender.sendMessage(ChatColor.LIGHT_PURPLE + Lang.HOST.toString() + ": " + ChatColor.GOLD + leader.getName());

					List<String> players = new ArrayList<String>();
					for (Player p : pp.getPlayers())
					{
						if (!(p.equals(leader)))
						{
							players.add(p.getName());
						}
					}
					sender.sendMessage(ChatColor.LIGHT_PURPLE + Lang.PLAYERS.toString() + ": " + ChatColor.YELLOW + players);
					if (player.equals(leader))
					{
						List<String> invited = new ArrayList<String>();
						for (OfflinePlayer p : pp.getInvited())
						{
							invited.add(p.getName());
						}
						sender.sendMessage(ChatColor.LIGHT_PURPLE + Lang.INVITED.toString() + ": " + ChatColor.YELLOW + invited);
					}
				} else
				{
					sender.sendMessage(Lang.NOPARTY.toString());
				}
				/*
				 * Used for testing the system
				 */
				//			} else if (args[0].equalsIgnoreCase("test"))
				//			{
				//				if (args.length < 2)
				//				{
				//					List<Player> playerList = new ArrayList<Player>();
				//					for (int i = 0; i < 100; i++)
				//					{
				//						playerList.add(player);
				//					}
				//					if (playerList.isEmpty())
				//					{
				//						sender.sendMessage(ChatColor.RED + "You are not invited to any parties :(");
				//					} else
				//					{
				//						ItemMenus("Test", "test", playerList, player, ChatColor.GRAY + "Click on a player's head ",
				//								ChatColor.GRAY + "to accept the host's inviation");
				//					}
				//					return true;
				//				}
				//				sender.sendMessage(args[1]);
			} else
			{
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Opens a chest with five rows of players heads that execute a subcommand of /party with the first agument bing subCommand and the second being
	 * the player that the user clicked on
	 * 
	 * @param title
	 *            The tittle of the gui
	 * @param subCommand
	 *            the party sub command to execute
	 * @param list
	 *            the list of players to view in the gui
	 * @param player
	 *            the player to show it to
	 */
	private void ItemMenus(String title, String subCommand, List<Player> list, Player player, String... lore)
	{
		if (list.isEmpty() || subCommand == null)
		{
			return;
		}
		int pages = Math2.dividedRoundedUp(list.size(), 45); // 45 = five lines of chest space
		List<ItemMenu> menus = new ArrayList<ItemMenu>();
		List<MenuItem> heads = new ArrayList<MenuItem>();

		MenuItem nextButton = (new ActionMenuItem(Lang.NEXTPAGE.toString(), new ItemClickHandler()
		{
			@Override
			public void onItemClick(ItemClickEvent event)
			{
				Player player = event.getPlayer();

				int newPage = 0;
				if (player.hasMetadata(subCommand))
				{
					int currPage = player.getMetadata(subCommand).get(0).asInt();
					if (currPage <= pages)
					{
						newPage = ++currPage;
					}

				}
				player.setMetadata(subCommand, new FixedMetadataValue(plugin, newPage));
				event.setWillClose(true);
				player.performCommand("party " + subCommand);
			}
		}, new ItemStack(Material.SLIME_BALL), new String[] {}));

		MenuItem prevButton = (new ActionMenuItem(Lang.PREVPAGE.toString(), new ItemClickHandler()
		{
			@Override
			public void onItemClick(ItemClickEvent event)
			{
				Player player = event.getPlayer();

				int newPage = 0;
				if (player.hasMetadata(subCommand))
				{
					int currPage = player.getMetadata(subCommand).get(0).asInt();
					if (currPage > 0)
					{
						newPage = --currPage;
					}
				}
				player.setMetadata(subCommand, new FixedMetadataValue(plugin, newPage));
				event.setWillClose(true);
				player.performCommand("party " + subCommand);
			}
		}, new ItemStack(Material.MAGMA_CREAM), new String[] {}));

		MenuItem exitButton = (new ActionMenuItem(Lang.EXITGUI.toString(), new ItemClickHandler()
		{
			@Override
			public void onItemClick(ItemClickEvent event)
			{
				Player player = event.getPlayer();
				player.removeMetadata(subCommand, plugin);
				player.removeMetadata(subCommand + "opened", plugin);
				event.setWillClose(true);
			}
		}, new ItemStack(Material.BARRIER), new String[] {}));

		String openGUI = (list.size() == 1) ? "" : MAGIC_REOPEN_KEY;

		for (Player p : list)
		{
			heads.add(new ActionMenuItem(ChatColor.WHITE + title + " " + p.getName(), new ItemClickHandler()
			{
				@Override
				public void onItemClick(ItemClickEvent event)
				{
					event.getPlayer().performCommand("party " + subCommand + " " + p.getName() + " " + openGUI);
					event.setWillClose(true);
				}
			}, getPlayerSkull(p), lore));
		}

		for (int i = 0; i < pages; i++)
		{
			ItemMenu menu = new ItemMenu(title + " - " + Lang.PAGE.toString() + " " + (i + 1) + "/" + pages, Size.SIX_LINE); // five for players, one for changing pagees
			int pos;
			for (int l = 0; l < 45; l++)
			{
				pos = l + (45 * i);
				if (heads.size() > pos)
				{
					menu.setItem(l, heads.get(pos));
				}

			}
			for (int m = 1; m < 8; m++)
			{
				if (m != 4)
				{ // 45 + 4 = 49, where the exitbutton is
					menu.setItem(m + 45, MenuItems.getGrayPanel());
				}
			}
			menu.setItem(45, prevButton);
			menu.setItem(49, exitButton);
			menu.setItem(53, nextButton);
			menus.add(menu);
		}

		boolean reset = true;
		if (player.hasMetadata(subCommand + "opened"))
		{

			long lastOpened;
			long now = System.currentTimeMillis();
			try
			{
				lastOpened = player.getMetadata(subCommand + "opened").get(0).asLong();
			} catch (Exception e)
			{
				lastOpened = now - 61 * 1000; // let it reset
			}
			reset = now - lastOpened > 1000 * 45; // 45 seconds
		}

		int page = 0;
		if (player.hasMetadata(subCommand) && !reset)
		{

			try
			{
				page = player.getMetadata(subCommand).get(0).asInt();
			} catch (IndexOutOfBoundsException e)
			{
			}

			if (menus.size() - 1 < page)
			{
				page = menus.size() - 1;
			}
		}

		player.setMetadata(subCommand, new FixedMetadataValue(plugin, page));
		player.setMetadata(subCommand + "opened", new FixedMetadataValue(plugin, System.currentTimeMillis()));

		if (menus == null || menus.isEmpty())
		{
			return;
		}

		ItemMenu currMenu = menus.get(page);

		// need to wait for the other GUI to close before opening this one
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{
			public void run()
			{
				currMenu.open(player);
			}
		}, 2L); // 2 ticks

		return;
	}

	/**
	 * @param player
	 *            Player to get skull of
	 * @return The skull of the player
	 */
	private ItemStack getPlayerSkull(Player player)
	{
		checkNotNull(player, "player");
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();

		meta.setOwner(player.getName());
		skull.setItemMeta(meta);

		return skull;
	}

	/**
	 * Check if a player is a valid party player
	 * 
	 * @param partyLeader
	 *            The party host
	 * @param target
	 *            The target to check
	 * @param inPartyIsGood
	 *            If it's good or bad that a player is in a team
	 * @return An error message or <tt>null</tt> if everything checks out
	 */
	private String checkTarget(Player partyLeader, Player target, Boolean inPartyIsGood)
	{
		if (target == null)
		{
			return Lang.NOSUCHPLAYER.toString();
		}
		if (partyLeader.equals(target))
		{
			return Lang.NOTYOURSELF.toString();
		}

		if (PlayerParty.isInAParty(target))
		{
			if (!inPartyIsGood)
			{
				return Lang.TARGETHAVEPARTY.toString();
			}
		} else
		{
			if (inPartyIsGood)
			{
				return Lang.TARGETDONOTHAVEPARTY.toString();
			}
		}
		return null;
	}

	/**
	 * @param partyLeader
	 *            The player to check if have a party
	 * @return <tt>null</tt> if the argument player have a party, String error message if not
	 */
	private String checkCanModify(Player partyLeader)
	{
		if (!PlayerParty.isInAParty(partyLeader))
		{
			return Lang.NOPARTY.toString();
		}
		if (!PlayerParties.isPlayerLeader(partyLeader))
		{
			return Lang.NOTHOST.toString();
		}
		return null;
	}

	private void delayedOpenGUI(Player player, @Nullable String subCommand)
	{
		checkNotNull(player, "player");
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{
			public void run()
			{
				if (subCommand == null)
				{
					player.performCommand("party");
				} else
				{
					player.performCommand("party " + subCommand);
				}
			}
		}, 2L);

	}

}
