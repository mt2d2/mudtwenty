package server.universe;

import java.util.ArrayList;

/**
 * A simple default universe that can be loaded for a demo, or when the universe file isn't there.
 * This condition is the exception. In a normal case, the universe is loaded from a file.
 */
public class DefaultUniverse extends Universe
{
	private static final long		serialVersionUID	= 1L;

	/**
	 * This constructor assembles a room or two. No need to use any creational patterns here...
	 * This is only a one-off, simple universe thing.
	 */
	public DefaultUniverse()
	{
		super(new Room("room", "an empty room", new ArrayList<Exit>(), new ArrayList<Item>()));
	}

}
