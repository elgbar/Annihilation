package com.gmail.nuclearcat1337.xpSystem.kitAchievement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.gmail.nuclearcat1337.xpSystem.database.AsyncQuery;
import com.gmail.nuclearcat1337.xpSystem.utils.Acceptor;

class QueryKitAchievements implements AsyncQuery
{
	private final UUID playerID;
	private final Acceptor<Integer> acceptor;
	private final KitAchivement ka;
	
	private int progress = 0;
	
	public QueryKitAchievements(UUID playerID, Acceptor<Integer> acceptor, KitAchivement ka)
	{
		assert playerID != null;
		assert acceptor != null;
		assert ka != null;
		
		this.playerID = playerID;
		this.acceptor = acceptor;
		this.ka = ka;
	}
	
	@Override
	public void run()
	{
		acceptor.accept(progress);
	}

	@Override
	public boolean isCallback()
	{
		return true;
	}

	@Override
	public String getQuerey()
	{
		return "SELECT * FROM "+KitSystem.PREFIX_TABLE+ ka.toString()+" WHERE ID='"+playerID.toString()+"'";
	}

	@Override
	public void setResult(ResultSet set)
	{
		try
		{
			if(set.next())
			{
				progress = set.getInt(ka.getRow());
				set.close();
			}
		}
		catch (SQLException e)
		{	
			e.printStackTrace();
		}
	}
}
