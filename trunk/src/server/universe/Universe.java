package server.universe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Universe represents the entire state of the virtual world. A universe
 * contains Rooms and Entities. Universe is a singleton class -- there is no
 * public constructor, but
 * 
 * The Universe keeps track
 */
public class Universe
{
	private Map<Creature, Room>	creatureToRoom;
	private Map<Room, Creature>	roomToCreature;
	
	private static Universe		theUniverse;
	private List<Player>		players;
	private List<Room>			rooms;

	/**
	 * In this private constructor, the universe should be loaded from a file if
	 * there is one. Otherwise, a default simple universe should be made.
	 */
	private Universe()
	{
		this.rooms = new ArrayList<Room>();
		this.players = new ArrayList<Player>();
	}

	/**
	 * Get the single instance of Universe.
	 */
	public static Universe getInstance()
	{
		if (theUniverse == null)
		{
			return new Universe();
		}
		else
		{
			return theUniverse;
		}
	}

	/**
	 * Return a list of all rooms in the universe.
	 * 
	 * @return A list of all of the rooms in the universe.
	 */
	public List<Room> getRooms()
	{
		return rooms;
	}

	/**
	 * Return a list of players.
	 * 
	 * @return A list of all of the players currently logged in to the universe.
	 */
	public List<Player> getPlayers()
	{
		return players;
	}

	/**
	 * Adds a player to this Universe. Currently only adds the player to the
	 * list of players, but this could be useful to start the player off in a
	 * default room or something.
	 * 
	 * @param player
	 *            to add
	 */
	public void addPlayer(Player player)
	{
		this.players.add(player);
	}

	/**
	 * Removes a player from the list of currently logged-in players.
	 * 
	 * @param player
	 *            to remove
	 */
	public void removePlayer(Player player)
	{
		this.players.remove(player);
	}
}
