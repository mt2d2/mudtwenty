package server.universe;

import Java.util.List;

/**
 * A Room represents a location in the universe.
 */
public class Room
{
	private List<Exit> exits;
	private List<Item> items;

	public Room(List<Exit> exits, List<Item> items)
	{
		this.exits = exits;
		this.items = items;
	}

	/**
	 * @return A list of all of the exits from the room.
	 */
	public List<Room> getExits()
	{
		return exits;
	}


	/**
	 * @return A list of all of the items in the room.
	 */
	public List<Player> getItems()
	{
		return players;
	}

}
