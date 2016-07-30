package com.gmail.nuclearcat1337.anniPro.enderFurnace.api;


import org.bukkit.inventory.ItemStack;

public abstract class FurnaceData
{
    private ItemStack[] items;
    private int burnTime;
    private int ticksForCurrentFuel;
    private int cookTime;


    /**
     *EntityFurnace getProperty() IDS
     *0 = burn time
     *1 = ticks for current fuel
     *2 = cook time
     *3 = cook time total
     * @param burnTime "getProperty(0)"
     * @param ticksForCurrentFuel "getProperty(1)"
     * @param cookTime "getProperty(2)"
     */
    public FurnaceData(ItemStack[] items, int burnTime, int ticksForCurrentFuel, int cookTime)
    {
        this.items = items;
        this.burnTime = burnTime;
        this.ticksForCurrentFuel = ticksForCurrentFuel;
        this.cookTime = cookTime;
    }

    public int getBurnTime()
    {
        return burnTime;
    }

    public int getTicksForCurrentFuel()
    {
        return ticksForCurrentFuel;
    }

    public int getCookTime()
    {
        return cookTime;
    }

    public ItemStack[] getItems()
    {
        return items;
    }
}
