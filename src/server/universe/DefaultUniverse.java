package server.universe;

import server.universe.item.Key;
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
		super();
		Room northRoom = new Room("North Room", "An other boring place.", false);
		Room southRoom = new Room("South Room", "A boring, empty place.", false);
		Room eastRoom = new Room("East Room", "a lockable room", true);
		southRoom.addRoom(Direction.NORTH, northRoom);
		southRoom.addRoom(Direction.EAST, eastRoom);

		// add some items
		northRoom.addItem(new Potion());
		northRoom.addItem(new Potion());
		northRoom.addItem(new Key("silver key", eastRoom));
		
		this.spawnMob(new Kitten("fluffy"), southRoom);

		this.setStartRoom(southRoom);
	}

}
