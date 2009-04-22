package server.universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Centralized session storage. This allows easy access to a players location
 * with regard to Rooms.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class Session
{
	private Map<Player, Room>	playersToRooms;
	private Map<Room, Player>	roomsToPlayers;

	/**
	 * Sole constructor, sets Session up for use.
	 */
	public Session()
	{
		this.playersToRooms = new HashMap<Player, Room>();
		this.roomsToPlayers = new HashMap<Room, Player>();
	}

	/**
	 * @param player
	 *            a Player to locate
	 * @return the current location of a Player
	 */
	public Room getRoomOfPlayer(Player player)
	{
		return this.playersToRooms.get(player);
	}

	/**
	 * Searches for players in a room. This could easily be extend to support
	 * searching for all entities instead.
	 * 
	 * @param room
	 *            this will be searched for all players in a the room and
	 *            returns them.
	 * @return list of players present in the room
	 */
	public List<Player> getPlayersInRoom(Room room)
	{
		List<Player> toReturn = new ArrayList<Player>();

		for (Map.Entry<Room, Player> entry : this.roomsToPlayers.entrySet())
		{
			if (entry.getKey().equals(room))
				toReturn.add(entry.getValue());
		}

		return toReturn;
	}
}
