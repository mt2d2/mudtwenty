package server.response;

import java.util.ArrayList;
import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Direction;
import server.universe.Entity;
import server.universe.Player;
import server.universe.Room;

/**
 * Responds to the look command as issued by the user.
 */
public class LookResponse implements ServerResponse
{

	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		Player player = serverThread.getPlayer();
		final Room room = Server.getUniverse().getRoomOfCreature(player);
		
		if (arguments.isEmpty())
		{
			return lookAtRoom(room, player);
		}
		else
		{
			try
			{
				Direction direction = Direction.valueOf(arguments.get(0).toUpperCase());
				return lookAtExit(room, direction);
			}
			catch (IllegalArgumentException e)
			{
				return new ClientMessage(arguments.get(0) + " was not found.");
			}
		}
	}
	
	private ClientMessage lookAtRoom(Room room, Player player)
	{
		String message = room.getDescription()
			+ describePlayers(player, room) + "\n"
			+ describeMOBs(room) + "\n"
			+ describeItems(room) + "\n";

		return new ClientMessage(message.toString());
	}
	
	/**
	 * Get a string description of players in the room.
	 */
	private String describePlayers(Player player, Room room)
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
	private String describeMOBs(Room room)
	{
		List<Entity> mobs = new ArrayList<Entity>();
		mobs.addAll(Server.getUniverse().getMOBsInRoom(room));
		
		if (mobs.isEmpty())
			return "There are no mobs in this room.";
		else
			return "MOBs in the room: " + listEntityNames(mobs);
	}
	
	private String describeItems(Room room)
	{
		List<Entity> items = new ArrayList<Entity>();
		items.addAll(room.getItems());
		
		if (items.isEmpty())
			return "There are no items in the room.";
		else
			return "Items in the room: " + listEntityNames(items);
	}
	
	/**
	 * Given a list of entities, return a properly formatted string representation.
	 * 
	 * Precondition: list is not empty.
	 */
	private String listEntityNames(List<Entity> entities)
	{
		if (entities.size() == 1)
			return entities.get(0).getName();
		else
			return entities.get(0).getName() + ", "
				+ listEntityNames(entities.subList(1, entities.size()));
	}
	
	private ClientMessage lookAtExit(Room room, Direction direction)
	{
		if (room.hasExit(direction))
		{
			String name = direction.toString().toLowerCase();
			String description = room.getExit(direction).getDescription();
			return new ClientMessage(name + ": "+ description);
		}
		else
		{
			return new ClientMessage("There is no exit in that direction.", Server.ERROR_TEXT_COLOR);
		}
	}
	
}
