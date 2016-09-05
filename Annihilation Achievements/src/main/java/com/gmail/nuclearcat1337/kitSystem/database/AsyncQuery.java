package com.gmail.nuclearcat1337.kitSystem.database;

import java.sql.ResultSet;

public interface AsyncQuery extends Runnable
{
	boolean isCallback();
	String getQuerey();
	void setResult(ResultSet set);
	
}
