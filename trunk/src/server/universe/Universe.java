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
	 * A map of <b>all players, even logged-out players,</b> to their locations.
	 */
	private Map<Player, Room> playerToRoom;

	/**
	 * A map of MOBs to their locations.
	 */
	private Map<MOB, Room> mobToRoom;
	private static Universe		theUniverse;
	private List<Player>		loggedInPlayers;
	private List<Room>			rooms;

	/**
	 * In this private constructor, a simple default universe might be made.
	 * The universe will not be loaded here, because it should be loaded from
	 * a file by Server.
	 */
	private Universe()
	{
		this.playerToRoom  = new HashMap<Player, Room>();
		this.mobToRoom  = new HashMap<MOB, Room>();
		this.rooms = new ArrayList<Room>();
		this.loggedInPlayers = new ArrayList<Player>();
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
	public List<MOB> getMOBsInRoom(Room room)
	{
		return null;
	}

	/**
	 * Return a list of all <i>currently logged in</i> players in a room.
	 */
	public Room getRoomOfCreature(Creature creature)
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
	 * Return a list of players that are currently logged in.
	 *
	 * @return A list of all of the players currently logged in to the universe.
	 */
	public List<Player> getPlayers()
	{
		return loggedInPlayers;
	}

	/**
	 * Adds a player to the list of logged-in players in the Universe. Currently only adds the player to the
	 * list of players, but this could be useful to start the player off in a
	 * default room or something.
	 *
	 * @param player
	 *            to add
	 */
	public void addPlayer(Player player)
	{
		this.loggedInPlayers.add(player);
	}

	/**
	 * Removes a player from the list of currently logged-in players.
	 *
	 * @param player
	 *            to remove
	 */
	public void removePlayer(Player player)
	{
		this.loggedInPlayers.remove(player);
	}
}
