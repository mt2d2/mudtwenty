package server.universe;

/**
 * The Item interface represents anything in the world that's not a Creature --
 * anything that has no intelligence or ability to do thing like talking and
 * fighting.
 */
public interface Item extends Entity
{
	/**
	 * @return price of an item, in the standard money system
	 */
	public int getPrice();
}