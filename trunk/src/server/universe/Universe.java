package server.universe;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * A Universe represents the entire state of the virtual world. A universe
 * contains Rooms and Entities. Universe is a singleton class -- there is no
 * public constructor, but
 *
 * The Universe keeps track
 */
public class Universe implements Serializable
{
	/**
	 * A map to get a list of <b>all players, even logged-out players,</b> in a given room.
	 */
	private Map<Room, List<Player>> roomToPlayers;

	/**
	 * A map to get a list of mobs in a given room.
	 */
	private Map<Room, List<MOB>> roomToMOBs;

	private static Universe		theUniverse;
	private List<Player>		players;
	private List<Room>			rooms;

	/**
	 * In this private constructor, a simple default universe might be made.
	 * The universe will not be loaded here, because it should be loaded from
	 * a file by Server.
	 */
	private Universe()
	{
		this.roomToPlayers = new HashMap<Room, List<Player>>();
		this.roomToMOBs = new HashMap<Room, List<MOB>>();
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
	 * Return a list of all <b>currently logged in</b> players in a room.
	 */
	public List<Player> getPlayersInRoom(Room room)
	{
		return null;
	}

	/**
	 * Return a list of all MOBs in a room.
	 */
	public List<Player> getMOBsInRoom(Room room)
	{
		return null;
	}

	/**
	 * Return a list of all <i>currently logged in</i> players in a room.
	 */
	public List<Player> getRoomOfCreature(Creature creature)
	{
		return null;
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
