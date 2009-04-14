package server.universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session
{
	private Map<Player, Room> playersToRooms;
	private Map<Room, Player> roomsToPlayers;
	
	public Session()
	{
		this.playersToRooms = new HashMap<Player, Room>();
		this.roomsToPlayers = new HashMap<Room, Player>();
	}
	
	public Room getRoomOfPlayer(Player player)
	{
		return this.playersToRooms.get(player);
	}
	
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
