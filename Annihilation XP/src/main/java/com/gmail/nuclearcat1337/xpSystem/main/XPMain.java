package com.gmail.nuclearcat1337.xpSystem.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.main.AnniCommand;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.Perm;
import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;
import com.gmail.nuclearcat1337.xpSystem.achievementViewer.AchievementViewer;
import com.gmail.nuclearcat1337.xpSystem.kitAchievement.KitListeners;
import com.gmail.nuclearcat1337.xpSystem.kitAchievement.KitSystem;
import com.gmail.nuclearcat1337.xpSystem.shop.Shop;
import com.gmail.nuclearcat1337.xpSystem.xp.MyXPCommand;
import com.gmail.nuclearcat1337.xpSystem.xp.XPListeners;
import com.gmail.nuclearcat1337.xpSystem.xp.XPSystem;

public final class XPMain extends JavaPlugin implements Listener
{
	private XPSystem xpSystem;
	private KitSystem kitSystem;
	private YamlConfiguration config;
	private File configFile;
	private static List<Perm> perms;

	@Override
	public void onEnable()
	{
		configFile = new File(AnnihilationMain.getInstance().getDataFolder(),"AnnihilationXPConfig.yml");
		Bukkit.getLogger().info("[AnnihilationXPSystem] Loading XP system...");
		Bukkit.getLogger().info("[AnnihilationXPSystem] Loading Kit Achievement system...");
		Bukkit.getPluginManager().registerEvents(this, this);

		checkFile(configFile);
		config = YamlConfiguration.loadConfiguration(configFile);
		
		//%# to be replaced by the number
		int x = 0;
		x += ConfigManager.setDefaultIfNotSet(config, "Nexus-Hit-XP", 1);
		x += ConfigManager.setDefaultIfNotSet(config, "Player-Kill-XP", 3);
		x += ConfigManager.setDefaultIfNotSet(config, "Winning-Team-XP", 100);
		x += ConfigManager.setDefaultIfNotSet(config, "Second-Place-Team-XP", 75);
		x += ConfigManager.setDefaultIfNotSet(config, "Third-Place-Team-XP", 50);
		x += ConfigManager.setDefaultIfNotSet(config, "Last-Place-Team-XP", 25);
		x += ConfigManager.setDefaultIfNotSet(config, "Gave-XP-Message", "&a+%# Annihilation XP");
		x += ConfigManager.setDefaultIfNotSet(config, "MyXP-Command-Message", "&dYou have &a%#&d Annihilation XP.");
		if(!config.isConfigurationSection("XP-Multipliers"))
		{
			ConfigurationSection multipliers = config.createSection("XP-Multipliers");
			multipliers.set("Multiplier-1.Permission", "Anni.XP.2");
			multipliers.set("Multiplier-1.Multiplier", 2);
			x++;
		}
		
		ConfigurationSection data = config.getConfigurationSection("Database_XP");
		if(data == null)
			data = config.createSection("Database_XP");
		
		x += setupDatebaseConfig(data);
		
		ConfigurationSection kits = config.getConfigurationSection("Database_KitAchievement");
		if(kits == null)
			kits = config.createSection("Database_KitAchievement");
		
		x += setupDatebaseConfig(kits);
		
		ConfigurationSection shopSec = config.getConfigurationSection("Kit-Shop");
		if(shopSec == null)
		{
			shopSec = config.createSection("Kit-Shop");
			shopSec.createSection("Kits");
		}
		
		x += ConfigManager.setDefaultIfNotSet(shopSec, "On", false);
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Already-Purchased-Kit", "&aPURCHASED");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Not-Yet-Purchased-Kit", "&cLOCKED. PURCHASE FOR &6%# &cXP");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Confirm-Purchase-Kit", "&aPUCHASE BEGUN. CONFIRM FOR &6%# &AXP");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Confirmation-Expired", "&cThe confirmation time has expired. Please try again.");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Not-Enough-XP", "&cYou do not have enough XP to purchase this kit.");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Kit-Purchased", "&aKit %w purchased!");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "No-Kits-To-Purchase", "&cNo kits left to purchase!");
		
		
		if(x > 0)
			this.saveConfig();
		
		this.xpSystem = new XPSystem(config.getConfigurationSection("Database_XP"));
		this.kitSystem = new KitSystem(config.getConfigurationSection("Database_KitAchievement"), xpSystem);
		if(!this.xpSystem.isActive())
		{
			Bukkit.getLogger().info("[AnnihilationXPSystem] Could NOT connect to the XP database");
			disable();
			return;
		} else if(!this.kitSystem.isActive())
		{
			Bukkit.getLogger().info("[AnnihilationXPSystem] Could NOT connect to the kit achievement database");
			disable();
			return;
		}
		Bukkit.getLogger().info("[AnnihilationXPSystem] CONNECTED to both databases");
		boolean useShop = config.getBoolean("Kit-Shop.On");
		if(useShop)
		{
			Bukkit.getLogger().info("[AnnihilationXPSystem] The shop is ENABLED");
			this.getCommand("Shop").setExecutor(new Shop(this.xpSystem,config.getConfigurationSection("Kit-Shop"), this));
			saveConfig();
		}
		else Bukkit.getLogger().info("[AnnihilationXPSystem] The shop is DISABLED");
		loadMultipliers(config.getConfigurationSection("XP-Multipliers"));
		loadXPVars(config); //This also loads the listeners with the values they need
		this.getCommand("achievements").setExecutor(new AchievementViewer(this.kitSystem, this));
		AnniCommand.registerArgument(new XPArgument(xpSystem));
		AnniCommand.registerArgument(new KitArgument(xpSystem));
		AnniCommand.registerArgument(new KitAchivementArgument(kitSystem));
		for(AnniPlayer p : AnniPlayer.getPlayers())
			xpSystem.loadKits(p, null);
	}
	
	private int setupDatebaseConfig(ConfigurationSection data){
		int x = 0;
		x += ConfigManager.setDefaultIfNotSet(data, "Host", "Test");
		x += ConfigManager.setDefaultIfNotSet(data, "Port", "Test");
		x += ConfigManager.setDefaultIfNotSet(data, "Database", "Test");
		x += ConfigManager.setDefaultIfNotSet(data, "Username", "Test");
		x += ConfigManager.setDefaultIfNotSet(data, "Password", "Test");
		return x;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void loadKits(PlayerJoinEvent e)
	{
		AnniPlayer p = AnniPlayer.getPlayer(e.getPlayer().getUniqueId());
		if(p != null)
			xpSystem.loadKits(p, null);
	}
	
	private void checkFile(File file)
	{
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void saveConfig()
	{
		try
		{
			config.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void disable()
	{
		detachAllDatabases();
		Bukkit.getLogger().info("[AnnihilationXPSystem] Disabling XP System.");
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	public void loadXPVars(ConfigurationSection section)
	{
		assert section != null;

		int nexusHitXP = section.getInt("Nexus-Hit-XP");
		int killXP = section.getInt("Player-Kill-XP");
		String gaveXPMessage = section.getString("Gave-XP-Message");
		String myXPMessage = section.getString("MyXP-Command-Message");
		int[] teamXPs = new int[4];
		teamXPs[0] = section.getInt("Winning-Team-XP");
		teamXPs[1] = section.getInt("Second-Place-Team-XP");
		teamXPs[2] = section.getInt("Third-Place-Team-XP");
		teamXPs[3] = section.getInt("Last-Place-Team-XP");
		
		XPListeners listeners = new XPListeners(this.xpSystem,gaveXPMessage,killXP,nexusHitXP,teamXPs);
		KitListeners KitListeners = new KitListeners(this.kitSystem);
		
		Bukkit.getPluginManager().registerEvents(listeners, this);
		Bukkit.getPluginManager().registerEvents(KitListeners, this);
		
		MyXPCommand xpCommand = new MyXPCommand(this.xpSystem,myXPMessage);

		this.getCommand("MyXP").setExecutor(xpCommand);

	}
	
	public void loadMultipliers(ConfigurationSection multipliers)
	{
		assert multipliers != null;
		perms = new ArrayList<Perm>();
		//ConfigurationSection multipliers = config.getConfigurationSection("XP-Multipliers");
		if(multipliers != null)
		{
			for(String key : multipliers.getKeys(false))
			{
				ConfigurationSection multSec = multipliers.getConfigurationSection(key);
				String perm = multSec.getString("Permission");
				int multiplier = multSec.getInt("Multiplier");
				if(perm != null && !perm.equals("") && multiplier > 0)
				{
					Permission p = new Permission(perm);
					p.setDefault(PermissionDefault.FALSE);
					Bukkit.getPluginManager().addPermission(p);
					p.recalculatePermissibles();
					perms.add(new Perm(perm,multiplier));			
				}
			}
			Collections.sort(perms);
		}
	}
	

	@Override
	public void onDisable()
	{
		detachAllDatabases();
	}
	
	public void detachAllDatabases(){
		if(xpSystem != null)
			xpSystem.disable();
		if (kitSystem != null){
			kitSystem.disable();
		}
	}
	
	public static String formatString(String string, int amount)
	{
		return ChatColor.translateAlternateColorCodes('&', string.replace("%#", ""+amount));
	}
	
	public static String[] toStringArray(String s)
	{
		if (s.contains("%n"))
			return s.split("%n");
		else
			return new String[] { s };
	}
	
	public static int checkMultipliers(Player player, int initialXP)
	{
		int returnXP = initialXP;
		if(perms.size() > 0)
		{
			for(Perm p : perms)
			{
				if(player.hasPermission(p.perm))
				{
					returnXP = (int)Math.ceil(((double)returnXP)*p.multiplier);
					break;
				}
			}
		}
		return returnXP;
	}
}
