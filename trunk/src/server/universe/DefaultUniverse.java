package server.universe;

import java.util.ArrayList;
import java.util.List;

import server.universe.item.Item;
import server.universe.item.Potion;
import server.universe.Direction;

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
		super(DefaultUniverse.assemble());
	}

	/**
	 * Assembles a simple Universe, returning a reference to the start room.
	 */
	private static Room assemble() {
		Room northRoom = new Room("North Room", "An other boring place.");
		Room southRoom = new Room("South Room", "A boring, empty place.");
		southRoom.addRoom(Direction.NORTH, northRoom);

		// add some items
		northRoom.addItem(new Potion());
		northRoom.addItem(new Potion());

		return southRoom;
	}

}
