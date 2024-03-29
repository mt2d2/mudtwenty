package server.universe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private List<Item>			items;
	private String				name;
	private String				blurb;
	
	private boolean				requiresKey;
	private long				timeLastUnlockedInMillis;

	/**
	 * Create a new room with the given attributes.
	 *
	 * @param name
	 *            name of the room users will see and reference to
	 * @param blurb
	 *            small blurb of what the room looks like, i.e., decoration,
	 *            weather, etc.
	 */
	public Room(String name, String blurb, boolean requiresKey)
	{
		this.name = name;
		this.blurb = blurb;
		this.exitMap = new HashMap<Direction, Exit>();
		this.items = new ArrayList<Item>();
		
		this.requiresKey = requiresKey;
		if (requiresKey)
			this.timeLastUnlockedInMillis = 0;
	}
	
	/**
	 * Sets the latest unlock time to the current time.
	 */
	public void unlockExit()
	{
		this.timeLastUnlockedInMillis = System.currentTimeMillis();
	}
	
	/**
	 * Tells whether this room is currently locked.
	 */
	public boolean isLocked()
	{
		return System.currentTimeMillis() - this.timeLastUnlockedInMillis < 60000;
	}
	
	public boolean requiresKey()
	{
		return this.requiresKey;
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
	 * Return a list of exits from the room.
	 */
	public List<Exit> getExits()
	{
		List<Exit> exits = new ArrayList<Exit>();
		exits.addAll(this.exitMap.values());
		return exits;
	}
	
	/**
	 * Return a list of exits to rooms that are not locked.
	 */
	public List<Exit> getUnlockedExits()
	{
		List<Exit> exits = new ArrayList<Exit>();
		for (Exit exit : this.exitMap.values())
			if (!exit.isLocked())
				exits.add(exit);
		return exits;
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

	/**
	 * Get a description of everything that is intrinsic to the room.
	 * Creatures and items inside can be listed separately.
	 */
	public String getDescription()
	{
		StringBuilder toReturn = new StringBuilder();
		
		// Add name and description.
		toReturn.append("Room: " + this.name + "\n");
		toReturn.append(this.blurb + "\n");
		
		// Add exits.
		List<Direction> directions = new ArrayList<Direction>();
		directions.addAll(this.exitMap.keySet());
		toReturn.append("Exits: " + listExits(directions) + "\n");

		return toReturn.toString();
	}

	/**
	 * Make a properly formatted string listing exit names.
	 * 
	 * Precondition: There is at least
	 * 
	 * @return String description of the exits
	 */
	private String listExits(List<Direction> directions)
	{
		if (directions.isEmpty())
			return "There are no exits.";
		else if (directions.size() == 1)
			return directions.get(0).toString().toLowerCase() + ".";
		else
			return directions.get(0).toString().toLowerCase()
				+ ", " + listExits(directions.subList(1, directions.size()));
	}
	
	/**
	 * Test whether this room is adjacent to another one (i.e. it has an exit leading to the other)
	 */
	public boolean isAdjacentTo(Room room)
	{
		for (Exit exit : exitMap.values())
			if (exit.getRoom().equals(room))
					return true;
		return false;
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
	public void setBlurb(String blurb)
	{
		this.blurb = blurb;
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
