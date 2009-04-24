package server.universe;

import java.util.List;

/**
 * A Room represents a location in the universe.
 * 
 * Thus quoteth The Spec:
 * 
 * "Rooms in a MUD should be thought of as locations rather than an indoor area
 * with walls and a ceiling (A "room" could be a clearing in the woods with a
 * path leading to the north and another to the west, or the middle of a field
 * with paths leading in all directions). Rooms have exits that connect them to
 * other rooms, descriptions, and contents (including items, players and MOBs)."
 */
public class Room implements Entity
{
	private List<Exit>	exits;
	private List<Item>	items;
	private String		name;
	private String		blurb;

	/**
	 * Create a new room with the given attributes.
	 * 
	 * @param name
	 *            name of the room users will see and reference to
	 * @param blurb
	 *            small blurb of what the room looks like, i.e., decoration,
	 *            weather, etc.
	 * @param exits
	 *            list of exists the room has
	 * @param items
	 *            list of items the room has
	 */
	public Room(String name, String blurb, List<Exit> exits, List<Item> items)
	{
		this.name = name;
		this.blurb = blurb;

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

	/**
	 * Add an item to the room. Rooms can contain infinitely many items!! Or
	 * rather, Room storage capacity is bounded by computer memory.
	 */
	public void addItem(Item item)
	{
		this.items.add(item);
	}

	/**
	 * Remove an item from the room.
	 */
	public void removeItem(Item item)
	{
		this.items.remove(item);
	}

	@Override
	public String getDescription()
	{
		StringBuilder toReturn = new StringBuilder();

		toReturn.append("Room: " + this.name + "\n");

		toReturn.append("Description" + this.blurb + "\n");

		toReturn.append("Exits:\n");
		for (Exit e : this.exits)
			toReturn.append("\t" + e.getDescription() + "\n");

		toReturn.append("Items:\n");
		for (Item i : this.items)
			toReturn.append("\t" + i.getDescription());

		return toReturn.toString();
	}

	@Override
	public String getName()
	{
		return this.name;
	}
}
