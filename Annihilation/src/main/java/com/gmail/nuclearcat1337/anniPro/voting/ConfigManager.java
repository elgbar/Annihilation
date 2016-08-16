package com.gmail.nuclearcat1337.anniPro.voting;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager
{
	private static YamlConfiguration mainConfig = null;
	private static File mainConfigFile = null;

	public static void load(Plugin p)
	{
		mainConfigFile = new File(p.getDataFolder().getAbsolutePath());
		if (!mainConfigFile.exists() || !mainConfigFile.isDirectory())
			mainConfigFile.mkdir();
		mainConfigFile = new File(p.getDataFolder().getAbsolutePath(), "AnniMainConfig.yml");
		try
		{
			if (!mainConfigFile.exists())
				mainConfigFile.createNewFile();
			mainConfig = YamlConfiguration.loadConfiguration(mainConfigFile);
			int save = 0;

			save += setDefaultIfNotSet(mainConfig, "useMOTD", false);
			save += setDefaultIfNotSet(mainConfig, "ProtocalHack", false);
			save += setDefaultIfNotSet(mainConfig, "EndGameCommand", "stop");
			save += setDefaultIfNotSet(mainConfig, "End-Of-Game-Countdown", 120);
			if (mainConfig.isSet("Kill-On-Leave"))
			{
				mainConfig.set("Kill-On-Leave", null); // We are clearing this from the config because its old and not necessary
				save++;
			}

			ConfigurationSection gameVars;
			if (!mainConfig.isConfigurationSection("GameVars"))
			{
				gameVars = mainConfig.createSection("GameVars");
				save += 1;
			} else
				gameVars = mainConfig.getConfigurationSection("GameVars");

			save += setDefaultIfNotSet(gameVars, "DefaultGameMode", "adventure");

			save += setDefaultIfNotSet(gameVars, "AutoStart.On", false);
			save += setDefaultIfNotSet(gameVars, "AutoStart.PlayersToStart", 4);
			save += setDefaultIfNotSet(gameVars, "AutoStart.CountdownTime", 30);

			save += setDefaultIfNotSet(gameVars, "AutoRestart.On", false);
			save += setDefaultIfNotSet(gameVars, "AutoRestart.PlayersToAutoRestart", 0);
			save += setDefaultIfNotSet(gameVars, "AutoRestart.CountdownTime", 15);

			save += setDefaultIfNotSet(gameVars, "MapLoading.Voting", true);
			save += setDefaultIfNotSet(gameVars, "MapLoading.Max-Maps-For-Voting", 3);
			save += setDefaultIfNotSet(gameVars, "MapLoading.UseMap", "plugins/Annihilation/Worlds/Test");

			save += setDefaultIfNotSet(gameVars, "Team-Balancing.On", true);
			save += setDefaultIfNotSet(gameVars, "Team-Balancing.Tolerance", 2);

			save += setDefaultIfNotSet(gameVars, "Anti-Log-System.On", true);
			save += setDefaultIfNotSet(gameVars, "Anti-Log-System.NPC-Time", 20);

			if (save > 0)
				saveMainConfig();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static int setDefaultIfNotSet(ConfigurationSection section, String path, Object value)
	{
		if (section != null)
		{
			if (!section.isSet(path))
			{
				section.set(path, value);
				return 1;
			}
		}
		return 0;
	}

	public static YamlConfiguration getConfig()
	{
		return mainConfig;
	}

	public static void saveMainConfig()
	{
		if (mainConfig != null)
		{
			try
			{
				mainConfig.save(mainConfigFile);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
