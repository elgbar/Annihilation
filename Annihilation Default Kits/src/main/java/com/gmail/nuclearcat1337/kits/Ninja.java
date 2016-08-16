package com.gmail.nuclearcat1337.kits;
import java.util.ArrayList;
import java.util.List;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.nuclearcat1337.base.AnniKit;

public class Ninja extends AnniKit
{
	
	public ArrayList<Player> inmunePlayers;
	
	@Override
	protected void onInitialize()
	{
		inmunePlayers = new ArrayList<Player>();
	}

	@Override
	protected ItemStack specialItem()
	{
		ItemStack firestorm  = KitUtils.addSoulbound(new ItemStack(Material.GOLD_BOOTS));
		ItemMeta meta = firestorm.getItemMeta();
		meta.setDisplayName(getSpecialItemName()+" "+ChatColor.GREEN+"READY");
		firestorm.setItemMeta(meta);
		return firestorm;
	}

	@Override
	protected String defaultSpecialItemName()
	{
		return ChatColor.AQUA+"Ninja Ability";
	}

	@Override
	protected boolean isSpecialItem(ItemStack stack)
	{
		if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
		{
			String name = stack.getItemMeta().getDisplayName();
			if(name.contains(getSpecialItemName()) && KitUtils.isSoulbound(stack))
				return true;
		}
		return false;
	}

	@Override
	protected long getDelayLength()
	{
		return 40000;
	}

	@Override
	protected String getInternalName()
	{
		return "Ninja";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.GOLD_BOOTS);
	}

	@Override
	protected List<String> getDefaultDescription()
	{
        final List<String> toReturn = new ArrayList<String>();
        final ChatColor aqua = ChatColor.AQUA;
        toReturn.add(aqua + "You are the ninja.");
        toReturn.add(" ");
        toReturn.add(aqua + "Use your special abilities");
        toReturn.add(aqua + "to bypass");
        toReturn.add(aqua + "your enemies' defenses and gain");
        toReturn.add(aqua + "an advantage over the enemy");
        toReturn.add(" ");
        toReturn.add(aqua + "You have been trained well");
        toReturn.add(aqua + "and you are now immune to");
        toReturn.add(aqua + "fall damage");
        toReturn.add(aqua + "Your boots allow you to give");
        toReturn.add(aqua + "yourself a jump boost");
        return toReturn;
	}

	@Override
	public void cleanup(Player player)
	{
		
	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addGoldSword().addWoodPick().addWoodAxe().addItem(super.getSpecialItem());
	}

	@Override
	protected boolean useDefaultChecking()
	{
		return true;
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			AnniPlayer aP = AnniPlayer.getPlayer(p.getUniqueId());
			if(aP.getKit().equals(this) || inmunePlayers.contains(p))
			{
				if(e.getCause() == DamageCause.FALL)
				{
					e.setCancelled(true);
					if(inmunePlayers.contains(p))
					{
						inmunePlayers.remove(p);
					}
				}
			}
		}else
			return;
	}

	@Override
	protected boolean performPrimaryAction(Player player, AnniPlayer p) {
		if(p.getTeam() != null)
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20, 2));
			return true;
		}
		else return false;
	}

	@Override
	protected boolean performSecondaryAction(Player player, AnniPlayer p) 
	{
		if(p.getTeam() != null)
		{
			player.sendMessage(ChatColor.GREEN + "You have given fall damage inmunity to players around you!");
			for(Entity e : player.getNearbyEntities(3, 3, 3))
			{
				if(!(e instanceof Player))
				{
					return false;
				}
				Player pl = (Player) e;
				if(player != pl)
				{
					this.inmunePlayers.add(pl);
					pl.sendMessage(ChatColor.GREEN + "You have been given inmunity to your next fall damage by the ninja " + ChatColor.DARK_GREEN + player.getName() + ChatColor.GREEN + "!");
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean passive() {
		// TODO Auto-generated method stub
		return false;
	}
}