package server.universe;

import java.util.List;

/**
 * A Universe represents the entire state of the virtual world. A universe
 * consists of a set of rooms and a set of players.
 */
public class Universe
{
	private List<Room>			rooms;
	private List<Player>		players;

	private transient Session	session;

	/**
	 * Create a universe with the given lists of rooms and players.
	 */
	public Universe(List<Room> rooms, List<Player> players)
	{
		this.rooms = rooms;
		this.players = players;
	}

	/**
	 * @return A list of all of the rooms in the universe.
	 */
	public List<Room> getRooms()
	{
		return rooms;
	}

	/**
	 * @return A list of all of the players currently logged in to the universe.
	 */
	public List<Player> getPlayers()
	{
		return players;
	}
	
	/**
	 * @return A table of sessions, i.e., all active users in the MUD.
	 */
	public Session getSession()
	{
		return this.session;
	}
}
