package server.universe;

import java.io.Serializable;

/**
 * The Player class represents a player's character. It should keep track of all
 * of the player's stats, password hash, and other such things.
 */
public class Player extends Creature implements Serializable
{
	private static final long	serialVersionUID	= 1L;
	private String	passwordHash;
	
	/**
	 * Constructs a Player with a username and password. This is used during
	 * registration of new players.
	 *
	 * There can only be one player with a given name. This method will assume
	 * that the given name is unique -- this should be checked BEFORE the player
	 * is created.
	 *
	 * @param name
	 *            name of the user, i.e., their username
	 * @param passwordHash
	 *            users MD5-hased password
	 */
	public Player(String name, String passwordHash)
	{
		super(name);
		this.passwordHash = passwordHash;
	}

	/**
	 * This allows for easy login procedures.
	 *
	 * @return <code>true</code> if the provided input matches the remembered
	 *         hash of a player.
	 */
	public boolean confirmPasswordHash(String hash)
	{
		return this.passwordHash.equals(hash);
	}

}