package com.gmail.nuclearcat1337.kitSystem.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nuclearcat1337.anniPro.main.AnniCommand;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.kitSystem.achievementViewer.AchievementViewer;
import com.gmail.nuclearcat1337.kitSystem.kitAchievement.KitListeners;
import com.gmail.nuclearcat1337.kitSystem.kitAchievement.KitSystem;
import com.gmail.nuclearcat1337.xpSystem.main.XPMain;

public final class KitMain extends JavaPlugin implements Listener
{
	private KitSystem kitSystem;
	private YamlConfiguration config;
	private File configFile;
	private final String CHAT_PREFIX = "[" + this.getName () + "] ";

	@ Override
	public void onEnable ()
	{
		//we're just using the same config as anniXP
		configFile = new File (AnnihilationMain.getInstance ().getDataFolder (), "AnnihilationXPConfig.yml");
		Bukkit.getLogger ().info (CHAT_PREFIX + "Loading Kit Achievement system...");
		Bukkit.getPluginManager ().registerEvents (this, this);

		checkFile (configFile);
		config = YamlConfiguration.loadConfiguration (configFile);

		try
		{
			this.kitSystem = new KitSystem (config.getConfigurationSection ("Database"), XPMain.getXpSystem ());
		} catch (NoClassDefFoundError e)
		{ //something went wrong with initlizing anni xp
			Bukkit.getLogger ().info (CHAT_PREFIX + "Could NOT connect to the kit achievement database, due to dependency of Annihilation XP");
			disable (); //We cannot function without the xp addon
			return;
		}
		Bukkit.getLogger ().info (CHAT_PREFIX + "CONNECTED to kit database");

		loadKitVars (config); //This also loads the listeners with the values they need

		AnniCommand.registerArgument (new KitAchivementArgument (kitSystem));
	}

	public void loadKitVars (ConfigurationSection section)
	{
		assert section != null;

		KitListeners listeners = new KitListeners (kitSystem);

		Bukkit.getPluginManager ().registerEvents (listeners, this);

		AchievementViewer kitCommand = new AchievementViewer (kitSystem);

		this.getCommand ("Achievements").setExecutor (kitCommand);
		this.getCommand ("Achievements").setAliases (new ArrayList<> (Arrays.asList ("Ach")));
	}

	//	private int setupDatebaseConfig (ConfigurationSection data)
	//	{
	//		int x = 0;
	//		x += ConfigManager.setDefaultIfNotSet (data, "Host", "Test");
	//		x += ConfigManager.setDefaultIfNotSet (data, "Port", "Test");
	//		x += ConfigManager.setDefaultIfNotSet (data, "Database", "Test");
	//		x += ConfigManager.setDefaultIfNotSet (data, "Username", "Test");
	//		x += ConfigManager.setDefaultIfNotSet (data, "Password", "Test");
	//		return x;
	//	}

	private void checkFile (File file)
	{
		if (!file.exists ())
		{
			try
			{
				file.createNewFile ();
			} catch (IOException e)
			{
				e.printStackTrace ();
			}
		}
	}

	@ Override
	public void saveConfig ()
	{
		try
		{
			config.save (configFile);
		} catch (IOException e)
		{
			e.printStackTrace ();
		}
	}

	private void disable ()
	{
		detachAllDatabases ();
		Bukkit.getLogger ().info (CHAT_PREFIX + "Disabling Kit System.");
		Bukkit.getPluginManager ().disablePlugin (this);
	}

	@ Override
	public void onDisable ()
	{
		detachAllDatabases ();
	}

	public void detachAllDatabases ()
	{
		if (kitSystem != null)
		{
			kitSystem.disable ();
		}
	}

	public static String formatString (String string, int amount)
	{
		return ChatColor.translateAlternateColorCodes ('&', string.replace ("%#", "" + amount));
	}

	public static String[] toStringArray (String s)
	{
		if (s.contains ("%n"))
			return s.split ("%n");
		else
			return new String[] { s };
	}

}
