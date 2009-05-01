package server.response;

import java.util.ArrayList;
import java.util.List;

import message.ClientMessage;
import server.ServerThread;
import server.Server;
import server.universe.Entity;
import server.universe.Player;
import server.universe.Room;

/**
 * Responds to the look command as issued by the user.
 *
 * @author Michael Tremel
 */
public class LookResponse implements ServerResponse
{

	/**
	 * Currently, this looks at the room. Later, it should be able to be used to look
	 * at any entity.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		
		StringBuilder message = new StringBuilder();
		Player player = serverThread.getPlayer();
		final Room roomOfPlayer = Server.getUniverse().getRoomOfCreature(player);
		
		// Append room info.
		message.append(roomOfPlayer.getDescription());

		// Append players in room.
		message.append(players(player, roomOfPlayer) + "\n");
		message.append(mobs(roomOfPlayer));

		return new ClientMessage(message.toString());
	}
	
	/**
	 * Get a string description of players in the room.
	 */
	private String players(Player player, Room room)
	{
		
		List<Entity> players = new ArrayList<Entity>();
		players.addAll(Server.getUniverse().getPlayersInRoom(room));
		
		players.remove(player); // don't include the player that's looking.
		if (players.isEmpty())
			return "There are no other players in this room.";
		else
			return "Players in the room: " + listEntityNames(players);
	}
	
	/**
	 * Get a string description of mobs in the room.
	 */
	private String mobs(Room room)
	{
		List<Entity> mobs = new ArrayList<Entity>();
		mobs.addAll(Server.getUniverse().getMOBsInRoom(room));
		
		if (mobs.isEmpty())
			return "There are no mobs in this room.";
		else
			return "MOBs in the room: " + listEntityNames(mobs);
	}
	
	/**
	 * Given a list of entities, return a properly formatted string representation. 
	 */
	private String listEntityNames(List<Entity> entities)
	{
		if (entities.isEmpty())
			return "None.";
		else if (entities.size() == 1)
			return entities.get(0).getName();
		else
			return entities.get(0).getName() + ", "
				+ listEntityNames(entities.subList(1, entities.size()-1));
	}
}
