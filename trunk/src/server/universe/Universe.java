package server.universe;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.Server;
import server.universe.mob.MOB;

import message.ClientMessage;

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
	private static final long			serialVersionUID	= 2L;

	// A map of all playernames to locations, including logged out players.
	private Map<String, Room>			nameToRoom;

	// A map of logged-in players to their locations.
	private transient Map<Player, Room>	playerToRoom;

	// A map of MOBs to their locations.
	private Map<MOB, Room>				mobToRoom;

	// The start room, which contains references to the other rooms.
	private Room						startRoom;

	/**
	 * 0-arg constructor. Be careful when using this as it sets startRoom to null.
	 * setStartRoom must be used if this is used.
	 */
	public Universe()
	{
		this.nameToRoom = new HashMap<String, Room>();
		this.playerToRoom = new HashMap<Player, Room>();
		this.mobToRoom = new HashMap<MOB, Room>();
		this.startRoom = null;
	}
	
	/**
	 * In this constructor, a simple default universe might be made.
	 */
	public Universe(Room startRoom)
	{
		this.nameToRoom = new HashMap<String, Room>();
		this.playerToRoom = new HashMap<Player, Room>();
		this.mobToRoom = new HashMap<MOB, Room>();
		this.startRoom = startRoom;
	}

	/**
	 * Get the player with the given name. Return null if player does not exist.
	 */
	public Player getPlayer(String playerName)
	{
		for (Player player : this.playerToRoom.keySet())
			if (player.getName().equalsIgnoreCase(playerName))
				return player;

		return null;
	}

	public void setStartRoom(Room room)
	{
		this.startRoom = room;
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
			Player player = (Player) creature;
			return this.playerToRoom.get(player);
		}
		else
		{
			MOB mob = (MOB) creature;
			return this.mobToRoom.get(mob);
		}
	}

	/**
	 * Change current location of a creature. This doesn't check whether the new
	 * room is adjacent or anything, it just does it.
	 */
	public void changeRoomOfCreature(Creature creature, Room room)
	{
		if (creature instanceof Player)
		{
			Player player = (Player) creature;
			this.playerToRoom.put(player, room);
			String name = player.getName();
			this.nameToRoom.put(name, room);
		}
		else
		{
			MOB mob = (MOB) creature;
			this.mobToRoom.put(mob, room);
		}
	}

	/**
	 * Get a list of all players that are currently logged in.
	 */
	public List<Player> getLoggedInPlayers()
	{
		List<Player> list = new ArrayList<Player>();
		list.addAll(playerToRoom.keySet());
		return list;
	}
	
	/**
	 * Test whether a player is logged in.
	 */
	public boolean isLoggedIn(Player player)
	{
		return playerToRoom.keySet().contains(player);
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
	 *
	 * @param name
	 *            name of the player
	 */
	public void register(String name)
	{
		this.nameToRoom.put(name, startRoom);
	}

	/**
	 * Remove a player from the list of registered players. If the player with
	 * the given name is in the game right now, boot them.
	 *
	 * @param name
	 *            name of the player
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
	 * Spawns a mob in the given room.
	 */
	public void spawnMob(MOB mob, Room room)
	{
		this.mobToRoom.put(mob, room);
		new Thread(mob).start();
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
	
	/**
	 * Send a message to one creature. 
	 */
	public void sendMessageToCreature(Creature sender, Creature receiver, ClientMessage message)
	{
		if (receiver instanceof Player)
		{
			Player player = (Player) receiver;
			Server.sendMessageToPlayer(player, message);
		}
		else
		{
			MOB mob = (MOB) receiver;
			mob.tell(sender, message.getPayload());
		}
	}
	
	/**
	 * Send a message to all creatures in a room. For players, this means sending
	 * some text to a client. For mobs, these means calling a method to notify the mob that
	 * it has been sent certain text. 
	 */
	public void sendMessageToCreaturesInRoom(Creature sender, Room room, ClientMessage textSaid)
	{
		for (MOB mob : this.getMOBsInRoom(room))
		{
			if (!mob.equals(sender))
				sendMessageToCreature(sender, mob, textSaid);
		}
		for (Player player : this.getPlayersInRoom(room))
		{			
			if (!player.equals(sender))
				Server.sendMessageToPlayer(player, textSaid);
		}
	}
	
	/**
	 * Called by Java during deserialization. This helps in restoring transient
	 * fields.
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException
	{
		stream.defaultReadObject();
		this.playerToRoom = new HashMap<Player, Room>();
	}
}
