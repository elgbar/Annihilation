package com.gmail.nuclearcat1337.kitSystem.kitAchievement;

import com.gmail.nuclearcat1337.anniPro.kits.Kit;

public enum KitAchivement
{
	WARRIOR("MELEE_KILLS"),
	SCOUT("NEXUS_DMG"),
	MINER("ORES_MINED", 1500),
	LUMBERJACK("LOG_CUT", 2000),
	ARCHER("BOW_KILLS", 200);

	private final String row;
	private final int actions;

	KitAchivement(String row)
	{
		this(row, 500);
	}

	KitAchivement(String row, int actions)
	{
		this.row = row;
		this.actions = actions;
	}

	@Override
	public String toString()
	{
		return this.name().toLowerCase();
	}
	
	/**
	 * 
	 * @param kit The name of a kit to be converted to a {@link KitAchivement}
	 * @return The {@link KitAchivement} version of a string, if nothing matched it returns null
	 */
	public static KitAchivement toKitAchivement(Kit kit){
		return toKitAchivement(kit.getName());
	}
	/**
	 * 
	 * @param kit The name of a kit to be converted to a {@link KitAchivement}
	 * @return The {@link KitAchivement} version of a string, if nothing matched it returns null
	 */
	public static KitAchivement toKitAchivement(String kit){
		for (KitAchivement ka : KitAchivement.values())
			if (kit.equalsIgnoreCase(ka.name()))
				return ka;
		return null;
	}
	/**
	 * 
	 * @return A {@link Kit} from a {@link KitAchivement}, if nothing matched it returns null
	 */
	public Kit toKit(){
		for (Kit kit : Kit.getKits())
			if (this.toString().equalsIgnoreCase(kit.toString()))
				return kit;
		return null;
	}
	/**
	 * 
	 * @return The number of times the action should be done before giving the kit
	 */
	public int getTotalActions()
	{
		return actions;
	}
	
	public String getRow()
	{
		return row;
	}
	
	/*
	 * What kit to reward the player with after completing the objective
	 */
	public Kit getRewardKit(){
		Kit kit = Kit.getKit(this.toString());
		if (kit != null)
			return kit;
		else 
			throw new NullPointerException("Could not find a kit with the name " + this.toString());
	}
}
