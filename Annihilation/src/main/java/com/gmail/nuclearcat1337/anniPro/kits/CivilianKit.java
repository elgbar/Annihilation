package com.gmail.nuclearcat1337.anniPro.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.main.Lang;

public class CivilianKit extends Kit
{
	public CivilianKit()
	{
		Bukkit.getPluginManager().registerEvents(this, AnnihilationMain.getInstance());
		this.Initialize();
	}

	private Loadout loadout;

	@Override
	public boolean Initialize()
	{
		loadout = new Loadout().addWoodSword().addWoodPick().addWoodAxe().addSoulboundItem(new ItemStack(Material.WORKBENCH)).addNavCompass()
				.finalizeLoadout();
		return true;
	}

	@Override
	public String getDisplayName()
	{
		return Lang.CIVILIANNAME.toString();
	}

	@Override
	public IconPackage getIconPackage()
	{
		return new IconPackage(new ItemStack(Material.WORKBENCH), Lang.CIVILIANLORE.toStringArray());
	}

	@Override
	public void onPlayerSpawn(Player player)
	{
		loadout.giveLoadout(player);
	}

	@Override
	public void cleanup(Player player)
	{

	}

	@Override
	public boolean hasPermission(Player player)
	{
		return true;
	}
}
