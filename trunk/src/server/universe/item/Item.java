package server.universe.item;

import java.io.Serializable;

import message.ClientMessage;
import server.universe.Entity;
import server.universe.Player;

/**
 * The Item interface represents anything in the world that's not a Creature --
 * anything that has no intelligence or ability to do thing like talking and
 * fighting.
 */
public interface Item extends Entity, Serializable
{
	/**
	 * @return price of an item, in the standard money system
	 */
	public int getPrice();

	/**
	 * Uses a specified item against the specified user, and returns a
	 * user-visible message detailing that change.
	 * 
	 * @param player the player using the item
	 * @return user-visible message detailing the change
	 */
	public ClientMessage use(Player player);
}