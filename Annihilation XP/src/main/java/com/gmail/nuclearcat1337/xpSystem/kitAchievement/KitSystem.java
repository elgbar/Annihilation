package com.gmail.nuclearcat1337.xpSystem.kitAchievement;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.xpSystem.database.AsyncLogQuery;
import com.gmail.nuclearcat1337.xpSystem.database.Database;
import com.gmail.nuclearcat1337.xpSystem.utils.Acceptor;
import com.gmail.nuclearcat1337.xpSystem.xp.XPSystem;

public class KitSystem
{
	private Database database = null;
	private final XPSystem xpSystem;
	public final static String PREFIX_TABLE = "tbl_achievement_";

	public KitSystem(ConfigurationSection databaseSection, XPSystem xpSystem)
	{
		assert databaseSection != null;
		this.database = loadDatabase(databaseSection);
		this.xpSystem = xpSystem;
		if (database != null)
		{
			try
			{
				for (KitAchivement ka : KitAchivement.values())
				{
					database.updateSQL("CREATE TABLE IF NOT EXISTS " + PREFIX_TABLE + ka.toString() + " (ID VARCHAR(40), " + ka.getRow()
							+ " INTEGER, UNIQUE (ID))");
				}
			} catch (Throwable t)
			{
				database = null;
			}
		}

	}

	public boolean isActive()
	{
		return database != null;
	}

	public void disable()
	{
		if (database != null)
			database.closeConnection();
		database = null;
	}

	private Database loadDatabase(ConfigurationSection section)
	{
		assert section != null;

		String host = section.getString("Host");
		String port = section.getString("Port");
		String data = section.getString("Database");
		String username = section.getString("Username");
		String password = section.getString("Password");
		return Database.getMySQLDatabase(host, port, data, username, password);
	}

	public void addKitAchivementProgess(final UUID playerID, final KitAchivement kit, final int progress)
	{
		if (progress > 0)
		{
			database.addNewAsyncLogQuery(new AsyncLogQuery()
			{
				@Override
				public String getQuery()
				{
					return "INSERT INTO " + PREFIX_TABLE + kit.toString() + " (ID, " + kit.getRow() + ") VALUES ('" + playerID.toString() + "', "
							+ progress + ") ON DUPLICATE KEY UPDATE " + kit.getRow() + "=" + kit.getRow() + "+VALUES(" + kit.getRow() + ");";
				}
			});
		}

		checkCompletedObjective(Bukkit.getPlayer(playerID), kit);
	}

	public void removeKitAchivementProgess(final UUID playerID, final KitAchivement kit, final int progress)
	{
		if (progress > 0)
		{
			database.addNewAsyncLogQuery(new AsyncLogQuery()
			{
				@Override
				public String getQuery()
				{
					return "INSERT INTO " + PREFIX_TABLE + kit.toString() + " (ID, " + kit.getRow() + ") VALUES ('" + playerID.toString() + "', "
							+ progress + ") ON DUPLICATE KEY UPDATE " + kit.getRow() + "=" + kit.getRow() + "-VALUES(" + kit.getRow() + ");";
				}
			});
		}
	}

	public void getKitProgress(UUID playerID, Acceptor<Integer> acceptor, KitAchivement ka)
	{
		database.addNewAsyncQuery(new QueryKitAchievements(playerID, acceptor, ka));
	}

	private void checkCompletedObjective(final Player player, final KitAchivement kit)
	{
		this.getKitProgress(player.getUniqueId(), new Acceptor<Integer>()
		{
			@Override
			public void accept(Integer amount)
			{
				if (amount.equals(kit.getTotalActions()))
				{
					xpSystem.addKit(player.getUniqueId(), kit.getRewardKit());
					player.sendMessage(ChatColor.GREEN + "You just completed the action for " + ChatColor.YELLOW + kit.toString() + ChatColor.GREEN
							+ "! You can use this kit in the next annihilation game.");

				}
			}
		}, kit);
	}
}
