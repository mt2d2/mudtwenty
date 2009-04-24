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

	private Map<String, Room>		nameToRoom; // A map of all players' names, including logged-out players, to rooms.
	private transient Map<Player, Room>		playerToRoom; //A map of only logged-in players to their locations.
	private Map<MOB, Room>			mobToRoom; // A map of MOBs to their locations.
	private List<Room>				rooms;
	private Room					startRoom;

	/**
	 * In this constructor, a simple default universe might be made.
	 */
	public Universe()
	{
// 		this.playerToRoom = new HashMap<Player, Room>();
// 		this.mobToRoom = new HashMap<MOB, Room>();
// 		this.rooms = new ArrayList<Room>();
// 		this.loggedInPlayers = new ArrayList<Player>();
// 		this.startRoom = new;
	}

	/**
	 * Return a list of all <b>currently logged in</b> players in a room.
	 */
	public List<Player> getPlayersInRoom(Room room)
	{
		List<Player> list = new ArrayList<Player>();
		for (Player player : this.playerToRoom.keySet())
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
		List<Player> list = new ArrayList<Player>();
		list.addAll(this.playerToRoom.keySet());
		return list;
	}

	/**
	 * Check whether a player name is registered.
	 */
	public boolean isRegistered(String name)
	{
		return this.nameToRoom.keySet().contains(name);
	}

	/**
	 * Register a player with this name. Put the player in the starting room.
	 * But, do not log them in yet.
	 */
	public void register(String name)
	{
		this.nameToRoom.put(name, startRoom);
	}

	/**
	 * Remove a player from the list of registered players. If the player with the
	 * given name is in the game right now, boot them.
	 */
	public void unregister(String name)
	{
		this.nameToRoom.remove(name);
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
	public void login(Player player)
	{
		Room room = nameToRoom.get(player.getName());
		this.playerToRoom.put(player, room);
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
	public void logout(Player player)
	{
		this.playerToRoom.remove(player);
	}
}
