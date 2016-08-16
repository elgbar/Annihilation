package com.gmail.nuclearcat1337.anniPro.enderFurnace.versions.v1_8_R3;

import com.gmail.nuclearcat1337.anniPro.enderFurnace.api.IFurnace;
import com.gmail.nuclearcat1337.anniPro.enderFurnace.api.ReflectionUtil;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.TileEntityFurnace;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

class Furnace_v1_8_R3 extends TileEntityFurnace implements IFurnace
{
	private EntityPlayer owningPlayer;

	public Furnace_v1_8_R3(Player p)
	{
		EntityPlayer player = ((CraftPlayer) p).getHandle();
		this.owningPlayer = player;
		this.world = player.world;
		super.a("Ender Furnace");
	}

	@Override
	public boolean a(EntityHuman entityhuman)
	{
		return true;
	}

	@Override
	public int g()
	{
		return 0;
	}

	@Override
	public InventoryHolder getOwner()
	{
		org.bukkit.block.Furnace furnace = new CraftFurnace(this.world.getWorld().getBlockAt(0, 0, 0));
		try
		{
			ReflectionUtil.setValue(furnace, "furnace", this);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return furnace;
	}

	@Override
	public void open()
	{
		Bukkit.getLogger().info("Attempted to open for player " + owningPlayer.getName());
		owningPlayer.openContainer(this);
	}

	@Override
	public void tick()
	{
		try
		{
			c();
		} catch (Throwable t)
		{
			// Do nothing since this seems to be a byproduct of having a furnace that doesnt actually exist in the world
		}
	}

	public void setItemStack(int i, ItemStack itemstack)
	{
		setItem(i, CraftItemStack.asNMSCopy(itemstack));
	}

	public ItemStack getItemStack(int i)
	{
		return CraftItemStack.asBukkitCopy(getItem(i));
	}

	@Override
	public FurnaceData getFurnaceData()
	{
		return new FurnaceData(this);
	}

	@Override
	public void load(final com.gmail.nuclearcat1337.anniPro.enderFurnace.api.FurnaceData data)
	{
		ItemStack[] items = data.getItems();
		for (int x = 0; x < 3; x++)
			this.setItem(x, org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asNMSCopy(items[x]));
		this.b(0, data.getBurnTime());
		this.b(1, data.getTicksForCurrentFuel());
		this.b(2, data.getCookTime());
	}
}
