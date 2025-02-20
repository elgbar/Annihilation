package com.gmail.nuclearcat1337.anniPro.anniMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnniEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.ResourceBreakEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.mapBuilder.FutureBlockReplace;

public final class RegeneratingBlocks implements Listener
{
	private Map<Material, Map<Integer, RegeneratingBlock>> blocks;
	private String world;
	private final ScheduledExecutorService executor;
	private final Random rand;

	public RegeneratingBlocks(String world)
	{
		this.world = world;
		blocks = new EnumMap<Material, Map<Integer, RegeneratingBlock>>(Material.class);
		rand = new Random(System.currentTimeMillis());
		executor = Executors.newScheduledThreadPool(3);
	}

	public RegeneratingBlocks(String world, ConfigurationSection configSection)
	{
		this(world);
		if (configSection != null)
		{
			for (String key : configSection.getKeys(false))
			{
				ConfigurationSection matSection = configSection.getConfigurationSection(key);
				if (matSection != null)
				{
					for (String dataKey : matSection.getKeys(false))
					{
						ConfigurationSection dataSection = matSection.getConfigurationSection(dataKey);
						if (dataSection != null)
						{
							Material mat = Material.getMaterial(dataSection.getString("Type"));
							Integer matData = dataSection.getInt("MaterialData");
							boolean regen = dataSection.getBoolean("Regenerate");
							boolean cobbleReplace = dataSection.getBoolean("CobbleReplace");
							boolean naturalBreak = dataSection.getBoolean("NaturalBreak");
							Integer time = dataSection.getInt("Time");
							TimeUnit unit;
							try
							{
								unit = TimeUnit.valueOf(dataSection.getString("Unit"));
							} catch (IllegalArgumentException e)
							{
								unit = null;
							}
							Integer xp = dataSection.getInt("XP");
							Material product;
							try
							{
								product = Material.getMaterial(dataSection.getString("Product"));
							} catch (Exception e)
							{
								product = null;
							}
							String amount = dataSection.getString("Amount");
							Integer productData = dataSection.getInt("ProductData");
							String effect = dataSection.getString("Effect");
							this.addRegeneratingBlock(new RegeneratingBlock(mat, matData, regen, cobbleReplace, naturalBreak, time, unit, xp, product,
									amount, productData, effect));
						}
					}
				}
			}
		}
	}

	public void addRegeneratingBlock(RegeneratingBlock block)
	{
		Map<Integer, RegeneratingBlock> datas = blocks.get(block.Type);
		if (datas == null)
			datas = new HashMap<Integer, RegeneratingBlock>();
		datas.put(block.MaterialData, block);
		blocks.put(block.Type, datas);
	}

	public RegeneratingBlock getRegeneratingBlock(Material type, Integer data)
	{
		Map<Integer, RegeneratingBlock> datas = blocks.get(type);
		if (datas != null)
			return datas.get(data);
		else
			return null;
	}

	public Map<Material, Map<Integer, RegeneratingBlock>> getRegeneratingBlocks()
	{
		return Collections.unmodifiableMap(blocks);
	}

	public void registerListeners(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterListeners()
	{
		HandlerList.unregisterAll(this);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void oreBreak(BlockBreakEvent event)
	{
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE && event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase(world))
		{
			final Player p = event.getPlayer();
			final Block block = event.getBlock();
			final AnniPlayer player = AnniPlayer.getPlayer(p.getUniqueId());
			if (player != null)
			{
				RegeneratingBlock b = getRegeneratingBlock(block.getType(), (int) block.getData());
				if (b == null)
				{
					b = getRegeneratingBlock(block.getType(), -1);
				}
				if (b != null)
				{
					if (b.NaturalBreak)
					{
						ResourceBreakEvent e = new ResourceBreakEvent(player, b, 0, (ItemStack[]) null);
						AnniEvent.callEvent(e);
						if (!e.isCancelled())
							executor.schedule(new FutureBlockReplace(event.getBlock()), b.Time, b.Unit);
						else
							event.setCancelled(true);
						return;
					}
					event.setCancelled(true);
					if (!b.Regenerate) {
						return;
					}
					if (b.Effect == null)
					{
						int amount = getAmount(b);
						ItemStack stack;
						int xp = b.XP;
						if (b.ProductData != -1)
							stack = new ItemStack(b.Product, amount, (byte) b.ProductData);
						else
							stack = new ItemStack(b.Product, amount);
						ResourceBreakEvent e = new ResourceBreakEvent(player, b, xp, stack);
						AnniEvent.callEvent(e);
						if (!e.isCancelled())
						{
							if (e.getXP() > 0)
								p.playSound(p.getLocation(), Sound.ORB_PICKUP, 0.6F, rand.nextFloat());
							p.giveExp(e.getXP());

							if (e.getProducts() != null)
							{
								for (ItemStack s : e.getProducts())
								{
									if (s != null)
									{
										p.getInventory().addItem(s);
									}
								}
							}
							executor.schedule(new FutureBlockReplace(event.getBlock(), b.CobbleReplace), b.Time, b.Unit);
						}
					} else if (b.Effect.equalsIgnoreCase("Gravel"))
					{
						
						List<ItemStack> l = gravelEffect();
						ResourceBreakEvent e = new ResourceBreakEvent(player, b, b.XP, l.toArray(new ItemStack[l.size()]));
						AnniEvent.callEvent(e);
						if (!e.isCancelled())
						{
							if (e.getXP() > 0)
								p.playSound(p.getLocation(), Sound.ORB_PICKUP, 0.6F, rand.nextFloat());
							p.giveExp(e.getXP());
							if (e.getProducts() != null)
							{
								for (ItemStack s : e.getProducts())
									p.getInventory().addItem(s);
							}
							executor.schedule(new FutureBlockReplace(event.getBlock(), b.CobbleReplace), b.Time, b.Unit);
						}
					}
				}
			}
		}
	}

	private List<ItemStack> gravelEffect()
	{
		List<ItemStack> l =	new ArrayList<ItemStack>();
		for (int x = 0; x < 5; x++)
		{
			int z;
			switch (x)
			{
				// bone
				case 0:
					z = rand.nextInt(2);
					if (z != 0)
						l.add(new ItemStack(Material.BONE, z));
					break;
				// feather
				case 1:
					z = rand.nextInt(3);
					if (z != 0)
						l.add(new ItemStack(Material.FEATHER, z));
					break;
				/// arrow
				case 2:
					z = rand.nextInt(4);
					if (z != 0)
						l.add(new ItemStack(Material.ARROW, z));
					break;
				// string
				case 3:
					z = rand.nextInt(2);
					if (z != 0)
						l.add(new ItemStack(Material.STRING, z));
					break;
				// flint
				case 4:
					z = rand.nextInt(3);
					if (z != 0)
						l.add(new ItemStack(Material.FLINT, z));
					break;
				default:
					break;
			}
		}
		return l;
	}

	private int getAmount(RegeneratingBlock b)
	{
		int amount = 0;
		try
		{
			amount = Integer.parseInt(b.Amount);
		} catch (NumberFormatException e)
		{
			try
			{
				if (b.Amount.contains("RANDOM"))
				{
					String x, y;
					x = b.Amount.split(",")[0];
					y = b.Amount.split(",")[1];
					x = x.substring(7);
					y = y.substring(0, y.length() - 1);
					try
					{
						int min = Integer.parseInt(x);
						int max = Integer.parseInt(y);
						amount = min + (int) (Math.random() * ((max - min) + 1));
					} catch (NumberFormatException exx)
					{
						return 0;
					}
				}
			} catch (ArrayIndexOutOfBoundsException ex)
			{
				return 0;
			}
		}
		return amount;
	}

	public boolean removeRegeneratingBlock(Material type, Integer data)
	{
		if (blocks.containsKey(type))
		{
			if (data == -1)
			{
				blocks.remove(type);
				return true;
			}

			Map<Integer, RegeneratingBlock> datas = blocks.get(type);
			if (datas != null)
			{
				if (datas.containsKey(data))
				{
					datas.remove(data);
					return true;
				}
			}
		}
		return false;
	}

	public void saveToConfig(ConfigurationSection configSection)
	{
		if (configSection != null)
		{
			for (Entry<Material, Map<Integer, RegeneratingBlock>> entry : blocks.entrySet())
			{
				ConfigurationSection matSection = configSection.createSection(entry.getKey().name());
				for (Entry<Integer, RegeneratingBlock> map : entry.getValue().entrySet())
				{
					ConfigurationSection dataSection = matSection.createSection(map.getKey().toString());
					RegeneratingBlock b = map.getValue();
					b.saveToConfig(dataSection);
				}
			}
		}
	}
}
