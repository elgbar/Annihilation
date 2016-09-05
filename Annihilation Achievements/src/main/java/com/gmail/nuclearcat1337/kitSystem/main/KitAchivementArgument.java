package com.gmail.nuclearcat1337.kitSystem.main;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;
import com.gmail.nuclearcat1337.anniPro.kits.Kit;
import com.gmail.nuclearcat1337.anniPro.main.AnniArgument;
import com.gmail.nuclearcat1337.anniPro.utils.IDTools;
import com.gmail.nuclearcat1337.kitSystem.kitAchievement.KitAchivement;
import com.gmail.nuclearcat1337.kitSystem.kitAchievement.KitSystem;
import com.google.common.base.Predicate;

public class KitAchivementArgument implements AnniArgument
{
	private KitSystem kitSystem;
	public KitAchivementArgument(KitSystem system)
	{
		this.kitSystem = system;
	}
	
	@Override
	public void executeCommand(final CommandSender sender, String label, final String[] args)
	{
		if(args != null && args.length > 3)
		{
			IDTools.getUUID(args[2], new Predicate<UUID>(){
				@Override
				public boolean apply(UUID id)
				{
					if(id != null)
					{
						Kit kit = Kit.getKit(args[1]);
						if(kit != null)
						{
							int num = 1;
							try{
								  num = Integer.parseInt(args[3]);
								} catch (NumberFormatException ignore) {
									
								}
							if(args[0].equalsIgnoreCase("add"))
							{	
								kitSystem.addKitAchivementProgess(id, KitAchivement.toKitAchivement(kit), num);
								sender.sendMessage(ChatColor.GREEN + "" + num + " units added to " + kit.getName());
//								sender.sendMessage("Added kit "+kit.getName());
							}
							else if(args[0].equalsIgnoreCase("remove"))
							{
								kitSystem.removeKitAchivementProgess(id, KitAchivement.toKitAchivement(kit), num);
								sender.sendMessage(ChatColor.GREEN + "" + num + " units removed from " +kit.getName());
//								sender.sendMessage("Removed kit "+kit.getName());
							}
							else 
								sender.sendMessage(ChatColor.RED+"Operation "+ChatColor.GOLD+args[0]+ChatColor.RED+" is not supported.");
						}
						else 
							sender.sendMessage(ChatColor.RED+"Could not locate the kit you specified.");
					}
					else 
						sender.sendMessage(ChatColor.RED+"Could not locate the player you specified.");
					return false;
				}});			
		}
	}

	@Override
	public String getArgumentName()
	{
		return "KitAchievement";
	}

	@Override
	public String getHelp()
	{			
		return ChatColor.LIGHT_PURPLE+"KitAchievement [add,remove] <kit> <player> <number>--"+ChatColor.GREEN+"adds or removes a number of units from a player.";
	}

	@Override
	public MenuItem getMenuItem()
	{
		return null;
	}

	@Override
	public String getPermission()
	{
		return null;
	}

	@Override
	public boolean useByPlayerOnly()
	{
		return false;
	}
}
