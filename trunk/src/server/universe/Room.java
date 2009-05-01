package server.universe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import server.universe.item.Item;

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
public class Room implements Entity, Serializable
{
	private static final long	serialVersionUID	= 1L;

	private Map<Direction, Exit> exitMap;
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
	 */
	public Room(String name, String blurb)
	{
		this.name = name;
		this.blurb = blurb;
		this.exitMap = new HashMap<Direction, Exit>();
		this.items = new ArrayList<Item>();
	}

	/**
	 * Get a particular exit.
	 *
	 * @return The exit in the given direction, or <code>null</code> if there is none.
	 */
	public Exit getExit(Direction direction)
	{
		if (hasExit(direction))
			return this.exitMap.get(direction);
		return null;
	}

	/**
	 * Test whether there is an exit in the given direction.
	 *
	 * @return <code>true</code> if there is an exit in the given direction, <code>false</code> for otherwise.
	 */
	public boolean hasExit(Direction direction)
	{
		return this.exitMap.containsKey(direction);
	}

	/**
	 * Add an exit to the room. Clobber any exit that was there previously.
	 */
	public void addExit(Direction direction, Exit exit)
	{
		this.exitMap.put(direction, exit);
	}
	
	/**
	 * @return the Direction an exit is facing
	 */
	public Direction getDirection(Exit exit)
	{
		for (Entry<Direction, Exit> entry : this.exitMap.entrySet())
			if (entry.getValue().equals(exit))
				return entry.getKey();

		return null;
	}

	/**
	 * List the items in the room.
	 *
	 * @return A list of all of the items in the room.
	 */
	public List<Item> getItems()
	{
		return this.items;
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

	public String getDescription()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("Room: " + this.name + "\n");
		toReturn.append("\tDescription: " + this.blurb + "\n");
		
		toReturn.append("\tExits:\n");
		for (Exit e : this.exitMap.values())
			toReturn.append("\t\t" + this.getDirection(e) + ": " + e.getDescription() + "\n");
		
		toReturn.append("\tItems:\n");
		for (Item i : this.items)
			toReturn.append("\t\t" + i.getName() + ": " + i.getDescription() + "\n");

		return toReturn.toString();
	}

	/**
	 * Get the name of the room. This will be part of the string returned when the room is looked at.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Set the blurb about the room. This will be some text describing how the room looks,
	 * regardless of items or creatures herein.
	 */
	public void setBlurb(String s)
	{
		this.blurb = s;
	}

	/**
	 * Add a room to this one in the given direction. Clobber any room that was there previously.
	 */
	public void addRoom(Direction direction, Room room)
	{
		this.addExit(direction, new Exit(room));
		room.addExit(direction.opposite(), new Exit(this));
	}

}
