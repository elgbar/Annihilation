package com.hcs.anniPro.boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("serial")
/**
 * Rewards you get after you kill one of the golems
 * @author kh498
 *
 */
public enum GolemReward
{
	// Common items
	IRON_INGOT_I(Rarity.COMMON, Material.IRON_INGOT, 4),
	IRON_INGOT_II(Rarity.COMMON, Material.IRON_INGOT, 8),
	IRON_INGOT_III(Rarity.COMMON, Material.IRON_INGOT, 16),
	DIAMOND_I(Rarity.COMMON, Material.DIAMOND, 4),
	DIAMOND_II(Rarity.COMMON, Material.DIAMOND, 8),
	GOLD_INGOT_I(Rarity.COMMON, Material.GOLD_INGOT, 8),
	GOLD_INGOT_II(Rarity.COMMON, Material.GOLD_INGOT, 16),
	GOLD_INGOT_III(Rarity.COMMON, Material.GOLD_INGOT, 32),
	WEB(Rarity.COMMON, Material.WEB, 16),

	// Uncommon items
	GOLDEN_APPLE_I(Rarity.UNCOMMON, Material.GOLDEN_APPLE, 4),
	GOLDEN_APPLE_II(Rarity.UNCOMMON, Material.GOLDEN_APPLE, 8),
	DIAMOND_HELMET(Rarity.UNCOMMON, Material.DIAMOND_HELMET, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		}
	}),
	IRON_CHESTPLATE(Rarity.UNCOMMON, Material.IRON_CHESTPLATE, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		}
	}),
	IRON_LEGGINGS(Rarity.UNCOMMON, Material.IRON_LEGGINGS, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		}
	}),
	DIAMOND_BOOTS(Rarity.UNCOMMON, Material.DIAMOND_BOOTS, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		}
	}),
	GOLD_SWORD(Rarity.UNCOMMON, Material.GOLD_SWORD, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.DAMAGE_ALL, 2);
		}
	}),
	ENCHANTED_BOOK_I(Rarity.UNCOMMON, Material.ENCHANTED_BOOK, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.DURABILITY, 3);
		}
	}),

	// Rare items
	GOLD_HELMET(Rarity.RARE, Material.GOLD_HELMET, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.PROTECTION_FIRE, 4);
			put(Enchantment.DURABILITY, 4);
		}
	}),
	GOLD_CHESTPLATE(Rarity.RARE, Material.GOLD_CHESTPLATE, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.THORNS, 3);
			put(Enchantment.DURABILITY, 3);
		}
	}),
	GOLD_LEGGINGS(Rarity.RARE, Material.GOLD_LEGGINGS, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			put(Enchantment.DURABILITY, 3);
		}
	}),
	GOLD_BOOTS(Rarity.RARE, Material.GOLD_BOOTS, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.PROTECTION_FALL, 4);
			put(Enchantment.DURABILITY, 3);
		}
	}),
	ENCHANTED_BOOK_II(Rarity.RARE, Material.ENCHANTED_BOOK, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		}
	}),
	ENCHANTED_BOOK_III(Rarity.RARE, Material.ENCHANTED_BOOK, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.DAMAGE_ALL, 5);
		}
	}),
	ENCHANTED_BOOK_IV(Rarity.RARE, Material.ENCHANTED_BOOK, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.KNOCKBACK, 2);
		}
	}),
	BOW(Rarity.RARE, Material.BOW, 1, new HashMap<Enchantment, Integer>()
	{
		{
			put(Enchantment.ARROW_DAMAGE, 4);
			put(Enchantment.DURABILITY, 2);
			put(Enchantment.ARROW_FIRE, 1);
			put(Enchantment.ARROW_INFINITE, 1);
		}
	}),;

	private final Rarity rarity;
	private final Material material;
	private final int quantity;
	private final Map<Enchantment, Integer> enchantments;

	private GolemReward(Rarity r, Material m, int i, Map<Enchantment, Integer> ench)
	{
		this.rarity = r;
		this.material = m;
		this.quantity = i;

		this.enchantments = ench;
	}

	private GolemReward(Rarity r, Material m, int i)
	{
		this(r, m, i, null);
	}

	public static ItemStack toItemStack(GolemReward gr)
	{
		ItemStack item = new ItemStack(gr.getMaterial(), gr.getQuantity());

		if (gr.getEnchantments() != null)
		{
			if (gr.getMaterial().equals(Material.ENCHANTED_BOOK))
			{
				EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) item.getItemMeta();

				for (Entry<Enchantment, Integer> entry : gr.getEnchantments().entrySet())
				{
					bookMeta.addStoredEnchant(entry.getKey(), entry.getValue(), false);
				}
				item.setItemMeta(bookMeta);

			} else
			{
				ItemMeta iMeta = item.getItemMeta();
				for (Entry<Enchantment, Integer> entry : gr.getEnchantments().entrySet())
				{
					iMeta.addEnchant(entry.getKey(), entry.getValue(), false);
				}
				item.setItemMeta(iMeta);

			}
		}
		return item;
	}

	/**
	 * @return the enchantments
	 */
	public Map<Enchantment, Integer> getEnchantments()
	{
		return enchantments;
	}
	
	public static List<GolemReward> getAllRarity(Rarity rar){
		List<GolemReward> l = new ArrayList<GolemReward>();
		for (GolemReward gr : GolemReward.values()){
			if (gr.getRarity().equals(rar)){
				l.add(gr);
			}
		}
		
		return l != null ? l : null;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity()
	{
		return quantity;
	}

	/**
	 * @return the material
	 */
	public Material getMaterial()
	{
		return material;
	}

	/**
	 * @return the rarity
	 */
	public Rarity getRarity()
	{
		return rarity;
	}
}
