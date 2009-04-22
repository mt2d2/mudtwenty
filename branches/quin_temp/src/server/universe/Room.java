package server.universe;

import java.util.List;

/**
 * A Room represents a location in the universe.
 */
public class Room
{
	private List<Exit> exits;
	private List<Item> items;

	/**
	 * Create a new room with the given attributes.
	 */
	public Room(List<Exit> exits, List<Item> items)
	{
		this.exits = exits;
		this.items = items;
	}

	/**
	 * @return A list of all of the exits from the room.
	 */
	public List<Exit> getExits()
	{
		return exits;
	}

	/**
	 * @return A list of all of the items in the room.
	 */
	public List<Item> getItems()
	{
		return items;
	}

}
