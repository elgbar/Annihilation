package com.gmail.kh498.xp2gSystem.xp;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.gmail.kh498.xp2gSystem.database.AsyncLogQuery;
import com.gmail.kh498.xp2gSystem.database.Database;
import com.gmail.kh498.xp2gSystem.utils.Acceptor;

public class XPSystem
{
	private Database database = null;

	public XPSystem (ConfigurationSection databaseSection)
	{
		assert databaseSection != null;
		database = loadDatabase (databaseSection);
		if (database != null)
		{
			try
			{
				database.updateSQL ("CREATE TABLE IF NOT EXISTS tbl_player_xp (ID VARCHAR(40), XP INTEGER, UNIQUE (ID))");
			} catch (Throwable t)
			{
				database = null;
			}
		}

	}

	/**
	 * @return False if the database is null
	 */
	public boolean isActive ()
	{
		return database != null;
	}

	/**
	 * Close the connection to the database
	 */
	public void disable ()
	{
		if (database != null)
			database.closeConnection ();
		database = null;
	}

	private Database loadDatabase (ConfigurationSection section)
	{
		assert section != null;

		String host = section.getString ("Host");
		String port = section.getString ("Port");
		String data = section.getString ("Database");
		String username = section.getString ("Username");
		String password = section.getString ("Password");
		return Database.getMySQLDatabase (host, port, data, username, password);
	}

	public void giveXP (final UUID playerID, final int XP)
	{
		if (XP > 0)
		{
			database.addNewAsyncLogQuery (new AsyncLogQuery ()
			{
				@ Override
				public String getQuery ()
				{
					return "INSERT INTO tbl_player_xp (ID, XP) VALUES ('" + playerID.toString () + "', " + XP
							+ ") ON DUPLICATE KEY UPDATE XP=XP+VALUES(XP);";
				}
			});
		}
	}

	public void removeXP (final UUID playerID, final int XP)
	{
		if (XP > 0)
		{
			database.addNewAsyncLogQuery (new AsyncLogQuery ()
			{
				@ Override
				public String getQuery ()
				{
					return "INSERT INTO tbl_player_xp (ID, XP) VALUES ('" + playerID.toString () + "', " + XP
							+ ") ON DUPLICATE KEY UPDATE XP=XP-VALUES(XP);";
				}
			});
		}
	}

	public void getXP (UUID playerID, Acceptor<Integer> acceptor)
	{
		database.addNewAsyncQuery (new QueryXP (playerID, acceptor));
	}
}
