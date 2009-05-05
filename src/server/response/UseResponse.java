package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Player;
import server.universe.item.Item;
import util.ArrayUtil;

/**
 * Responds to the use command as input by the user. Uses a specified item in
 * the users inventory.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class UseResponse implements ServerResponse
{
	
	/**
	 * Use an item if possible.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.isEmpty())
		{
			return new ClientMessage("The proper syntax is: use <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			final Player player = serverThread.getPlayer();
			String itemName = ArrayUtil.joinArguments(arguments, " ").trim();
			Item item = player.getItem(itemName);
			
			if (item == null)
				return new ClientMessage("You do not have " + itemName + ".", Server.ERROR_TEXT_COLOR);
			
			return item.use(player);
		}
	}
}
