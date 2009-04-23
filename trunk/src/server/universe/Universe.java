package server.universe;

import server.universe.Universe;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * A Universe represents the entire state of the virtual world. A universe
 * contains Rooms and Entities. Universe is a singleton class -- there is no
 * public constructor; instances are gotten with getInstance().
 * 
 * The Universe keeps track of all the creatures and their locations, including
 * the players, and whether the players are logged in or not.
 */
public class Universe implements Serializable
{
	private static final long		serialVersionUID	= 1L;

	/**
	 * A list of all the players that are logged in. Every time that the
	 * Universe is reloaded from a file, this should be empty.
	 */
	private transient List<Player>	loggedInPlayers;

	/**
	 * A map of <b>all players, even logged-out players,</b> to their locations.
	 * 
	 * Rationale: The Universe keeps track of logged-out players so that players
	 * can easily be loaded into the correct Room when they log back in.
	 * 
	 * Also, the logged-out players are still sort-of part of the Universe, even
	 * if they are not currently in the game.
	 */
	private Map<Player, Room>		playerToRoom;

	/**
	 * A map of MOBs to their locations.
	 */
	private Map<MOB, Room>			mobToRoom;

	/**
	 * The single Universe! It will be null when the universe is not yet
	 * created. This is perhaps what our universe may have been -- a null
	 * reference.
	 */
	private static Universe			theUniverse;
	private List<Room>				rooms;
	private Room					startRoom;

	/**
	 * In this private constructor, a simple default universe might be made.
	 * 
	 * This constructor will only be called if a Universe could not be loaded
	 * from a file? Or does this constructor potentially load from a file? If it
	 * does load from a file, it should get the proper filename from the
	 * configuration.properties file.
	 */
	private Universe()
	{
		this.playerToRoom = new HashMap<Player, Room>();
		this.mobToRoom = new HashMap<MOB, Room>();
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
		List<Player> list = new ArrayList<Player>();
		for (Player player : this.loggedInPlayers)
		{
			if (this.playerToRoom.get(player).equals(room))
			{
				list.add(player);
			}
		}
		return list;
	}

	/**
	 * Return a list of all MOBs in a room.
	 */
	public List<MOB> getMOBsInRoom(Room room)
	{
		List<MOB> list = new ArrayList<MOB>();
		for (MOB mob : this.mobToRoom.keySet())
		{
			if (this.mobToRoom.get(mob).equals(room))
			{
				list.add(mob);
			}
		}
		return list;
	}

	/**
	 * Get the current location of a creature.
	 */
	public Room getRoomOfCreature(Creature creature)
	{
		if (creature instanceof Player)
		{
			return this.playerToRoom.get(creature);
		}
		else
		{
			return this.mobToRoom.get(creature);
		}
	}

	/**
	 * Return a list of players that are currently logged in.
	 * 
	 * @return A list of all of the players currently logged in to the universe.
	 */
	public List<Player> getLoggedInPlayers()
	{
		return loggedInPlayers;
	}

	/**
	 * Return a list of all players that are registered in the Universe, either
	 * logged in or not.
	 * 
	 * @return A list of all of the players, included logged-out players.
	 */
	public List<Player> getAllPlayers()
	{
		List<Player> list = new ArrayList<Player>();
		list.addAll(this.playerToRoom.keySet());
		return list;
	}

	/**
	 * Adds a player to the list of logged-in players in the Universe. Tell the
	 * server that a user is now logged in. The Universe can now find a rightful
	 * place for the user.
	 * 
	 * If the Player was not previously in the list of players on the server, it
	 * will now be added and put into the starting room.
	 * 
	 * @param player
	 *            to add
	 */
	public void addPlayer(Player player)
	{
		this.loggedInPlayers.add(player);
	}

	/**
	 * Register a new Player. Add them to the list of Players and assign them a
	 * location, but do not yet
	 * 
	 * @param player
	 *            to add
	 */
	public void addNewPlayer(Player player)
	{
		this.loggedInPlayers.add(player);
	}

	/**
	 * Removes a player from the list of currently logged-in players.
	 * 
	 * This removes the player from the world, but it doesn't lose track of the
	 * player's location. It does not delete a Player from the list of Players
	 * in the Universe.
	 * 
	 * @param player
	 *            to remove
	 */
	public void removePlayer(Player player)
	{
		this.loggedInPlayers.remove(player);
	}
}
