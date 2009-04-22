package server.universe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import server.PropertyLoader;

/**
 * A Universe represents the entire state of the virtual world. A universe
 * consists of a set of rooms and a set of players.
 */
public class Universe
{
	private List<Room>				rooms;
	private List<Player>			players;

	/**
	 * Server configuration and properties, used for setting up the server and
	 * its universe.
	 */
	private static final Properties	conf	= PropertyLoader.loadProperties("server/configuration.properties");

	/**
	 * 
	 */
	public Universe()
	{
		this.rooms = new ArrayList<Room>();
		this.players = new ArrayList<Player>();
	}

	/**
	 * Create a universe with the given lists of rooms and players.
	 */
	public Universe(List<Room> rooms, List<Player> players)
	{
		this.rooms = rooms;
		this.players = players;
	}

	/**
	 * @param username
	 * @param password 
	 * @throws InvalidLoginException
	 */
	public void login(String username, String password) throws InvalidLoginException
	{
		final String dataRoot = conf.getProperty("data.root");
		final File sessionPath = new File(dataRoot + File.separatorChar + "sessions" + File.separatorChar + username + ".dat");

		if (sessionPath.exists() && sessionPath.canRead())
		{
			// read the file, check password
		}
		else
		{
			throw new InvalidLoginException("invalid user, " + username + ", please use the register command instead");
		}

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
}
