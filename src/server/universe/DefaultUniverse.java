package server.universe;

import server.universe.item.Potion;
import server.universe.mob.Kitten;
import server.universe.mob.RoomGiftMob;
import server.universe.mob.RoomThiefMob;

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
		this.setStartRoom(this.spawnRooms());
	}
	
	private Room spawnRooms()
	{
		Room rootRoom = new Room("Root room", "A starting place.", false);

		Room northRoom = new Room("North Room", "A boring, empty place in the north.", false);
		Room eastRoom = new Room("East Room", "a boring room in the east.", false);
		Room westRoom = new Room("West Room", "a boring, lockable room in the west.", true);
		Room southRoom = new Room("South Room", "A boring, empty place uin the south.", false);
		rootRoom.addRoom(Direction.NORTH, northRoom);
		rootRoom.addRoom(Direction.EAST, eastRoom);
		rootRoom.addRoom(Direction.WEST, westRoom);
		rootRoom.addRoom(Direction.SOUTH, southRoom);

		// add some items
		rootRoom.addItem(new Potion());
		rootRoom.addItem(new Potion());
		
		// add some mobs
		this.spawnMob(new Kitten("fluffy"), southRoom);
		this.spawnMob(new RoomGiftMob("santa"), northRoom);
		this.spawnMob(new RoomThiefMob("not santa"), southRoom);

		return rootRoom;
	}

}
