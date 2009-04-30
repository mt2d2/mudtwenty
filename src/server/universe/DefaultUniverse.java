package server.universe;

import java.util.ArrayList;
import java.util.List;

import server.universe.item.Item;
import server.universe.item.Potion;

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
		List<Exit> northRoomExits = new ArrayList<Exit>();
		List<Exit> southRoomExits = new ArrayList<Exit>();

		List<Item> northRoomItems = new ArrayList<Item>();
		List<Item> southRoomItems = new ArrayList<Item>();

		Room northRoom = new Room("North Room", "An other boring place.", northRoomExits, northRoomItems);
		Room southRoom = new Room("South Room", "A boring, empty place.", southRoomExits, southRoomItems);

		Exit toNorth = new Exit("north", "A passageway to the North2", Direction.NORTH, northRoom);
		Exit toSouth = new Exit("south", "A passageway to the South", Direction.SOUTH, southRoom);

		northRoomExits.add(toSouth);
		southRoomExits.add(toNorth);
		
		// add some items
		northRoom.addItem(new Potion());
		northRoom.addItem(new Potion());

		return southRoom;
	}

}