package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.AnniKit;

public class Swapper extends AnniKit
{
	@Override
	protected void onInitialize()
	{
	}

	@Override
	protected ItemStack specialItem()
	{
		ItemStack swapper = KitUtils.addSoulbound(new ItemStack(Material.GREEN_RECORD));
		ItemMeta meta = swapper.getItemMeta();
		meta.setDisplayName(getSpecialItemName() + " " + ChatColor.GREEN + "READY");
		swapper.setItemMeta(meta);
		return swapper;
	}

	@Override
	protected String defaultSpecialItemName()
	{
		return ChatColor.AQUA + "Swapper";
	}

	@Override
	protected boolean isSpecialItem(ItemStack stack)
	{
		if (stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
		{
			String name = stack.getItemMeta().getDisplayName();
			if (name.contains(getSpecialItemName()) && KitUtils.isSoulbound(stack))
				return true;
		}
		return false;
	}

	@Override
	protected boolean performPrimaryAction(Player player, AnniPlayer p)
	{
		if (p.getTeam() != null)
		{
			Player e = instance.getPlayerInSight(player, 15);
			if (e != null)
			{
				AnniPlayer pl = AnniPlayer.getPlayer(e.getUniqueId());
				if (pl != null && !pl.getTeam().equals(p.getTeam()))
				{
					Location playerLoc = player.getLocation().clone();
					Location entityLoc = e.getLocation().clone();

					Vector playerLook = playerLoc.getDirection();
					Vector playerVec = playerLoc.toVector();
					Vector entityVec = entityLoc.toVector();
					Vector toVec = playerVec.subtract(entityVec).normalize();

					e.teleport(playerLoc.setDirection(playerLook.normalize()));
					player.teleport(entityLoc.setDirection(toVec));
					e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 1));
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected long getDelayLength()
	{
		return 20000;
	}

	@Override
	protected String getInternalName()
	{
		return "Swapper";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.GREEN_RECORD);
	}

	@Override
	protected List<String> getDefaultDescription()
	{
		List<String> l = new ArrayList<String>();
		addToList(l, aqua + "The swapper is able to", aqua + "swap places with a nearby", aqua + "enemy every 20 seconds.", "",
				aqua + "The enemy that is swapped", aqua + "has slowness 2 applied", aqua + "for three seconds.", "", aqua + "The swapper is a team",
				aqua + "based ganking class that", aqua + "is best suited for players", aqua + "moving in groups and can",
				aqua + "be used to bring enemies", aqua + "to a location or bring", aqua + "yourself to a more advantageous",
				aqua + "position once held by", aqua + "your enemy.");
		return l;
	}

	@Override
	public void cleanup(Player arg0)
	{
	}

	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addWoodSword().addWoodPick().addWoodAxe().addItem(super.getSpecialItem());
	}

	@Override
	protected boolean useDefaultChecking()
	{
		return true;
	}

	@Override
	protected boolean performSecondaryAction(Player player, AnniPlayer p)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean passive()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
