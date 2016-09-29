package com.gmail.kh498.xp2gSystem.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.kh498.xp2gSystem.xp.XPSystem;

public final class XP2GMain extends JavaPlugin implements Listener
{
	private static XPSystem xpSystem = null;
	private YamlConfiguration config;
	private File configFile;
	private final String CHAT_PREFIX = "[" + this.getName () + "] ";
	private static XP2GMain instance;

	@ Override
	public void onEnable ()
	{
		instance = this;

		//		private static YamlConfiguration mainConfig = null;
		//		private static File mainConfigFile = null;

		File mainConfigFile = new File (this.getDataFolder ().getAbsolutePath ());
		if (!mainConfigFile.exists () || !mainConfigFile.isDirectory ())
			mainConfigFile.mkdir ();

		configFile = new File (this.getDataFolder (), "AnnihilationXP2GConfig.yml");
		Bukkit.getLogger ().info (CHAT_PREFIX + "Loading XP system...");
		Bukkit.getPluginManager ().registerEvents (this, this);

		checkFile (configFile);
		config = YamlConfiguration.loadConfiguration (configFile);

		//Create or load config
		int x = 0;
		ConfigurationSection data = config.getConfigurationSection ("Database");
		if (data == null)
			data = config.createSection ("Database");

		x += setDefaultIfNotSet (data, "Host", "Test");
		x += setDefaultIfNotSet (data, "Port", "Test");
		x += setDefaultIfNotSet (data, "Database", "Test");
		x += setDefaultIfNotSet (data, "Username", "Test");
		x += setDefaultIfNotSet (data, "Password", "Test");

		if (x > 0)
			this.saveConfig ();

		//connect to the database
		XP2GMain.xpSystem = new XPSystem (config.getConfigurationSection ("Database"));
		if (!XP2GMain.xpSystem.isActive ())

		{
			Bukkit.getLogger ().info (CHAT_PREFIX + "Could NOT connect to the XP database");
			disable ();
			return;
		}
		Bukkit.getLogger ().info (CHAT_PREFIX + "CONNECTED to XP database");

		//enable the shop
		Convert convert = new Convert (XP2GMain.xpSystem);
		this.getCommand (Convert.CMD).setExecutor (convert);
	}

	private int setDefaultIfNotSet (ConfigurationSection section, String path, Object value)
	{
		if (section != null)
		{
			if (!section.isSet (path))
			{
				section.set (path, value);
				return 1;
			}
		}
		return 0;
	}

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
		Bukkit.getLogger ().info (CHAT_PREFIX + "Disabling XP System.");
		Bukkit.getPluginManager ().disablePlugin (this);
	}

	@ Override
	public void onDisable ()
	{
		detachAllDatabases ();
	}

	public void detachAllDatabases ()
	{
		if (xpSystem != null)
			xpSystem.disable ();
	}

	//	public static String formatString (String string, int amount)
	//	{
	//		return ChatColor.translateAlternateColorCodes ('&', string.replace ("%#", "" + amount));
	//	}

	public static XPSystem getXpSystem ()
	{
		return xpSystem;
	}

	public static XP2GMain getInstance ()
	{
		return instance;
	}
}
