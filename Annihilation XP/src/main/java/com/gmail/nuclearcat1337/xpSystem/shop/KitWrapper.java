package com.gmail.nuclearcat1337.xpSystem.shop;

import com.gmail.nuclearcat1337.anniPro.kits.Kit;

public class KitWrapper
{
	public Kit kit;
	public int price;
	public boolean isBuyable;
	
	public KitWrapper(Kit k, int price, boolean isBuyable)
	{
		this.kit = k;
		this.price = price;
		this.isBuyable = isBuyable;
	}
}
