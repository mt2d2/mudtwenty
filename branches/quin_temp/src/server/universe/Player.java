package server.universe;

import java.util.List;

/**
 * The Player class represents a player's character. It should keep track of all
 * of the player's stats.
 */
public class Player extends Creature
{
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
		return null;
	}

	/**
	 * This allows for easy login procedures.
	 * 
	 * @return <code>true</code> if the provided input matches the remembered
	 *         hash of a player.
	 */
	public boolean confirmPasswordHash(String hash)
	{
		return false;
	}

}