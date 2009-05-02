package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Player;
import server.universe.Room;
import server.universe.item.Item;

/**
 * Responds to the get command as issued by the user. This command gets an item
 * that exists in the same room as the user. The proper syntax is get <name of
 * item in room>.
 */
public class GetResponse implements ServerResponse
{

	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 1)
		{
			return new ClientMessage("Improper syntax: the proper syntax is get <item>");
		}
		else
		{
			Player player = serverThread.getPlayer();
			Room room = Server.getUniverse().getRoomOfCreature(player);

			// locate the item
			List<Item> itemsInRoom = room.getItems();
			Item itemToGet = null;
			String itemName = arguments.get(0);
			for (Item item : itemsInRoom)
				if (item.getName().equalsIgnoreCase(itemName))
					itemToGet = item;
		
			if (itemToGet == null)
				return new ClientMessage("The item you are trying to get, " + itemName + ", does not exist in this room.");
			
			// give the item to player, remove from room
			serverThread.getPlayer().addItem(itemToGet);
			room.removeItem(itemToGet);
			return new ClientMessage("You got " + itemName + " from this room.");
		}
	}	
}
