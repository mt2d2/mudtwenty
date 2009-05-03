/**
 * 
 */
package server.universe.mob;

import server.universe.Creature;

public class NullDialog implements DialogStrategy
{
	private static final long serialVersionUID = 1L;

	/**
	 * Listen but do not speak. How peaceful.
	 */
	public void tell(Creature sender, String message)
	{
	}
}