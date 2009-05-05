package server.universe;

import server.universe.item.CheapTreat;
import server.universe.item.FancyTreat;
import server.universe.item.LargePotion;
import server.universe.item.Potion;
import server.universe.mob.Kitten;
import server.universe.mob.RoomGiftMob;
import server.universe.mob.RoomThiefMob;

/**
 * A simple default universe that can be loaded for a demo, or when the universe
 * file isn't there. This condition is the exception. In a normal case, the
 * universe is loaded from a file.
 */
public class DefaultUniverse extends Universe
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * This constructor assembles a room or two. No need to use any creational
	 * patterns here... This is only a one-off, simple universe thing.
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
		Room westRoom = new Room("West Room", "a boring, lockable room in the west.", true);
		Room southRoom = new Room("South Room", "A boring, empty place in the south.", false);
		rootRoom.addRoom(Direction.NORTH, northRoom);
		rootRoom.addRoom(Direction.WEST, westRoom);
		rootRoom.addRoom(Direction.SOUTH, southRoom);

		// use the node maker to add on to the east
		Room appendRoom = rootRoom;
		for (int i = 0; i < 9; i++)
		{
			Room node = this.makeRoomNode(i);
			appendRoom.addRoom(Direction.EAST, node);
			appendRoom = node;
		}

		// add some items
		rootRoom.addItem(new Potion());
		rootRoom.addItem(new LargePotion());
		northRoom.addItem(new FancyTreat());
		northRoom.addItem(new CheapTreat());

		// add some mobs
		this.spawnMob(new Kitten("fluffy"), southRoom);
		this.spawnMob(new RoomGiftMob("santa"), northRoom);
		this.spawnMob(new RoomThiefMob("thief"), southRoom);

		return rootRoom;
	}

	private Room makeRoomNode(int num)
	{
		Room rootRoom = new Room("Node root room: " + num, "the " + num + " node root", false);

		// west is already taken
		Room northRoom = new Room("North Room part of node " + num, "A boring, empty place in the north.", false);
		Room southRoom = new Room("South Room part of node " + num, "A boring, empty place in the south.", false);
		rootRoom.addRoom(Direction.NORTH, northRoom);
		rootRoom.addRoom(Direction.SOUTH, southRoom);

		return rootRoom;
	}
}
