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
import server.universe.item.Item;
import server.universe.mob.MOB;

/**
 * Responds to the look command as issued by the user. There is a possibility of
 * looking at things by supplying an argument. Look can view items, players, or
 * mobs in the player's current room.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class LookResponse implements ServerResponse
{

	/**
	 * If there are no arguments, send a description of the room. If there is an
	 * argument, send a description of the named entity if it exists; otherwise
	 * send an error message.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		final Player player = serverThread.getPlayer();
		final Room room = player.getRoom();

		if (arguments.isEmpty())
		{
			return lookAtRoom(room, player);
		}
		else
		{
			try
			{
				Item item = getItem(Server.getUniverse().getRoomOfCreature(serverThread.getPlayer()).getItems(), arguments.get(0));
				Player person = getPlayer(Server.getUniverse().getRoomOfCreature(serverThread.getPlayer()), arguments.get(0));
				MOB mob = getMob(Server.getUniverse().getMOBsInRoom(Server.getUniverse().getRoomOfCreature(serverThread.getPlayer())), arguments.get(0));

				if (item != null)
					return new ClientMessage("Info about " + arguments.get(0) + ": " + item.getDescription());
				else if (person != null)
					return new ClientMessage("Info about " + arguments.get(0) + ": " + person.getDescription());
				else if (mob != null)
					return new ClientMessage("Info about " + arguments.get(0) + ": " + mob.getDescription());
				else
				{
					Direction direction = Direction.valueOf(arguments.get(0).toUpperCase());
					return lookAtExit(room, direction);
				}
			}
			catch (IllegalArgumentException e)
			{
				return new ClientMessage("The thing you are searching for, " + arguments.get(0) + ", could not be found.", Server.ERROR_TEXT_COLOR);
			}
		}
	}

	private MOB getMob(List<MOB> list, String string)
	{
		for (MOB m : list)
			if (m.getName().equalsIgnoreCase(string))
				return m;

		return null;
	}

	private Item getItem(List<Item> list, String string)
	{
		for (Item i : list)
			if (i.getName().equalsIgnoreCase(string))
				return i;

		return null;
	}

	private Player getPlayer(Room room, String string)
	{
		for (Player p : Server.getUniverse().getPlayersInRoom(room))
			if (p.getName().equalsIgnoreCase(string))
				return p;

		return null;
	}

	/**
	 * Return a ClientMessage with a description of the given room.
	 */
	private ClientMessage lookAtRoom(Room room, Player player)
	{
		String message = room.getDescription() + describePlayers(player, room) + "\n" + describeMOBs(room) + "\n" + describeItems(room) + "\n";

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

	/**
	 * Get a string description of items in the room.
	 */
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
	 * Given a list of entities, return a properly formatted string
	 * representation.
	 * 
	 * PRECONDITION: list is not empty.
	 */
	private String listEntityNames(List<Entity> entities)
	{
		if (entities.size() == 1)
			return entities.get(0).getName();
		else
			return entities.get(0).getName() + ", " + listEntityNames(entities.subList(1, entities.size()));
	}

	/**
	 * Return a ClientMessage with a description of the exit.
	 */
	private ClientMessage lookAtExit(Room room, Direction direction)
	{
		if (room.hasExit(direction))
		{
			String name = direction.toString().toLowerCase();
			String description = room.getExit(direction).getDescription();
			return new ClientMessage(name + ": " + description);
		}
		else
		{
			return new ClientMessage("There is no exit in that direction.", Server.ERROR_TEXT_COLOR);
		}
	}
}
