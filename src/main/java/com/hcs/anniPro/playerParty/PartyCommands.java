package com.hcs.anniPro.playerParty;

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
				if (!PlayerParty.isInATeam(player)) {
					AnniPlayer ap = AnniPlayer.getPlayer(player.getUniqueId());
					AnniTeam at = null;
					try
					{
						at = ap.getTeam();
					} catch (NullPointerException e) {}

					new PlayerParty(player, at);
					sender.sendMessage(ChatColor.DARK_PURPLE + "Succsessfully created a party. Invite with /party invite");
				} else {
					sender.sendMessage(ChatColor.RED + "You cannot create a party when you are a part of one. Do /party leave to leave yours.");
				}
			} else if (args[0].equalsIgnoreCase("leave"))
			{
				if (PlayerParty.isInATeam(player)) {
					PartyListner.playerLeave(player);
				}else{
					sender.sendMessage(ChatColor.RED + "You are not in a party");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("invite"))
			{
				if (args.length < 2){
					//TODO some GUI here
					return false;
				}
				Player target = Bukkit.getServer().getPlayer(args[1]);
				if (target != null)
				{
					String errMsg = checkCanModify(player, target);
					if (errMsg == null)
					{
						PlayerParty.getParty(player).addPlayer(target);
					} else
					{
						sender.sendMessage(errMsg);
					}
				}
			} else if (args[0].equalsIgnoreCase("kick"))
			{
				if (args.length < 2){
					return false;
				}
				Player target = Bukkit.getServer().getPlayer(args[1]);
				if (target != null)
				{
					String errMsg = checkCanModify(player, target);
					if (errMsg == null)
					{
						PlayerParty.getParty(player).removePlayer(target);
					} else
					{
						sender.sendMessage(errMsg);
					}
				}
			} else
			{
				return false;
			}
			return true;
		}
		return false;
	}

	private String checkCanModify(Player invitor, Player target)
	{
		if (!PlayerParty.isInATeam(target))
		{
			if (PlayerParty.isInATeam(invitor))
			{
				PlayerParty pp = PlayerParty.getParty(invitor);
				if (pp.getTeamLeader().equals(invitor))
				{
					return null;
				} else
				{
					return ChatColor.RED + "You cannot invite players when you are not the host.";
				}
			} else
			{
				return ChatColor.RED + "You cannot invite players while you are not in a party.";
			}
		} else
		{
			return ChatColor.RED + "That player is in a party.";
		}
	}
}
