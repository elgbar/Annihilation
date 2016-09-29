package com.gmail.kh498.xp2gSystem.database;

import java.sql.ResultSet;

public interface AsyncQuery extends Runnable
{
	boolean isCallback ();

	String getQuerey ();

	void setResult (ResultSet set);

}
