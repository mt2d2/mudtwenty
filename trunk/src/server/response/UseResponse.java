/**
 * 
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Item;
import server.universe.Player;
import server.universe.Potion;

/**
 * Responds to the use command as input by the user. Uses a specified item in
 * the users inventory.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class UseResponse implements ServerResponse
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
			return new ClientMessage("proper syntax of use: use <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			Player player = serverThread.getPlayer();
			Item item = player.getItem(arguments.get(0));
			
			if (item instanceof Potion) {
				player.increaseHealth(((Potion)item).getHealingPower());
				return new ClientMessage("You use " + item.getName() + " and heal " + ((Potion)item).getHealingPower() + " points");
			}
			
			return new ClientMessage("You do not have that item", Server.ERROR_TEXT_COLOR);
		}
	}
}
