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
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.isEmpty())
		{
			return new ClientMessage("proper syntax of use: use <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			Player player = serverThread.getPlayer();
			String name = ArrayUtil.joinArguments(arguments, " ").trim();
			Item item = player.getItem(name);
			
			if (item != null)
				return item.use(player);
			else
				return new ClientMessage("You do not have " + name, Server.ERROR_TEXT_COLOR);
		}
	}
}
