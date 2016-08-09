package com.hcs.anniPro.playerParty;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;

public class PartyCommands implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.toString().equals("Party")){
			if (!(sender instanceof Player))
			{
				if (args.length == 0)
				{
					return false;
				} else
				{
					Player player = (Player) sender;
					if (args[0].equalsIgnoreCase("Create")){
						
						AnniPlayer ap = AnniPlayer.getPlayer(player.getUniqueId());
						AnniTeam at = null;
						try
						{
							at = ap.getTeam();
						} catch (NullPointerException e)
						{
						}
						
						//create a new party
						if (!PlayerParty.isInATeam(player))
							new PlayerParty(player, at);
						
						return true;
					}
				}
			} else
				sender.sendMessage(ChatColor.RED + "You must be a player to use /Party");
		}
		return false;
	}
}
