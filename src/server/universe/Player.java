package server.universe;

import java.io.Serializable;

/**
 * The Player class represents a player's character. It should keep track of all
 * of the player's stats, password hash, and other such things.
 */
public class Player extends Creature implements Serializable
{
	private static final long	serialVersionUID	= 1L;
	private String				password;

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
	 * @param password
	 *            users plaintext password- super awesome
	 */
	public Player(String name, String password)
	{
		super(name);
		this.password = password;
		this.setAttack(10);
	}

	/**
	 * This allows for easy login procedures.
	 * 
	 * @return <code>true</code> if the provided input matches the remembered
	 *         password of a player.
	 */
	public boolean confirmPassword(String hash)
	{
		return this.password.equals(hash);
	}
}