package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Room;
import server.universe.item.Item;
import util.ArrayUtil;

/**
 * Responds to the get command as issued by the user. This command gets an item
 * that exists in the same room as the user. The proper syntax is get <name of
 * item in room>.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class GetResponse implements ServerResponse
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see server.response.ServerResponse#respond(server.ServerThread,
	 * java.util.List)
	 */
	@Override
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 1)
		{
			return new ClientMessage("Improper syntax: the proper syntax is get <name of item in room>");
		}
		else
		{
			Room room = Server.getUniverse().getRoomOfCreature(serverThread.getPlayer());
			List<Item> itemsInRoom = room.getItems();
			Item itemToGet = null;
			
			// locate the item
			String search = ArrayUtil.joinArguments(arguments, " " ).trim();
			for (Item i : itemsInRoom)
				if (i.getName().equalsIgnoreCase(search))
					itemToGet = i;
		
			if (itemToGet == null)
				return new ClientMessage("the item you are trying to get, " + search + ", does not exist in this room");
			
			// give the item to player, remove from room
			serverThread.getPlayer().addItem(itemToGet);
			room.removeItem(itemToGet);
			return new ClientMessage("you got " + itemToGet.getName() + " from this room");
		}
	}	
}
