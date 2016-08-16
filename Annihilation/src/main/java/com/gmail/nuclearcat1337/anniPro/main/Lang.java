package com.gmail.nuclearcat1337.anniPro.main;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang
{
	// %# will be replaced with the a number
	// %w will be replaced with a word when needed
	// %n will be the line separator

	COMPASSTEXT("compass-text", "Pointing to"),

	SCOREBOARDMAP("scoreboard-map-word", "Map:"),

	NPCDEATH("npc-has-died", ChatColor.DARK_PURPLE + "YOU LOGGED OUT AND WERE KILLED."),
	NPCSURVIVE("npc-has-survived", ChatColor.GREEN + "YOU LOGGED OUT AND WERE TELEPORTED BACK TO YOUR BASE."),
	NPCALIVE("npc-still-alive", "Your NPC has not despawned yet."),

	TEAM("the-word-team", "team"),

	// Team destroyed message
	TEAMDESTROYED("team-destroyed", "%w team has been destroyed."),

	// Death messages
	DEATHPHRASE("was-killed-by", ChatColor.GRAY + "was killed by"),
	REMEMBRANCE("kill-with-dead-team", ChatColor.GRAY + "in remembrance of his team"),
	NEXUSKILL("kill-near-nexus", ChatColor.GRAY + "in defense of his nexus"),

	// Civilian kit name
	CIVILIANNAME("civilian-kit-name", "Civilian"),
	CIVILIANLORE(
			"civilian-kit-lore",
			"&bYou are the backbone.%n%n&bFuel all facets of the%n&bwar machine with your%n&bset of wooden tools and%n&bprepare for battle!"),
	// &b is the color aqua

	// Custom Items
	NAVCOMPASS("nexus-compass-name", ChatColor.DARK_PURPLE + "Right click to change target nexus"),
	KITMAP("kit-book-name", ChatColor.AQUA + "Right click to choose a kit"),
	VOTEMAP("vote-item-name", ChatColor.AQUA + "Right click to vote for a map"),
	TEAMMAP("team-iteam-name", ChatColor.AQUA + "Right click to join a team"),

	// Invis reveal
	INVISREVEAL("invis-reveal", ChatColor.GOLD + "You have been revealed!"),

	// Signs
	SHOP("the-word-shop", ChatColor.DARK_PURPLE + "Shop"),
	TEAMSIGN("team-join-sign", ChatColor.DARK_PURPLE + "Right Click%n" + ChatColor.DARK_PURPLE + "To Join:%n" + "%w Team"),
	BREWINGSIGN("brewing-shop-sign", ChatColor.BLACK + "Brewing"),
	WEAPONSIGN("weapon-shop-sign", ChatColor.BLACK + "Weapon"),

	// Shop strings
	PURCHASEDITEM("shop-item-purchased", ChatColor.GREEN + "Item Purchased!"),
	COULDNOTPURCHASE("shop-item-not-purchased", ChatColor.RED + "Could not purchase the item. Please check the price again."),
	COST("shop-item-cost", ChatColor.GOLD + "Cost: %# Gold Ingots"),
	QUANTITY("shop-item-quantity", ChatColor.GOLD + "Quantity: %#"),

	// Team Command things
	TEAMCHECK("team-check-players", "There are %# Players on the %w Team."),
	JOINTEAM("team-join", ChatColor.GRAY + "You have joined the"),
	LEAVETEAM("team-leave", ChatColor.DARK_PURPLE + "You left %w Team"),
	NOTEAM("team-none", ChatColor.RED + "You do not have a team to leave."),
	CANNOTLEAVE("team-cannot-leave", ChatColor.RED + "You cannot leave a team while the game is running."),
	INVALIDTEAM("team-invalid", ChatColor.RED + "Invalid Team specified!"),
	TEAMHELP("team-help", "/Team [Name] (Red,Green,Blue,Yellow)"),
	WRONGPHASE("team-join-wrong-phase", ChatColor.RED + "You cannot join during this phase."),
	DESTROYEDTEAM("team-join-destroyed", ChatColor.RED + "You cannot join a team whose nexus is destroyed!"),
	JOINANOTHERTEAM("team-join-another", ChatColor.RED + "Please join another team until the player counts even out!"),
	ISINPARTY("has-party", ChatColor.RED + "You cannot join a team solo when you're in a party!"),
	LEAVEPARTY("leave-team-in-party", ChatColor.RED + "You cannot leave your team while you're in a party."),

	// Team
	YELLOWTEAM("yellow-team", "Yellow"),
	REDTEAM("red-team", "Red"),
	GREENTEAM("green-team", "Green"),
	BLUETEAM("blue-team", "Blue"),

	// Phase bar
	PHASEBAR("phase-bar-name", "Phase %#"),

	CANT_SELECT_KIT("cant-select-kit", ChatColor.RED + "Sorry, you can not select this kit."),

	// Scoreboard phase
	SCOREBOARDPHASE("scoreboard-phase-name", ChatColor.DARK_PURPLE + "Current Phase"),

	// Phase Start
	PHASESTART("phase-start", ChatColor.GOLD + "Phase %#" + ChatColor.GRAY + " has started"),

	// Phase messages
	PHASE1MESSAGE("phase-1-message", ChatColor.GRAY + "The nexus is " + ChatColor.GOLD + "invincible"),
	PHASE2MESSAGE(
			"phase-2-message",
			ChatColor.GRAY + "the nexus has " + ChatColor.GOLD + "lost" + ChatColor.GRAY + " its " + ChatColor.GOLD + "invincibility"),
	PHASE3MESSAGE(
			"phase-3-message",
			ChatColor.AQUA + "Diamonds " + ChatColor.GRAY + "have spawned in the middle" + "%n" + ChatColor.GRAY + "of the map"),
	PHASE4MESSAGE(
			"phase-4-message",
			ChatColor.GOLD + "Blaze Powder " + ChatColor.GRAY + "is now available" + "%n" + ChatColor.GRAY + "from the brewing shop"),
	PHASE5MESSAGE(
			"phase-5-message",
			ChatColor.GOLD + "Extra damage " + ChatColor.GRAY + "is inflicted on the " + ChatColor.GOLD + "nexus" + "%n" + ChatColor.GRAY
					+ "when breaking it" + "%n" + ChatColor.RED + "x2 " + ChatColor.GOLD + "extra damage"),

	//Party
	NOCREATE("no-create-while-running", ChatColor.RED + "You cannot create a party while a game is running"),
	NOEDIT("no-edit-while-running", ChatColor.RED + "You cannot edit parties while the game is running."),

	//You
	ALREADYHAVEPARTY("party-already-have", ChatColor.RED + "You already have a party!"),
	NOPARTY("no-party", ChatColor.RED + "You are not in a party."),
	NOTHOST("not-host", ChatColor.RED + "Only the host can do that."),
	NOTINVITEDPLAYER("not-invited", ChatColor.RED + "You are not invited to join this party"),
	NOTYOURSELF("arg-is-player", ChatColor.RED + "Player argument cannot be yourself."),
	NOPLAYERSINPARTY("no-players-in-party", ChatColor.RED + "There are no players in your party"),
	PARTYREMOVED("party-removed", ChatColor.DARK_PURPLE + "Your party was removed."),
	PARTYREMOVEDPLAYER(
			"party-removed-player",
			ChatColor.DARK_PURPLE + "You party was disbanded due to the host," + ChatColor.GOLD + "%w" + ChatColor.DARK_PURPLE
					+ ", leaving."),

	//Target
	TARGETHAVEPARTY("target-have-party", ChatColor.RED + "That player is in a party."),
	TARGETDONOTHAVEPARTY("target-have-party", ChatColor.RED + "That player is not in a party."),
	TARGETHAVENOPARTY("target-have-no-party", ChatColor.RED + "That player does not have a party"),
	NOSUCHPLAYER("no-such-player", ChatColor.RED + "That player does not exist."),
	NOTINVITEDTARGET("not-invited", ChatColor.RED + "That player is not invited to join your party."),

	//Cancel invites
	NOLONGERINVITEDHOST("no-longer-invited-host", ChatColor.DARK_PURPLE + "Canceled invitation for " + ChatColor.GOLD + "%w"),
	NOLONGERINVITEDTARGET(
			"no-longer-invited-target",
			ChatColor.DARK_PURPLE + "You are no longer invited to " + ChatColor.GOLD + "%w" + ChatColor.DARK_PURPLE + "'s party."),
	NOPENDINGINVITES("no-pending-invites", ChatColor.RED + "You have no pending invites to your party."),
	CANCELINVITE("cancel-invite", "Cancel Invite"),

	//accept
	ACCEPTINVITE("accept-invite", "Accept Invite"),
	ACCEPTHOST("player-accept-host", ChatColor.DARK_PURPLE + "The player " + ChatColor.GOLD + "%w" + ChatColor.DARK_PURPLE + " joined your party."),
	ACCEPTPLAYER("player-accept-player", ChatColor.DARK_PURPLE + "You joined " + ChatColor.GOLD + "%w" + ChatColor.DARK_PURPLE + "'s party"),
	NOTINVITED("not-invited-to-parties", ChatColor.RED + "You are not invited to any parties :("),
	NOJOINWHENINPARTY("no-join-when-in-party", ChatColor.RED + "You cannot join a party when you are in a party"),

	//kicked
	KICKPLAYERS("kick-players", "Kick Players"),
	KICKEDAPLAYER("player-kicked", ChatColor.DARK_PURPLE + "You kicked " + ChatColor.GOLD + "%w" + ChatColor.DARK_PURPLE + " from your party."),
	PLAYERKICKED(
			"player-kicked",
			ChatColor.DARK_PURPLE + "You were kicked from your party by " + ChatColor.GOLD + "%w" + ChatColor.DARK_PURPLE + "."),

	//invite
	NOONETOINVITE("no-one-to-invite", ChatColor.RED + "There is no one to invite."),
	INVITEPLAYERS("invite-players", "Invite Players"),
	INVITEHOST("invite-host", ChatColor.DARK_PURPLE + "You invited " + ChatColor.GOLD + "%w" + ChatColor.DARK_PURPLE + " to join your party."),
	INVITETARGET("invite-target", ChatColor.DARK_PURPLE + "You are invited to join " + ChatColor.GOLD + "%w" + ChatColor.DARK_PURPLE + "'s party."),

	//CREATE
	CREATESUCCESS("create-success", ChatColor.DARK_PURPLE + "Succsessfully created a party!"),
	CREATEFAILED("create-failed", ChatColor.RED + "You cannot create a party when you are a part of one."),

	//leave

	LEFTPARTY("player-leave-party", ChatColor.DARK_PURPLE + "You left the party."),

	//Party GUI
	NEXTPAGE("next-page", ChatColor.YELLOW + "Next Page"),
	PREVPAGE("previous-page", ChatColor.YELLOW + "previous Page"),
	EXITGUI("exit-gui", ChatColor.RED + "Exit"),

	//HEAD LORE
	HEADLOREINVITE("head-lore-kick", ChatColor.GRAY + "Click on a player's head%n" + ChatColor.GRAY + "to invite the player to your party"),
	HEADLOREKICK("head-lore-kick", ChatColor.GRAY + "Click on a player's head%n" + ChatColor.GRAY + "to kick them from your party"),
	HEADLOREACCEPT("head-lore-accept", ChatColor.GRAY + "Click on a player's head%n" + ChatColor.GRAY + "to accept the host's inviation"),
	HEADLORECANCELINV(
			"head-lore-cancel-inv",
			ChatColor.GRAY + "Click on a player's head%n" + ChatColor.GRAY + "to cancel the invitation from your party"),

	//One-two words
	PAGE("page", "Page"),
	HOST("host", "Host"),
	PLAYERS("players", "Players"),
	INVITED("invited", "Invited"),
	PLAYERPARTY("player-party", "Player Parties"),;

	private String path;
	private String def;
	private static YamlConfiguration LANG;

	/**
	 * Lang enum constructor.
	 * 
	 * @param path
	 *            The string path.
	 * @param start
	 *            The default string.
	 */
	Lang(String path, String start)
	{
		this.path = path;
		this.def = start;
	}

	/**
	 * Set the {@code YamlConfiguration} to use.
	 * 
	 * @param config
	 *            The config to set.
	 */
	public static void setFile(YamlConfiguration config)
	{
		LANG = config;
	}

	@Override
	public String toString()
	{
		return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
	}

	public String toStringReplacement(String word)
	{
		return replaceWord(toString(), word);
	}

	public String toStringReplacement(int number)
	{
		return replaceNumber(toString(), number);
	}

	public String toStringReplacement(int number, String word)
	{
		return replaceWord(replaceNumber(toString(), number), word);
	}

	private String replaceWord(String string, String word)
	{
		return string.replace("%w", word);
	}

	private String replaceNumber(String string, int number)
	{
		return string.replace("%#", "" + number);
	}

	public String[] toStringArray()
	{
		String s = toString();
		if (s.contains("%n"))
			return s.split("%n");
		else
			return new String[] { s };
	}

	public String[] toStringArray(int number)
	{
		String s = this.toStringReplacement(number);
		if (s.contains("%n"))
			return s.split("%n");
		else
			return new String[] { s };
	}

	public String[] toStringArray(int number, String word)
	{
		String s = this.toStringReplacement(number, word);
		if (s.contains("%n"))
			return s.split("%n");
		else
			return new String[] { s };
	}

	public String[] toStringArray(String word)
	{
		String s = this.toStringReplacement(word);
		if (s.contains("%n"))
			return s.split("%n");
		else
			return new String[] { s };
	}

	/**
	 * Get the default value of the path.
	 * 
	 * @return The default value of the path.
	 */
	public String getDefault()
	{
		return this.def;
	}

	/**
	 * Get the path to the string.
	 * 
	 * @return The path to the string.
	 */
	public String getPath()
	{
		return this.path;
	}
}
