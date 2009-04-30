package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Player;
import server.universe.item.Item;
import util.ArrayUtil;

/**
 * Invokes give at requested by the user. This command allows a user to give an
 * item to a user. The syntax is give <player name> <item name>.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class GiveResponse implements ServerResponse
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
		if (arguments.size() < 2)
		{
			return new ClientMessage("Invalid syntax: the proper syntax is give <player name> <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			// identify the recipient
			Player recipient = Server.getUniverse().getPlayer(arguments.get(0));
			if (recipient == null)
				return new ClientMessage("that player, " + arguments.get(0) + ", was not found on the system");
			
			// check to see if user is in the same room
			List<Player> players = Server.getUniverse().getPlayersInRoom(Server.getUniverse().getRoomOfCreature(serverThread.getPlayer()));
			boolean playerFound = false;
			for (Player p : players)
			{ 
				if (p.getName().equalsIgnoreCase(recipient.getName()))
				{
					playerFound = true;
					break;
				}	
			}
			
			if (! playerFound)
				return new ClientMessage(recipient.getName() + " is not in the same room as you");
			
			// identify the item
			arguments.remove(0);
			Item itemToGive = serverThread.getPlayer().getItem(ArrayUtil.joinArguments(arguments, " ").trim());
			if (itemToGive == null)
				return new ClientMessage("that item, " + arguments.get(1) + ", was not found in your inventory");
		
			// give and alert
			serverThread.getPlayer().giveItem(recipient, itemToGive);
			Server.sendMessageToPlayer(recipient.getName(), new ClientMessage("you have recieved " + itemToGive.getName() + " from " + serverThread.getPlayer().getName()));
			return new ClientMessage("you gave " + recipient.getName() + " " + itemToGive.getName());
		}
	}
}
