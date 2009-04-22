package server.universe;

import java.io.Serializable;
import java.util.List;

/**
 * The Player class represents a player's character. It should keep track of all
 * of the player's stats.
 */
public class Player extends Creature implements Serializable
{
	private static final long	serialVersionUID	= 1L;

	private String	name;
	private String	passwordHash;

	/**
	 * Constructs a user with a username and password. This is used during
	 * registration.
	 * 
	 * @param name
	 *            name of the user, i.e., their username
	 * @param passwordHash
	 *            users MD5-hased password
	 */
	public Player(String name, String passwordHash)
	{
		this.name = name;
		this.passwordHash = passwordHash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.universe.Creature#getItems()
	 */
	public List<Item> getItems()
	{
		return null;
	}

	/**
	 * @return the username (i.e., login) associated with this player. It is
	 *         used in the confirmation of a clients identity.
	 */
	public String getUsername()
	{
		return this.name;
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