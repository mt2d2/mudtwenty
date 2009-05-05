package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Player;
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

	/**
	 * Drop the item named into the room if the item exists.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 1)
		{
			return new ClientMessage("The proper syntax is: drop <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			Player player = serverThread.getPlayer();
			String itemName = ArrayUtil.joinArguments(arguments, " ").trim();
			Item item = player.getItem(itemName);

			if (item != null)
			{
				// remove from user, add to room
				player.removeItem(item);
				player.getRoom().addItem(item);
				return new ClientMessage("The item " + itemName + " was removed from your inventory.");
			}
			else
			{
				return new ClientMessage("That item name was unrecognized. Use the inventory command to see what you have.");
			}

		}
	}
}
