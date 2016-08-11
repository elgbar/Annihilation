package com.hcs.anniPro.playerParty;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;

public class PartyCommands implements CommandExecutor
{
	public PartyCommands(JavaPlugin p)
	{
		p.getCommand("Party").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			if (args.length == 0)
			{
				return false;
			}
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("Create"))
			{
				if (!PlayerParty.isInATeam(player))
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
					sender.sendMessage(ChatColor.DARK_PURPLE + "Succsessfully created a party. Invite with /party invite");
				} else
				{
					sender.sendMessage(ChatColor.RED + "You cannot create a party when you are a part of one. Do /party leave to leave yours.");
				}
			} else if (args[0].equalsIgnoreCase("leave"))
			{
				if (PlayerParty.isInATeam(player))
				{
					PartyListner.playerLeave(player);
					if (!PlayerParties.isPlayerLeader(player))
					{
						sender.sendMessage(ChatColor.DARK_PURPLE + "You left the party.");
					}
				} else
				{
					sender.sendMessage(ChatColor.RED + "You are not in a party");
				}
			} else if (args[0].equalsIgnoreCase("invite"))
			{
				if (args.length < 2)
				{
					// TODO some GUI here
					sender.sendMessage("/party invite [<player>]");
					return true;
				}
				Player target = Bukkit.getServer().getPlayer(args[1]);
				String errMsg = checkBoth(player, target);
				if (errMsg == null)
				{
					PlayerParty.getParty(player).addInvite(target);
					sender.sendMessage(ChatColor.DARK_PURPLE + "You invited " + ChatColor.GOLD + target.getName() + ChatColor.DARK_PURPLE
							+ " to join your party.");
					target.sendMessage(ChatColor.DARK_PURPLE + "You are invited to join " + ChatColor.GOLD + player.getName() + ChatColor.DARK_PURPLE
							+ "'s party. Do " + ChatColor.GOLD + "'/party accept " + player.getName() + "'" + ChatColor.DARK_PURPLE
							+ " to join the party");
				} else
				{

					sender.sendMessage(errMsg.replace("%w", "invite"));
				}
			} else if (args[0].equalsIgnoreCase("kick"))
			{
				if (args.length < 2)
				{
					sender.sendMessage("/party kick [<player>]");
					return true;
				}
				Player target = Bukkit.getServer().getPlayer(args[1]);
				String errMsg = checkBoth(player, target);
				if (errMsg == null)
				{
					PlayerParty.getParty(player).removePlayer(target);
					sender.sendMessage(
							ChatColor.DARK_PURPLE + "You kicked " + ChatColor.GOLD + target.getName() + ChatColor.DARK_PURPLE + " from your party.");
					target.sendMessage(ChatColor.DARK_PURPLE + "You were kicked from your party.");
				} else
				{
					sender.sendMessage(errMsg.replace("%w", "kick"));
				}
			} else if (args[0].equalsIgnoreCase("accept"))
			{
				if (args.length < 2)
				{
					// TODO GUI?
					sender.sendMessage("/party accept [<player>]");
					return true;
				}

				Player teamLeder = Bukkit.getServer().getPlayer(args[1]);
				if (teamLeder != null)
				{
					PlayerParty pp = PlayerParty.getParty(teamLeder);
					if (pp != null)
					{
						if (pp.getIfInvited(player))
						{
							pp.addPlayer(player);
							teamLeder.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.DARK_PURPLE + " joined your party.");
							sender.sendMessage(ChatColor.DARK_PURPLE + "You joined the party");
						} else
						{
							sender.sendMessage(ChatColor.RED + "You are not invited to join this party");
						}
					} else
					{
						sender.sendMessage(ChatColor.RED + "This player does not have a party");
					}

				} else
				{
					sender.sendMessage(ChatColor.RED + "That player does not exist.");
				}
			} else if (args[0].equalsIgnoreCase("cancelInvite"))
			{
				if (args.length < 2)
				{
					sender.sendMessage("/party cancelInvite [<player>]");
					return false;
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
							player.sendMessage(ChatColor.DARK_PURPLE + "Canceled invitation for " + ChatColor.GOLD + target.getName());
							target.sendMessage(ChatColor.DARK_PURPLE + "You are no longer invited to " + ChatColor.GOLD + player.getName()
									+ ChatColor.DARK_PURPLE + "'s party");
						} else
						{
							sender.sendMessage(ChatColor.RED + "That player is not invited to join your party");
						}
					} else
					{
						sender.sendMessage(errMsg.replace("%w", "cancel invitation"));
					}
				} else
				{
					sender.sendMessage(ChatColor.RED + "That player does not exist.");
				}

			} else if (args[0].equalsIgnoreCase("show"))
			{
				if (PlayerParty.isInATeam(player))
				{
					PlayerParty pp = PlayerParty.getParty(player);
					Player leader = pp.getTeamLeader();

					sender.sendMessage(ChatColor.LIGHT_PURPLE + "Host: " + ChatColor.GOLD + leader.getName());

					List<String> players = new ArrayList<String>();
					for (Player p : pp.getPlayers())
					{
						if (!(p.equals(leader)))
						{
							players.add(p.getName());
						}
					}
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "Players: " + ChatColor.YELLOW + players);
					if (player.equals(leader)){
						List<String> invited = new ArrayList<String>();
						for (Player p : pp.getInvited())
						{
							invited.add(p.getName());
						}
						sender.sendMessage(ChatColor.LIGHT_PURPLE + "Invited: " + ChatColor.YELLOW + invited);
					}
				} else
				{
					sender.sendMessage(ChatColor.RED + "You are not in a party");
				}
			} else
			{
				return false;
			}
			return true;
		}
		return false;
	}

	// TODO find a better name here
	private String checkBoth(Player teamLeader, Player target)
	{
		String errMsg = checkCanModify(teamLeader);
		String errMsg2 = checkTarget(teamLeader, target);
		return (errMsg == null) ? ((errMsg2 == null) ? null : errMsg2) : errMsg;

	}

	private String checkTarget(Player teamLeader, Player target)
	{
		if (target == null)
		{
			return ChatColor.RED + "That player does not exist.";
		}
		if (teamLeader.equals(target))
		{
			return ChatColor.RED + "Player argument cannot be yourself.";
		} else if (PlayerParty.isInATeam(target))
		{
			return ChatColor.RED + "That player is in a party.";
		}
		return null;
	}

	/** @param teamLeader
	 *            The player to check if have a party
	 * @return null if teamLeader have a team, String error message if not (with the placeholder <tt>%w</tt> for the activity they tried to do) */
	private String checkCanModify(Player teamLeader)
	{
		if (!PlayerParty.isInATeam(teamLeader))
		{
			return ChatColor.RED + "You cannot %w players while you are not in a party.";
		}
		if (!PlayerParties.isPlayerLeader(teamLeader))
		{
			return ChatColor.RED + "You cannot %w players when you are not the host.";
		}
		return null;
	}
}
