package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.item.Item;
import util.ArrayUtil;

/**
 * Responds to the drop command as input by the user. Drops a specified item
 * from the inventory.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class DropResponse implements ServerResponse
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
		if (arguments.size() != 1)
		{
			return new ClientMessage("the proper syntax for drop is drop <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			String itemName = ArrayUtil.joinArguments(arguments, " ").trim();
			Item item = serverThread.getPlayer().getItem(itemName);

			if (item != null)
			{
				// remove from user, add to room
				serverThread.getPlayer().removeItem(item);
				Server.getUniverse().getRoomOfCreature(serverThread.getPlayer()).addItem(item);
				return new ClientMessage("the item, " + itemName + " was removed from your inventory");
			}
			else
			{
				return new ClientMessage("that item name was unrecognized, use the inventory command to see what you have");
			}

		}
	}
}
