/**
 * 
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Armor;
import server.universe.Item;
import server.universe.Player;
import server.universe.Potion;
import server.universe.Weapon;
import util.ArrayUtil;

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
		if (arguments.size() == 0)
		{
			return new ClientMessage("proper syntax of use: use <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			Player player = serverThread.getPlayer();
			String name = ArrayUtil.joinArguments(arguments, " ").trim();
			Item item = player.getItem(name);
			
			if (item instanceof Potion) {
				player.increaseHealth(((Potion)item).getHealingPower());
				player.removeItem(item);
				return new ClientMessage("You use " + item.getName() + " and heal " + ((Potion)item).getHealingPower() + " points");
			}
			
			if (item instanceof Armor || item instanceof Weapon) {
				player.equip(item);
				return new ClientMessage("You equipped " + item.getName());
			}
			
			return new ClientMessage("You do not have " + name, Server.ERROR_TEXT_COLOR);
		}
	}
}
