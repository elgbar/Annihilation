package com.gmail.nuclearcat1337.anniPro.anniGame;

import java.io.File;

import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

public class GameVars
{
	private static GameMode defaultGamemode = GameMode.SURVIVAL;
	private static String endGameCommand = "stop";

	private static boolean AutoStart = false;
	private static int PlayerstoStart = 4;
	private static int CountdowntoStart = 30;

	private static boolean Voting = true;
	private static int maxVotingMaps = 3;
	private static String Map = "";

	private static boolean AutoReStart = false;
	private static int PlayersToRestart = 0;
	private static int CountdowntoRestart = 15;

	private static boolean useMOTD = false;

	private static boolean useTeamBalance = true;
	private static int balanceTolerance = 2;

	private static boolean useAntiLog;
	private static int npcTimeout;

	private static int endOfGameCountdown = 120;

	public static int getEndOfGameCountdown()
	{
		return endOfGameCountdown;
	}

	public static int getMaxMapsForVoting()
	{
		return maxVotingMaps;
	}

	public static boolean getUseAntiLog()
	{
		return useAntiLog;
	}

	public static int getNpcTimeout()
	{
		return npcTimeout;
	}

	public static int getBalanceTolerance()
	{
		return balanceTolerance;
	}

	public static boolean useTeamBalance()
	{
		return useTeamBalance;
	}

	public static GameMode getDefaultGamemode()
	{
		return defaultGamemode;
	}

	public static String getEndOfGameCommand()
	{
		return endGameCommand;
	}

	public static boolean useMOTD()
	{
		return useMOTD;
	}

	public static boolean getAutoRestart()
	{
		return AutoReStart;
	}

	public static int getCountdownToRestart()
	{
		return CountdowntoRestart;
	}

	public static int getPlayersToRestart()
	{
		return PlayersToRestart;
	}

	public static boolean getAutoStart()
	{
		return AutoStart;
	}

	public static int getPlayersToStart()
	{
		return PlayerstoStart;
	}

	public static int getCountdownToStart()
	{
		return CountdowntoStart;
	}

	public static boolean getVoting()
	{
		return Voting;
	}

	public static File getMap()
	{
		return new File(Map);
	}

	public static void loadGameVars(ConfigurationSection config)
	{
		if (config != null)
		{
			useMOTD = config.getBoolean("useMOTD");
			endOfGameCountdown = config.getInt("End-Of-Game-Countdown");
			String command = config.getString("EndGameCommand");
			if (command != null)
				endGameCommand = command.trim();
			ConfigurationSection gameVars = config.getConfigurationSection("GameVars");
			if (gameVars != null)
			{
				ConfigurationSection auto = gameVars.getConfigurationSection("AutoStart");
				AutoStart = auto.getBoolean("On");
				PlayerstoStart = auto.getInt("PlayersToStart");
				CountdowntoStart = auto.getInt("CountdownTime");

				ConfigurationSection mapload = gameVars.getConfigurationSection("MapLoading");
				Voting = mapload.getBoolean("Voting");
				maxVotingMaps = mapload.getInt("Max-Maps-For-Voting");
				Map = mapload.getString("UseMap");

				ConfigurationSection autorestart = gameVars.getConfigurationSection("AutoRestart");
				AutoReStart = autorestart.getBoolean("On");
				PlayersToRestart = autorestart.getInt("PlayersToAutoRestart");
				CountdowntoRestart = autorestart.getInt("CountdownTime");

				ConfigurationSection balance = gameVars.getConfigurationSection("Team-Balancing");
				useTeamBalance = balance.getBoolean("On");
				balanceTolerance = balance.getInt("Tolerance");

				ConfigurationSection antilog = gameVars.getConfigurationSection("Anti-Log-System");
				useAntiLog = antilog.getBoolean("On");
				npcTimeout = antilog.getInt("NPC-Time");

				String gamemode = gameVars.getString("DefaultGameMode");
				if (gamemode != null)
				{
					try
					{
						defaultGamemode = GameMode.valueOf(gamemode.toUpperCase());
					} catch (Exception e)
					{
						defaultGamemode = GameMode.SURVIVAL;
					}
				}
			}
		}
		File worldFolder = new File(AnnihilationMain.getInstance().getDataFolder().getAbsolutePath() + "/Worlds");
		if (!worldFolder.exists())
			worldFolder.mkdir();
	}
}
