package com.gmail.nuclearcat1337.xpSystem.database;

import java.sql.ResultSet;

public interface AsyncQuery extends Runnable
{
	boolean isCallback();
	String getQuerey();
	void setResult(ResultSet set);
	
}
